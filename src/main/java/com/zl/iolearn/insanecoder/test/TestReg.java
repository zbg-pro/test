package com.zl.iolearn.insanecoder.test;

import com.dd.tools.TString;
import org.junit.Test;

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
