package com.zl.gaozhiliang152jianyi.thread;

import org.voovan.tools.TDateTime;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CyclicBarrierTest {

    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(2, new Runnable() {
            @Override
            public void run() {
                System.out.println(TDateTime.now() + "隧道已经打通后的【回调】，他们全部执行完成后才会打印");
            }
        });

        new Thread(new Worker(cb), "工人1").start();
        new Thread(new Worker(cb), "工人2").start();

    }

}

class Worker implements Runnable {

    private CyclicBarrier cb;

    public Worker(CyclicBarrier cb){
        this.cb = cb;
    }

    @Override
    public void run() {
        try {
            //Thread.sleep(new Random().nextInt(1000));
            TimeUnit.SECONDS.sleep(new Random().nextInt(10));
            System.out.println(TDateTime.now() +Thread.currentThread().getName() + "-到达汇合点");
            cb.await();
        } catch (Exception e) {

        }
    }
}