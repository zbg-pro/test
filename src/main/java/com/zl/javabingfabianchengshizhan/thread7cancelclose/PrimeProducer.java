package com.zl.javabingfabianchengshizhan.thread7cancelclose;

import net.jcip.examples.LaunderThrowable;

import java.math.BigInteger;
import java.util.concurrent.*;


/**
 * @author Allen.zhang
 * @title: PrimeProducer
 * @projectName zl
 * @description:  BrokenPrimeProducer的问题解决方式：不要通过boolean进行中断，通过中断进行请求取消
 * @date 2021/12/241:15
 */
public class PrimeProducer extends Thread {

    private final BlockingQueue<BigInteger> queue;

    PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }


    public void run(){
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted()) {
                queue.put(p = p.nextProbablePrime());
            }
        } catch (InterruptedException e) {
            //允许线程推出
        }
    }

    public void cancel (){
        interrupt();
    }


    /**
     * 1 这是一种非常简单的中断方式，但是却破坏了以下规则，在中断线程之前，应了解中断策略，
     * 2 因为timedRun可以从任何一个线程中调用，因此他无法知道这个调用线程的中断策略
     * 3 如果任务在超时之前完成，那么中断操作timedRun所被调用的线程的取消任务的执行，
     *    时间时间点将是：本线程执行完成之后才运行的，而我们不知道这种情况下，执行的结果是好还是坏
     * 4 而且如果任务不响应中断，那么timedRun会在任务结束时候才返回，此时可能已经超过了指定的时限或者没超过，
     * 如果某个时限运行的任务没有在指定时间内返回，那么将对调用者带来负面影响
     */
    private static final ScheduledExecutorService cancelExecutor = new ScheduledThreadPoolExecutor(30);
    public static void timedRun(Runnable runnable, long timeout, TimeUnit timeUnit){
        final Thread taskThread = Thread.currentThread();
        cancelExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                taskThread.interrupt();
            }
        }, timeout, timeUnit);

        runnable.run();
    }


    /**
     * 在专门的线程中执行中断任务
     * 在这个示例中解决了前面示例的问题，但是由于依赖了join，无法知道执行控制是因为线程的正常推出还是因为join超时而返回
     */
    public static void timedRun2(final Runnable runnable, long timeout, TimeUnit timeUnit) throws InterruptedException {

        class RethrowableTask implements Runnable {

            private volatile Throwable t;
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Throwable t) {
                    this.t = t;
                }
            }

            void rethrow(){
                if (t != null) {
                    throw LaunderThrowable.launderThrowable(t);
                }
            }
        }

        RethrowableTask task = new RethrowableTask();
        final Thread taskThread = new Thread(task);
        taskThread.start();

        cancelExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                taskThread.interrupt();
            }
        },timeout, timeUnit);

        //相当于双重机制保证timeout时间后结束线程
        taskThread.join(timeUnit.toMillis(timeout));
        task.rethrow();

    }

    public static void timedRun3(Runnable runnable, long timeout, TimeUnit timeUnit){
        Future<?> task = cancelExecutor.submit(runnable);

        try {
            task.get(timeout,timeUnit);
        } catch (TimeoutException e) {
            task.cancel(true);
        } catch (ExecutionException | InterruptedException e) {
            throw LaunderThrowable.launderThrowable(e);
        } finally {
            task.cancel(true);
        }



    }



}
