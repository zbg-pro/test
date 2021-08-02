package com.zl.gaozhiliang152jianyi.proxy;



/**
 * @version 1.0
 * @desc:
 * @date 2021/7/24 9:40 上午
 * @auth ALLEN
 */
public class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println(this.getClass().getTypeName() + " request");
    }

}
