package com.zl.javabingfabianchengshizhan.threadSafe;

import net.jcip.annotations.Immutable;

/**
 * @author Allen.zhang
 * @title: Point
 * @projectName zl
 * @description: TODO
 * @date 2021/11/71:04
 */
@Immutable
public class Point {

    public int x, y;

    public Point(int x, int y ){
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
