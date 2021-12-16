package com.zl.javabingfabianchengshizhan.theadSafePoint;

import net.jcip.annotations.NotThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Allen.zhang
 * @title: ListHelper
 * @projectName zl
 * @description: TODO
 * @date 2021/11/73:57
 */
@NotThreadSafe
public class ListHelper<E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());

    public synchronized boolean putIfAbsent(E x){
        boolean absent = !list.contains(x);
        if (absent) {
            list.add(x);
        }
        return absent;
    }

}
