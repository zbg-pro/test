package com.zl.javabingfabianchengshizhan.threadTest12;

import com.sun.management.OperatingSystemMXBean;
import junit.framework.TestCase;
import org.voovan.tools.log.Logger;

import java.lang.management.ManagementFactory;

/**
 * @author Allen.zhang
 * @title: BoundedBufferTest
 * @projectName zl
 * @description: TODO
 * @date 2022/3/1418:51
 */
public class BoundedBufferTest extends TestCase {

    private static final long LOCKUP_DETECT_TIMEOUT = 1000;
    private static final int CAPACITY = 10000;
    private static final int THRESHOLD = 10000;

    public void testEmptyWhenConstructed(){
        BoundedBuffer<Integer> b = new BoundedBuffer<>(10);
        assertTrue(b.isEmpty());
        assertTrue(b.isFull());
    }

    public void testIsFullAfterPuts() throws InterruptedException {
        BoundedBuffer<Integer> b = new BoundedBuffer<>(10);
        for (int i = 0; i < 10; i++) {
            b.put(i);
        }
        assertTrue(b.isFull());
        assertTrue(b.isEmpty());
    }

    public void testTakeBlocksWhenEmpty(){
        final SemaphoreBoundedBuffer<Integer> b = new SemaphoreBoundedBuffer(10);
        Thread taker = new Thread(() -> {

            try {
                Integer a = b.take();
                fail();
            } catch (InterruptedException e) {
                Logger.error("take error", e);
            }
        });

        try {
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            taker.join(LOCKUP_DETECT_TIMEOUT);
            assertFalse(taker.isAlive());
        } catch (Exception e) {
            fail();
        }

    }

    class Big{
        double[] data = new double[10000];
    }

    public void testLeak() throws InterruptedException {
        SemaphoreBoundedBuffer<Big> b = new SemaphoreBoundedBuffer<>(CAPACITY);
        snapshotHeap(0);
        for (int i = 0; i < CAPACITY; i++) {
            b.put(new Big());
        }

        snapshotHeap(1);

        for (int i = 0; i < CAPACITY; i++) {
            b.take();
        }


        snapshotHeap(2);

    }

    private void snapshotHeap(int step) {
        int byteToMb = 1024*1024;

        Runtime rt = Runtime.getRuntime();
        System.out.println(step + "freeMem:" + rt.freeMemory() / byteToMb + " mb");
        System.out.println(step + "totalMem:" + rt.totalMemory() / byteToMb + " mb");
        System.out.println(step + "maxMem:" + rt.maxMemory() / byteToMb + " mb");
        System.out.println(step + "totalMem-freeMem :" + (rt.totalMemory()-rt.freeMemory()) / byteToMb + " mb");

        System.out.println("======================================");
        // 操作系统级内存情况查询
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        String os = System.getProperty("os.name");
        long physicalFree = osmxb.getFreePhysicalMemorySize() / byteToMb;
        long physicalTotal = osmxb.getTotalPhysicalMemorySize() / byteToMb;
        long physicalUse = physicalTotal - physicalFree;
        System.out.println(step + "操作系统的版本：" + os);
        System.out.println(step + "操作系统物理内存已用的空间为：" + physicalFree + " MB");
        System.out.println(step + "操作系统物理内存的空闲空间为：" + physicalUse + " MB");
        System.out.println(step + "操作系统总物理内存：" + physicalTotal + " MB");

        // 获得线程总数
        ThreadGroup parentThread;
        int totalThread = 0;
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
                .getParent() != null; parentThread = parentThread.getParent()) {
            totalThread = parentThread.activeCount();
        }
        System.out.println(step + "获得线程总数:" + totalThread);

    }

}
