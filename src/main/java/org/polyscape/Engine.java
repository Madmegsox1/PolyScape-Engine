package org.polyscape;

import org.polyscape.eventbus.EventBus;
import org.polyscape.render.Renderer;
import org.polyscape.render.Window;

public abstract class Engine extends Thread {
    protected static EventBus eventBus;
    protected static Window window;

    protected static Renderer renderer;

    public Engine(){

    }

    public static EventBus getEventBus() {return eventBus;}


    public static void setEventBus(EventBus bus) {
        eventBus = bus;
    }

    public static Window getWindow(){
        return window;
    }

    public static Renderer getRenderer() {
        return renderer;
    }
}
