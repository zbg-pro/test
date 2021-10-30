package com.zl.daimazhengjiezhidao.thread.pool;


import org.voovan.tools.log.Logger;

import java.net.Socket;

/**
 * @author Allen.zhang
 * @title: ClientRequestProcessor
 * @projectName zl
 * @description: TODO
 * @date 2021/10/3014:08
 */
public class ClientRequestProcessor {

    private Socket socket;

    public ClientRequestProcessor(ClientConnection clientConnection) {
        socket = clientConnection.getSocket();
    }

    public void process() {
        try {
            Logger.info("server getting message:");
            String message = MessageUtils.getMessage(socket);
            Logger.infof("server got message: {}", message);
            Thread.sleep(1000);

            Logger.infof("server sending reply message:{}", message);
            MessageUtils.sendMessage(socket, "Processed:" + message);
            Logger.info("server sent");
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
