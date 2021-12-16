package com.zl.javabingfabianchengshizhan.threadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Allen.zhang
 * @title: NotSafeNumberRanger
 * @projectName zl
 * @description: TODO
 * @date 2021/11/71:45
 */
public class NotSafeNumberRanger {

    private final AtomicInteger lower = new AtomicInteger(0);

    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i) {
        if (i > upper.get()) {
            throw new IllegalArgumentException("can't set lower " + i + " > upper");
        }
        lower.set(i);
    }

    public void setUpper(int i){
        if (i < lower.get()) {
            throw new IllegalArgumentException("can't set upper " + i + " < lower");
        }
        upper.set(i);
    }

    public boolean isInRanger(int i) {
        return (i>=lower.get() && i<upper.get());
    }

}

