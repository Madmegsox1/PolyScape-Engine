package org.polyscape.test;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.event.EventBus;
import org.polyscape.event.EventMetadata;
import org.polyscape.event.IEvent;
import org.polyscape.font.Font;
import org.polyscape.font.FontRenderer;
import org.polyscape.object.BaseObject;
import org.polyscape.object.ObjectManager;
import org.polyscape.object.StaticObject;
import org.polyscape.rendering.Display;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.rendering.shaders.LightingShader;

import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;
import static org.lwjgl.opengl.GL11.*;

public class EngineTest extends Engine {

    public static void main(String[] args) {

        Profile.Display.BACKGROUND_COLOR = new float[]{0f/255f, 0f/255f,0f/255f, 1.0f};



        java.awt.Font jFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 20);

        fontRenderer = new FontRenderer(new Font(jFont, true));

        java.awt.Font jFont2 = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10);

        fontRenderer.addFont(new Font(jFont2, true));

        eventBus = new EventBus();
        display = new Display("TEST");
        display.init(true);

        renderer = new Renderer(display);
        renderer.init();

        fontRenderer.compileFontTextures();
        //SpriteSheet spriteSheet = new SpriteSheet("003", 10, 10);


        AtomicInteger vectorX = new AtomicInteger(200);
        AtomicInteger vectorY = new AtomicInteger(200);
        float x = RenderEngine.normalize(vectorX.get(), Profile.Display.WIDTH, 0);
        float y = RenderEngine.normalize(vectorY.get(), Profile.Display.HEIGHT, 0);
        AtomicReference<FloatBuffer> position = new AtomicReference<>(BufferUtils.createFloatBuffer(4));
        position.get().put(new float[] { x, y, 1f, 0f, });
        position.get().flip();



        LightingShader s = new LightingShader();
        s.create();

        LightingShader s2 = new LightingShader();
        s2.create();

        ObjectManager.clearObjects();

        BaseObject ob = new BaseObject();

        ob.setPosition(new Vector2(300, 300));
        ob.setWidth(100);
        ob.setHeight(100);


        ObjectManager.addObject(ob);


        StaticObject ob2 = new StaticObject();
        ob2.setPosition(new Vector2(200, 600));
        ob2.setWidth(400);
        ob2.setHeight(200);
        ob2.setBaseColor(Color.GREEN);

        ObjectManager.addObject(ob2);

        StaticObject ob3 = new StaticObject();
        ob3.setPosition(new Vector2(400, 400));
        ob3.setWidth(200);
        ob3.setHeight(400);
        ob3.setBaseColor(Color.BLUE);

        ObjectManager.addObject(ob3);


        Color lightColor = new Color(1, 10, 10);



        Vector2 light = new Vector2(vectorX.get(), vectorY.get());


        IEvent<RenderEvent> renderEvent = e -> {

            //RenderEngine.drawQuadTexture(new Vector2(100, 100), 100, 100, texture1);

            fontRenderer.renderFont("FPS: " + RenderEngine.fps, new Vector2(10, 10));
            fontRenderer.renderFont("Delta: " + RenderEngine.deltaTime, new Vector2(10, 60));
            fontRenderer.renderFont("Pos: " + ob.getPosition().toString(), new Vector2(10, 110));
            fontRenderer.renderFont(ob.getSpeed().toString(), new Vector2(Profile.Display.WIDTH - 200, 10));
            fontRenderer.renderFont(ob.getVelocity().toString(), new Vector2(Profile.Display.WIDTH - 200, 60));

            glColorMask(false, false, false, false);
            glStencilFunc(GL_ALWAYS, 1, 1);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);


            glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
            glStencilFunc(GL_EQUAL, 0, 1);
            glColorMask(true, true, true, true);



            s.bind();
            s.loadLightLocation(new Vector2(vectorX.get(), vectorY.get()));
            s.loadLightBrightness(2f);
            s.loadLightWidth(10f);
            s.loadLightHeight(10f);
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

            glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
            glStencilFunc(GL_EQUAL, 0, 1);
            glColorMask(true, true, true, true);

            s2.bind();
            s2.loadLightLocation(new Vector2(700, 400));
            s2.loadLightBrightness(2f);
            s2.loadLightWidth(10f);
            s2.loadLightHeight(10f);
            s2.loadLightColor(lightColor);


            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE);
            glBegin(GL_QUADS); {
                glVertex2f(0, 0);
                glVertex2f(0, Profile.Display.HEIGHT);
                glVertex2f(Profile.Display.WIDTH, Profile.Display.HEIGHT);
                glVertex2f(Profile.Display.WIDTH, 0);
            } glEnd();


            glDisable(GL_BLEND);
            s2.unbind();
            glClear(GL_STENCIL_BUFFER_BIT);
            glColor3f(0, 0, 0);
            //ObjectManager.renderObjects();
            //ob.runAction("Test");

        };

        IEvent<KeyEvent> keyEvent = e -> {
            float yv = 0;
            float xv = 0;

            if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_W)){
                yv -= 20f;
            }
            if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_S)){
                yv += 20f;
            }
            if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_A)){
                xv -= 20f;
            }
            if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_D)){
                xv += 20f;

            }

            ob.addForce(xv, yv);


            float x1 = RenderEngine.normalize(vectorX.get(), Profile.Display.WIDTH, 0);
            float y2 = RenderEngine.normalize(vectorY.get(), Profile.Display.HEIGHT, 0);
            position.set(BufferUtils.createFloatBuffer(4));
            position.get().put(new float[] { x1, y2, 1f, 0f, });
            position.get().flip();
        };

        IEvent<MouseClickEvent> mouseEvent = e -> {

        };

        RenderEvent.addEvent(renderEvent, new EventMetadata(RenderEvent.class, 0));
        KeyEvent.addEvent(keyEvent, new EventMetadata(KeyEvent.class, 0));
        MouseClickEvent.addEvent(mouseEvent, new EventMetadata(MouseClickEvent.class, 0));



        renderEngine = new RenderEngine();
        renderEngine.render(renderer, display);

        s.remove();
    }
}
