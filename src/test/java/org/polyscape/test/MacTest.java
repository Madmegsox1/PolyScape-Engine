package org.polyscape.test;

import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.event.EventBus;
import org.polyscape.event.IEvent;
import org.polyscape.font.FontMac;
import org.polyscape.object.ObjectManager;
import org.polyscape.rendering.Display;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.test.ui.HomeScreen;
import org.polyscape.ui.ScreenManager;

import java.io.IOException;

public class MacTest extends Engine {
    public static void main(String[] args) throws IOException {
        Profile.Display.BACKGROUND_COLOR = new float[]{255f/255f, 255f/255f,255f/255f, 1.0f};
        display = new Display("Mac Test");
        display.init(false);

        renderer = new Renderer(display);
        renderer.init();

        renderEngine = new RenderEngine();
        eventBus = new EventBus();


        ObjectManager.clearObjects();


        IEvent<RenderEvent> renderEvent = e -> {
            ObjectManager.collisionCheck();
            ObjectManager.renderObjects();
        };

        RenderEvent.addEvent(renderEvent, RenderEvent.class);

        ScreenManager screenManager = new ScreenManager();

        screenManager.addScreen("Home", new HomeScreen());

        screenManager.setCurrentUi(1, "Home");

        renderEngine.render(renderer, display);
    }

}
