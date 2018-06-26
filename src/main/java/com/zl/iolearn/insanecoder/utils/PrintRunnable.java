package com.zl.iolearn.insanecoder.utils;

import java.io.DataInputStream;
import java.io.IOException;

public class PrintRunnable implements Runnable {

    private DataInputStream dis;
    private String remoteName;

    public PrintRunnable(DataInputStream dis, String remoteName) {
        this.dis = dis;
        this.remoteName = remoteName;
    }

    public void run() {

        while (true) {
            try {
                String msgReceived = dis.readUTF();
                System.out.println(this.remoteName + " : " + msgReceived);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
