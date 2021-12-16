package com.zl.javabingfabianchengshizhan.synutils;

import org.voovan.tools.TEnv;

import java.util.concurrent.CountDownLatch;

/**
 * @author Allen.zhang
 * @title: TestHarness
 * @projectName zl
 * @description: TODO
 * @date 2021/11/81:01
 */
public class TestHarness {

    public static long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread thread = new Thread(() -> {
                try {
                    startGate.await();

                    try {
                        task.run();
                    } finally {
                        endGate.countDown();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

            thread.start();
        }
        
        long startTime = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long endTime = System.nanoTime();
        return endTime-startTime;

    }

    public static void main(String[] args) throws InterruptedException {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TEnv.sleep(2);
            }
        };

        long a = timeTasks(10, runnable);
        System.out.println(a/1000);

    }


}
