package com.zl.daimazhengjiezhidao.thread.pool;

import org.voovan.tools.log.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author Allen.zhang
 * @title: Server
 * @projectName zl
 * @description: TODO
 * @date 2021/10/302:06
 */
public class Server implements Runnable {

    ServerSocket serverSocket;
    volatile boolean keepProcessing = true;
    ClientManager connectionManager;

    @Deprecated
    ClientScheduler clientScheduler2 = new ThreadPreRequestScheduler();

    ClientScheduler clientScheduler = new ExecutorClientScheduler(100);


    public Server(int port, int millSecondTimeout) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(millSecondTimeout);
        connectionManager = new ClientManager(serverSocket);
    }

    @Override
    public void run() {
        Logger.info("Server starting...");

        while (keepProcessing) {
            try {
                ClientConnection clientConnection = connectionManager.awaitClient();
                ClientRequestProcessor requestProcessor = new ClientRequestProcessor(clientConnection);
                clientScheduler.schedule(requestProcessor);
            } catch (Exception e) {
                handler(e);
            } finally {

            }
        }

        try {
            connectionManager.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handler(Exception e) {
        if (e instanceof SocketException) {
            Logger.info(e);
            e.printStackTrace();
        }
    }

    public void stopProcessing(){
        keepProcessing = false;
        closeIgnoringException(serverSocket);
    }

    @Deprecated
    private void process(Socket socket) {

        if (socket == null) {
            return;
        }


        Runnable clientHandler = () -> {

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

        };

        Thread clientConnection = new Thread(clientHandler);
        clientConnection.start();
    }

    private void closeIgnoringException(ServerSocket serverSocket) {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (Exception e) {
                    Logger.error(e);
                }
            }

    }

    private void closeIgnoringException(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                Logger.error(e);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(8888, 100000);
        server.run();
    }
}
