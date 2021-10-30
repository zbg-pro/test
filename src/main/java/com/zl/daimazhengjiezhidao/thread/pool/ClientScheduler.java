package com.zl.daimazhengjiezhidao.thread.pool;

/**
 * @author Allen.zhang
 * @title: ClientScheduler
 * @projectName zl
 * @description: TODO
 * @date 2021/10/3014:07
 */
public interface ClientScheduler {

    void schedule(ClientRequestProcessor clientRequestProcessor);

}
