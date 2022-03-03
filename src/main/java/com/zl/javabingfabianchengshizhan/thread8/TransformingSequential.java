package com.zl.javabingfabianchengshizhan.thread8;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author Allen.zhang
 * TransformingSequential
 * <p/>
 * Transforming sequential execution into parallel execution
 * @date 2022/3/215:18
 */
public abstract class TransformingSequential {

    void processSequential(List<Element> elements) {
        for (Element e: elements)
            process(e);
    }

    void processInParallel(Executor exec, List<Element> elements){
        for (Element e: elements) {
            exec.execute(() -> process(e));
        }
    }

    protected abstract void process(Element e);

    public <T> void sequentialRecursive(List<Node<T>> list, Collection<T> results){
        for (Node<T> node: list) {
            results.add(node.compute());
            sequentialRecursive(node.getChildren(), results);
        }
    }

    public <T> void parallelRecursive(final Executor exec, List<Node<T>> list, final Collection<T> results) {
        for (Node<T> node: list) {
            exec.execute(() -> {
                results.add(node.compute());
            });
            sequentialRecursive(node.getChildren(), results);
        }
    }

    public <T> Collection<T> getParallelResults(List<Node<T>> nodes) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Queue<T> resultQueue = new ConcurrentLinkedQueue<T>();
        parallelRecursive(exec, nodes, resultQueue);
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        return resultQueue;
    }

    interface Element {
    }

    interface Node<T> {
        T compute();
        List<Node<T>> getChildren();
    }
}
