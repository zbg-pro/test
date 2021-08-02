package com.zl.iolearn.insanecoder.test;

import org.junit.Test;
import org.voovan.tools.TString;

/**
 * Created by hl on 2018/7/2.
 */
public class TestReg {

    @Test
    public void test1(){
        System.out.println(TString.regexMatch("runob", "runoo*b"));
        System.out.println(TString.regexMatch("runoob", "runoo?b"));
    }

}
