package com.zl.javabingfabianchengshizhan.theadSafePoint;

import org.junit.runner.notification.RunListener;
import org.voovan.tools.reflect.annotation.NotSerialization;

import java.util.Vector;

/**
 * @author Allen.zhang
 * @title: BetterVector
 * @projectName zl
 * @description: TODO
 * @date 2021/11/73:44
 */
@RunListener.ThreadSafe
public class BetterVector<E> extends Vector<E> {

    public synchronized boolean putIfAbsent(E x){
        boolean absent = !contains(x);
        if (absent) {
            add(x);
        }
        return absent;
    }


}
