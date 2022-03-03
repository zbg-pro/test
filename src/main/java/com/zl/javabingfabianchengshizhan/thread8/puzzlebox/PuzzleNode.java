package com.zl.javabingfabianchengshizhan.thread8.puzzlebox;

import net.jcip.annotations.Immutable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Allen.zhang
 * @title: puzzleNode
 * @projectName zl
 * @description: TODO
 * @date 2022/3/216:31
 */
@Immutable
public class PuzzleNode<P, M> {
    final P pos;
    final M move;
    final PuzzleNode<P, M> prev;

    public PuzzleNode(P pos, M move, PuzzleNode<P, M> prev) {
        this.pos = pos;
        this.move = move;
        this.prev = prev;
    }

    List<M> asMoveList() {
        List<M> solution = new LinkedList<M>();
        for (PuzzleNode<P, M> n = this; n.move != null; n = n.prev) {
            solution.add(0, n.move);
        }
        return solution;
    }

}
