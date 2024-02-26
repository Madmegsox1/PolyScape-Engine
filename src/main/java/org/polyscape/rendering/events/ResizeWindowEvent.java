package org.polyscape.rendering.events;

import org.polyscape.event.Event;

public class ResizeWindowEvent extends Event<ResizeWindowEvent> {
    public long window;

    public int width;

    public int height;

    public ResizeWindowEvent(long window, int width, int height){
        this.window = window;
        this.width = width;
        this.height = height;
    }

}
