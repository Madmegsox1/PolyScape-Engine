package org.polyscape.test;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.event.EventBus;
import org.polyscape.event.EventMetadata;
import org.polyscape.event.IEvent;
import org.polyscape.logic.LogicManager;
import org.polyscape.logic.script.LogicScriptLoader;
import org.polyscape.object.*;
import org.polyscape.rendering.Display;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.rendering.sprite.SpriteSheet;
import org.polyscape.test.ui.HomeScreen;
import org.polyscape.ui.ScreenManager;

import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.opengl.GL11.*;

public class MacTest extends Engine {
    @Test
    public void test() {
        Profile.Display.BACKGROUND_COLOR = new float[]{255f / 255f, 255f / 255f, 255f / 255f, 1.0f};
        display = new Display("Mac Test");
        display.init(false);

        renderer = new Renderer(display);
        renderer.init();

        renderEngine = new RenderEngine();
        eventBus = new EventBus();

        try {
            LogicScriptLoader scriptLoader = new LogicScriptLoader();
        } catch (FileNotFoundException | ScriptException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        LogicManager.loadAllLogic("./res/jars/TestLogic-1.0.jar");
        LogicManager.initLogic();

        SpriteSheet forestTiles = new SpriteSheet("ForestTiles", 16,16);

        SpriteSheet spriteSheet = new SpriteSheet("002", 25, 25);
        ObjectManager.clearObjects();

        Level l = new Level(1, "Level 1");
        ObjectManager.addLevel(l);
        ObjectManager.loadLevel(1);

        BaseObject object = new BaseObject();
        object.setPosition(new Vector2(100, 200));
        object.setWidth(50);
        object.setHeight(50);
        object.setBodyType(BodyType.DYNAMIC, true);
        object.setSpriteSheet(spriteSheet);
        object.setWireframe(true);
        object.setTexture(0);
        object.setObjectId(999);
        object.setLevel(1);


        StaticObject object1 = new StaticObject();
        object1.setPosition(new Vector2(25, 500));
        object1.setWidth(400);
        object1.setHeight(50);
        object1.setBodyType(BodyType.STATIC, true);
        object1.setWireframe(true);
        object1.setLevel(1);

        ObjectManager.addObject(object1);


        BaseObject object2 = new BaseObject();
        object2.setPosition(new Vector2(0,0));
        object2.setWidth(100);
        object2.setHeight(100);
        object2.setSpriteSheet(forestTiles);
        object2.setTexture(0);
        object2.setLevel(1);
        BaseObject object3 = new BaseObject();
        object3.setPosition(new Vector2(100,0));
        object3.setWidth(100);
        object3.setHeight(100);
        object3.setSpriteSheet(forestTiles);
        object3.setTexture(1);
        object3.setLevel(1);
        BaseObject object5 = new BaseObject();
        object5.setPosition(new Vector2(200,0));
        object5.setWidth(100);
        object5.setHeight(100);
        object5.setSpriteSheet(forestTiles);
        object5.setTexture(2);
        object5.setLevel(1);

        BaseObject object6 = new BaseObject();
        object6.setPosition(new Vector2(300,0));
        object6.setWidth(100);
        object6.setHeight(100);
        object6.setSpriteSheet(forestTiles);
        object6.setTexture(3);
        object6.setLevel(1);

        ObjectManager.addObject(object2);
        ObjectManager.addObject(object3);
        ObjectManager.addObject(object5);
        ObjectManager.addObject(object6);

        ObjectManager.addObject(object);

        FluidObject fluidObject = new FluidObject(10f);
        fluidObject.setLevel(1);
        fluidObject.setObjectId(991);

        //fluidObject.createFluid(200, 100, 200);

        ObjectManager.addObject(fluidObject);

        LogicManager.initLogicObject(object);
        LogicManager.initLogicObject(fluidObject);
        LogicManager.loadLogic();

        ScreenManager screenManager = new ScreenManager();

        screenManager.addScreen("Home", new HomeScreen());

        screenManager.setCurrentUi(1, "Home");

        renderEngine.render(renderer, display);
    }

}
