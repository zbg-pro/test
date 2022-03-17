package com.zl.javabingfabianchengshizhan.threadTest12;

import net.jcip.annotations.GuardedBy;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Allen.zhang
 * @title: BoundedBuffer2
 * @projectName zl
 * @description: TODO
 * @date 2022/3/111:26
 */
public class SemaphoreBoundedBuffer<E> {

    private final Semaphore availableItems, availableSpaces;

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock rLock = rwLock.readLock();
    private final Lock wLock = rwLock.writeLock();


    @GuardedBy("this")
    private final E[] items;

    @GuardedBy("this")
    private int putPosition = 0, takePosition = 0;

    public SemaphoreBoundedBuffer(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException();
        availableItems = new Semaphore(0);
        availableSpaces = new Semaphore(capacity);
        items = (E[])new Object[capacity];
    }

    public boolean isEmpty(){
        return availableItems.availablePermits() == 0;
    }

    public boolean isFull(){
        return availableSpaces.availablePermits() == 0;
    }

    public void put(E x) throws InterruptedException {
        availableSpaces.acquire();
        doInsert2(x);
        availableItems.release();
    }

    public E take() throws InterruptedException {
        availableItems.acquire();
        E item = doExtract2();
        availableSpaces.release();
        return item;
    }

    public synchronized void doInsert(E x){
        int i = putPosition;
        items[i] = x;
        putPosition = (++i == items.length)?0:i;
    }

    private synchronized E doExtract() {
        int i = takePosition;
        E x = items[i];
        takePosition = (++i == items.length)? 0: i;
        return x;
    }

    public void doInsert2(E x){
        wLock.lock();
        try {
            int i = putPosition;
            items[i] = x;
            putPosition = (++i == items.length)?0:i;
        } finally {
            wLock.unlock();
        }
    }

    private E doExtract2() {
        wLock.lock();
        try {
            int i = takePosition;
            E x = items[i];
            takePosition = (++i == items.length)? 0: i;
            return x;
        } finally {
            wLock.unlock();
        }

    }

}
