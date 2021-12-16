package com.zl.javabingfabianchengshizhan.threadSafe;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Allen.zhang
 * @title: VisualComponent
 * @projectName zl
 * @description: TODO
 * @date 2021/11/71:34
 */
public class VisualComponent {

    private final List<KeyListener> keyListeners = new CopyOnWriteArrayList<>();

    private final List<MouseListener> mouseListeners = new CopyOnWriteArrayList<>();

    public void addKeyListener(KeyListener keyListener){
        keyListeners.add(keyListener);
    }

    public void addMouseListener(MouseListener mouseListener){
        mouseListeners.add(mouseListener);
    }

    public boolean removeKeyListener(KeyListener keyListener){
        return keyListeners.remove(keyListener);
    }

    public boolean removeMouseListener(MouseListener mouseListener){
        return mouseListeners.remove(mouseListener);
    }
}
