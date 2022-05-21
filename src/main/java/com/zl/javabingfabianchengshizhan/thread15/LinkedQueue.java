package com.zl.javabingfabianchengshizhan.thread15;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Allen.zhang
 * @title: LinkedQueue
 * @projectName zl
 * @description: TODO
 * @date 2022/5/210:45
 */
@ThreadSafe
public class LinkedQueue <E> {

    private static class Node<E> {
        final E item;
        final AtomicReference<Node<E>> next;

        public Node(E item, Node<E> next) {
            this.item = item;
            this.next = new AtomicReference<>(next);
        }
    }

    private final Node<E> dummy = new Node<E>(null, null);
    private final AtomicReference<Node<E>> head = new AtomicReference<Node<E>>(dummy);
    private final AtomicReference<Node<E>> tail = new AtomicReference<>(dummy);

    public boolean put(E item) {
        Node<E> newNode = new Node(item, null);
        while (true) {
            Node<E> curTail = tail.get();
            Node<E> tailNext = curTail.next.get();

            if (curTail != tail.get()) {
                continue;
            }

            if (tailNext != null) {
                //队列处于中间状态，尝试推进尾节点
                tail.compareAndSet(curTail, tailNext);
            } else if (curTail.next.compareAndSet(null, newNode)) { //处于稳定状态，尝试推进新节点
                tail.compareAndSet(curTail, newNode); //插入操作成功，尝试推进尾节点
                return true;
            }
        }
    }

}
