package org.polyscape.ui.screens;

import org.jbox2d.dynamics.BodyType;
import org.lwjgl.glfw.GLFW;
import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.object.BaseObject;
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
import org.polyscape.ui.Screen;
import org.polyscape.ui.UiEngine;
import org.polyscape.ui.component.button.Button;

import static org.lwjgl.opengl.GL11.*;


public final class Editor extends Screen {
    ProjectInfo info;
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
    }



    @Override
    public void onLoad() {
        t = new Texture("transparent");
        draggingLower = false;
        FontMac font = new FontMac("Segoe UI", 25);
        setFont(font);
        ObjectManager.clearObjects();
        cameraVector = new Vector2(0,0);
        cameraZoom = 1f;
        startCameraDrag = new Vector2(0,0);

        GLFW.glfwSetCursorPosCallback(Engine.getDisplay().getWindow(), (w, mx, my) ->{
           if(draggingCamera){
               float dx = (float) (mx - startCameraDrag.x);
               float dy = (float) (my - startCameraDrag.y);

               cameraVector.addToVect(dx, dy);

               startCameraDrag = new Vector2(mx, my);
           }
        });

        GLFW.glfwSetScrollCallback(Engine.getDisplay().getWindow(), (w, sx, sy) -> {
            Vector2 v2M = Display.getMousePosition(Engine.getDisplay().getWindow());
            if(isInStageBounds(v2M.x, v2M.y)){
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
        RenderEngine.drawQuadTexture(new Vector2(0,0), Profile.Display.WIDTH, Profile.Display.HEIGHT,0,0,35,20, t);

        ObjectManager.renderObjects(event.alpha);
        glPopMatrix();

        RenderEngine.drawQuadA(new Vector2(leftWidth, lowerY), Profile.Display.WIDTH, lowerHeight, Profile.UiThemes.Dark.background);
        RenderEngine.drawLine(new Vector2(leftWidth, lowerY), new Vector2(Profile.Display.WIDTH, lowerY), 8f, Profile.UiThemes.Dark.foregroundDark);

        RenderEngine.drawQuadA(new Vector2(0, 0), leftWidth, Profile.Display.HEIGHT, Profile.UiThemes.Dark.background);
        RenderEngine.drawLine(new Vector2(leftWidth, 0), new Vector2(leftWidth, Profile.Display.HEIGHT), 8f, Profile.UiThemes.Dark.foregroundDark);

        if(selectedId != -1){
            font.renderText(selectedObject.getPosition().toString(), new Vector2(leftWidth + 10, 30), Profile.UiThemes.Dark.foregroundDark);
        }

    }

    public void newObject(BaseObject object){
        ObjectManager.addObject(object);
        Button button = new Button(5, objectButtonY, this, object.getClass().getSimpleName() + object.getObjectId(), "ObjectButton:" + object.getObjectId());
        button.baseColor = Profile.UiThemes.Dark.foregroundDark;
        button.setClickAction(n -> {
            setSelectedId(object.getObjectId());
        });
        addComponent(button);


        objectButtonY += 10;
        objectButtonY += font.getHeight(button.getText());
    }

    @Override
    public void click(MouseClickEvent event) {
        if(event.action == 1 && inBoundsOfBLine(event)){
            draggingLower = true;
        }

        if(event.action == 0 && draggingLower){
            draggingLower = false;
        }

        if(event.action == 1 && inBoundsOfLLine(event)){
            draggingLeft = true;
        }

        if(event.action == 0 && draggingLeft){
            draggingLeft = false;
        }

        if(event.action == 1 && isInStageBounds((float) event.mX, (float) event.mY)){
            startCameraDrag = Display.getMousePosition(Engine.getDisplay().getWindow());
            draggingCamera = true;
        }

        if(draggingCamera && event.action == 0){
            draggingCamera = false;
        }
    }

    public boolean isInStageBounds(float mx, float my){
        return mx >= leftWidth && mx <= Profile.Display.WIDTH && my >= 0 && my <= lowerY;
    }

    @Override
    public void key(KeyEvent event) {
        if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)){
            if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_N)){
                Vector2 v2M = Display.getMousePosition(Engine.getDisplay().getWindow());
                Vector2 v2 = Display.getWorldMousePosition(Engine.getDisplay().getWindow(), cameraVector, cameraZoom);
                if(isInStageBounds(v2M.x, v2M.y)) {
                    var base = new BaseObject();
                    base.setPosition(v2);
                    base.setWidth(100);
                    base.setHeight(100);
                    base.setBaseColor(Color.BLACK);
                    base.setUpPhysicsBody(BodyType.STATIC);
                    newObject(base);
                    setSelectedId(base.getObjectId());
                }
            }
        }

        if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_UP)){
            if(selectedId != -1) {
                selectedObject.addToPos(0, -1);
            }
        }
        if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_DOWN)){
            if(selectedId != -1) {
                selectedObject.addToPos(0, 1);
            }
        }
        if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_LEFT)){
            if(selectedId != -1) {
                selectedObject.addToPos(-1, 0);
            }
        }
        if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_RIGHT)){
            if(selectedId != -1) {
                selectedObject.addToPos(1, 0);
            }
        }
    }

    private boolean inBoundsOfLLine(MouseClickEvent event){
        if (event.mX >= leftWidth - 4 && event.mX <= leftWidth + 4) {
            return event.mY >= 0 && event.mY <= Profile.Display.HEIGHT;
        }
        return false;
    }
    private boolean inBoundsOfBLine(MouseClickEvent event){
        if(event.mX >= leftWidth && event.mX <= Profile.Display.WIDTH){
            return event.mY >= lowerY - 4 && event.mY <= lowerY + 4;
        }
        return false;
    }

    public void setSelectedId(int id){
        if(selectedId != -1){
            var comp = getComponentById("ObjectButton:" + selectedObject.getObjectId());
            if(comp != null){
                comp.foregroundColor = Profile.UiThemes.Dark.foreground;
            }
            selectedObject.setWireframe(false);
        }
        this.selectedId = id;
        var obj = ObjectManager.getObject(id);
        selectedObject = obj;
        obj.setWireframe(true);

        var comp = getComponentById("ObjectButton:" + id);
        if(comp != null){
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
