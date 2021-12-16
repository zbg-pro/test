package com.zl.javabingfabianchengshizhan.synutils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * @author Allen.zhang
 * @title: BoundedHasH
 * @projectName zl
 * @description: TODO
 * @date 2021/11/81:47
 */
public class BoundedHashSet<T> {

    private final Set<T> set;

    private final Semaphore semaphore;

    public BoundedHashSet(int bound){
        this.set = Collections.synchronizedSet(new HashSet<>());
        this.semaphore = new Semaphore(bound);
    }

    public boolean add(T obj) throws InterruptedException {
        semaphore.acquire();

        boolean wasAdded = false;
        try {
            wasAdded = set.add(obj);
            return wasAdded;
        } finally {
            if (!wasAdded) {
                semaphore.release();
            }
        }

    }

    public boolean remove(T obj){
        boolean wasRemoved = set.remove(obj);
        if (wasRemoved) {
            semaphore.release();
        }

        return wasRemoved;
    }

}
