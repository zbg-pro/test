package com.zl.iolearn.insanecoder.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class NIOTcpClient {

    private static final int PORT = 5555;
    private static final String IP_ADDRESS = "localhost";
    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        socketChannel.connect(new InetSocketAddress(IP_ADDRESS, PORT));
        while (!socketChannel.finishConnect());

        new Thread(new SendRunnable(socketChannel)).start();

        System.out.println("Connecting to " + IP_ADDRESS + " on " + PORT);
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {

                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    StringBuilder sb = new StringBuilder();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    while (channel.read(byteBuffer) > 0) {
                        byteBuffer.flip();
                        sb.append(Charset.forName("utf8").decode(byteBuffer).toString());
                        byteBuffer.clear();
                    }
                    System.out.println("[server] " + sb.toString());
                }
                iterator.remove();
            }
        }
    }

    private static class SendRunnable implements Runnable {

        private SocketChannel socketChannel;
        public SendRunnable(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }
        public void run() {
            System.out.println("Type to send message:");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                /**
                 * 这里需要提醒各位同学一个问题，由于我在测试的时候采用的是一台机器连续发送数据来模拟高并发的场景，
                 * 所以在测试的时候会发现服务器端收到的数据包的个数经常会小于包的序号，好像发生了丢包。但经过仔细分析可以发现，
                 * 这种情况是因为TCP发送缓存溢出导致的丢包，也就是这个数据包根本没有发出来。也就是说，发送端发送数据过快，
                 * 导致接收端缓存很快被填满，这个时候接收端会把通知窗口设置为0从而控制发送端的流量，这样新到的数据只能暂存在发送端的发送缓存中，当发送缓存溢出后
                 * ，就出现了我上面提到的丢包，这个问题可以通过增大发送端缓存来缓解这个问题
                 */
                socketChannel.socket().setSendBufferSize(102400);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            int number = 0;
            while (true) {
                try {
                    // take input as the message source
                    String msg = bufferedReader.readLine();
                    // send the message continuously
//                    String msg = "hello world " + number++;
                    // send data normally
                    socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
                    // add the head represent the data length
                    //socketChannel.write(ByteBuffer.wrap(new PacketWrapper(msg).getBytes()));
                    // make the data length fixed
//                    socketChannel.write(ByteBuffer.wrap(new FixLengthWrapper(msg).getBytes()));
//                    System.out.println(number);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
