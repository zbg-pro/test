package com.zl.javabingfabianchengshizhan.thread7cancelclose;

import org.voovan.tools.TEnv;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Allen.zhang
 * @title: PrimeGenerator
 * @projectName zl
 * @description:  71. 7.2使用volatile类型的域来保存取消状态
 *
 * aSecondPrimes
 *
 * @date 2021/12/181:39
 */
public class PrimeGenerator implements Runnable {

    private final List<BigInteger> primes = new ArrayList<>();

    private volatile boolean canceled;


    @Override
    public void run() {
        BigInteger p = BigInteger.ONE;
        while (!canceled) {
            p = p.nextProbablePrime();
            synchronized (this) {
                primes.add(p);
            }
            TEnv.sleep(30);
        }
    }

    public void cancel(){
        this.canceled = true;
    }

    public synchronized List<BigInteger> get(){
        return new ArrayList<>(primes);
    }

    /**
     * 1类的使用示例：即让素数生成器允许1s后取消
     * 2但是素数生成器并不会刚好1s内运行完成，因此在请求取消时刻和run方法循环执行下一次检查cancel之间可能存在延迟，
     * 如果cancel没有被执行，那么搜索素数的程序将永远执行下去
     * 3 如果run方法里面调用了一个阻塞方法，如BlockingQueue.put,那么可能产生另外一个更严重的问题，当run方法在put方法中阻塞时，任务可能永远不检查取消标志
     * @return
     */
    List<BigInteger> aSecondPrimes() {
        PrimeGenerator generator =  new PrimeGenerator();
        new Thread(generator).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            generator.cancel();
        }

        return generator.get();
    }

}
