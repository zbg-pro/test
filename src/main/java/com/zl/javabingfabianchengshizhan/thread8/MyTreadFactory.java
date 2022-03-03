package com.zl.javabingfabianchengshizhan.thread8;

/**
 * @author Allen.zhang
 * @title: MyTreadFactory
 * @projectName zl
 * @description: TODO
 * @date 2022/3/22:31
 */
public class MyTreadFactory implements TreadFactory{

    private final String poolName;

    public MyTreadFactory(String poolName) {
        this.poolName = poolName;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new MyAppThread(runnable, poolName);
    }
}
