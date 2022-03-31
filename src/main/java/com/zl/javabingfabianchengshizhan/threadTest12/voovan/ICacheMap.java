package com.zl.javabingfabianchengshizhan.threadTest12.voovan;

import java.util.Map;
import java.util.function.Function;

/**
 * @author Allen.zhang
 * @title: ICacheMap
 * @projectName zl
 * @description: TODO
 * @date 2022/3/1813:10
 */
public interface ICacheMap<K, V> extends Map<K, V> {

    V put(K key, V value, long expire);

    V putIfAbsent(K key, V value, long expire);

    void putAll(Map<? extends K, ? extends V> map, long expire);

    /**
     * 获取数据创建的 Function 对象
     * @return Function
     */
    Function<K, V> getSupplier();

    ICacheMap<K, V> supplier(Function<K, V> buildSupplier);

    long getExpire();

    ICacheMap<K, V> expire(long expire);

    long getTTL(K key);

    /**
     * 更新某个key的超时时间
     * @param key
     * @param expire
     * @return true or false
     */
    boolean setTTL(K key, long expire);

    V get(Object k, Function<K, V> appointedSupplier, Long expire, boolean refresh);

     default V get(Object k, Function<K, V> appointedSupplier, Long expire) {
        return get(k, appointedSupplier, expire, false);
    }

    default V get(Object k, Function<K, V> appointedSupplier) {
         return get(k, appointedSupplier, null, false);
    }

    default V get(Object key) {
        return get(key, null, null, false);
    }

    default V getAndRefresh(Object key, Function<K, V> appointedSupplier, long expire){
         return get(key, appointedSupplier, expire, true);
    }

    default V getAndRefresh(Object key, Function<K, V> appointedSupplier){
         return get(key, appointedSupplier, null, true);
    }

    default V getAndRefresh(Object key){
         return get(key, null, null, true);
    }
}
