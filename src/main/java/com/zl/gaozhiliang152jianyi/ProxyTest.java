package com.zl.gaozhiliang152jianyi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @version 1.0
 * @desc:
 * @date 2021/7/23 7:26 下午
 * @auth ALLEN
 */
public class ProxyTest {


}

interface Subject{
    public void request();
}

class RealSubject implements Subject {

    @Override
    public void request() {

    }
}

class StaticProxy implements Subject{

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
    }

    private void after() {
    }
}

class SubjectHandler implements InvocationHandler {

    private Subject subject;

    public SubjectHandler(Subject subject){
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("before");

        Object obj = method.invoke(subject, args);

        System.out.println("after");

        return obj;
    }

    public static void main(String[] args) {
        Subject subject = new RealSubject();
        InvocationHandler handler = new SubjectHandler(subject);
        ClassLoader cl = subject.getClass().getClassLoader();
        Subject proxy = (Subject)java.lang.reflect.Proxy.newProxyInstance(cl, subject.getClass().getInterfaces(), handler);
        proxy.request();
    }
}
