package org.polyscape.test;

import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.event.EventBus;
import org.polyscape.event.IEvent;
import org.polyscape.object.BaseObject;
import org.polyscape.object.ObjectManager;
import org.polyscape.rendering.Display;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.RenderEvent;

public class MacTest extends Engine {
    public static void main(String[] args){
        Profile.Display.BACKGROUND_COLOR = new float[]{255f/255f, 255f/255f,255f/255f, 1.0f};
        display = new Display("Mac Test");
        display.init(true);

        renderer = new Renderer(display);
        renderer.init();
        renderEngine = new RenderEngine();
        eventBus = new EventBus();

        ObjectManager.clearObjects();
        BaseObject object = new BaseObject();
        object.setPosition(new Vector2(100, 100));
        object.setWidth(100);
        object.setHeight(100);
        object.setBaseColor(Color.BLUE);
        ObjectManager.addObject(object);

        IEvent<RenderEvent> renderEvent = e -> {
            ObjectManager.collisionCheck();
            ObjectManager.renderObjects();
        };

        RenderEvent.addEvent(renderEvent, RenderEvent.class);
        renderEngine.render(renderer, display);
    }

}
