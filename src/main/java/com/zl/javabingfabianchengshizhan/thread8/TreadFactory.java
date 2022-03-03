package com.zl.javabingfabianchengshizhan.thread8;

/**
 * @author Allen.zhang
 * @title: TreadFactory
 * @projectName zl
 * @description: TODO
 * @date 2022/3/22:30
 */
public interface TreadFactory {
    Thread newThread(Runnable runnable);
}
