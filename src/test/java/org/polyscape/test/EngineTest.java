package org.polyscape.test;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.event.EventBus;
import org.polyscape.event.IEvent;
import org.polyscape.font.Font;
import org.polyscape.font.FontRenderer;
import org.polyscape.rendering.Display;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Texture;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.rendering.shaders.LightingShader;

import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class EngineTest extends Engine {

    public static void main(String[] args){

        Profile.Display.BACKGROUND_COLOR = new float[]{0f/255f, 0f/255f,0f/255f, 1.0f};
        eventBus = new EventBus();

        display = new Display("TEST");
        display.init(true);

        renderer = new Renderer(display);
        renderer.init();

        java.awt.Font jFont = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30);

        fontRenderer = new FontRenderer(new Font(jFont, true));

        java.awt.Font jFont2 = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10);

        fontRenderer.addFont(new Font(jFont2, true));

        Texture texture1 = new Texture("001");

        FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
        ambient.put(new float[] { 1f, 1f, 1f, 1f });
        ambient.flip();

        AtomicInteger vectorX = new AtomicInteger(10);
        AtomicInteger vectorY = new AtomicInteger(10);
        float x = RenderEngine.normalize(vectorX.get(), Profile.Display.WIDTH, 0);
        float y = RenderEngine.normalize(vectorY.get(), Profile.Display.HEIGHT, 0);
        AtomicReference<FloatBuffer> position = new AtomicReference<>(BufferUtils.createFloatBuffer(4));
        position.get().put(new float[] { x, y, 1f, 0f, });
        position.get().flip();


        FloatBuffer dif = BufferUtils.createFloatBuffer(4);
        dif.put(new float[] { 0.5f, 0.5f, 0.5f, 0f, });
        dif.flip();

        LightingShader s = new LightingShader();
        s.create();





        Color lightColor = new Color((float) Math.random() * 10, (float) Math.random() * 10, (float) Math.random() * 10);

        IEvent<RenderEvent> renderEvent = e -> {

            //RenderEngine.drawQuad(new Vector2(vectorX.get(), vectorY.get()), 10, 10, Color.WHITE);
            RenderEngine.drawQuad(new Vector2(200, 200), 100, 100, Color.WHITE);
            RenderEngine.drawQuadTexture(new Vector2(100, 100), 100, 100, texture1);

            glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
            glStencilFunc(GL_EQUAL, 0, 1);
            glColorMask(true, true, true, true);


            s.bind();
            s.loadLightLocation(new Vector2(vectorX.get(), vectorY.get()));
            s.loadLightColor(lightColor);
            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE);
            glBegin(GL_QUADS); {
                glVertex2f(0, 0);
                glVertex2f(0, Profile.Display.HEIGHT);
                glVertex2f(Profile.Display.WIDTH, Profile.Display.HEIGHT);
                glVertex2f(Profile.Display.WIDTH, 0);
            } glEnd();


            glDisable(GL_BLEND);
            s.unbind();
            glClear(GL_STENCIL_BUFFER_BIT);








        };

        IEvent<KeyEvent> keyEvent = e -> {
            if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_W)){
                vectorY.set(vectorY.get() - 10);
            }
            if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_S)){
                vectorY.set(vectorY.get() + 10);
            }
            if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_A)){
                vectorX.set(vectorX.get() - 10);
            }
            if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_D)){
                vectorX.set(vectorX.get() + 10);
            }

            float x1 = RenderEngine.normalize(vectorX.get(), Profile.Display.WIDTH, 0);
            float y2 = RenderEngine.normalize(vectorY.get(), Profile.Display.HEIGHT, 0);
            position.set(BufferUtils.createFloatBuffer(4));
            position.get().put(new float[] { x1, y2, 1f, 0f, });
            position.get().flip();
        };

        IEvent<MouseClickEvent> mouseEvent = e -> {

        };

        RenderEvent.addEvent(renderEvent, RenderEvent.class);
        KeyEvent.addEvent(keyEvent, KeyEvent.class);
        MouseClickEvent.addEvent(mouseEvent, MouseClickEvent.class);



        renderEngine = new RenderEngine();
        renderEngine.render(renderer, display);

        s.remove();
    }
}