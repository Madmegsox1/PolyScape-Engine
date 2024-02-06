package org.polyscape.test;

import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.event.EventBus;
import org.polyscape.event.IEvent;
import org.polyscape.font.FontMac;
import org.polyscape.object.BaseObject;
import org.polyscape.object.ObjectManager;
import org.polyscape.object.StaticObject;
import org.polyscape.rendering.Display;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Texture;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.HomeScreen;
import org.polyscape.ui.ScreenManager;

import java.io.IOException;

public class MacTest extends Engine {
    public static void main(String[] args) throws IOException {
        Profile.Display.BACKGROUND_COLOR = new float[]{255f/255f, 255f/255f,255f/255f, 1.0f};
        display = new Display("Mac Test");
        display.init(true);

        renderer = new Renderer(display);
        renderer.init();

        FontMac test = new FontMac();



        renderEngine = new RenderEngine();
        eventBus = new EventBus();

        //Texture t = new Texture(1024, 1024, test.atlas);

        ObjectManager.clearObjects();
        StaticObject object = new StaticObject();
        object.setPosition(new Vector2(100, 100));
        object.setWidth(1024);
        object.setHeight(1024);
        object.setTextured(true);
        object.setTexture(test.texture);
        //object.setBaseColor(Color.BLACK);
        //object.setWireframe(true);
        object.setWireframeTextured(true);

        //ObjectManager.addObject(object);




        IEvent<RenderEvent> renderEvent = e -> {
            ObjectManager.collisionCheck();
            ObjectManager.renderObjects();
            test.generateGlyphs(100, 100);
        };

        RenderEvent.addEvent(renderEvent, RenderEvent.class);

        //ScreenManager screenManager = new ScreenManager();

        //screenManager.addScreen("Home", new HomeScreen());

        //screenManager.setCurrentUi(1, "Home");

        renderEngine.render(renderer, display);
    }

}
