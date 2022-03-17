package com.zl.javabingfabianchengshizhan.thread8.puzzlebox;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;

/**
 * @author Allen.zhang
 * @title: ValueLatch
 * @projectName zl
 * @description: TODO
 * @date 2022/3/412:17
 */
@ThreadSafe
public class ValueLatch<T> {

    @GuardedBy("this")
    private T value = null;

    private final CountDownLatch done = new CountDownLatch(1);

    public boolean isSet() {
        return done.getCount() == 0;
    }

    public synchronized void setValue(T value) {
        if (!isSet()) {
            this.value = value;
            done.countDown();
        }
    }

    public T getValue() throws InterruptedException {
        done.await();
        synchronized (this) {
            return this.value;
        }
    }

}
