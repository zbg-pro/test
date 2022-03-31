package com.zl.javabingfabianchengshizhan.thread14;

import net.jcip.annotations.GuardedBy;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Allen.zhang
 * @title: SemaphoreOnLock
 * @projectName zl
 * @description: TODO
 * @date 2022/3/2312:27
 */
public class SemaphoreOnLock {
    private final Lock lock = new ReentrantLock();

    private final Condition permitsAvailable = lock.newCondition();

    @GuardedBy("lock")
    private int permits;

    public SemaphoreOnLock(int initialPermits) {
        lock.lock();
        try {
            this.permits = initialPermits;
        }finally {
            lock.unlock();
        }
    }

    public void acquire() throws InterruptedException {
        Semaphore semaphore = new Semaphore(12);
        semaphore.tryAcquire(1);
        lock.lock();
        try {
            while (permits <= 0)
                permitsAvailable.await();
            --permits;
        } finally {
            lock.unlock();
        }
    }

    public void release(){
        lock.lock();
        try {
            ++permits;
            permitsAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

}
