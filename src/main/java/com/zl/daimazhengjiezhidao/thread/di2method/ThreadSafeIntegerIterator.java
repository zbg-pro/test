package com.zl.daimazhengjiezhidao.thread.di2method;

/**
 * @author Allen.zhang
 * @title: ThreadSafeIntegeriterator
 * @projectName zl
 * @description: TODO
 * @date 2021/10/3016:53
 */
public class ThreadSafeIntegerIterator {

    private IntegerIterator iterator = new IntegerIterator();

    public synchronized Integer getNextOrNull(){
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }


}
