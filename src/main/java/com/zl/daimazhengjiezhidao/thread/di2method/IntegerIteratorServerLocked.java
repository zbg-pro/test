package com.zl.daimazhengjiezhidao.thread.di2method;

/**
 * @author Allen.zhang
 * @title: IntegerIteratorServerLocked
 * @projectName zl
 * @description: TODO
 * @date 2021/10/3016:49
 */
public class IntegerIteratorServerLocked {

    private Integer nextValue = 0;

    public synchronized Integer getNextOrNull(){
        if (nextValue < 100000) {
            return nextValue++;
        } else {
            return null;
        }
    }

}
