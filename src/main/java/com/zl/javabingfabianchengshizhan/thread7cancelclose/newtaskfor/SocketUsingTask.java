package com.zl.javabingfabianchengshizhan.thread7cancelclose.newtaskfor;

import net.jcip.annotations.GuardedBy;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * @author Allen.zhang
 * @title: SocketUsingTask
 * @projectName zl
 * @description: TODO
 * @date 2022/1/41:36
 */
public abstract class SocketUsingTask<T> implements CancellableTask<T>{

    @GuardedBy("this") private Socket socket;

    protected synchronized void setSocket(Socket socket){
        this.socket = socket;
    }

    public synchronized void cancel(){
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {

        }
    }


    public RunnableFuture<T> newTask(){

        return new FutureTask<T>(this) {

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                try {
                    SocketUsingTask.this.cancel();
                 } catch (Exception e) {

                } finally {
                    return super.cancel(mayInterruptIfRunning);
                }
            }

        };


    }



}
