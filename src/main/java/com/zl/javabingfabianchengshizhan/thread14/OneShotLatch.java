package com.zl.javabingfabianchengshizhan.thread14;

import org.voovan.Global;
import org.voovan.tools.TEnv;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author Allen.zhang
 * @title: OneSlotLauch
 * @projectName zl
 * @description: TODO
 * @date 2022/3/2921:33
 */
public class OneShotLatch {

    private final Sync sync = new Sync();

    public void signal(){
        sync.releaseShared(0);
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(0);
    }

    private class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected int tryAcquireShared(int arg) {
            // Succeed if latch is open (state == 1), else fail
            return getState() == 1? 1:-1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            setState(1);// Latch is now open
            return true;// Other threads may now be able to acquire

        }
    }

    public static void main(String[] args) {
        OneShotLatch oneShotLatch = new OneShotLatch();

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            Global.getThreadPool().execute(() -> {

            if (finalI %2 == 0) {
                try {
                    System.out.println("卡住 i="+ finalI);
                    oneShotLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("dosth i = " + finalI);

            TEnv.sleep(1000);
            });
        }


        TEnv.sleep(5000);
        oneShotLatch.signal();
    }
}
