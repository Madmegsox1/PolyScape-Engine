package org.polyscape.logic.script;

import org.polyscape.Engine;
import org.polyscape.event.Event;
import org.polyscape.event.EventBus;
import org.polyscape.font.FontRenderer;
import org.polyscape.rendering.Display;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;
import org.polyscape.ui.ScreenManager;

public class LogicScriptApi {

    public static EventBus getEventBus(){
        return Engine.getEventBus();
    }

    public static RenderEngine getRenderEngine(){
        return Engine.getRenderEngine();
    }

    public static Display getDisplay(){
        return Engine.getDisplay();
    }

    public static Renderer getRenderer(){
        return Engine.getRenderer();
    }

    public static FontRenderer getFontRenderer() {
        return Engine.getFontRenderer();
    }

    public static ScreenManager getScreenManager() {
        return Engine.getScreenManager();
    }

    public static void writeLine(String message){
        System.out.println("[JS] "+message);
    }


}
