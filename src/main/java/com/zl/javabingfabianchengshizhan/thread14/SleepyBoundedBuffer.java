package com.zl.javabingfabianchengshizhan.thread14;

import com.zl.javabingfabianchengshizhan.threadTest12.BaseBoundedBuffer;
import org.voovan.tools.TEnv;

/**
 * @author Allen.zhang
 * @title: SleepBoundedBuffer
 * @projectName zl
 * @description: TODO
 * @date 2022/3/2115:05
 */
public class SleepyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

    int SLEEP_GRANULARITY = 60;

    protected SleepyBoundedBuffer() {
        super(100);
    }

    protected SleepyBoundedBuffer(int size) {
        super(size);
    }

    public void put(V v){
        while (true) {

            synchronized (this) {
                if (!isFull()) {
                    super.doPut(v);
                    return;
                }
            }
            TEnv.sleep(SLEEP_GRANULARITY);

        }
    }

    public V take(){
        while (true) {

            synchronized (this) {
                if (!isEmpty()) {
                   return super.doTake();
                }
            }
            TEnv.sleep(SLEEP_GRANULARITY);

        }
    }

}
