package com.zl.javabingfabianchengshizhan.thread15;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Allen.zhang
 * @title: CasNumberRange
 * @projectName zl
 * @description: TODO
 * @date 2022/5/1112:24
 */
@ThreadSafe
public class CasNumberRange {

    @Immutable
    private static class IntPair {
        final int lower;
        final int upper;

        public IntPair(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }
    }

    private final AtomicReference<IntPair> values =
            new AtomicReference<>(new IntPair(0, 0));

    public int getLower() {
        return values.get().lower;
    }

    public int getUpper() {
        return values.get().upper;
    }

    public void setLower(int i) {
        while (true) {
            IntPair oldv = values.get();
            if (i > oldv.upper) {
                throw new IllegalArgumentException("Can't set lower to " + i + " > upper");
            }

            IntPair newv = new IntPair(i, oldv.upper);
            if (values.compareAndSet(oldv, newv))
                return;
        }

    }

    public void setUpper(int i) {

        while (true) {
            IntPair oldv = values.get();
            if (i < oldv.lower) {
                throw new IllegalArgumentException("Can't set lower to " + i + " < lower");
            }

            IntPair newv = new IntPair(oldv.lower, i);
            if (values.compareAndSet(oldv, newv))
                return;
        }

    }

    public static void main(String[] args) {
        CasNumberRange casNumberRange = new CasNumberRange();
        System.out.println(casNumberRange.getLower());
        System.out.println(casNumberRange.getUpper());

        int[] arr = new int[]{1,4,6,8,4,28,2,0,4,39,11};
        for (int i = 0; i < arr.length; i++) {
            casNumberRange.setUpper(arr[i]);
        }

        System.out.println(casNumberRange.getUpper());
    }
}


