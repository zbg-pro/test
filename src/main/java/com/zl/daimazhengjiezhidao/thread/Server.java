package com.zl.daimazhengjiezhidao.thread;

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

    public Server(int port, int millSecondTimeout) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(millSecondTimeout);
    }

    @Override
    public void run() {
        Logger.info("Server starting...");

        while (keepProcessing) {
            try {
                Logger.info("accepting client...");
                Socket socket = serverSocket.accept();
                Logger.info("got client");
                process(socket);
            } catch (Exception e) {
                handler(e);
            } finally {

            }
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
        Server server = new Server(8881, 100000);
        server.run();
    }
}
