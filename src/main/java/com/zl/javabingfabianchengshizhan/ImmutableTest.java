package com.zl.javabingfabianchengshizhan;

import net.jcip.annotations.Immutable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Allen.zhang
 * @title: ImmutableTest
 * @projectName zl
 * @description: TODO
 * @date 2021/11/24:06
 */
@Immutable
public final class ImmutableTest {

    private Set<String> stooges = new HashSet<>();

    public ImmutableTest(){
        stooges.add("aa");
        stooges.add("bb");
        stooges.add("cc");
        stooges = Collections.unmodifiableSet(stooges);
    }

    public boolean isStooge(String name){
        return stooges.contains(name);
    }

    public static void main(String[] args) {
        ImmutableTest immutableTest = new ImmutableTest();
        immutableTest.stooges.add("aaa");
        System.out.println(immutableTest.isStooge("aaa"));
    }


}
