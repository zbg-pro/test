package com.zl.gaozhiliang152jianyi.thread;

import org.voovan.tools.TDateTime;

/**
 * @version 1.0
 * @desc:
 * @date 2021/7/29 11:04 上午
 * @auth ALLEN
 */
public class TestThread implements Runnable {
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        for (int i = 0; i < 100000; i++) {
            Math.hypot(Math.pow(924526789.6, i), Math.cos(i));
        }

        System.out.println(TDateTime.now("yyyy-MM-dd HH:mm:ss:SS z") + " Priority:" + Thread.currentThread().getPriority());

    }

    public void start(int priority){
        Thread t = new Thread(this);

        t.setPriority(priority);

        t.start();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new TestThread().start(i % 10 + 1);
        }

        String DATE = TDateTime.now("yyyyMMdd");
        System.out.println(DATE);
    }


}
