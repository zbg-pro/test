package com.zl.javabingfabianchengshizhan.threadTest12;

import net.jcip.annotations.ThreadSafe;

/**
 * @author Allen.zhang
 * @title: BoundedBuffer
 * @projectName zl
 * @description: TODO
 * @date 2022/3/110:39
 */
@ThreadSafe
public class BoundedBuffer <V> extends BaseBoundedBuffer<V> {

    protected BoundedBuffer() {
        this(100);
    }

    protected BoundedBuffer(int capacity) {
        super(capacity);
    }

    public synchronized void put(V v) throws InterruptedException {
        while (isFull())
            wait();

        doPut(v);
        notifyAll();
    }

    public synchronized V take() throws InterruptedException {
        while (isEmpty())
            wait();

        V v = doTake();
        notifyAll();
        return v;
    }

    public synchronized void alternatePut (V v) throws InterruptedException {
        while (isFull()) {
            wait();
        }

        boolean wasEmpty = isEmpty();
        doPut(v);
        if (wasEmpty)
            notifyAll();

    }


}
