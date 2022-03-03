package com.zl.javabingfabianchengshizhan.thread7cancelclose;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Allen.zhang
 * @title: LogWriter
 * @projectName zl
 * @description: TODO
 * @date 2022/1/42:27
 */
public class LogWriter {

    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    private static final int CAPACITY = 1000;

    public LogWriter(Writer writer) {
        this.loggerThread = new LoggerThread(writer);
        this.queue = new LinkedBlockingDeque<String>(CAPACITY);
    }

    public void start(){
        loggerThread.start();
    }

    public void log(String message) throws InterruptedException {
        queue.put(message);
    }

    private class LoggerThread extends Thread {
        private final PrintWriter writer;

        private LoggerThread(Writer writer) {
            this.writer = new PrintWriter(writer, true); //autoflush
        }

        @Override
        public void run() {
            try {
                while (true)
                    writer.println(queue.take());
            } catch (InterruptedException e) {

            } finally {
                writer.close();
            }
        }
    }


}
