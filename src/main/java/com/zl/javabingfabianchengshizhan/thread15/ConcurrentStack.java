package com.zl.javabingfabianchengshizhan.thread15;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Allen.zhang
 * @title: ConcurrentStack
 * @projectName zl
 * @description: TODO
 * @date 2022/5/1821:55
 */
@ThreadSafe
public class ConcurrentStack <E> {
    AtomicReference<Node<E>> top = new AtomicReference<>();


    public E pop(){
        Node<E> oldHead;
        Node<E> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null)
                return null;
            newHead = oldHead.next;
        }while (!top.compareAndSet(oldHead, newHead));

        return oldHead.item;
    }


    public void push(E item){
        Node<E> newHead = new Node<E>(item);
        Node<E> oldHead;

        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));

    }

    private static class Node<E> {
        public final E item;
        public Node<E> next;
        public Node(E item) {
            this.item = item;
        }
    }
}
