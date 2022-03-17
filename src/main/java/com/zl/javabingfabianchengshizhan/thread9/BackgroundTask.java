package com.zl.javabingfabianchengshizhan.thread9;

import java.util.concurrent.*;

/**
 * @author Allen.zhang
 * @title: BackGroundTask
 * @projectName zl
 * @description: TODO
 * @date 2022/3/50:05
 */
public abstract class BackgroundTask<V> implements Runnable, Future<V> {

    private final FutureTask<V> computation = new BackgroundTask.Computation();


    private class Computation extends FutureTask<V> {

        public Computation() {
            super(BackgroundTask.this::compute);
        }

        protected final void done(){
            GuiExecutor.instance().execute(() -> {
                V value = null;
                Throwable thrown = null;
                boolean cancelled = false;

                try {
                    value = get();
                } catch (ExecutionException e) {
                    thrown = e;
                } catch (InterruptedException e) {

                } catch (CancellationException e) {
                    cancelled = true;
                } finally {
                    onCompletion(value, thrown, cancelled);
                }

            });
        }

    }

    protected abstract V compute();

    protected void setProgress(final int current, final int max) {
        net.jcip.examples.GuiExecutor.instance().execute(new Runnable() {
            public void run() {
                onProgress(current, max);
            }
        });
    }

    // Called in the event thread
    protected void onCompletion(V result, Throwable exception,
                                boolean cancelled) {
    }

    protected void onProgress(int current, int max) {
    }

    public boolean cancel(boolean mayInterruptIfRunning){
        return computation.cancel(mayInterruptIfRunning);
    }

    public V get() throws InterruptedException, ExecutionException {
        return computation.get();
    }

    public V get(long timeout, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return computation.get(timeout, timeUnit);
    }

    public boolean isCancelled() {
        return computation.isCancelled();
    }

    public boolean isDone() {
        return computation.isDone();
    }

    public void run() {
        computation.run();
    }

}
