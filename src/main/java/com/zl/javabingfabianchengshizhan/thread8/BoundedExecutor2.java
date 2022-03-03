package com.zl.javabingfabianchengshizhan.thread8;

import net.jcip.annotations.ThreadSafe;

import javax.security.auth.callback.Callback;
import java.util.concurrent.*;

/**
 * @author Allen.zhang
 * @title: BoundedExecutor
 * @projectName zl
 * @description: TODO
 * @date 2022/3/22:06
 */
@ThreadSafe
public class BoundedExecutor2 {

    private final ExecutorService executor;

    private final Semaphore semaphore;


    public BoundedExecutor2(ExecutorService executor, Semaphore semaphore) {
        this.executor = executor;
        this.semaphore = semaphore;
    }

    public <T extends Callable> void submitTask(final Callable<T> command) throws InterruptedException {
        semaphore.acquire();
        try {
            executor.submit(() -> {
                try {
                    return command.call();
                } catch (Exception e) {
                    System.out.println("error");
                } finally {
                    semaphore.release();
                }
                return null;
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }

    }

}
