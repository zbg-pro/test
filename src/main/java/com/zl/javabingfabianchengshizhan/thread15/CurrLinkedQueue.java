package com.zl.javabingfabianchengshizhan.thread15;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author Allen.zhang
 * @title: LinkedQueue
 * @projectName zl
 * @description: TODO
 * @date 2022/5/210:45
 */
@ThreadSafe
public class CurrLinkedQueue<E> {

    private static class Node<E> {
        private E item;
        private volatile Node<E> next;

        public Node(E item) {
            this.item = item;
        }

        private boolean compareAndSet(Node<E> expect, Node<E> update){
            return updater.compareAndSet(this, expect, update);
        }
    }

    private static AtomicReferenceFieldUpdater<Node, Node> updater =
            AtomicReferenceFieldUpdater.newUpdater(Node.class, Node.class, "next");



    private final Node<E> dummy = new Node<E>(null);
    private final AtomicReference<Node<E>> head = new AtomicReference<Node<E>>(dummy);
    private final AtomicReference<Node<E>> tail = new AtomicReference<>(dummy);

    public boolean put(E item) {
        Node<E> newNode = new Node(item);
        while (true) {
            Node<E> curTail = tail.get();
            Node<E> tailNext = curTail.next;

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
