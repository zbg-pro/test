package com.zl.gaozhiliang152jianyi.thread;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @version 1.0
 * @desc:
 * @date 2021/8/2 8:20 下午
 * @auth ALLEN
 */
public class LockSynTest {

    public static void runTasks(Class<? extends Runnable> clz) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println("***开始执行" + clz.getName() + "任务***");

        for (int i = 0; i < 3; i++) {
            executorService.submit(clz.newInstance());
        }

        TimeUnit.SECONDS.sleep(6);

        System.out.println("***执行" + clz.getName() + "任务完毕***\n");
        executorService.shutdown();
    }

    public static void main(String[] args) throws Exception {
        runTasks(TaskWithLock.class);
        runTasks(TaskWithSync.class);
    }
}

class Task {
    public void doSomething(){
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuffer sb = new StringBuffer();
        sb.append("线程名称：").append(Thread.currentThread().getName());
        sb.append("执行时间：").append(Calendar.getInstance().get(13) + "s");
        System.out.println(sb.toString());
    }
}

class TaskWithLock extends Task implements Runnable {

    private static final Lock lock = new ReentrantLock();

    @Override
    public void run() {
        try {
            lock.lock();
            doSomething();
        }finally {
            lock.unlock();
        }
    }
}

class TaskWithSync extends Task implements Runnable {
    @Override
    public void run() {
        synchronized ("A") {
            doSomething();
        }
    }
}
