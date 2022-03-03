package com.zl.javabingfabianchengshizhan.thread8;

import org.voovan.tools.log.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Allen.zhang
 * @title: TimingThreadPool
 * @projectName zl
 * @description: TODO
 * @date 2022/3/213:30
 */
public class TimingThreadPool extends ThreadPoolExecutor {

    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTime = new AtomicLong();

    public TimingThreadPool() {
        super(1, 1, 0L, TimeUnit.SECONDS, null);
    }

    protected void beforeExecute(Thread t, Runnable r){
        super.beforeExecute(t, r);
        Logger.infof("Thread :{}, start:{}", t, r);
        startTime.set(System.nanoTime());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try {
            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();
            numTasks.incrementAndGet();
            totalTime.addAndGet(taskTime);
            Logger.infof("Thread :{}, start:{}, taskTime:{}", t, r, taskTime);
        } finally {
            super.afterExecute(r, t);
        }
    }

    @Override
    protected void terminated() {
        try {
            Logger.infof("terminated: avg:{}", totalTime.get()/numTasks.get());
        } finally {
            super.terminated();
        }
    }
}
