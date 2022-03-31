package com.zl.javabingfabianchengshizhan.threadTest12.voovan;

import org.voovan.tools.TDateTime;
import org.voovan.tools.TEnv;
import org.voovan.tools.collection.CollectionSearch;
import org.voovan.tools.hashwheeltimer.HashWheelTimer;
import org.voovan.tools.log.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Allen.zhang
 * @title: CacheMap
 * @projectName zl
 * @description: TODO
 * @date 2022/3/1813:54
 */
public class CacheMap<K, V> implements ICacheMap<K, V> {

    protected final static HashWheelTimer CACHE_MAP_WHEEL_TIMER = new HashWheelTimer("CacheMap", 60 ,1000);
    private Function<K, V> supplier = null;
    private int interval = 1;
    private boolean autoRemove = true;
    private BiFunction<K, V, Long> destroy;

    private long expire;

    static {
        CACHE_MAP_WHEEL_TIMER.rotate();
    }

    private Map<K, V> cacheData = null;
    private ConcurrentHashMap<K, TimeMark<K>> cacheMark = new ConcurrentHashMap<K, TimeMark<K>>();
    private int maxSize;

    public CacheMap(Map<K, V> map ,int maxSize) {
        this.cacheData = map;
        this.maxSize = maxSize;
    }

    public CacheMap(Map<K, V> map) {
        this.maxSize = Integer.MAX_VALUE;
        this.cacheData = map;
    }

    public CacheMap(Integer maxSize){
        this.maxSize = maxSize==null?Integer.MAX_VALUE: maxSize;
        this.cacheData = new ConcurrentHashMap<>();
    }

    public CacheMap(){
        this.maxSize = Integer.MAX_VALUE;
        this.cacheData = new ConcurrentHashMap<>();
    }

    @Override
    public Function<K, V> getSupplier() {
        return supplier;
    }

    @Override
    public CacheMap<K, V> supplier(Function<K, V> buildFunction) {
        this.supplier = buildFunction;
        this.autoRemove = false;
        return this;
    }

    public CacheMap<K, V> supplier(Function<K, V> buildFunction, boolean autoRemove){
        this.supplier = buildFunction;
        this.autoRemove = autoRemove;
        return this;
    }

    public BiFunction<K, V, Long> getDestroy(){
        return destroy;
    }

    public CacheMap<K, V> destroy(BiFunction<K, V, Long> destroy){
        this.destroy = destroy;
        return this;
    }

    public CacheMap<K, V> maxSize(int maxSize){
        this.maxSize = maxSize;
        return this;
    }

    public CacheMap<K, V> interval(int interval) {
        this.interval = interval;
        return this;
    }

    public CacheMap<K, V> autoRemove(boolean autoRemove){
        this.autoRemove = autoRemove;
        return this;
    }

    public CacheMap<K, V> expire(long expire){
        this.expire = expire;
        return this;
    }

    public long getExpire(){
        return expire;
    }

    public ConcurrentHashMap<K, TimeMark<K>> getCacheMark(){
        return cacheMark;
    }

    public CacheMap<K, V> create(){
        final CacheMap<K, V> finalCacheMap = this;

        if (interval >= 1) {
            CACHE_MAP_WHEEL_TIMER.addTask(() -> {

                if (!cacheMark.isEmpty()) {
                    cacheMark.values().forEach(e -> {
                        checkAndDoExpire(e);
                    });
                    fixSize();
                }

            }, interval);
        }

        return this;
    }

    private void checkAndDoExpire(K key) {
        checkAndDoExpire(cacheMark.get(key));
    }

    /**
     * 检查或处理过期的数据
     * @param timeMark 检查的 TimeMark 对象
     * @return true: 已过期, false: 未过期, 有可能已经通过 supplier 重置
     */
    private boolean checkAndDoExpire(TimeMark<K> timeMark) {
        //如果为空，校验返回false
        if (timeMark == null || !timeMark.isExpire()) {
            return false;
        }

        //如果自动刷新是true
        if (autoRemove) {
            if (destroy != null) {

                V data = cacheData.get(timeMark.key);
                if (data == null) {
                    this.remove(timeMark.key);
                }

                // 1.返回 null 则刷新为默认超时时间
                // 2.小于0 的数据, 则移除对象
                // 3.大于0的数据则重新设置返回值为新的超时时间
                Long value = destroy.apply(timeMark.key, data);
                if (value == null) {
                    timeMark.refresh(true);
                } else if (value < 0) {
                    remove(timeMark.key);
                } else {
                    timeMark.setExpireTime(value);
                }

            } else {
                remove(timeMark.key);
            }
        } else if (getSupplier() != null){
            if (createCache(timeMark.key, supplier, timeMark.getExpireTime())) {
                timeMark.refresh(true);
                return false;
            } else {
                return true;
            }
        }

        return true;
    }

    /**
     * 通过创建方法对象进行创建，并且把创建好的对象放入缓存map
     * @param key
     * @param supplier
     * @param createExpire
     * @return
     */
    private boolean createCache(K key, Function<K,V> supplier, long createExpire) {
        if (supplier == null)
            return false;

        try {
            V value = supplier.apply(key);
            if (value == null) {
                remove(key);
                return false;
            }

            if (expire == Long.MAX_VALUE) {
                this.put(key, value);
            } else {
              this.put(key, value, createExpire);
            }
            return true;
        }catch (Exception e) {
            Logger.error("Create with supplier failed: ", e);
            return false;
        }
    }

    @Override
    public V get(Object key, Function<K, V> appointedSupplier, Long createExpire, boolean refresh) {

        TimeMark<K> timeMark = cacheMark.get(key);
        appointedSupplier = appointedSupplier==null? supplier: appointedSupplier;
        createExpire = createExpire==null? expire: createExpire;

        //如果timeMark不存在，尝试创建，并且存储到缓存中
        if (timeMark == null) {
            synchronized (cacheMark) {
                timeMark = cacheMark.get(key);
                if (timeMark == null && createCache((K) key, appointedSupplier, createExpire)) {
                    timeMark = new TimeMark(this, key, createExpire);
                    cacheMark.put((K) key, timeMark);
                }
            }
        }

        //如果timeMark存在，判断是否超时，并且刷新访问时间
        else {
            synchronized (timeMark) {
                if (!timeMark.isExpire()) {
                    timeMark.refresh(refresh);
                } else {
                    if (checkAndDoExpire(timeMark)) {
                        if (appointedSupplier != null) {
                            timeMark.refresh(true);
                            createCache((K) key, appointedSupplier, createExpire);
                        }
                    }
                }
            }
        }

        return cacheData.get(key);
    }

    public void putAll(Map<? extends  K, ? extends V> map) {
        putAll(map, expire);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map, long expire) {
        for (Entry<? extends K, ? extends V> entry: map.entrySet()) {
            cacheMark.put(entry.getKey(), new TimeMark<>(this, entry.getKey(), expire));
        }
        cacheData.putAll(map);
    }

    @Override
    public int size() {
        return cacheData.size();
    }

    @Override
    public boolean isEmpty() {
        return cacheData.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return cacheData.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return cacheData.containsValue(value);
    }

    @Override
    public V put(K key, V value) {
        return put(key, value, expire);
    }

    @Override
    public V put(K key, V value, long expire) {
        if (key == null || value == null){
            throw new NullPointerException();
        }

        cacheMark.put(key, new TimeMark<>(this, key, expire));
        return cacheData.put(key, value);
    }

    public V putIfAbsent(K key, V value) {
        return putIfAbsent(key, value, expire);
    }

    @Override
    public V putIfAbsent(K key, V value, long expire) {
        if (key == null || value == null){
            throw new NullPointerException();
        }

        V result = cacheData.putIfAbsent(key, value);
        cacheMark.putIfAbsent(key, new TimeMark<>(this, key, expire));

        return result;
    }

    public boolean isExpire(K k) {
       return cacheMark.get(k).isExpire();
    }

    /**
     * 清理过多的数据记录
     */
    private void fixSize() {
        //如果超出容量限制
        int diffSize = this.size() - maxSize;
        if (diffSize <= 0) {
            return;
        }

        List<TimeMark<K>> removedTimeMark =
                (List<TimeMark<K>>)CollectionSearch
                        .newInstance(cacheMark.values())
                        .setParallelStream(true)
                        .sort("visitCount")
                        .limit(10 * diffSize)
                        .sort("lastTime")
                        .limit(diffSize)
                        .search()
                        .stream()
                        .collect(Collectors.toList());

        for (TimeMark<K> timeMark: removedTimeMark) {
            if (destroy == null) {
                this.remove(timeMark.key);
            }

            else {
                V data = cacheData.get(timeMark.key);
                if (data == null) {
                    this.remove(timeMark.key);
                    continue;
                }

                Long value = destroy.apply(timeMark.getKey(), data);
                if (value == null) {
                    timeMark.refresh(true);
                } else if (value < 0) {
                    this.remove(timeMark.key);
                } else {
                    timeMark.setExpireTime(value);
                }

            }

        }
    }

    @Override
    public long getTTL(Object key) {
        TimeMark<K> timeMark = cacheMark.get(key);
        if (timeMark != null)
            return timeMark.getExpireTime();
        else
            return -1;
    }

    @Override
    public boolean setTTL(Object key, long expire) {
        TimeMark<K> timeMark = cacheMark.get(key);
        if (timeMark != null) {
            timeMark.setExpireTime(expire);
            timeMark.refresh(true);
            return true;
        } else
            return false;
    }


    @Override
    public V remove(Object key) {
        synchronized (key) {
            cacheMark.remove(key);
            return cacheData.remove(key);
        }
    }

    public boolean remove(Object key, Object value) {
        synchronized (key) {
            cacheMark.remove(key);
            return cacheData.remove(key, value);
        }
    }

    @Override
    public synchronized void clear() {
        cacheMark.clear();
        cacheData.clear();
    }

    @Override
    public Set<K> keySet() {
        return cacheData.keySet();
    }

    @Override
    public Collection<V> values() {
        return cacheData.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return cacheData.entrySet();
    }


    private class TimeMark<K> {

        private CacheMap<K, V> mainMap;
        private K key;

        //超时时间
        private AtomicLong expireTime = new AtomicLong(0);

        //最后访问时间
        private AtomicLong lastTime = new AtomicLong(0);

        //访问次数
        private AtomicLong visitCount = new AtomicLong(0);

        //是否正在生成数据
        private AtomicBoolean createFlag = new AtomicBoolean(false);

        public TimeMark(CacheMap<K, V> mainMap, K key, long expireTime){
            this.key = key;
            this.mainMap = mainMap;
            this.expireTime.set(expireTime);
            visitCount.set(0);
            refresh(true);
        }

        private void refresh(boolean updateLastTime) {
            visitCount.incrementAndGet();
            if (updateLastTime)
                this.lastTime.set(System.currentTimeMillis());
        }

        public boolean isExpire() {
            if (expireTime.get() == 0)
                return false;

            boolean b = System.currentTimeMillis()-lastTime.get() > expireTime.get()*1000;

            if (b) {
                return true;
            } else
                return false;
        }

        public CacheMap<K, V> getMainMap() {
            return mainMap;
        }

        public void setMainMap(CacheMap<K, V> mainMap) {
            this.mainMap = mainMap;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public long getExpireTime() {
            return expireTime.get();
        }

        public void setExpireTime(long expireTime) {
            this.expireTime.set(expireTime);
        }

        public long getLastTime() {
            return lastTime.get();
        }

        public AtomicLong getVisitCount() {
            return visitCount;
        }

        public boolean isOnCreate() {
            return createFlag.get();
        }

        public boolean tryLockOnCreate(){
            return createFlag.compareAndSet(false, true);
        }

        public void releaseCreateLock(){
            createFlag.set(false);
        }
    }

    public static void main(String[] args) {
        AtomicBoolean a = new AtomicBoolean(false);
        a.compareAndSet(false, true);
        System.out.println(a.get());

        CacheMap<Long, String> cacheMap = new CacheMap<Long, String>()
                .autoRemove(true)
                .maxSize(2)
                .expire(1)
                .supplier(id -> {
                    System.out.println(TDateTime.now() + " " + id);
                   return  "value-" + id;
                }).create();

        cacheMap.get(1L);
        TEnv.sleep(2000);
        System.out.println("1-" +cacheMap.cacheData);

        cacheMap.get(1L);
        cacheMap.get(2L);
        TEnv.sleep(2000);
        System.out.println("2-" +cacheMap.cacheData);

        cacheMap.get(3L);
        TEnv.sleep(2000);
        System.out.println("3-" +cacheMap.cacheData);

        TEnv.sleep(2000);
        System.out.println("4-" +cacheMap.cacheData);

        TEnv.sleep(Integer.MAX_VALUE);

    }

}
