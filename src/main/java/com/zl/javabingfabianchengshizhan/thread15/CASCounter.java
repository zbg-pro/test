package com.zl.javabingfabianchengshizhan.thread15;

import net.jcip.annotations.ThreadSafe;

/**
 * @author Allen.zhang
 * @title: CASCounter
 * @projectName zl
 * @description: TODO
 * @date 2022/5/1021:14
 */
@ThreadSafe
public class CASCounter {
    private SimulatedCAS value;

    public int getValue() {
        return value.get();
    }

    public int increment() {
        int v;
        do {
            v = value.get();
        }while (v != value.compareAndSwap(v, v+1));
        return v+1;
    }


}
