package com.zl.javabingfabianchengshizhan.thread8.puzzlebox;

import io.netty.util.internal.ConcurrentSet;

import java.util.List;
import java.util.Set;

/**
 * @author Allen.zhang
 * @title: SequentialPuzzleSolver
 * @projectName zl
 * @description: TODO
 * @date 2022/3/216:58
 */
public class SequentialPuzzleSolver<P, M> {
    private final Puzzle<P, M> puzzle;
    private final Set<P> seen = new ConcurrentSet<>();

    public SequentialPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
    }

    public List<M> solve(){
        P pos = puzzle.initialPosition();
        return search(new PuzzleNode(pos, null, null));

    }

    private List<M> search(PuzzleNode<P, M> startNode) {
        if (seen.contains(startNode.move))
            return null;

        seen.add(startNode.pos);
        if (puzzle.isGoal(startNode.pos))
            return startNode.asMoveList();

        for (M move: puzzle.legalMoves(startNode.pos)) {
            P newPos = puzzle.move(startNode.pos, move);
            PuzzleNode<P, M> child = new PuzzleNode<>(newPos, move, startNode);
            List<M> result = search(child);
            if (result != null)
                return result;
        }

        return null;
    }
}
