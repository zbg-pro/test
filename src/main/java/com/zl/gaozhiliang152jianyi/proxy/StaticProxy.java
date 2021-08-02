package com.zl.gaozhiliang152jianyi.proxy;



/**
 * @version 1.0
 * @desc:
 * @date 2021/7/24 9:42 上午
 * @auth ALLEN
 */
public class StaticProxy implements Subject {
    private Subject subject = null;

    public StaticProxy(){
        subject = new RealSubject();
    }

    public StaticProxy(Subject subject){
        this.subject = subject;
    }


    @Override
    public void request() {
        before();
        subject.request();
        after();
    }

    private void before() {
        System.out.println("before");
    }

    private void after() {
        System.out.println("after");
    }

    public static void main(String[] args) {
        StaticProxy staticProxy = new StaticProxy(new RealSubject());
        staticProxy.request();
    }
}
