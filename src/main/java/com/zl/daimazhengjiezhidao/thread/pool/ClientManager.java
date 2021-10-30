package com.zl.daimazhengjiezhidao.thread.pool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Allen.zhang
 * @title: ClientManager
 * @projectName zl
 * @description: TODO
 * @date 2021/10/3014:42
 */
public class ClientManager {

    ServerSocket serverSocket;
    public ClientManager(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ClientConnection awaitClient() throws IOException {
        Socket socket = serverSocket.accept();
        ClientConnection clientConnection = new ClientConnection(socket);
        return clientConnection;
    }

    public void shutdown() throws IOException {
        serverSocket.close();
    }

}
