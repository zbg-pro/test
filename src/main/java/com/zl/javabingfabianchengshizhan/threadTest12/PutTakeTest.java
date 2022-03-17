package com.zl.javabingfabianchengshizhan.threadTest12;

import junit.framework.TestCase;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Allen.zhang
 * @title: PutTakeTest
 * @projectName zl
 * @description: TODO
 * @date 2022/3/1421:56
 */
public class PutTakeTest  extends TestCase {

    protected static final ExecutorService pool = Executors.newCachedThreadPool();

    protected final AtomicLong putSum = new AtomicLong(0);
    protected final AtomicLong takeSum =  new AtomicLong(0);

    protected final int nTrials, nPairs;

    protected final SemaphoreBoundedBuffer<Long> bb;
    protected CyclicBarrier barrier;

    public PutTakeTest(int capacity, int nparis, int ntrials){
        this.bb = new SemaphoreBoundedBuffer<>(capacity);

        this.nTrials = ntrials;
        this.nPairs = nparis;
        this.barrier = new CyclicBarrier(nparis*2+1);
    }

    public void test(){
        try {
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            barrier.await();//全部准备好开始添加和弹出
            barrier.await();//全部准备好计算完成
            System.out.println(putSum.get() + "\n" + takeSum.get());

            assertEquals(putSum.get(), takeSum.get());
            assertNotSame(putSum.get(), takeSum.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    static long xorShift(long y) {
        /*y ^= (y << 2);
        y ^= (y >> 3);
        y ^= (y << 4);*/
        long x = y+System.nanoTime()%19;
        return x;
    }

    class Producer implements Runnable {

        @Override
        public void run() {
            try {
                long seed = System.nanoTime()%31;
                int sum = 0;
                barrier.await();

                for (int i = nTrials; i > 0 ; --i) {
                    bb.put(seed);
                    sum += seed;
                    seed = xorShift(seed);
                }
                putSum.getAndAdd(sum);
                barrier.await();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    class Consumer implements Runnable {
        @Override
        public void run() {

            try {
                barrier.await();
                int sum = 0;
                for (int i = nTrials; i > 0; --i) {
                    sum += bb.take();
                }
                takeSum.getAndAdd(sum);
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static void main(String[] args) throws Exception {
        new PutTakeTest(10, 10, 20000).test();
        pool.shutdown();
    }



}
