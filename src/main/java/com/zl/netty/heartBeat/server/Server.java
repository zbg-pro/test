package com.zl.netty.heartBeat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by hl on 2018/4/4.
 */
public class Server {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1000);//bossGroup, 用于处理客户端的连接请求
        NioEventLoopGroup workGroup = new NioEventLoopGroup(4);//workerGroup, 用于处理与各个客户端连接的 IO 操作

        /**
         * 当前2个线程组的分配策略：boss作为处理连接socket的操作，可以同时支撑指定数量的线程连接进行处理，
         * worker作为处理连接进来后建立的channel，会被多个 Channel 所共享。这使得可以通过尽可能少量的 Thread 来支
                撑大量的 Channel，而不是每个 Channel 分配一个 Thread

         所有的EventLoop都经由workergroup线程组进行分配操作，每个eventloop都对应一个独立线程，这个eventloop负责可能多个的channel的整个生命周期
         */

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new IdleStateHandler(10, 0, 0));
                            p.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0));
                            p.addLast(new ServerHandler());
                        }
                    });

            Channel ch = serverBootstrap.bind(12345).sync().channel();

            ch.eventLoop().schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println(1);
                }
            }, 60, TimeUnit.SECONDS);

            ch.closeFuture().sync();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

}
