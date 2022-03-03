package com.zl.javabingfabianchengshizhan.thread7cancelclose.newtaskfor;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.*;

/**
 * @author Allen.zhang
 * @title: CancellExecutor
 * @projectName zl
 * @description: TODO
 * @date 2022/1/41:32
 */
@ThreadSafe
public class CancellableExecutor extends ThreadPoolExecutor {

    public CancellableExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof CancellableTask) {
            return ((CancellableTask<T>) callable).newTask();
        } else {
            return super.newTaskFor(callable);
        }

    }
}
