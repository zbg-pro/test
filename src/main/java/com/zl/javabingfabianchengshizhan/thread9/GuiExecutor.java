package com.zl.javabingfabianchengshizhan.thread9;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Allen.zhang
 * @title: GuiExecutor
 * @projectName zl
 * @description: TODO
 * @date 2022/3/416:24
 */
public class GuiExecutor extends AbstractExecutorService {

    public static final GuiExecutor instance = new GuiExecutor();

    private GuiExecutor(){}

    public static GuiExecutor instance() {
        return instance;
    }

    public void execute(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else
            SwingUtilities.invokeLater(r);
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

}
