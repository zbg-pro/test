package com.zl.javabingfabianchengshizhan.thread7cancelclose;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Allen.zhang
 * @title: CheckForMail
 * @projectName zl
 * @description: TODO
 * @date 2022/1/50:33
 */
public class CheckForMail {

    public boolean checkMail(Set<String> hosts, long timeout, TimeUnit unit) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        AtomicBoolean hasNewEmail = new AtomicBoolean(false);

        try {
            for (final String host: hosts) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (check(host)) {
                            hasNewEmail.set(true);
                        }
                    }

                });
            }
        } finally {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        }

        return hasNewEmail.get();
    }


    private boolean check(String host) {
        /* check code */
        return false;
    }
}
