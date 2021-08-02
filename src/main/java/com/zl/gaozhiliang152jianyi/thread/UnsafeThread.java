package com.zl.gaozhiliang152jianyi.thread;

/**
 * @version 1.0
 * @desc:
 * @date 2021/7/30 10:34 上午
 * @auth ALLEN
 */
public class UnsafeThread implements Runnable {

    private volatile int count = 0;

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            Math.hypot(Math.pow(123232323.23,i), Math.cos(i));
        }
        count++;
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        int value = 1000;

        int loops = 0;

        ThreadGroup tg = Thread.currentThread().getThreadGroup();

        while (loops++ < value) {

            UnsafeThread ut = new UnsafeThread();

            for (int i = 0; i < value; i++) {
                new Thread(ut).start();
            }

            /*do {
                Thread.sleep(1);
            } while (tg.activeCount() != 1);*/

            if (ut.getCount() != value) {
                System.out.println("循环到第" + loops + "遍， 出现线程不安全情况");
                System.out.println("此时，count=" + ut.getCount());
                //System.exit(0);
            }

        }

    }

}
