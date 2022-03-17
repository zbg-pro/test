package com.zl.javabingfabianchengshizhan.xingnengyushengsuoxing11;

import org.voovan.tools.TDateTime;
import org.voovan.tools.TEnv;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Allen.zhang
 * @title: ReadWriteLockTest
 * @projectName zl
 * @description: TODO
 * @date 2022/3/1023:07
 */
public class ReadWriteLockTest {

    static class Counter{
        private final Lock lock = new ReentrantLock();
        private int[] count = new int[100000];

        public void inc(int index){
            lock.lock();
            try {
                count[index] += index;
            } finally {
                lock.unlock();
            }
        }

        public String get(){
            lock.lock();
            try {
                String s = "";
                for (int a: count) {
                    s = s+a + ",";
                }
                return s;
            } finally {
                lock.unlock();
            }
        }

        public String getByIndex(int index) {
            String s = "";
            lock.lock();
            if (index%3 == 0)
            TEnv.sleep(1);
            try {
                s = s + count[index];
                if (index == 0) {
                    System.out.println(new Date().getTime() + " s: " + s);
                }
                if (index == 99999) {
                    System.out.println(new Date().getTime() + " s: " + s);
                }
            } finally {
                lock.unlock();
            }

            return s;
        }

    }

    static class Counter2 {
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        private final Lock rLock = rwLock.readLock();
        private final Lock wLock = rwLock.writeLock();
        private int[] count = new int[100000];

        public void inc(int index) {
            wLock.lock();

            try {
                count[index] += index;
            } finally {
                wLock.unlock();
            }
        }

        public String get(){
            rLock.lock();
            try {
                String s = "";
                for (int a: count) {
                    s = s+a + ",";
                }
                return s;
            } finally {
                rLock.unlock();
            }
        }

        public String getByIndex(int index) {
            String s = "";
            rLock.lock();
            if (index%3 == 0)
            TEnv.sleep(1);
            try {
                s = s + count[index];
                if (index == 0) {
                    System.out.println(new Date().getTime() + " s: " + s);
                }
                if (index == 99999) {
                    System.out.println(new Date().getTime() + " s: " + s);
                }
            } finally {
                rLock.unlock();
            }

            return s;
        }



    }

    // c1 0.7s   c2 57s
    //从counter 和counter2可以看出，如果存在并发读写时候，读写锁性能差不多，因为读取时候需要占用锁
    //但是如果提前写入完成后，读锁的效率是非常高,因为此时没有了写入线程，读取是不需要加锁的
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        Counter2 counter = new Counter2();
        for (int i = 0; i<100000; i++) {
            int finalI = i;
            executor.submit(() -> counter.inc(finalI));
        }
        TEnv.sleep(5000);

        for (int i = 0; i<100000; i++) {
            int finalI = i;
            executor.submit(() -> counter.getByIndex(finalI));
        }

        TEnv.sleep(Integer.MAX_VALUE);
    }


}
