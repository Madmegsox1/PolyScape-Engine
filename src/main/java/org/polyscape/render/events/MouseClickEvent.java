package org.polyscape.render.events;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.polyscape.eventbus.Event;

import java.nio.DoubleBuffer;

public class MouseClickEvent extends Event<MouseClickEvent> {
    public final int key, action;
    public final double mX, mY;
    public final long window;

    public MouseClickEvent(int key, int action, long window){
        this.key = key;
        this.action = action;
        this.window = window;

        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(window, x, y);
        mX = x.get(0);
        mY = y.get(0);
        x.clear();
        y.clear();
    }
}
