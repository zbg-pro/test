package com.zl.daimazhengjiezhidao.thread.pool;

/**
 * @author Allen.zhang
 * @title: ThreadPreRequestScheduler
 * @projectName zl
 * @description: TODO
 * @date 2021/10/3014:10
 */
public class ThreadPreRequestScheduler implements ClientScheduler{
    @Override
    public void schedule(ClientRequestProcessor clientRequestProcessor) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                clientRequestProcessor.process();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
