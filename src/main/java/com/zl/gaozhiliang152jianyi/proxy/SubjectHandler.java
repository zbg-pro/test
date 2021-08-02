package com.zl.gaozhiliang152jianyi.proxy;



import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @version 1.0
 * @desc:
 * @date 2021/7/24 9:44 上午
 * @auth ALLEN
 */
public class SubjectHandler implements InvocationHandler {

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

    public static void main(String[] args) throws Throwable {
        Subject subject = new RealSubject();

        InvocationHandler handler = new SubjectHandler(subject);

        Method method = subject.getClass().getMethods()[0];

        //handler.invoke(null, method, null);

        ClassLoader classLoader = subject.getClass().getClassLoader();

        Subject proxy = (Subject) Proxy.newProxyInstance(classLoader, subject.getClass().getInterfaces(), handler);

        proxy.request();
    }
}
