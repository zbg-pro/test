package com.zl.iolearn.insanecoder.server;

import com.zl.iolearn.insanecoder.utils.PrintRunnable;
import com.zl.iolearn.insanecoder.utils.SendRunnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;


public class NormalTcpServer {

    private static final int PORT = 5555;
    private static final String MESSAGE_TO_SEND = " from server";

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("listening on port " + PORT);
        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = serverSocket.accept();
        SocketAddress socketAddress = socket.getRemoteSocketAddress();
        System.out.println("get a client, remote client address: " + socketAddress);

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        Thread sendThread = new Thread(new SendRunnable(out));
        Thread printThread = new Thread(new PrintRunnable(in, "client"));

        sendThread.start();
        printThread.start();

        sendThread.join();
        printThread.join();

        socket.close();
    }
}
