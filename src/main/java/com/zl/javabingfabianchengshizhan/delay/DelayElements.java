package com.zl.javabingfabianchengshizhan.delay;

import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author Allen.zhang
 * @title: DelayElements
 * @projectName zl
 * @description: TODO
 * @date 2022/3/312:48
 */
public class DelayElements implements Delayed {

    private String key;

    // 存活时间
    private long aliveTime;
    // 过期时间
    private long timeOut;

    public DelayElements(){super();}

    public DelayElements(String key, long aliveTime){
        super();
        this.key = key;
        this.aliveTime = aliveTime;
        this.timeOut = TimeUnit.NANOSECONDS.convert(aliveTime, TimeUnit.SECONDS) + System.nanoTime();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(timeOut - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DelayElements other = (DelayElements) o;
//        return aliveTime == that.aliveTime && timeOut == that.timeOut && Objects.equals(key, that.key);
        if (key == null && other.key != null) {
            return false;
        } else if (!key.equals(other.key)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime*result + (key == null? 0: key.hashCode());
        return result;
    }

    @Override
    public int compareTo(Delayed o) {
        if (o == null)
            return -1;

        if (o == this)
            return 0;

        if (o instanceof DelayElements) {
            DelayElements de = (DelayElements) o;
            if (this.timeOut > de.getTimeOut()) {
                return 1;
            } else if (this.timeOut < de.getTimeOut()) {
                return -1;
            }
        }

        return 0;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getAliveTime() {
        return aliveTime;
    }

    public void setAliveTime(long aliveTime) {
        this.aliveTime = aliveTime;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }
}
