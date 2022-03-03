package com.zl.javabingfabianchengshizhan.thread8.puzzlebox;

import java.util.Set;

/**
 * @author Allen.zhang
 * @title: Puzzle
 * @projectName zl
 * @description: TODO
 * @date 2022/3/216:05
 */
public interface Puzzle<P, M> {
    P initialPosition();
    boolean isGoal(P position);//判断是否是有效移动的规则集
    Set<M> legalMoves(P position);
    P move(P position, M move);
}
