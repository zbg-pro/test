package com.zl.javabingfabianchengshizhan.thread8;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

/**
 * @author Allen.zhang
 * @title: BoundedExecutor
 * @projectName zl
 * @description: TODO
 * @date 2022/3/22:06
 */
@ThreadSafe
public class BoundedExecutor {

    private final Executor executor;

    private final Semaphore semaphore;


    public BoundedExecutor(Executor executor, Semaphore semaphore) {
        this.executor = executor;
        this.semaphore = semaphore;
    }

    public void submitTask(final Runnable command) throws InterruptedException {
        semaphore.acquire();
        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        command.run();
                    } finally {
                        semaphore.release();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }

    }

}
