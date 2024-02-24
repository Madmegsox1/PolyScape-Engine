package org.polyscape.test;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.event.EventBus;
import org.polyscape.event.IEvent;
import org.polyscape.object.BaseObject;
import org.polyscape.object.FluidObject;
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
import org.polyscape.rendering.sprite.SpriteSheet;
import org.polyscape.test.ui.HomeScreen;
import org.polyscape.ui.ScreenManager;

import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.opengl.GL11.*;

public class MacTest extends Engine {
    public static void main(String[] args) {
        Profile.Display.BACKGROUND_COLOR = new float[]{255f / 255f, 255f / 255f, 255f / 255f, 1.0f};
        display = new Display("Mac Test");
        display.init(false);

        renderer = new Renderer(display);
        renderer.init();

        renderEngine = new RenderEngine();
        eventBus = new EventBus();


        SpriteSheet spriteSheet = new SpriteSheet("002", 25, 25);
        ObjectManager.clearObjects();

        BaseObject object = new BaseObject();
        object.setPosition(new Vector2(100, 200));
        object.setWidth(50);
        object.setHeight(50);
        object.setUpPhysicsBody(BodyType.DYNAMIC);
        object.setSpriteSheet(spriteSheet);
        object.setWireframe(true);
        object.setTexture(0);


        StaticObject object1 = new StaticObject();
        object1.setPosition(new Vector2(25, 500));
        object1.setWidth(400);
        object1.setHeight(50);
        object1.setUpPhysicsBody(BodyType.STATIC);
        object1.setWireframe(true);
        ObjectManager.addObject(object);
        ObjectManager.addObject(object1);


        FluidObject fluidObject = new FluidObject(10f);

        //fluidObject.createFluid(200, 100, 200);

        ObjectManager.addObject(fluidObject);

        AtomicReference<Vector2> startDrag = new AtomicReference<>();

        AtomicReference<Float> translateX = new AtomicReference<>();

        AtomicReference<Float> translateY= new AtomicReference<>();


        IEvent<RenderEvent> renderEvent = e -> {

            if (startDrag.get() != null) {
                Vector2 v = Display.getMousePosition(display.getWindow());
                RenderEngine.drawQuadA(startDrag.get(), v.x - startDrag.get().x, v.y - startDrag.get().y, new Color(0, 0, 0, 100));
            }

            Vector2 vector = object.getInterpolatedPosition(e.alpha);
            float playerX = vector.x;
            float playerY = vector.y;

            // Assuming screenWidth and screenHeight are the dimensions of your window
            float halfWidth = Profile.Display.WIDTH / 2.0f;
            float halfHeight = Profile.Display.HEIGHT / 2.0f;

            // Calculate the translation needed to keep the player at the center
            translateX.set(-playerX + halfWidth);
            translateY.set(-playerY + halfHeight);

            // Apply this translation to the view matrix or directly using OpenGL's legacy functions
            glLoadIdentity(); // Load the identity matrix to reset transformations
            glPushMatrix();
            glTranslatef(translateX.get(), translateY.get(), 0.0f);
            //glEnable(GL_CULL_FACE);
            ObjectManager.renderObjects(e.alpha);
            //glDisable(GL_CULL_FACE);
            glPopMatrix();
        };

        IEvent<KeyEvent> keyEvent = e -> {
            float yv = 0;
            float xv = 0;

            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_W)) {
                yv += 20f;
            }
            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_S)) {
                yv -= 20f;
            }
            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_A)) {
                xv -= 20f;
            }
            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_D)) {
                xv += 20f;
            }


            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_R)) {
                Vector2 pos = Display.getMousePosition(display.getWindow());

                ObjectManager.iterateObjects(n -> {
                    if (n.getBody() != null) {
                        if (n.isPointInObject(pos)) {
                            n.getBody().setTransform(new Vec2(0, 0), 10);
                        }
                    }
                });
            }

            float finalXv = xv;
            float finalYv = yv;
            ObjectManager.iterateObjects(n -> {
                if (n.getBody() != null) {
                    n.addForce(finalXv, finalYv);
                }
            });

        };


        IEvent<MouseClickEvent> clickEvent = e -> {

            // Assume a scale factor where >1.0 is zoomed in, <1.0 is zoomed out
            float scaleFactor = 1.0f; // Example scale factor

// Adjust calculations for scale
            float adjustedMouseX = (float) e.mX / scaleFactor;
            float adjustedMouseY = (float) e.mY / scaleFactor;

// Then apply translation as before
            e.mX = adjustedMouseX - translateX.get();
            e.mY = adjustedMouseY - translateY.get();

            if (e.action == 0 && e.key == 0) {
                fluidObject.createFluid((float) e.mX, (float) e.mY, 20);
            }
            if (e.action == 0 && e.key == 2) {
                ObjectManager.clearObjects();
            }

            if (e.action == 1 && e.key == 1) {
                startDrag.set(new Vector2(e.mX, e.mY));
            }

            if (e.action == 0 && e.key == 1) {
                StaticObject object4 = new StaticObject();
                object4.setPosition(startDrag.get());
                object4.setWidth((int) (e.mX - startDrag.get().x));
                object4.setHeight((int) (e.mY - startDrag.get().y));
                object4.setUpPhysicsBody(BodyType.DYNAMIC);
                object4.setWireframe(false);
                startDrag.set(null);
                ObjectManager.addObject(object4);
            }
        };

        RenderEvent.addEvent(renderEvent, RenderEvent.class);
        KeyEvent.addEvent(keyEvent, KeyEvent.class);
        MouseClickEvent.addEvent(clickEvent, MouseClickEvent.class);

        ScreenManager screenManager = new ScreenManager();

        screenManager.addScreen("Home", new HomeScreen());

        screenManager.setCurrentUi(1, "Home");

        renderEngine.render(renderer, display);
    }

}
