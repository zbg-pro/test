package com.zl.javabingfabianchengshizhan.delay;

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

    @Override
    public long getDelay(TimeUnit unit) {
        return 0;
    }

    @Override
    public int compareTo(Delayed o) {
        if (o == null)
            return -1;

        if (o == this)
            return 0;

        if (o instanceof DelayElements) {
            DelayElements delayElements = (DelayElements) o;
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
