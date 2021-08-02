package com.zl.gaozhiliang152jianyi.exception;

/**
 * @version 1.0
 * @desc:
 * @date 2021/7/28 1:42 下午
 * @auth ALLEN
 */
public class Foo {

    public static boolean m(){
        StackTraceElement[] ste = new Throwable().getStackTrace();

        for (StackTraceElement st: ste) {
            if (st.getMethodName().equals("m1")) {
                return true;
            }
        }
        return false;
    }

}

class Invoker {
    public static void m1(){
        System.out.println("m1："+Foo.m());
    }

    public static void m2(){
        System.out.println("m2:"+Foo.m());
    }

    public static void main(String[] args) {
        m1();
        m2();
    }
}
