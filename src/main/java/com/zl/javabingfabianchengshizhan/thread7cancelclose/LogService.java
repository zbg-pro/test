package com.zl.javabingfabianchengshizhan.thread7cancelclose;

import net.jcip.annotations.GuardedBy;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Allen.zhang
 * @title: LogService
 * @projectName zl
 * @description: TODO
 * @date 2022/1/42:57
 */
public class LogService {
    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    private final PrintWriter writer;
    @GuardedBy("this") private boolean isShutdown;
    @GuardedBy("this") private long reservations;

    public LogService(Writer writer){
        this.queue = new LinkedBlockingQueue<>();
        this.writer = new PrintWriter(writer, true);
        loggerThread = new LoggerThread();
    }

    public void start(){
        loggerThread.start();
    }

    public void stop() {
        synchronized (this) {
            isShutdown = true;
        }
        loggerThread.interrupt();
    }

    public void log(String msg) throws InterruptedException {
        synchronized (this) {
            if (isShutdown) {
                throw new IllegalStateException("log service stop...");
            }
            ++reservations;
        }
        queue.put(msg);
    }









    private class LoggerThread extends Thread{

        @Override
        public void run() {
            try {

                while (true) {

                    synchronized (LogService.this) {
                        if (isShutdown && reservations == 0) {
                            break;
                        }
                    }

                    String msg = queue.take();
                    synchronized (LogService.this) {
                        --reservations;
                    }
                    writer.println(msg);

                }


            } catch (InterruptedException e) {
                /* retry */
            } finally {
                writer.close();
            }
        }
    }
}
