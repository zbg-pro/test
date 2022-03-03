package com.zl.javabingfabianchengshizhan.cache;

import net.jcip.annotations.GuardedBy;
import net.jcip.examples.LaunderThrowable;
import net.jcip.examples.ValueLatch;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Allen.zhang
 * @title: Memoizer1
 * @projectName zl
 * @description: TODO
 * @date 2021/11/92:08
 */
public class Memoizer3<A, V> implements Computable<A, V> {

    @GuardedBy("this")
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

    private final Computable<A, V> c;

    public Memoizer3(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);
        if (f == null) {
            Callable<V> eval = (Callable) () -> c.compute(arg);
            FutureTask<V> ft = new FutureTask<V>(eval);
            f = ft;
            cache.put(arg, f);
            ft.run();
        }

        try {
            return f.get();
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e);
        }
    }

}
