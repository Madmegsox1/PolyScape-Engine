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

import static org.lwjgl.opengl.GL11.glGetError;

public class MacTest extends Engine {
    public static void main(String[] args) {
        Profile.Display.BACKGROUND_COLOR = new float[]{255f/255f, 255f/255f,255f/255f, 1.0f};
        display = new Display("Mac Test");
        display.init(false);

        renderer = new Renderer(display);
        renderer.init();

        renderEngine = new RenderEngine();
        eventBus = new EventBus();




        SpriteSheet spriteSheet = new SpriteSheet("003", 10, 10);
        ObjectManager.clearObjects();

        BaseObject object = new BaseObject();
        object.setPosition(new Vector2(100, 200));
        object.setWidth(50);
        object.setHeight(50);
        object.setUpPhysicsBody(BodyType.DYNAMIC);
        object.setTextured(true);
        object.setTexture(new Texture("003"));

        StaticObject object1 = new StaticObject();
        object1.setPosition(new Vector2(25, 500));
        object1.setWidth(400);
        object1.setHeight(50);
        object1.setUpPhysicsBody(BodyType.STATIC);
        object1.setWireframe(false);
        ObjectManager.addObject(object);
        ObjectManager.addObject(object1);


        FluidObject fluidObject = new FluidObject(10f);

        //fluidObject.createFluid(200, 100, 200);

        ObjectManager.addObject(fluidObject);

        AtomicReference<Vector2> startDrag = new AtomicReference<>();

        IEvent<RenderEvent> renderEvent = e -> {
            if(startDrag.get() != null){
                Vector2 v = Display.getMousePosition(display.getWindow());
                RenderEngine.drawQuadA(startDrag.get(), v.x - startDrag.get().x , v.y - startDrag.get().y , new Color(0,0,0, 100));
            }
            ObjectManager.renderObjects();
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

            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_E)) {

            }

            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_R)){
                Vector2 pos = Display.getMousePosition(display.getWindow());

                ObjectManager.iterateObjects(n -> {
                    if(n.getBody() != null){
                        if(n.isPointInObject(pos)){
                            n.getBody().setTransform(new Vec2(0,0), 10);
                        }
                    }
                });
            }

            float finalXv = xv;
            float finalYv = yv;
            ObjectManager.iterateObjects(n -> {
                if(n.getBody() != null){
                    n.addForce(finalXv, finalYv);
                }
            });

        };



        IEvent<MouseClickEvent> clickEvent = e -> {
          if(e.action == 0 && e.key == 0){
              fluidObject.createFluid((float) e.mX, (float) e.mY, 20);
          }
          if(e.action ==0 && e.key == 2){
            ObjectManager.clearObjects();
          }

            if(e.action == 1 && e.key == 1){
                startDrag.set(new Vector2(e.mX, e.mY));
            }

          if(e.action == 0 && e.key == 1){
              StaticObject object4 = new StaticObject();
              object4.setPosition(startDrag.get());
              object4.setWidth((int)(e.mX - startDrag.get().x));
              object4.setHeight((int)( e.mY - startDrag.get().y));
              object4.setUpPhysicsBody(BodyType.STATIC);
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
