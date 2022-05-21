package com.zl.javabingfabianchengshizhan.thread15;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Allen.zhang
 * @title: ReentrantLockPseudoRandom
 * @projectName zl
 * @description: 基于reentrantLock实现随机数生成器
 * @date 2022/5/1115:23
 */
public class ReentrantLockPseudoRandom extends PseudoRandom {
    private final Lock lock = new ReentrantLock(false);
    private int seed;

    public ReentrantLockPseudoRandom(int seed) {
        this.seed = seed;
    }

    public int nextInt(int n) {
        lock.lock();
        try {
            int s = seed;
            seed = calculateNext(s);
            int remainder = s%n;
            return remainder > 0? remainder: remainder + n;
        }finally {
            lock.unlock();
        }
    }

}
