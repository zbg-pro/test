package com.zl.gaozhiliang152jianyi;

/**
 * @version 1.0
 * @desc:
 * @date 2021/8/3 2:49 下午
 * @auth ALLEN
 */
public class Apple implements Cloneable {

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error();
        }

    }

    public static void main(String[] args) throws CloneNotSupportedException {
        final int maxLoops = 10*10000;
        int loops = 0;
        long start = System.nanoTime();
        Apple apple = new Apple();
        while (++loops < maxLoops) {
            apple.clone();
        }

        long mid = System.nanoTime();
        System.out.println("clone方法耗时：  " + (mid-start) + "ns");

        while (--loops > 0) {
            new Apple();
        }

        long end = System.nanoTime();

        System.out.println("new生成对象耗时：" + (end-mid) + "ns");


    }
}
