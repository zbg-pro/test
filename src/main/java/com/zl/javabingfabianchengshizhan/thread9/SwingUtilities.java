package com.zl.javabingfabianchengshizhan.thread9;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

/**
 * @author Allen.zhang
 * @title: SwingUtilities
 * @projectName zl
 * @description: TODO
 * @date 2022/3/416:08
 */
public class SwingUtilities {

    private static final ExecutorService exec = Executors.newSingleThreadExecutor(new SwingThreadFactory());

    private static volatile Thread swingThread;

    private static class SwingThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            swingThread = new Thread(r);
            return swingThread;
        }
    }

    public static boolean isEventDispatchThread(){
        return Thread.currentThread() == swingThread;
    }

    public static void invokeLater(Runnable task){
        exec.execute(task);
    }

    public static void invokeAndWait(Runnable task) throws InterruptedException, InvocationTargetException {
        Future f = exec.submit(task);
        try {
            f.get();
        } catch (ExecutionException e) {
            throw new InvocationTargetException(e);
        } finally {

        }
    }

}
