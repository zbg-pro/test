package com.zl.gaozhiliang152jianyi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @version 1.0
 * @desc:
 * @date 2021/7/21 8:33 下午
 * @auth ALLEN
 */
@Access(level = CommonIdentifier.Author)
class Foo {
}

class Test{
    public static void main(String[] args) {
        Foo b = new Foo();
        Access access = b.getClass().getAnnotation(Access.class);
        if (access == null || !access.level().identify()) {
            System.out.println(access.level().REFUSE_WORD);
        }
    }

}

class Foo2{
    public void arrayMethod(String[] arr) {

    }

    public void arrayMethod(Integer[] arr) {

    }

    public void listMethod(List<String> list){

    }

    /*public void listMethod(List<Integer> list) {

    }*/
}

class Foo3<T> {
    private T t;
    private T[] arrT;
    private List<T> list = new ArrayList<>();
    public Foo3(){
        try {
            Class<?> type = Class.forName("");
            t = (T)type.newInstance();
            arrT = (T[])Array.newInstance(type, 5);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}

class ArrayUtils{
    public static <T> List<T> asList(T ...t){
        List<T> list = new ArrayList<>();
        Collections.addAll(list, t);
        return list;
    }

    public static void main(String[] args) {
        List<String> list = asList("1", "a", "b");
        List<Integer> list1 = asList();
        list.add("c");
        list1.add(1);

        System.out.println(list);
        System.out.println(list1);

        List<String> list2 = Arrays.asList("a");
        //list2.add("1");

        List<Number> list3 = ArrayUtils.<Number>asList(1,2,3.2);

        read2(list);

    }


    public static <E> void read(List<? super E> list) {
        for (Object o: list) {
            System.out.println(o);
        }
    }

    public static <E> void read2(List<? extends E> list){
        for (Object o: list) {
            System.out.println(o);
        }
    }

    public static void write(List<? super Number> list) {
        list.add(1);
        list.add(null);
    }
}