package com.zl.daimazhengjiezhidao.thread.pool;

import java.net.Socket;

/**
 * @author Allen.zhang
 * @title: ClientConnection
 * @projectName zl
 * @description: TODO
 * @date 2021/10/3014:08
 */
public class ClientConnection {

    Socket socket;
    public ClientConnection(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
