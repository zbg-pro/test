package com.zl.javabingfabianchengshizhan.cache;

/**
 * @author Allen.zhang
 * @title: Computable
 * @projectName zl
 * @description: 构建高效且可伸缩的结果缓存
 * @date 2021/11/92:05
 */
public interface Computable<A, V> {

    V compute(A arg) throws InterruptedException;

}
