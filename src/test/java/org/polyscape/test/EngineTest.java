package org.polyscape.test;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.event.EventBus;
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
import org.polyscape.rendering.elements.Texture;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.rendering.shaders.LightingShader;
import org.polyscape.rendering.sprite.SpriteSheet;
import org.polyscape.ui.HomeScreen;
import org.polyscape.ui.ScreenManager;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class EngineTest extends Engine {

    public static void main(String[] args) throws IOException {

        Profile.Display.BACKGROUND_COLOR = new float[]{0f/255f, 0f/255f,0f/255f, 1.0f};
        eventBus = new EventBus();

        display = new Display("TEST");
        display.init(true);

        renderer = new Renderer(display);
        renderer.init();

        java.awt.Font jFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 20);

        fontRenderer = new FontRenderer(new Font(jFont, true));

        java.awt.Font jFont2 = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10);

        fontRenderer.addFont(new Font(jFont2, true));

        SpriteSheet spriteSheet = new SpriteSheet("003", 10, 10);


        AtomicInteger vectorX = new AtomicInteger(200);
        AtomicInteger vectorY = new AtomicInteger(200);
        float x = RenderEngine.normalize(vectorX.get(), Profile.Display.WIDTH, 0);
        float y = RenderEngine.normalize(vectorY.get(), Profile.Display.HEIGHT, 0);
        AtomicReference<FloatBuffer> position = new AtomicReference<>(BufferUtils.createFloatBuffer(4));
        position.get().put(new float[] { x, y, 1f, 0f, });
        position.get().flip();


        ScreenManager screenManager = new ScreenManager();

        screenManager.addScreen("Home", new HomeScreen());

        screenManager.setCurrentUi(1, "Home");

        LightingShader s = new LightingShader();
        s.create();

        LightingShader s2 = new LightingShader();
        s2.create();

        ObjectManager.clearObjects();

        BaseObject ob = new BaseObject();

        ob.setPosition(new Vector2(300, 300));
        ob.setWidth(100);
        ob.setHeight(100);
        ob.setSpriteSheet(spriteSheet);
        ob.setTexture(2);
        ob.addAction("Test", (object) -> {
            object.setTexture(0);
            sleep(1000);
            object.setTexture(1);
            sleep(1000);
            object.setTexture(2);
            sleep(1000);
            object.setTexture(3);
            sleep(1000);
        });

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


            ObjectManager.iterateObjects(obj1 -> {

                Vector2[] vertices = obj1.getVectorPoints();

                for (int i = 0; i < vertices.length; i++) {
                    Vector2 currentVertex = vertices[i];
                    Vector2 nextVertex = vertices[(i + 1) % vertices.length];
                    Vector2 edge = Vector2.sub(nextVertex, currentVertex);
                    Vector2 normal = new Vector2(edge.y, -edge.x);
                    Vector2 lightToCurrent = Vector2.sub(currentVertex, light);
                    if (Vector2.dot(normal, lightToCurrent) > 0) {
                        Vector2 point1 = Vector2.add(currentVertex, Vector2.sub(currentVertex, light).scale(800));
                        Vector2 point2 = Vector2.add(nextVertex, Vector2.sub(nextVertex, light).scale(800));
                        glColor3f(0, 0, 0);
                        glBegin(GL_QUADS); {
                            glVertex2f(currentVertex.x, currentVertex.y);
                            glVertex2f(point1.x, point1.y);
                            glVertex2f(point2.x, point2.y);
                            glVertex2f(nextVertex.x, nextVertex.y);
                        } glEnd();
                    }
                }

            });

            ObjectManager.collisionCheck();



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
            ObjectManager.renderObjects();
            ob.runAction("Test");

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

        RenderEvent.addEvent(renderEvent, RenderEvent.class);
        KeyEvent.addEvent(keyEvent, KeyEvent.class);
        MouseClickEvent.addEvent(mouseEvent, MouseClickEvent.class);



        renderEngine = new RenderEngine();
        renderEngine.render(renderer, display);

        s.remove();
    }
}
