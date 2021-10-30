package com.zl.daimazhengjiezhidao.thread;

import org.voovan.tools.TEnv;
import org.voovan.tools.log.Logger;

import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * @author Allen.zhang
 * @title: ClientTest
 * @projectName zl
 * @description: TODO
 * @date 2021/10/302:26
 */
public class ClientTest {

    public static void main(String[] args) {
        connectSendReceive(8881);
    }

    private static void connectSendReceive(int i) {
        try {
            Socket socket = new Socket("localhost", i);
            Logger.info("client sending message");
            String message = "Hello World2!";
            MessageUtils.sendMessage(socket, message);
            Logger.infof("client sent message:{}", message);
            String replyMessage = MessageUtils.getMessage(socket);
            System.out.println("aaaaaa:" + replyMessage);
            Logger.infof("client receive reply server message:{}", replyMessage);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
