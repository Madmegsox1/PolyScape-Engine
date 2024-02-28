package org.polyscape.ui.screens;

import org.jbox2d.dynamics.BodyType;
import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.Loader;
import org.polyscape.Profile;
import org.polyscape.event.IEvent;
import org.polyscape.font.FontMac;
import org.polyscape.object.BaseObject;
import org.polyscape.object.Level;
import org.polyscape.object.ObjectManager;
import org.polyscape.project.model.ProjectInfo;
import org.polyscape.rendering.Display;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Texture;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.rendering.events.ResizeWindowEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.UiEngine;
import org.polyscape.ui.component.button.Button;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;


public final class Editor extends Screen {


    public Editor(){
        IEvent<ResizeWindowEvent> resizeWindowEventIEvent = e -> {
            if(UiEngine.getScreenManager().getCurrentUiMap().get(0) == this) {
                UiEngine.getScreenManager().setCurrentUi(0, "Editor");
                UiEngine.getScreenManager().setScreenModel(0, model);
            }
        };

        ResizeWindowEvent.addEvent(resizeWindowEventIEvent, ResizeWindowEvent.class);
    }

    public static ProjectInfo info;
    Texture t;

    public static int lowerY = 940;
    public static int lowerHeight = 140;
    boolean draggingLower;

    public static int leftWidth = 250;

    public Vector2 startCameraDrag;
    public static Vector2 cameraVector;

    public static float cameraZoom;

    public boolean draggingCamera;

    boolean draggingLeft;

    int selectedId = -1;

    BaseObject selectedObject;

    int objectButtonY = 30;

    @Override
    public void model() {
        info = getModel();
        UiEngine.getDisplay().setTitle("Polyscape - Editing " + info.projectName);
        try {

            var levels = Loader.projectLoader.loadLevels(info.projectPath);
            if(!levels.isEmpty() && !ObjectManager.getLevels().isEmpty()){
                ObjectManager.clearLevels();
            }
            for (var lvl : levels.values()){
                ObjectManager.addLevel(lvl);
                ObjectManager.loadLevel(lvl.getLevelNumber());
            }

            var objs = Loader.projectLoader.loadObject(info.projectPath);
            for (var obj : objs) {
                obj.getBody().setAwake(false);
                addObject(obj);
            }


        } catch (IOException e) {
            System.err.println(e);
        }


    }


    public void loadObject() {
        try {
            var objs = Loader.projectLoader.loadObject(info.projectPath);
            for (var obj : objs) {
                addObject(obj);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void loadObjectPositions() {
        try {
            var objs = Loader.projectLoader.loadObject(info.projectPath);
            for (var obj : objs) {
                ObjectManager.getObject(obj.getObjectId()).setPosition(obj.getPosition());
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }


    @Override
    public void onLoad() {
        t = new Texture("transparent");
        draggingLower = false;
        FontMac font = new FontMac("Segoe UI", 25);
        setFont(font);

        Button lvlButton = new Button(5, (lowerY + lowerHeight) - 40, this, "Levels", "lvlButton");
        lvlButton.setClickAction(n -> {
            UiEngine.getScreenManager().setCurrentUi(2, "LevelList");
            UiEngine.getScreenManager().setScreenModel(2, info);
        });
        lvlButton.baseColor = Profile.UiThemes.Dark.accent2;


        Button objButton = new Button(font.getWidth("Levels") + 20, (lowerY + lowerHeight) - 40, this, "Objects", "objButton");
        objButton.setClickAction(n -> {
            UiEngine.getScreenManager().setCurrentUi(2, "ObjectList");
            UiEngine.getScreenManager().setScreenModel(2, info);
            if(selectedId != -1) {
                setSelectedId(selectedId);
            }
        });
        objButton.baseColor = Profile.UiThemes.Dark.accent2;



        addComponent(lvlButton);
        addComponent(objButton);



        ObjectManager.clearObjects();
        Level level = new Level(1, "Untitled Level");
        level.levelHeight = 1000;
        level.levelWidth = 1000;

        ObjectManager.addLevel(level);
        ObjectManager.loadLevel(1);

        cameraVector = new Vector2(0, 0);
        cameraZoom = 1f;
        startCameraDrag = new Vector2(0, 0);
        GLFW.glfwSetCursorPosCallback(Engine.getDisplay().getWindow(), (w, mx, my) -> {
            if (draggingCamera) {
                float dx = (float) (mx - startCameraDrag.x);
                float dy = (float) (my - startCameraDrag.y);

                cameraVector.addToVect(dx, dy);

                startCameraDrag = new Vector2(mx, my);
            }
        });

        GLFW.glfwSetScrollCallback(Engine.getDisplay().getWindow(), (w, sx, sy) -> {
            Vector2 v2M = Display.getMousePosition(Engine.getDisplay().getWindow());
            if (isInStageBounds(v2M.x, v2M.y)) {
                float scaleFactor = 0.01f;
                updateScale((float) (sy * scaleFactor));
            }
        });


    }

    @Override
    public void render(RenderEvent event) {
        if (draggingLower) {
            lowerY = (int) Display.getMousePosition(Engine.getDisplay().getWindow()).y;
            lowerHeight = Profile.Display.HEIGHT - lowerY;
        }


        if (draggingLeft) {
            leftWidth = (int) Display.getMousePosition(Engine.getDisplay().getWindow()).x;
        }
        glLoadIdentity(); // Load the identity matrix to reset transformations
        glPushMatrix();
        glTranslatef(cameraVector.x, cameraVector.y, 1.0f);
        glScalef(cameraZoom, cameraZoom, 0f);
        RenderEngine.drawQuadTexture(new Vector2(0, 0), ObjectManager.getCurrentLevel().levelWidth,  ObjectManager.getCurrentLevel().levelHeight, 0, 0, ObjectManager.getCurrentLevel().levelWidth/20f, ObjectManager.getCurrentLevel().levelHeight/20f, t);

        ObjectManager.renderObjects(event.alpha);
        glPopMatrix();

        RenderEngine.drawQuadA(new Vector2(leftWidth, lowerY), Profile.Display.WIDTH, lowerHeight, Profile.UiThemes.Dark.background);
        RenderEngine.drawLine(new Vector2(leftWidth, lowerY), new Vector2(Profile.Display.WIDTH, lowerY), 8f, Profile.UiThemes.Dark.foregroundDark);

        RenderEngine.drawQuadA(new Vector2(0, 0), leftWidth, Profile.Display.HEIGHT, Profile.UiThemes.Dark.background);
        RenderEngine.drawLine(new Vector2(leftWidth, 0), new Vector2(leftWidth, Profile.Display.HEIGHT), 8f, Profile.UiThemes.Dark.foregroundDark);

        if (selectedId != -1) {
            font.renderText(selectedObject.getPosition().toString(), new Vector2(leftWidth + 10, 30), Profile.UiThemes.Dark.foregroundDark);
        }

    }

    public void addObject(BaseObject object) {

        ObjectList objList = (ObjectList) UiEngine.getScreenManager().getUi("ObjectList");

        ObjectManager.addObject(object);
        Button button = new Button(5, objList.buttonY, this, object.getClass().getSimpleName() + object.getObjectId(), "ObjectButton:" + object.getObjectId());
        button.baseColor = Profile.UiThemes.Dark.foregroundDark;
        button.setClickAction(n -> {
            Editor editor = (Editor) UiEngine.getScreenManager().getUi("Editor");
            editor.setSelectedId(object.getObjectId());
        });
        objList.addComponent(button);


        objList.buttonY += 10;
        objList.buttonY += font.getHeight(button.getText());
    }

    public void newObject(BaseObject object) {
        addObject(object);
        saveObjects();
    }

    @Override
    public void click(MouseClickEvent event) {
        if (event.action == 1 && inBoundsOfBLine(event)) {
            draggingLower = true;
        }

        if (event.action == 0 && draggingLower) {
            draggingLower = false;
        }

        if (event.action == 1 && inBoundsOfLLine(event)) {
            draggingLeft = true;
        }

        if (event.action == 0 && draggingLeft) {
            draggingLeft = false;
        }

        if (event.action == 1 && isInStageBounds((float) event.mX, (float) event.mY)) {
            startCameraDrag = Display.getMousePosition(Engine.getDisplay().getWindow());
            draggingCamera = true;
        }

        if (draggingCamera && event.action == 0) {
            draggingCamera = false;
        }

        if (event.action == 0) {
            Vector2 v2 = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
            ObjectManager.iterateObjects(n -> {

                if (n.isPointInObject(v2)) {
                    setSelectedId(n.getObjectId());
                }

            });
        }
    }

    public boolean isInStageBounds(float mx, float my) {
        return mx >= leftWidth && mx <= Profile.Display.WIDTH && my >= 0 && my <= lowerY;
    }

    public static void saveObjects() {
        try {
            Loader.projectLoader.saveObjects(info.projectPath);
            Loader.projectLoader.saveLevels(info.projectPath);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    public void key(KeyEvent event) {
        if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
            if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_N)) {
                Vector2 v2M = Display.getMousePosition(Engine.getDisplay().getWindow());
                Vector2 v2 = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
                if (isInStageBounds(v2M.x, v2M.y)) {
                    var base = new BaseObject();
                    base.setPosition(v2);
                    base.setWidth(100);
                    base.setHeight(100);
                    base.setBaseColor(Color.BLACK);
                    base.setBodyType(BodyType.STATIC, true);
                    base.getBody().setAwake(false);
                    base.setLevel(ObjectManager.getCurrentLevel().getLevelNumber());
                    newObject(base);
                    setSelectedId(base.getObjectId());
                }
            }
        }

        if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_UP)) {
            if (selectedId != -1) {
                selectedObject.addToPos(0, -1);
            }
        }
        if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_DOWN)) {
            if (selectedId != -1) {
                selectedObject.addToPos(0, 1);
            }
        }
        if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
            if (selectedId != -1) {
                selectedObject.addToPos(-1, 0);
            }
        }
        if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
            if (selectedId != -1) {
                selectedObject.addToPos(1, 0);
            }
        }
    }

    private boolean inBoundsOfLLine(MouseClickEvent event) {
        if (event.mX >= leftWidth - 4 && event.mX <= leftWidth + 4) {
            return event.mY >= 0 && event.mY <= Profile.Display.HEIGHT;
        }
        return false;
    }

    private boolean inBoundsOfBLine(MouseClickEvent event) {
        if (event.mX >= leftWidth && event.mX <= Profile.Display.WIDTH) {
            return event.mY >= lowerY - 4 && event.mY <= lowerY + 4;
        }
        return false;
    }

    public void setSelectedId(int id) {
        ObjectList objectList = (ObjectList) UiEngine.getScreenManager().getUi("ObjectList");

        if (selectedId != -1) {
            var comp = objectList.getComponentById("ObjectButton:" + selectedObject.getObjectId());
            if (comp != null) {
                comp.foregroundColor = Profile.UiThemes.Dark.foreground;
            }
            selectedObject.setWireframe(false);
        }
        this.selectedId = id;
        var obj = ObjectManager.getObject(id);

        selectedObject = obj;
        obj.setWireframe(true);
        var comp = objectList.getComponentById("ObjectButton:" + id);
        if (comp != null) {
            comp.foregroundColor = Color.BLUE;
            UiEngine.getScreenManager().setCurrentUi(1, "ObjectEditor");
            UiEngine.getScreenManager().setScreenModel(1, obj);
        }
    }

    public static void updateScale(float scaleFactor) {
        cameraZoom += scaleFactor;
        // Clamp the scale to prevent it from becoming too small or too large
        cameraZoom = Math.max(0.1f, Math.min(cameraZoom, 10.0f));
    }
}
