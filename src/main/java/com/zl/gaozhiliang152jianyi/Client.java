package com.zl.gaozhiliang152jianyi;

import java.util.*;

/**
 * @version 1.0
 * @desc:
 * @date 2021/7/19 6:44 下午
 * @auth ALLEN
 */
public class Client {

    public static void main(String[] args) {
        int fee = 2;
        saveDefault:save(fee);

        Integer[] a = {1,2,3,4,5};
        List list = Arrays.asList(a);
        List list2 = new ArrayList(list);
        List list3 = new ArrayList(list);
        System.out.println(list3.equals(list2));
        System.out.println(list);

        List<String> c1 = new ArrayList<>();
        c1.add("A");c1.add("B");
        List<String> c2 = new ArrayList<>(c1);
        System.out.println("c1==c2?" + c1.equals(c2));

        int stuNum = 80*10000;
        List<Integer> scores = new ArrayList<>(stuNum);
        for (int i = 0; i < stuNum; i++) {
            scores.add(new Random().nextInt(150));
        }

        Long startTime = System.currentTimeMillis();
        System.out.println("平均分数：" + average2(scores));
        System.out.println("耗时：" + (System.currentTimeMillis()-startTime));

        List<Integer> initData = Collections.nCopies(100, 0);
        System.out.println(initData);
        ArrayList list1 = new ArrayList(initData);
        list1.subList(20,30).clear();
        System.out.println(list1.size());

    }

    public static int average(List<Integer> list){
        Integer sum = list.parallelStream().mapToInt(Integer::intValue).sum();
        return sum/list.size();
    }

    public static int average2(List<Integer> list) {
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum+=list.get(i);
        }
        return sum/list.size();
    }

    static void saveDefault(){
        System.out.println(1);
    }

    static void save(int fee){
        System.out.println("fee:"+fee);
    }

}
