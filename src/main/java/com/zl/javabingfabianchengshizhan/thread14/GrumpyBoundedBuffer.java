package com.zl.javabingfabianchengshizhan.thread14;

import com.zl.javabingfabianchengshizhan.threadTest12.BaseBoundedBuffer;
import org.voovan.tools.TEnv;

/**
 * @author Allen.zhang
 * @title: GrumpyBoundedBuffer
 * @projectName zl
 * @description: TODO
 * @date 2022/3/2114:11
 */
public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

    protected GrumpyBoundedBuffer(int capacity) {
        super(capacity);
    }

    public synchronized void put(V v) {
        if (isFull()) {
            throw new BufferFullException();
        }
        super.doPut(v);
    }

    public synchronized V take() {
        if (isEmpty()) {
            throw new BufferEmptyException();
        }

       return super.doTake();
    }

}

class ExampleUsage {
    private GrumpyBoundedBuffer<String> grumpyBoundedBuffer = null;
     int SLEEP_GRANULARITY = 50;

    void usage() {
        while (true) {
            try {
                grumpyBoundedBuffer.take();
                break;
            } catch (Exception e) {
                TEnv.sleep(SLEEP_GRANULARITY);
            }
        }
    }
}

class BufferFullException extends RuntimeException {}

class BufferEmptyException extends RuntimeException {}
