package com.zl.javabingfabianchengshizhan.thread14;

import net.jcip.annotations.GuardedBy;

/**
 * @author Allen.zhang
 * @title: ThreadGate
 * @projectName zl
 * @description: TODO
 * @date 2022/3/2117:24
 */
public class ThreadGate {

    @GuardedBy("this")
    private boolean isOpen;

    @GuardedBy("this")
    private int generation;

    public synchronized void close(){
        isOpen = false;
    }

    public synchronized void open(){
        ++generation;
        isOpen = true;
        notifyAll();
    }

    public synchronized void await() throws InterruptedException {
        int arrivalGeneration = generation;
        while (!isOpen && arrivalGeneration == generation) {
            wait();
        }
    }
}
