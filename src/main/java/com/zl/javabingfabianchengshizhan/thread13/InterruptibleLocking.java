package com.zl.javabingfabianchengshizhan.thread13;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Allen.zhang
 * @title: InterruptiableLocking
 * @projectName zl
 * @description: TODO
 * @date 2022/3/1915:39
 */
public class InterruptibleLocking {

    private Lock lock = new ReentrantLock();

    public boolean sendOnShareLine(String message) throws InterruptedException {
        lock.lockInterruptibly();

        try {
            return cancellableSendOnShareLine(message);
        } finally {
            lock.unlock();
        }
    }

    private boolean cancellableSendOnShareLine(String message) {
        //dosth
        return true;
    }
}
