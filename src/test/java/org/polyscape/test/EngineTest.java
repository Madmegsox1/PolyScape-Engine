package org.polyscape.test;

import org.junit.jupiter.api.Test;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.event.EventBus;
import org.polyscape.event.EventMetadata;
import org.polyscape.event.IEvent;
import org.polyscape.rendering.Display;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Texture;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;

import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class EngineTest extends Engine {

    @Test
    public void LightTest() {

        Profile.Display.BACKGROUND_COLOR = new float[]{0f / 255f, 0f / 255f, 0f / 255f, 1.0f};


        eventBus = new EventBus();
        display = new Display("TEST");
        display.init(true);

        renderer = new Renderer(display);
        renderer.init();

        //SpriteSheet spriteSheet = new SpriteSheet("003", 10, 10);


        AtomicInteger vectorX = new AtomicInteger(200);
        AtomicInteger vectorY = new AtomicInteger(200);
        float x = RenderEngine.normalize(vectorX.get(), Profile.Display.WIDTH, 0);
        float y = RenderEngine.normalize(vectorY.get(), Profile.Display.HEIGHT, 0);
        AtomicReference<FloatBuffer> position = new AtomicReference<>(BufferUtils.createFloatBuffer(4));
        position.get().put(new float[]{x, y, 1f, 0f,});
        position.get().flip();


        var txt = new Texture("001");
        var txt2 = new Texture("002");
        var txt3 = new Texture("003");

        IEvent<RenderEvent> renderEvent = e -> {

            RenderEngine.drawQuadTextureAngleNew(new Vector2(100, 100), 0, 100, 100, txt, Color.WHITE);
            RenderEngine.drawQuadTextureAngleNew(new Vector2(200, 100), 0, 100, 100, txt2, Color.WHITE);
            RenderEngine.drawQuadTextureAngleNew(new Vector2(300, 100), 0, 100, 100, txt3, Color.WHITE);
            RenderEngine.drawLineNew(new Vector2(300, 300), new Vector2(400, 400), 4f, Color.BLUE);
            RenderEngine.drawCircleAngleTexturedNew(new Vector2(300, 500), 50f, 0, 360, txt2);

        };

        IEvent<KeyEvent> keyEvent = e -> {
            float yv = 0;
            float xv = 0;

            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_W)) {
                yv -= 20f;
            }
            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_S)) {
                yv += 20f;
            }
            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_A)) {
                xv -= 20f;
            }
            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_D)) {
                xv += 20f;

            }


            float x1 = RenderEngine.normalize(vectorX.get(), Profile.Display.WIDTH, 0);
            float y2 = RenderEngine.normalize(vectorY.get(), Profile.Display.HEIGHT, 0);
            position.set(BufferUtils.createFloatBuffer(4));
            position.get().put(new float[]{x1, y2, 1f, 0f,});
            position.get().flip();
        };

        IEvent<MouseClickEvent> mouseEvent = e -> {
            var pos = Display.getWorldMousePosition(getDisplay().getWindow());
            vectorX.set((int) pos.x);
            vectorY.set((int) pos.y);

        };

        RenderEvent.addEvent(renderEvent, new EventMetadata(RenderEvent.class, 0));
        KeyEvent.addEvent(keyEvent, new EventMetadata(KeyEvent.class, 0));
        MouseClickEvent.addEvent(mouseEvent, new EventMetadata(MouseClickEvent.class, 0));


        renderEngine = new RenderEngine();
        renderEngine.render(renderer, display);

    }
}
