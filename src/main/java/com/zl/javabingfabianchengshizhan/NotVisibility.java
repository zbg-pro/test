package com.zl.javabingfabianchengshizhan;

import org.voovan.tools.TEnv;

/**
 * @author Allen.zhang
 * @title: NotVisibility
 * @projectName zl
 * @description: TODO
 * @date 2021/11/11:35
 */
public class NotVisibility {
    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready) {
                Thread.yield();
                System.out.println("111");
            }

            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();
        number = 42;
        TEnv.sleep(1000);
        ready = true;
    }

}
