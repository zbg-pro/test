package com.zl.javabingfabianchengshizhan.theadSafePoint;

import net.jcip.annotations.ThreadSafe;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Allen.zhang
 * @title: ListHelper2
 * @projectName zl
 * @description: TODO
 * @date 2021/11/74:08
 */
@ThreadSafe
public class ListHelper2<E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<>());

    public boolean putIfAbsent(E x){
        synchronized (list) {
            boolean absent = !list.contains(x);
            if (absent) {
                list.add(x);
            }
            return absent;
        }
    }
}
