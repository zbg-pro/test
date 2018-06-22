package com.zl.netty.heartBeat.client;

import com.zl.netty.heartBeat.common.CustomHeartbeatHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by hl on 2018/4/4.
 */
public class Client {

    private NioEventLoopGroup  workGroup = new NioEventLoopGroup(4);

    private Channel channel;

    private Bootstrap bootstrap;


    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.start();
        client.sendData();
    }
    protected void doConnect(){
        if (channel != null && channel.isActive()) {
            return;
        }

        final ChannelFuture future = bootstrap.connect("127.0.0.1", 12345);

        final ScheduledFuture<?> future2 = future.channel().eventLoop().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(1);
            }
        }, 0, 1, TimeUnit.SECONDS);


        //*************************************************
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000*8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                boolean mayInterruptedIfRunning = true;
                future2.cancel(mayInterruptedIfRunning);//此功能仅仅取消当前的任务
            }
        }).start();
        //*************************************************

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
              if(channelFuture.isSuccess()) {
                  channel = channelFuture.channel();
                  System.out.println("Connect to server successfully!");
              }  else {
                  System.out.println("Failed to connect to server, try connect after 10s");
                  channel.eventLoop().schedule(new Runnable() {
                      @Override
                      public void run() {
                          doConnect();
                      }
                  }, 10, TimeUnit.SECONDS);
              }
            }
        });
    }

    public void start(){
        bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new IdleStateHandler(0, 0, 5));
                        p.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0));
                        p.addLast(new ClientHandler(Client.this));
                    }
                });
        doConnect();
    }


    public void sendData() throws InterruptedException {
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 1000; i++) {
            if(channel != null && channel.isActive()){
                String content = "client msg " + i;
                ByteBuf byteBuffer = channel.alloc().buffer(5 + content.getBytes().length);
                byteBuffer.writeInt(5 + content.getBytes().length);
                byteBuffer.writeByte(CustomHeartbeatHandler.CUSTOM_MSG);
                byteBuffer.writeBytes(content.getBytes());
                channel.writeAndFlush(byteBuffer);
            }
            Thread.sleep(random.nextInt(20000));
        }
    }






}
