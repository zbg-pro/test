package com.zl.daimazhengjiezhidao.thread.pool;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Allen.zhang
 * @title: ExceutorClientScheduler
 * @projectName zl
 * @description: TODO
 * @date 2021/10/3014:25
 */
public class ExecutorClientScheduler implements ClientScheduler {

    Executor executor;

    public ExecutorClientScheduler(int availableTreads){
        executor = Executors.newFixedThreadPool(availableTreads);
    }

    @Override
    public void schedule(final ClientRequestProcessor requestProcessor) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                requestProcessor.process();
            }
        };
        executor.execute(runnable);
    }



}
