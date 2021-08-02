package com.zl.gaozhiliang152jianyi;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @version 1.0
 * @desc:
 * @date 2021/7/23 6:49 下午
 * @auth ALLEN
 */
public class ClassTest extends TestCase {

    @Test
    public void test1() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        System.out.println(String.class);
        System.out.println(new String().getClass());
        System.out.println(ArrayList.class);
        System.out.println(new ArrayList<String[]>().getClass());

        System.out.println(Arrays.asList(String.class.getMethods()));

        String s = (String) Class.forName("java.lang.String").newInstance();
        System.out.println(s.getClass());
    }

}
