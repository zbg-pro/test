package com.zl.javabingfabianchengshizhan.thread13;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Allen.zhang
 * @title: TimedLocking
 * @projectName zl
 * @description: TODO
 * @date 2022/3/1915:43
 */
public class TimedLocking {

    private Lock lock = new ReentrantLock();

    public boolean trySendOnShareLine(String message, long timeout, TimeUnit timeUnit) throws InterruptedException {
        long a = timeUnit.toNanos(timeout) - costTime(message);

        boolean b = lock.tryLock(a, timeUnit);
        if (!b)
            return false;

        try {
            return sendOnShareLine(message);
        } finally {
            lock.unlock();
        }

    }

    private boolean sendOnShareLine(String message) {
        return true;
    }

    private long costTime(String message) {
        return message.length();
    }


}
