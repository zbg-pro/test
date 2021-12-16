package com.zl.dd;

import org.voovan.tools.FastThreadLocal;

/**
 * @author Allen.zhang
 * @title: ThreadLocalTest
 * @projectName zl
 * @description: TODO
 * @date 2021/12/1213:02
 */
public class ThreadLocalTest {

    public static FastThreadLocal<Integer> XX = FastThreadLocal.withInitial(()-> {
        return 1;
    });

    public static void main(String[] args) {
        Integer a = new Integer(100);
        XX.set(a);
        Integer aa = XX.get();
        System.out.println(aa);
    }


}
