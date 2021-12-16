package com.zl.javabingfabianchengshizhan.theadSafePoint;

import net.jcip.annotations.ThreadSafe;
import org.junit.runner.notification.RunListener;

/**
 * @author Allen.zhang
 * @title: SafePoint
 * @projectName zl
 * @description: TODO
 * @date 2021/11/73:08
 */
@ThreadSafe
public class SafePoint {
    private int x, y;

    private SafePoint(int[] a){
        this(a[0], a[1]);
    }

    public SafePoint(SafePoint p) {
        this(p.get());
    }

    public SafePoint(int x, int y){
        this.x = x;
        this.y = y;
    }

    public synchronized int[] get(){
        return new int[]{x, y};
    }

    public synchronized void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
