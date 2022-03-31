package com.zl.javabingfabianchengshizhan.threadTest12;

import net.jcip.annotations.ThreadSafe;
import org.voovan.Global;
import org.voovan.tools.TEnv;

/**
 * @author Allen.zhang
 * @title: BoundedBuffer
 * @projectName zl
 * @description: TODO
 * @date 2022/3/110:39
 */
@ThreadSafe
public class BoundedBuffer <V> extends BaseBoundedBuffer<V> {

    protected BoundedBuffer() {
        this(100);
    }

    protected BoundedBuffer(int capacity) {
        super(capacity);
    }

    public synchronized void put(V v) throws InterruptedException {
        while (isFull())
            wait();

        doPut(v);
        notifyAll();
    }

    public synchronized V take() throws InterruptedException {
        while (isEmpty()) {
            System.out.println(111);
            wait();
            System.out.println(222);
        }


        V v = doTake();
        notifyAll();
        return v;
    }

    public synchronized void alternatePut (V v) throws InterruptedException {
        while (isFull()) {
            wait();
        }

        //这么泻的作用是为了优化每次put都通知的问题，使用“条件通知”
        boolean wasEmpty = isEmpty();
        doPut(v);
        if (wasEmpty)
            notifyAll();

    }

    /**
     * 本测试表明，在synchronized中，如果take没能执行，然后走了wait，wait是会释放同步锁的，然后等待put后，唤醒wait可以继续执行
     * @param args
     */
    public static void main(String[] args) {
        BoundedBuffer<String> boundedBuffer = new BoundedBuffer();
        Global.getThreadPool().execute(() ->{
            try {
                String a = boundedBuffer.take();
                System.out.println("take:" + a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        TEnv.sleep(1000);
        Global.getThreadPool().execute(() ->{
            try {
                boundedBuffer.put("ssssss");
                System.out.println("put:" + "ssssss");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        TEnv.sleep(Integer.MAX_VALUE);
    }


}
