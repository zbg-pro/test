package com.zl.javabingfabianchengshizhan.threadTest12;

import java.util.concurrent.CyclicBarrier;

/**
 * @author Allen.zhang
 * @title: TimedPutTakeTest
 * @projectName zl
 * @description: TODO
 * @date 2022/3/172:14
 */
public class TimedPutTakeTest extends PutTakeTest {

    private BarrieTimer timer = new BarrieTimer();

    public TimedPutTakeTest(int capacity, int nparis, int ntrials) {
        super(capacity, nparis, ntrials);
        barrier = new CyclicBarrier(nparis*2+1, timer);
    }

    public void test() {
        try {
            timer.clear();
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }

            barrier.await(); // wait for all threads to be ready
            barrier.await(); // wait for all threads to finish

            long nsPerItem = timer.getTime()/(nPairs*(long)nTrials);
            System.out.println("Throughput: " + nsPerItem + " ns/item");

            System.out.println(putSum.get() + "\n" + takeSum.get());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        int tpt = 1000; // trials per thread
        /*for (int cap = 1; cap <= 1000; cap *= 10) {
            System.out.println("Capacity: " + cap);
            for (int pairs = 1; pairs <= 128; pairs *= 2) {
                TimedPutTakeTest t = new TimedPutTakeTest(cap, pairs, tpt);
                System.out.println("Pairs: " + pairs + "\t");
                t.test();
                System.out.print("\n");
                Thread.sleep(1000);
                *//*t.test();
                System.out.println();
                Thread.sleep(1000);*//*
            }
        }*/

        TimedPutTakeTest t = new TimedPutTakeTest(1000, 64, tpt);
        t.test();
        pool.shutdown();
    }
}
