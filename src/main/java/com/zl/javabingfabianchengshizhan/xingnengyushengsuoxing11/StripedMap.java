package com.zl.javabingfabianchengshizhan.xingnengyushengsuoxing11;

/**
 * @author Allen.zhang
 * @title: StripedMap
 * @projectName zl
 * @description: TODO
 * @date 2022/3/920:57
 */
public class StripedMap {
    private static class Node {
        Node next;
        Object key;
        Object value;
    }

    private final Object[] locks;
    private final Node[] buckets;
    private static final int N_LOCKS = 16;

    public StripedMap(){
        buckets = new Node[N_LOCKS];
        locks = new Object[N_LOCKS];
        for (int i = 0; i < N_LOCKS; i++)
            locks[i] = new Object();
    }

    public StripedMap(int numBuckets){
        buckets = new Node[numBuckets];
        locks = new Object[N_LOCKS];
        for (int i = 0; i < N_LOCKS; i++)
            locks[i] = new Object();
    }

    private final int hash(Object key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    public Object get(Object key) {
        int hash = hash(key);
        synchronized (locks[hash%N_LOCKS]) {
            for (Node m = buckets[hash]; m != null; m = m.next) {
                if (m.key.equals(key))
                    return m.value;
            }
        }

        return null;
    }

    public void put(Object key, Object value){
        //...
        int hash = hash(key);
        synchronized (locks[hash%N_LOCKS]) {

            Node m = buckets[hash];
            if (m != null) {
                while (m.next != null) {
                    m = m.next;
                }
                Node nextNode = new Node();
                nextNode.key = key;
                nextNode.value = value;
                nextNode.next = null;
                m.next = nextNode;
            } else {
                m = new Node();
                m.key = key;
                m.value = value;
                m.next = null;
                buckets[hash] = m;
            }

        }
    }

    public void clear(){
        for (int i = 0; i < buckets.length; i++) {
            synchronized (locks[i%N_LOCKS]) {
                buckets[i] = null;
            }
        }
    }

    public static void main(String[] args) {
        StripedMap stripedMap = new StripedMap();
        stripedMap.put("aa", "bb");
        System.out.println(stripedMap.get("aa"));
        stripedMap.put("cc", "dd");
        System.out.println(stripedMap.get("cc"));

    }

}
