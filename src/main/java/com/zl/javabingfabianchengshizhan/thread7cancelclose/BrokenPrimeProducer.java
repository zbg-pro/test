package com.zl.javabingfabianchengshizhan.thread7cancelclose;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Allen.zhang
 * @title: BrokenPrimeProducer
 * @projectName zl
 * @description: 😒 不可靠的取消操作将把生产者置于阻塞的操作中
 * @date 2021/12/183:11
 */
public class BrokenPrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;
    private volatile boolean canceled = false;
    BrokenPrimeProducer(BlockingQueue<BigInteger> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;

            while (!canceled) {
                p = p.nextProbablePrime();
                queue.put(p);
            }


        } catch (InterruptedException e) {

        }
    }

    public void cancel(){
        this.canceled = true;
    }

    void consumePrimes(){
        BlockingQueue<BigInteger> primes = new LinkedBlockingDeque<>();
        BrokenPrimeProducer primeProducer = new BrokenPrimeProducer(primes);
        primeProducer.start();
        try {
            while (needMorePrimes()) {
                consume(primes.take());
            }
        } catch (Exception e) {

        } finally {
            primeProducer.cancel();
        }


    }

    private void consume(BigInteger take) {
    }

    private boolean needMorePrimes() {
        if (true)
            return true;
        return false;
    }
}
