package com.zl.javabingfabianchengshizhan.synutils;

import org.voovan.tools.TString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Allen.zhang
 * @title: CelluarAutoMata
 * @projectName zl
 * @description: TODO
 * @date 2021/11/83:37
 */
public class CellularAutomata {

    private final Board mainBoard;
    private final CyclicBarrier cyclicBarrier;
    private final Worker[] workers;

    public CellularAutomata(Board board){
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors();
        this.cyclicBarrier = new CyclicBarrier(count, mainBoard::commitNewValues);//所有程序都准备好后，就执行commitNewValues的方法

        this.workers = new Worker[count];
        for (int i = 0; i < count; i++) {
            workers[i] = new Worker(mainBoard.getSubBoard(count, i));
        }

        System.out.println("workers size:" + workers.length);
    }

    public void start(){
        for (Worker worker : workers) {
            new Thread(worker).start();
        }

        mainBoard.waitForConvergence();
    }

    private class Worker implements Runnable{

        private final Board board;

        private Worker(Board board) {
            this.board = board;
        }

        @Override
        public void run() {

            while (board.hasConverged()) {
                for (int i = 0; i < board.getMaxX(); i++)
                    for (int j = 0; j < board.getMaxY(); j++)
                        board.setNewValue(i, j, computeValue(i, j));

                System.out.println(Thread.currentThread().getName() + "刚准备好。。。");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

        private int computeValue(int x, int y) {
            // Compute the new value that goes in (x,y)
            return (x+y)/2;
        }
    }

    interface Board {
        int getMaxX();
        int getMaxY();
        void setNewValue(int x, int y, int value);
        void commitNewValues();
        boolean hasConverged();//汇聚
        void waitForConvergence();
        Board getSubBoard(int numPartitions, int index);
    }

    static class ExBoard implements Board {
        private Map<String, Integer> newValues = new TreeMap<>();
        private List<Board> list = new ArrayList<>();

        private int x;

        private int y;

        public ExBoard(int x, int y){
            this.x = x;
            this.y = y;
        }


        @Override
        public int getMaxX() {
            return x;
        }

        @Override
        public int getMaxY() {
            return y;
        }

        @Override
        public void setNewValue(int x, int y, int value) {
            String key = TString.tokenReplace("{}#{}", x, y);
             newValues.put(key, value);
        }

        @Override
        public void commitNewValues() {
            System.out.println("commitNewValues ... : " + list);
        }

        @Override
        public boolean hasConverged() {
            int total = newValues.values().stream().mapToInt(e -> e).sum();
            return total<=100;
        }

        @Override
        public void waitForConvergence() {
            System.out.println("newValues:" + newValues);
        }

        @Override
        public Board getSubBoard(int numPartitions, int index) {

            Board board = new ExBoard(x-numPartitions, y-index);
            list.add(board);
            return board;
        }

        @Override
        public String toString() {
            return "ExBoard{" +
                    "newValues=" + newValues +
                    ", list=" + list +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
    public static void main(String[] args) {
        Board board = new ExBoard(14,15);

        CellularAutomata cellularAutomata = new CellularAutomata(board);
        cellularAutomata.start();
    }

}
