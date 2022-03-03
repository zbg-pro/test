package com.zl.javabingfabianchengshizhan.cache;

import net.jcip.annotations.GuardedBy;
import net.jcip.examples.LaunderThrowable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Allen.zhang
 * @title: Memoizer1
 * @projectName zl
 * @description: TODO
 * @date 2021/11/92:08
 */
public class Memoizer<A, V> implements Computable<A, V> {

    @GuardedBy("this")
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

    private final Computable<A, V> c;

    public Memoizer(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        while (true) {

            Future<V> f = cache.get(arg);
            if (f == null) {
                Callable<V> eval = (Callable) () -> c.compute(arg);
                FutureTask<V> ft = new FutureTask<V>(eval);
                f = ft;
                cache.putIfAbsent(arg, ft);
                ft.run();
            }

            try {
                return f.get();
            } catch (CancellationException e) {
                cache.remove(arg, f);
            } catch (ExecutionException e) {
                throw LaunderThrowable.launderThrowable(e);
            }

        }

    }

    public static void main(String[] args) throws InterruptedException {

        Computable<BigInteger, BigInteger[]> c = arg -> {
            BigInteger[] bigIntegers = new BigInteger[1];
            bigIntegers[0] = arg;
            return bigIntegers;
        };

        Computable<BigInteger, BigInteger[]> cache = new Memoizer<BigInteger, BigInteger[]>(c);

        System.out.println(cache.compute(new BigInteger("1")));
    }

}
