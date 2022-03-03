package com.zl.javabingfabianchengshizhan.thread7cancelclose;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author Allen.zhang
 * @title: ReaderThread
 * @projectName zl
 * @description: ReaderThread 给出了如何封装非标准的取消操作，它给出了一个管理套接字连接，采用同步方法从该套接字读取数据，并将接收到的数据
 * 传递给processBuffer，为了结束某个某个用户的连接或者关闭服务器，ReaderThread改写了interrupt方法，使其既能处理标准的中断，也能关闭底层的套接字，
 * 因此，无论ReaderThread线程是在read方法中阻塞，还是在某个可中断的阻塞方法中阻塞，都可以被中断并停止执行当前工作
 *
 * 1 selector的异步I/O，如果一个线程在调用selector.select方法时阻塞了，那么调用clase或者wakeup方法会抛出ClosedSelectorException并提前返回
 * 2 获取某个锁：如果一个线程由于等待一个内置锁而阻塞，那么将无法响应中断，因为线程认为他肯定获得锁，所以将不会理会中断请求。
 *   但是Lock类提供了lockInterruptibly方法，该方法允许在等待一个锁的时候仍能响应中断，见13章
 *
 *
 *
 * @date 2022/1/323:12
 */
public class ReaderThread extends Thread {

    private static final int BUFSZ = 1024;
    private final Socket socket;

    private final InputStream in;

    public ReaderThread(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
    }

    public void interrupt(){
        try {
            socket.close();
        } catch (IOException e) {

        }finally {
            super.interrupt();
        }
    }

    @Override
    public void run() {
        try {
            byte[] bytes = new byte[BUFSZ];

            while (true) {
                int count = in.read(bytes);
                if (count < 0) {
                    break;
                } else if (count > 0) {
                    processBuffer(bytes, count);
                }
            }

        } catch (Exception e) {

        } finally {

        }
    }

    private void processBuffer(byte[] bytes, int count) {

    }
}
