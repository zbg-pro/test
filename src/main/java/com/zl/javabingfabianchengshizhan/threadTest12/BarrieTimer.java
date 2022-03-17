package com.zl.javabingfabianchengshizhan.threadTest12;

/**
 * @author Allen.zhang
 * @title: BarrieTimer
 * @projectName zl
 * @description: TODO
 * @date 2022/3/172:14
 */
public class BarrieTimer implements Runnable{

    private boolean started;

    private long startNanoTime, endNanoTime;

    @Override
    public void run() {
        long t = System.nanoTime();
        if (!started) {
            started = true;
            startNanoTime = t;
        } else {
            endNanoTime = t;
        }
    }

    public synchronized void clear() {
        started = false;
    }

    public synchronized long getTime(){
       return  endNanoTime - startNanoTime;
    }
}
