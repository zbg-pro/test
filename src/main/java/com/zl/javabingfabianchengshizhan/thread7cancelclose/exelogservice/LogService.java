package com.zl.javabingfabianchengshizhan.thread7cancelclose.exelogservice;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.*;

/**
 * @author Allen.zhang
 * @title: LogService
 * @projectName zl
 * @description: TODO
 * @date 2022/1/413:23
 */
public class LogService {

    private final ExecutorService exec = Executors.newSingleThreadExecutor();

    private final BlockingQueue<String> queue;
    private final PrintWriter writer;

    public LogService(Writer writer){
        this.queue = new LinkedBlockingQueue<>();
        this.writer = new PrintWriter(writer, true);
    }

    public void start(){}

    public void stop(){
        try {
            exec.shutdown();
            exec.awaitTermination(3, TimeUnit.MILLISECONDS);
        } catch (Exception e) {

        } finally {

        }
    }

    public void log(String msg) {
        try {
            exec.submit(new WriteTask());
        } catch (RejectedExecutionException e) {

        }
    }

}
