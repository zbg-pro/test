package com.zl.gaozhiliang152jianyi.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
    public static void main(String[] args) {
        final A a = new A();
        final B b = new B();

        new Thread(new Runnable() {
            @Override
            public void run() {
                a.a1(b);
            }
        }, "线程A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                b.b1(a);
            }
        }, "线程B").start();

    }
}

class A{

    public synchronized void a1(B b){
        String name = Thread.currentThread().getName();
        System.out.println(name + "进入a.a1()");

        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }

        System.out.println(name + "试图进入b.b2()");

        b.b3();
    }

    public synchronized void a2(){
        System.out.println("进入a.a2()");
    }

}


class B{
    public synchronized void b1(A a){
        String name = Thread.currentThread().getName();
        System.out.println(name + "进入b.b1()");

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        System.out.println(name + "试图进入a.a2()");

        a.a2();
    }

    public synchronized void b2(){
        System.out.println("进入b.b2()");
    }

    static Lock lock = new ReentrantLock();
    public void b3(){
        try {
            if (lock.tryLock(2, TimeUnit.SECONDS)) {
                System.out.println("进入b.b3()");
            }
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }
    }
}