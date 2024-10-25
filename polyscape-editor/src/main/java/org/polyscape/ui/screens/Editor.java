package org.polyscape.ui.screens;

import org.jbox2d.dynamics.BodyType;
import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.Loader;
import org.polyscape.Profile;
import org.polyscape.event.EventMetadata;
import org.polyscape.event.IEvent;
import org.polyscape.font.FontMac;
import org.polyscape.font.FontRenderer;
import org.polyscape.object.BaseObject;
import org.polyscape.object.CircleObject;
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
import org.polyscape.rendering.sprite.SpriteSheet;
import org.polyscape.rendering.sprite.SpriteSheetManager;
import org.polyscape.ui.MovementMode;
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

        ResizeWindowEvent.addEvent(resizeWindowEventIEvent, new EventMetadata(ResizeWindowEvent.class, 0));
    }

    public static ProjectInfo info;
    Texture t;

    public static int lowerY = 900;
    public static int lowerHeight = 140;

    private static boolean renderLevel = true;

    boolean draggingLower;

    public static int leftWidth = 300;

    public Vector2 startCameraDrag;
    public static Vector2 cameraVector;

    public static float cameraZoom;

    public boolean draggingCamera;

    boolean draggingLeft;

    boolean draggingObjectX;

    boolean draggingObjectY;

    boolean draggingObjectRot;

    boolean draggingCircle;

    CircleObject currentCircleObject;

    Vector2 draggingVectorObject;

    float previousAngle;

    int selectedId = -1;

    BaseObject selectedObject;

    int objectButtonY = 30;

    MovementMode movementMode;

    Button moveButton;

    Button rotateButton;

    Button circleButton;

    @Override
    public void model() {
        info = getModel();
        UiEngine.getDisplay().setTitle("Polyscape - Editing " + info.projectName);
        try {
            var spriteSheet = Loader.projectLoader.loadSpriteSheets(info.projectPath);
            if(!spriteSheet.isEmpty() && !SpriteSheetManager.getSpriteSheets().isEmpty()) {
                SpriteSheetManager.clearSpriteSheets();
            }

            for(var sprite : spriteSheet) {
                SpriteSheetManager.addSpriteSheet(sprite);
            }

            var levels = Loader.projectLoader.loadLevels(info.projectPath);
            if(!levels.isEmpty() && !ObjectManager.getLevels().isEmpty()){
                ObjectManager.clearLevels();
            }
            for (var lvl : levels.values()){
                ObjectManager.addLevel(lvl);
                ObjectManager.loadLevel(lvl.getLevelNumber());
            }

            var objs = Loader.projectLoader.loadObject(info.projectPath);
            ObjectManager.clearAllObjects();
            for (var obj : objs) {
                if(obj.getBody() != null && obj.getBody().isActive()) {
                    obj.getBody().setAwake(false);
                }
                addObject(obj , false);
            }


        } catch (IOException e) {
            System.err.println(e);
        }


    }


    @Override
    public void onLoad() {
        movementMode = MovementMode.MOVE;
        t = new Texture("transparent");
        draggingLower = false;
        FontMac font = new FontMac("Segoe UI", 25);
        setFont(font);

        Button lvlButton = new Button(5, (lowerY + lowerHeight) - 60, this, "Levels", "lvlButton");
        lvlButton.setClickAction(n -> {
            UiEngine.getScreenManager().setCurrentUi(2, "LevelList");
            UiEngine.getScreenManager().setScreenModel(2, info);
        });
        lvlButton.baseColor = Profile.UiThemes.Dark.accent2;


        Button objButton = new Button(font.getWidth("Levels") + 20, (lowerY + lowerHeight) - 60, this, "Objects", "objButton");
        objButton.setClickAction(n -> {
            UiEngine.getScreenManager().setCurrentUi(2, "ObjectList");
            UiEngine.getScreenManager().setScreenModel(2, info);
            if(selectedId != -1) {
                setSelectedId(selectedId);
            }
        });
        objButton.baseColor = Profile.UiThemes.Dark.accent2;

        Button spriteButton = new Button((int) objButton.getX() + objButton.getWidth() + 5, (lowerY + lowerHeight) - 60, this, "Sprite Sheets", "spriteButton");
        spriteButton.setClickAction(n -> {
            UiEngine.getScreenManager().setCurrentUi(2, "SpriteSheetList");
            UiEngine.getScreenManager().setScreenModel(2, info);
        });

        spriteButton.baseColor = Profile.UiThemes.Dark.accent2;

        Button logicButton = new Button(5, (lowerY + lowerHeight) - 20, this, "Logic", "logicButton");
        logicButton.setClickAction(n -> {
            UiEngine.getScreenManager().setCurrentUi(2, "LogicList");
            UiEngine.getScreenManager().setScreenModel(2, info);
        });

        logicButton.baseColor = Profile.UiThemes.Dark.accent2;



        addComponent(lvlButton);
        addComponent(objButton);
        addComponent(spriteButton);
        addComponent(logicButton);

        ObjectManager.clearObjects();
        if(ObjectManager.getLevels().isEmpty()) {
            Level level = new Level(1, "Untitled Level");
            level.levelHeight = 1000;
            level.levelWidth = 1000;

            ObjectManager.addLevel(level);
            ObjectManager.loadLevel(1);
        }

        cameraVector = new Vector2(0, 0);
        cameraZoom = 1f;
        startCameraDrag = new Vector2(0, 0);
        draggingVectorObject = new Vector2(0, 0);

        GLFW.glfwSetCursorPosCallback(Engine.getDisplay().getWindow(), (w, mx, my) -> {
            if(movementMode == MovementMode.MOVE) {
                if (draggingObjectX) {
                    Vector2 worldMx = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
                    float dx = (worldMx.x - draggingVectorObject.x);
                    selectedObject.addToPos(dx, 0);

                    draggingVectorObject.x = worldMx.x;
                }

                if (draggingObjectY) {
                    Vector2 worldMy = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
                    float dy = (worldMy.y - draggingVectorObject.y);
                    selectedObject.addToPos(0, dy);

                    draggingVectorObject.y = worldMy.y;
                }
            }
            else if(movementMode == MovementMode.ROTATE) {
                if(draggingObjectRot) {
                    Vector2 worldMy = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);

                    float endAngle = calculateAngle(worldMy, selectedObject.getCenter());
                    float diff = endAngle - previousAngle;

                    selectedObject.setAngle(selectedObject.getAngle() + -Math.toDegrees(diff));
                    previousAngle = endAngle;
                }
            }
            else if(movementMode == MovementMode.CIRCLE) {
                if(draggingCircle) {
                    Vector2 worldMy = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
                    int distance = (int) Math.abs(Vector2.distance(worldMy, currentCircleObject.getPosition()));
                    currentCircleObject.setRadius(distance);
                }

            }

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


        Button moveButton = new Button(leftWidth + 10, 50, this, "Move Mode", "moveButton");
        moveButton.setClickAction(n -> {
            this.movementMode = MovementMode.MOVE;
        });

        this.moveButton = moveButton;
        addComponent(moveButton);

        Button rotateButton = new Button(leftWidth + 140, 50, this, "Rotate Mode", "rotateButton");
        rotateButton.setClickAction(n -> {
            this.movementMode = MovementMode.ROTATE;
        });

        this.rotateButton = rotateButton;
        addComponent(rotateButton);

        Button circleTool = new Button(leftWidth + 140 + 140, 50, this,"Circle Tool", "circleTool");
        circleTool.setClickAction(n -> {
            this.movementMode = MovementMode.CIRCLE;
        });

        this.circleButton = circleTool;
        addComponent(circleButton);

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

        glLoadIdentity();
        glPushMatrix();
        glTranslatef(cameraVector.x, cameraVector.y, 1.0f);
        glScalef(cameraZoom, cameraZoom, 0f);
        if(renderLevel) {
            RenderEngine.drawQuadTextureNew(new Vector2(0, 0), ObjectManager.getCurrentLevel().levelWidth, ObjectManager.getCurrentLevel().levelHeight, 0, 0, ObjectManager.getCurrentLevel().levelWidth / 20f, ObjectManager.getCurrentLevel().levelHeight / 20f, t);

            ObjectManager.renderObjects(event.alpha);

            if (selectedObject != null && movementMode == MovementMode.MOVE) {
                RenderEngine.drawLineNew(selectedObject.getCenter(), new Vector2(selectedObject.getCenter().x + selectedObject.getWidth() + 30, selectedObject.getCenter().y), 2f, Color.RED);
                RenderEngine.drawLineNew(selectedObject.getCenter(), new Vector2(selectedObject.getCenter().x, selectedObject.getCenter().y - selectedObject.getHeight() - 30), 2f, Color.GREEN);
            }
            if (selectedObject != null && movementMode == MovementMode.ROTATE) {
                RenderEngine.drawHollowCircle(selectedObject.getCenter(), selectedObject.getWidth(), 100, 2f, Color.BLUE);
            }
            if (currentCircleObject != null && movementMode == MovementMode.CIRCLE) {
                Vector2 worldMy = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
                RenderEngine.drawLineNew(currentCircleObject.getCenter(), worldMy, 2f, Color.GREEN);
                font.renderText("R " + currentCircleObject.getRadius(), new Vector2(currentCircleObject.getPosition().x, currentCircleObject.getPosition().y), Color.BLUE);
            }
        }
        else if(renderSpriteSheet()){
            var currentSheet = getCurrentSpriteSheet();
            if (currentSheet != null) {
                RenderEngine.drawQuadTextureNew(new Vector2(0, 0), currentSheet.width, currentSheet.height, currentSheet.getMasterTexture());
                for (int y = 0; y < currentSheet.getRows(); y++) {
                    RenderEngine.drawLineNew(new Vector2(0, y*currentSheet.getChunkHeight()), new Vector2(currentSheet.width, y*currentSheet.getChunkHeight()), 2f, Color.BLUE);
                }
                for (int x = 0; x < currentSheet.getCols(); x++) {
                    RenderEngine.drawLineNew(new Vector2(x*currentSheet.getChunkWidth(), 0), new Vector2(x*currentSheet.getChunkWidth(), currentSheet.height), 2f, Color.BLUE);
                }
            }
        }
        glPopMatrix();

        RenderEngine.drawQuadNew(new Vector2(leftWidth, lowerY), Profile.Display.WIDTH, Profile.Display.HEIGHT, Profile.UiThemes.Dark.background);
        RenderEngine.drawLineNew(new Vector2(leftWidth, lowerY), new Vector2(Profile.Display.WIDTH, lowerY), 8f, Profile.UiThemes.Dark.foregroundDark);

        RenderEngine.drawQuadNew(new Vector2(0, 0), leftWidth, Profile.Display.HEIGHT, Profile.UiThemes.Dark.background);
        RenderEngine.drawLineNew(new Vector2(leftWidth, 0), new Vector2(leftWidth, Profile.Display.HEIGHT), 8f, Profile.UiThemes.Dark.foregroundDark);

        if (selectedId != -1) {
            font.renderText(selectedObject.getPosition().toString(), new Vector2(leftWidth + 10, 30), Profile.UiThemes.Dark.foregroundDark);
        }

        moveButton.setPosition(leftWidth + 10, 50);
        rotateButton.setPosition(leftWidth + 140, 50);


    }

    public void addObject(BaseObject object, boolean b) {

        ObjectList objList = (ObjectList) UiEngine.getScreenManager().getUi("ObjectList");
        ObjectManager.addObject(object);
        if(b) {
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
    }

    public void newObject(BaseObject object) {
        addObject(object, true);
        saveObjects();
    }

    @Override
    public void click(MouseClickEvent event) {

        if(renderLevel) {
            if (movementMode == MovementMode.MOVE) {
                if (event.action == 1 && isWithinBoundsOfDragY()) {
                    draggingVectorObject = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
                    draggingObjectY = true;
                    return;
                }

                if (event.action == 0 && draggingObjectY) {
                    saveObjects();
                    draggingObjectY = false;
                }

                if (event.action == 1 && isWithinBoundsOfDragX()) {
                    draggingVectorObject = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
                    draggingObjectX = true;
                    return;
                }

                if (event.action == 0 && draggingObjectX) {
                    saveObjects();
                    draggingObjectX = false;
                }
            } else if (movementMode == MovementMode.ROTATE) {
                if (event.action == 1 && isWithingBoundsOfRot()) {
                    draggingVectorObject = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
                    previousAngle = 0f;
                    draggingObjectRot = true;
                    return;
                }
                if (event.action == 0 && draggingObjectRot) {
                    saveObjects();
                    draggingObjectRot = false;
                }
            }
            else if(movementMode == MovementMode.CIRCLE) {
                if(event.action == 1 && isInStageBounds((float) event.mX, (float) event.mY)) {
                    var pos = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
                    CircleObject circleObject = new CircleObject();
                    circleObject.setPosition(pos);
                    circleObject.setRadius(0);
                    circleObject.setBaseColor(Color.BLACK);
                    circleObject.setBodyType(BodyType.STATIC, true);
                    circleObject.getBody().setAwake(false);
                    circleObject.setLevel(ObjectManager.getCurrentLevel().getLevelNumber());
                    this.draggingCircle = true;
                    this.currentCircleObject = circleObject;
                    addObject(circleObject, false);
                    return;
                }
                if(event.action == 0 && draggingCircle) {
                    saveObjects();
                    draggingCircle = false;
                    setSelectedId(currentCircleObject.getObjectId());
                    currentCircleObject = null;
                    movementMode = MovementMode.MOVE;
                }
            }
        }


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

        if(renderLevel) {
            if (event.action == 0) {
                Vector2 v2 = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
                ObjectManager.iterateObjects(n -> {

                    if (n.isPointInObject(v2)) {
                        setSelectedId(n.getObjectId());
                    }

                });
            }
        }
    }

    public boolean isInStageBounds(float mx, float my) {
        return mx >= leftWidth && mx <= Profile.Display.WIDTH && my >= 0 && my <= lowerY;
    }

    public static void saveObjects() {
        try {
            Loader.projectLoader.saveObjects(info.projectPath);
            Loader.projectLoader.saveLevels(info.projectPath);
            Loader.projectLoader.saveSpriteSheets(info.projectPath);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    public void key(KeyEvent event) {

        if(!renderLevel) return;
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
        else if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)){
            if(selectedObject != null) {
                if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_UP)) {
                    var oldVector = selectedObject.getPosition();
                    var newVector = new Vector2(oldVector.x, oldVector.y - selectedObject.getHeight());
                    duplicateObject(newVector);
                }
                if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_DOWN)) {
                    var oldVector = selectedObject.getPosition();
                    var newVector = new Vector2(oldVector.x, oldVector.y + selectedObject.getHeight());
                    duplicateObject(newVector);
                }
                if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
                    var oldVector = selectedObject.getPosition();
                    var newVector = new Vector2(oldVector.x - selectedObject.getWidth(), oldVector.y);
                    duplicateObject(newVector);
                }
                if (KeyEvent.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
                    var oldVector = selectedObject.getPosition();
                    var newVector = new Vector2(oldVector.x + selectedObject.getHeight(), oldVector.y);
                    duplicateObject(newVector);
                }
            }
        }
        else {

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
    }

    private boolean isWithinBoundsOfDragY() {
        Vector2 v2 = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
        if(selectedObject == null) return false;

        if(v2.y <= selectedObject.getCenter().y && v2.y >= selectedObject.getCenter().y - selectedObject.getHeight() - 30) {
            return v2.x >= selectedObject.getCenter().x - 2 && v2.x <= selectedObject.getCenter().x + 2;
        }
        return false;
    }

    private boolean isWithinBoundsOfDragX() {
        Vector2 v2 = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
        if(selectedObject == null) return false;
        if(v2.x >= selectedObject.getCenter().x && v2.x <= selectedObject.getCenter().x + selectedObject.getWidth() + 30) {
            return v2.y >= selectedObject.getCenter().y - 2 && v2.y <= selectedObject.getCenter().y + 2;
        }
        return false;
    }

    private boolean isWithingBoundsOfRot(){
        Vector2 point = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
        if(selectedObject == null) return false;
        Vector2 center = selectedObject.getCenter();
        float radius = selectedObject.getWidth();
        float dx = point.x - center.x;
        float dy = point.y - center.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Calculate the acceptable range considering the line width
        float halfLineWidth = 1.0f;
        float minRadius = radius - halfLineWidth;
        float maxRadius = radius + halfLineWidth;

        // Check if the distance is within the range
        return distance >= minRadius && distance <= maxRadius;

    }

    private float calculateAngle(Vector2 point, Vector2 center) {
        float dx = point.x - center.x;
        float dy = point.y - center.y;
        return (float) Math.atan2(dy, dx);
    }

    private void duplicateObject(Vector2 pos) {
        var base = new BaseObject();
        base.setPosition(pos);
        base.setWidth(selectedObject.getWidth());
        base.setHeight(selectedObject.getHeight());
        base.setBaseColor(selectedObject.getBaseColor());
        base.setUpPhysicsBody(selectedObject.getBodyType(), selectedObject.getFriction(), selectedObject.getDensity(), selectedObject.getLinearDamping(), selectedObject.isAngleCals());
        if (selectedObject.getBody().isActive()) {
            base.setBodyActive();
        } else {
            base.destroyPhysicsBody();
        }
        base.setLevel(selectedObject.getLevel());
        //base.getBody().setActive(false);
        newObject(base);
        setSelectedId(base.getObjectId());
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

    public static boolean shouldRenderLevel() {
        return renderLevel;
    }

    public static void setRenderLevel(boolean render) {
        renderLevel = render;
    }

    private boolean renderSpriteSheet(){
        return UiEngine.getScreenManager().getCurrentScreenAtIndex(1).getName().equals("SpriteSheets");
    }

    private SpriteSheet getCurrentSpriteSheet() {
        if(renderSpriteSheet()) {
            return (SpriteSheet) UiEngine.getScreenManager().getScreenModel(1);
        }
        return null;
    }
}
