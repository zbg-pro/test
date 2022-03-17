package com.zl.javabingfabianchengshizhan.thread8.puzzlebox;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author Allen.zhang
 * @title: ConcurrentPuzzleSolover
 * @projectName zl
 * @description: TODO
 * @date 2022/3/412:13
 */
public class ConcurrentPuzzleSolver<P, M> {

    private final Puzzle<P, M> puzzle;

    private final ExecutorService executorService;

    private final ConcurrentHashMap<P, Boolean> seen;

    protected final ValueLatch<PuzzleNode<P, M>> solution = new ValueLatch<>();

    public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle, ExecutorService executorService,
                                  ConcurrentHashMap<P, Boolean> seen) {
        this.puzzle = puzzle;
        this.executorService = executorService;
        this.seen = seen;
    }


    private Runnable newTask(P move, M m, PuzzleNode<P, M> node) {
        return new SolverTask(move, m, node);
    }

    public List<M> solve() throws InterruptedException{
        try {
            P pos = puzzle.initialPosition();
            executorService.execute(new SolverTask(pos, null, null));

            //阻塞知道找到解答
            PuzzleNode<P, M> solutionNode = solution.getValue();
            return solutionNode == null? null: solutionNode.asMoveList();

        } finally {
            executorService.shutdown();
        }
    }

    class SolverTask extends PuzzleNode<P, M> implements Runnable {

        public SolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
        }

        @Override
        public void run() {
            // already solved or seen this position, 担心会重复找到其他节点，所以使用isSet顶住
            if (solution.isSet() || seen.putIfAbsent(pos, true) != null) {
                return;
            }

            if (puzzle.isGoal(pos)) {
                solution.setValue(this);
            } else {
                for (M m: puzzle.legalMoves(pos))
                    executorService.execute(newTask(puzzle.move(pos, m), m, this));
            }



        }
    }

    public static void main(String[] args) {
        ConcurrentHashMap<String, Boolean> seen = new ConcurrentHashMap<>();
        System.out.println(seen.putIfAbsent("aa", true) + "...." + seen);
    }

}
