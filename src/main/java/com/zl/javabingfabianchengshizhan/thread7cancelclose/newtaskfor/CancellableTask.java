package com.zl.javabingfabianchengshizhan.thread7cancelclose.newtaskfor;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

/**
 * @author Allen.zhang
 * @title: CancellableTask
 * @projectName zl
 * @description: TODO
 * @date 2022/1/41:30
 */
public interface CancellableTask<T> extends Callable<T> {
    void cancel();
    RunnableFuture<T> newTask();
}
