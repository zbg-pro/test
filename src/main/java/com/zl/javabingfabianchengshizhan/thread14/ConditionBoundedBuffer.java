package com.zl.javabingfabianchengshizhan.thread14;

import net.jcip.annotations.GuardedBy;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Allen.zhang
 * @title: ConditionBoundedBuffer
 * @projectName zl
 * @description: TODO
 * @date 2022/3/2118:48
 */
public class ConditionBoundedBuffer<T> {
    protected final Lock lock = new ReentrantLock();
    protected final Condition notFull = lock.newCondition();
    protected final  Condition notEmpty = lock.newCondition();
    private static final int BUFFER_SIZE = 100;
    @GuardedBy("lock") private final T[] items = (T[]) new Object[BUFFER_SIZE];
    @GuardedBy("lock") private int tail, head, count;

    public  void put(T x) throws InterruptedException {
        lock.lock();

        try {
            while (count == items.length)
                notFull.wait();

            items[tail] = x;

            if (++tail == items.length) {
                tail = 0;
            }

            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }

    }

    public T take() throws InterruptedException {

        lock.lock();
        try {
            //如果没有元素卡住
            while (count == 0)
                notEmpty.await();

            //弹出一个队头元素
            T t = items[head];
            items[head] = null;

            //队头指针前进
            if (++head == items.length) {
                head = 0;
            }

            //总元素量更新
            --count;

            //尝试唤醒put线程
            notFull.signal();

            return t;
        } finally {
            lock.unlock();
        }
    }





}
