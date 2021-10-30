package com.zl.daimazhengjiezhidao.thread.pool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Allen.zhang
 * @title: MessageUtils
 * @projectName zl
 * @description: TODO
 * @date 2021/10/302:17
 */
public class MessageUtils {
    public static void sendMessage(Socket socket, String message) throws IOException, InterruptedException {
        DataOutputStream dos =new DataOutputStream(socket.getOutputStream());//给客户端写入消息
        dos.writeUTF(message);//这边是阻塞的，client 没有写 没有flush，这边会一直会block。 这个时候 其他线程进不来
        dos.flush();
    }

    public static String getMessage(Socket socket) throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());//获取客户端发送过来的消息
        String message = dis.readUTF();
        return message;
    }
}
