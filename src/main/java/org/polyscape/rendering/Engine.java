package org.polyscape.rendering;


import org.polyscape.event.EventBus;
import org.polyscape.font.FontRenderer;

public abstract class Engine extends Thread {
    protected static EventBus eventBus;
    protected static Display display;
    protected static Renderer renderer;
    protected static RenderEngine renderEngine;
    protected static FontRenderer fontRenderer;


    public Engine(){

    }

    public static EventBus getEventBus() {return eventBus;}


    public static void setEventBus(EventBus bus) {
        eventBus = bus;
    }

    public static Display getDisplay(){
        return display;
    }

    public static Renderer getRenderer() {
        return renderer;
    }

    public static RenderEngine getRenderEngine() {
        return renderEngine;
    }

    public static FontRenderer getFontRenderer() {
        return fontRenderer;
    }


}
