package com.zl.javabingfabianchengshizhan.cache;

import java.math.BigInteger;

/**
 * @author Allen.zhang
 * @title: ExpensiveFunction
 * @projectName zl
 * @description: TODO
 * @date 2021/11/92:07
 */
public class ExpensiveFunction implements Computable<String, BigInteger>{

    @Override
    public BigInteger compute(String arg) throws InterruptedException {

        //todo 计算
        return new BigInteger(arg);
    }
}
