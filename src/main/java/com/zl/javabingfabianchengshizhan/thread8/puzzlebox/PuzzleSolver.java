package com.zl.javabingfabianchengshizhan.thread8.puzzlebox;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Allen.zhang
 * @title: PuzzleSolver
 * @projectName zl
 * @description: TODO
 * @date 2022/3/414:28
 */
public class PuzzleSolver<P, M> extends ConcurrentPuzzleSolver<P, M>{

    private final AtomicInteger taskCount = new AtomicInteger(0);

    public PuzzleSolver(Puzzle<P, M> puzzle, ExecutorService executorService, ConcurrentHashMap<P, Boolean> seen) {
        super(puzzle, executorService, seen);
    }

    protected Runnable newTask(P pos, M m, PuzzleNode<P, M> node){
        return new CountingSolverTask(pos, m, node);
    }

    class CountingSolverTask extends SolverTask {

        public CountingSolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
            taskCount.incrementAndGet();
        }

        @Override
        public void run() {
            try {
                super.run();
            } finally {
                if (taskCount.decrementAndGet() == 0)
                    solution.setValue(null);
            }
        }
    }


}
