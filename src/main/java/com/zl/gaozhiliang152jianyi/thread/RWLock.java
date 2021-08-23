package com.zl.gaozhiliang152jianyi.thread;

import org.voovan.tools.TDateTime;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @version 1.0
 * @desc:
 * @date 2021/8/2 9:11 下午
 * @auth ALLEN
 */
public class RWLock {

    public static void main(String[] args) {
        Foo foo = new Foo();

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    foo.write(finalI + "-");
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    foo.read();

                }
            }).start();
        }


    }

}

class Foo {
    private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

    private final Lock r = rw.readLock();

    private final Lock w = rw.writeLock();

    private String text = "";

    public void read(){
        try {
            r.lock();
            Thread.sleep(2000);
            System.out.println(TDateTime.now() +" read...text:" + text);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            r.unlock();
        }
    }

    public void write(String str){
        try {
            w.lock();

            text = text + str;
            System.out.println(TDateTime.now() +" writing...text:" + text + ", append str:" + str);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            w.unlock();
        }
    }


}
