package com.zl.daimazhengjiezhidao.thread.di2method;

/**
 * @author Allen.zhang
 * @title: IntegerIterator
 * @projectName zl
 * @description: TODO
 * @date 2021/10/3016:09
 */
public class IntegerIterator {

    private Integer nextValue = 0;

    public synchronized boolean hasNext(){
        return nextValue < 100000;
    }

    public synchronized Integer next() throws IteratorPastEndException {
        if (nextValue == 100000) {
            throw new IteratorPastEndException();
        }
        return nextValue++;
    }


    public synchronized Integer getNextValue(){
        return nextValue;
    }

    public static void main(String[] args) {
        IntegerIterator iterator = new IntegerIterator();
        while (iterator.hasNext()) {
            //todo  基于客户端的代码锁定
            int nextValue = iterator.next();
            //do something
        }
    }
}
