package com.zl.javabingfabianchengshizhan.thread8;

import org.voovan.tools.log.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Allen.zhang
 * @title: MyAppThread
 * @projectName zl
 * @description: TODO
 * @date 2022/3/22:33
 */
public class MyAppThread extends Thread {

    public static final String DEFAULT_NAME = "MyAppThread";

    public static volatile boolean debugLifecycle = false;

    public static final AtomicInteger created = new AtomicInteger();

    public static final AtomicInteger alive = new AtomicInteger();

    public MyAppThread(Runnable r) {
        this(r, DEFAULT_NAME);
    }

    public MyAppThread(Runnable runnable, String poolName) {
        super(runnable, poolName);
        created.incrementAndGet();
        setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Logger.error("UNCAUGHT in thread:" + t.getName(), e);
            }
        });
    }

    public void run(){
        boolean debug = debugLifecycle;
        if (debug) Logger.debugf("created: {}", getName());

        try {
            alive.incrementAndGet();
            super.run();
        } finally {
            alive.decrementAndGet();
            if (debug) Logger.debugf("Exiting:{}", getName());
        }
    }

    public static boolean getDebug() {
        return debugLifecycle;
    }

    public static void setDebug(boolean b) {
        debugLifecycle = b;
    }

    public  static int getCreated(){
        return created.get();
    }

    public  static int getAlive(){
        return alive.get();
    }
}
