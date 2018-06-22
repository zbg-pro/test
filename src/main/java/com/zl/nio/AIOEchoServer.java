package com.zl.nio;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;

/**
 * Created by hl on 2018/6/19.
 */
public class AIOEchoServer {

    private static AsynchronousServerSocketChannel server;

    public static void main(String[] args) throws Exception {
        init("127.0.0.1", 1122);
    }

    public static void init(String host, int port) throws Exception{
        //ChannelGroup用来管理共享资源
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(), 10);
        server = AsynchronousServerSocketChannel.open(group);

        //通过setOption配置Socket
        server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        server.setOption(StandardSocketOptions.SO_RCVBUF, 16*1024);

        //绑定到指定主机，端口
        server.bind(new InetSocketAddress(host, port));
        System.out.println("Listening on " + host + ":" + port);

        //输出provider
        System.out.println(server.provider());

        //等待连接，并注册CompletionHandler处理内核完成后的操作。
        server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>()  {
            final ByteBuffer buffer = ByteBuffer.allocate(1024);

            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                System.out.println("waiting...");
                buffer.clear();

                try {
                    //把socket中的数据读取到buffer中
                    result.read(buffer).get();
                    buffer.flip();
                    System.out.println("Echo " + new String(buffer.array()).trim() + " to " + result);

                    //把收到的直接返回给客户端
                    result.write(buffer);
                    buffer.flip();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        result.close();
                        server.accept(null, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.print("Server failed...." + exc.getCause());
            }
        });

        Thread.sleep(Integer.MAX_VALUE);

    }
}
