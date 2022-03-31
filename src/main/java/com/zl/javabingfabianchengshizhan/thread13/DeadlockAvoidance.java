package com.zl.javabingfabianchengshizhan.thread13;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author Allen.zhang
 * @title: DeadlockAvoidance
 * @projectName zl
 * @description: TODO
 * @date 2022/3/1821:46
 */
public class DeadlockAvoidance {

    private static Random rnd = new Random();
    private static final int DELAY_FIXED = 1;
    private static final int DELAY_RANDOM = 2;

    public boolean transferMoney(Account fromAcct,
                                 Account toAcct,
                                 DollarAmount amount,
                                 long timeout,
                                 TimeUnit unit) throws InsufficientFundsException, InterruptedException {
        long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
        long randMod = getRandomDelayModulusNanos(timeout, unit);
        long stopTime = System.nanoTime() + unit.toNanos(timeout);

        boolean a;
        while (true) {
            boolean fromLock = fromAcct.lock.tryLock();
            if (!fromLock)
                continue;

            if (fromAcct.getBalance().compareTo(amount) < 0)
                throw new InsufficientFundsException();

            try {
                boolean toLock = toAcct.lock.tryLock();
                if (!toLock)
                    continue;

                try {
                    fromAcct.debit(amount);
                    toAcct.credit(amount);
                    a = true;
                } finally {
                    toAcct.lock.unlock();
                }

            } finally {
                fromAcct.lock.unlock();
            }

            if (System.nanoTime() < stopTime) {
                a = false;
            }

            TimeUnit.NANOSECONDS.sleep(fixedDelay+ rnd.nextLong()%randMod);

            return a;
        }

    }

    static long getFixedDelayComponentNanos(long timeout, TimeUnit unit) {
        return DELAY_FIXED;
    }

    static long getRandomDelayModulusNanos(long timeout, TimeUnit unit) {
        return DELAY_RANDOM;
    }

    static class DollarAmount implements Comparable<DollarAmount> {
        public int compareTo(DollarAmount other) {
            return 0;
        }

        DollarAmount(int dollars) {
        }
    }

    class Account{
        public Lock lock;

        void debit(DollarAmount d) {}

        void credit(DollarAmount d) {}

        DollarAmount getBalance(){
            return null;
        }
    }
    class InsufficientFundsException extends Exception {
    }
}


