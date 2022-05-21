package com.zl.javabingfabianchengshizhan.thread15;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * @author Allen.zhang
 * @title: SimulatedCAS
 * @projectName zl
 * @description: TODO
 * @date 2022/5/1020:21
 */
@ThreadSafe
public class SimulatedCAS {

    @GuardedBy("this")
    private int value;

    public synchronized int get() {
        return value;
    }

    public synchronized int compareAndSwap(int expectedValue, int newValue) {
        int oldValue = value;
        if (oldValue == expectedValue) {
            value = newValue;
        }
        return oldValue;
    }

    public synchronized boolean compareAndSet(int expectedValue, int newValue) {
        return expectedValue == compareAndSwap(expectedValue, newValue);
    }

}
