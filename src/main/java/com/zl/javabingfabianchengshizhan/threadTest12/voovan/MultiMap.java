package com.zl.javabingfabianchengshizhan.threadTest12.voovan;

import org.voovan.tools.json.JSON;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Allen.zhang
 * @title: MultiMap
 * @projectName zl
 * @description: TODO
 * @date 2022/3/173:16
 */
public class MultiMap<K, V> extends ConcurrentHashMap<K, List<V>> {

    public MultiMap() {}

    public MultiMap(Map<K, List<V>> map) {
        super(map);
    }

    public List<V> getValues(K key){
        List<V> values = get(key);

        if (values == null || values.isEmpty())
            return null;

        return values;
    }

    public V getValue(K key, int index) {
        List<V> values = get(key);
        if (values == null)
            return null;

        return values.get(index);
    }

    public List<V> putValue(K key, V value) {
        if (value == null) {
            return (List)super.put(key, null);
        }
        List<V> vals = getValueList(key);
        vals.add(value);
        return vals;
    }

    private List<V> getValueList(K key) {
        List<V> lo = get(key);
        if (lo == null) {
            lo = new Vector<V>();
            List retValue = putIfAbsent(key, lo);
            lo = retValue == null ? lo : retValue;
        }
        return lo;
    }

    public void putAllValue(Map<K, V> input) {
        for (Entry<K, V> entry: input.entrySet()) {
            putValue(entry.getKey(), entry.getValue());
        }
    }

    public List<V>  putValues(K k, List<V> values){
       return super.put(k, values);
    }

    public List<V> putValues(K key, V... values) {
        List<V> list = getValueList(key);
        list.addAll(Arrays.asList(values));
        return super.put(key, list);
    }

    public void addValue(K key , V value) {

    }

    public void addValues(K Key, List<V> list) {

    }

    public void addValue(K key, V... values) {

    }

    public void addAllValue(MultiMap<K,V> map) {

    }

    public V removeValue(K key, int index) {
        List<V> lo = (List)get(key);
        if ((lo == null) || (lo.isEmpty())) {
            return null;
        }
        V ret = lo.remove(index);
        if (lo.isEmpty()) {
            remove(key);
        }

        return ret;
    }

    public boolean removeValue(K key, V value) {
        List<V> lo = get(key);
        if ((lo == null) || (lo.isEmpty())) {
            return false;
        }
        boolean ret = lo.remove(value);
        if (lo.isEmpty()) {
            remove(key);
        }

        return ret;
    }

    public boolean containsValues(V o){
        for (List<V> values: values()) {
            if (values.size() > 0 && values.contains(0)) {
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        return JSON.toJSON(this);
    }

}

