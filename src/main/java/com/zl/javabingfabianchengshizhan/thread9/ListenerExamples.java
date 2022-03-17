package com.zl.javabingfabianchengshizhan.thread9;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Allen.zhang
 * @title: ListenerExamples
 * @projectName zl
 * @description: TODO
 * @date 2022/3/50:45
 */
public class ListenerExamples {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private JButton colorButton = new JButton("Change color");

    private final Random random = new Random();

    private void backgroundRandom(){
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorButton.setBackground(new Color(random.nextInt()));
            }
        });
    }

    private final JButton computeButton = new JButton("Big computation");

    private void longRunningTask(){
        computeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Big computation
                    }
                });
            }
        });
    }

    private final JButton button = new JButton("Do");
    private final JLabel label = new JLabel("idle");
    private void longRunningTaskWithFeedback(){
        button.addActionListener(e -> {
            button.setEnabled(false);
            label.setText("busy");
            executorService.submit(() ->{

                try {
                    //dosth
                } finally {
                    GuiExecutor.instance().execute(()->{
                        button.setEnabled(true);
                        label.setText("idle");
                    });
                }

            });
        });
    }


    private final JButton startButton = new JButton("Start");
    private final JButton cancelButton = new JButton("Cancel");
    private Future<?> runningTask = null;
    private void taskWithCancelButton(){

        startButton.addActionListener(e ->{
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    while (moreWork()) {
                        if (Thread.currentThread().isInterrupted()) {
                            cleanUpPartialWork();
                            break;
                        }
                        doSomeWork();
                    }
                }

                public boolean moreWork(){return false;}

                public void cleanUpPartialWork(){}

                public void doSomeWork(){}

            });
        });


        cancelButton.addActionListener(e->{
            if (runningTask != null)
                runningTask.cancel(true);
        });

    }


    private void runInBackground(final Runnable runnable) {

        startButton.addActionListener(e ->{

            class CancelListener implements ActionListener {
                BackgroundTask<?> task;
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (task != null)
                        task.cancel(true);
                }
            }


            final CancelListener cancelListener = new CancelListener();
            cancelListener.task = new BackgroundTask<Void>() {
                @Override
                protected Void compute() {
                    return null;
                }

                private boolean moreWork() {
                    return false;
                }

                private void doSomeWork() {
                }

                public void onCompletion(boolean cancelled, String s, Throwable exception) {
                    cancelButton.removeActionListener(cancelListener);
                    label.setText("done");
                }
            };



        });
    }




}
