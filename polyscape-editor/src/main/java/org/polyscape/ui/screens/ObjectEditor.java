package org.polyscape.ui.screens;

import org.jbox2d.dynamics.BodyType;
import org.lwjgl.glfw.GLFW;
import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.object.BaseObject;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Texture;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.button.Button;
import org.polyscape.ui.component.checkbox.CheckBox;
import org.polyscape.ui.component.checkbox.CheckBoxType;
import org.polyscape.ui.component.input.Input;
import org.polyscape.ui.component.lable.Label;

import static org.polyscape.ui.screens.Editor.saveObjects;

public final class ObjectEditor extends Screen {

    BaseObject object;

    @Override
    public void model() {

        object = getModel();
        getComponentById("posX").setText(String.valueOf((int) object.getPosition().x));
        getComponentById("posY").setText(String.valueOf((int) object.getPosition().y));
        getComponentById("angle").setText(String.valueOf((int) object.getAngle()));
        getComponentById("width").setText(String.valueOf(object.getWidth()));
        getComponentById("height").setText(String.valueOf(object.getHeight()));
        var txt = object.getTexture();
        if(txt != null){
            var loaded = Texture.loadedTextures.get(txt.getTexture());
            if(!loaded.isEmpty()) {
                getComponentById("texture").setText(loaded);
            }
        }

        if(!object.getBody().isActive()){
            getComponentById("bodyButton").setText("Create Physics");
            getComponentById("dynamic").hidden = true;

            getComponentById("friction").hidden = true;
            getComponentById("frictionLabel").hidden = true;
            getComponentById("density").hidden = true;
            getComponentById("densityLabel").hidden = true;
            getComponentById("linearDamping").hidden = true;
            getComponentById("linearLabel").hidden = true;
            getComponentById("angleCals").hidden = true;
        }
        else {
            getComponentById("bodyButton").setText("Destroy Physics");
            getComponentById("dynamic").hidden = false;

            getComponentById("friction").hidden = false;
            getComponentById("frictionLabel").hidden = false;
            getComponentById("density").hidden = false;
            getComponentById("densityLabel").hidden = false;
            getComponentById("linearDamping").hidden = false;
            getComponentById("linearLabel").hidden = false;
            getComponentById("angleCals").hidden = false;
        }

        var dynamic = (CheckBox) getComponentById("dynamic");
        dynamic.state = object.getBodyType() == BodyType.DYNAMIC ? 1 : 0;

        getComponentById("friction").setText(String.valueOf(object.getFriction()));
        getComponentById("density").setText(String.valueOf(object.getDensity()));
        getComponentById("linearDamping").setText(String.valueOf(object.getLinearDamping()));

        var angleCals = (CheckBox) getComponentById("angleCals");
        angleCals.state = object.isAngleCals() ? 1 : 0;


    }

    @Override
    public void onLoad() {
        components.clear();
        FontMac font = new FontMac("Segoe UI", 22);
        setFont(font);

        Input posX = new Input(Editor.leftWidth + 20, Editor.lowerY + 50, 70, 30, 2f, "posX", this);
        posX.setText("X");

        Label posXLabel = new Label(0, -5, "Pos X", "posXLabel", this, posX);

        Input posY = new Input(Editor.leftWidth + 20 + 70 + 20, Editor.lowerY + 50, 70, 30, 2f, "posY", this);
        posY.setText("Y");

        Label posYLabel = new Label(0, -5, "Pos Y", "posYLabel", this, posY);

        Input angle = new Input(Editor.leftWidth + 20 + (70*2) + 40, Editor.lowerY + 50, 70, 30, 2f, "angle", this);
        posY.setText("Angle");

        Label angleLabel = new Label(0, -5, "Angle", "angleLabel", this, angle);

        Input width = new Input(Editor.leftWidth + 20 + (70*3) + 100, Editor.lowerY + 50, 70, 30, 2f, "width", this);
        posY.setText("Width");

        Label widthLabel = new Label(0, -5, "Width", "widthLabel", this, width);

        Input height = new Input(Editor.leftWidth + 20 + (70*4) + 120, Editor.lowerY + 50, 70, 30, 2f, "height", this);
        posY.setText("height");

        Label heightLabel = new Label(0, -5, "Height", "heightLabel", this, height);

        Button bodyButton = getButton();

        Input texture = new Input(Editor.leftWidth + 20, Editor.lowerY + 105, 150, 30, 2f, "texture", this);
        texture.setText("Texture");

        Label textureLabel = new Label(0, -5, "Texture", "textureLabel", this, texture);

        CheckBox dynamic = new CheckBox(Editor.leftWidth + 20 + 150 + 20, Editor.lowerY + 105, "dynamic", CheckBoxType.Untextured, this);
        dynamic.setText("Dynamic Object");
        dynamic.baseColor = (Profile.UiThemes.Dark.foregroundDark);

        Input friction = new Input(Editor.leftWidth + 20 + 150 + 200, Editor.lowerY + 105, 70,30,2f,"friction", this);
        friction.setText("Friction");

        Label frictionLabel = new Label(0, -5, "Friction", "frictionLabel", this, friction);

        Input density = new Input(Editor.leftWidth + 20 + 150 + 300, Editor.lowerY + 105, 70,30,2f,"density", this);
        density.setText("Density");

        Label densityLabel = new Label(0, -5, "Density", "densityLabel", this, density);

        Input linearDamping = new Input(Editor.leftWidth + 20 + 150 + 400, Editor.lowerY + 105, 70,30,2f,"linearDamping", this);
        linearDamping.setText("Damping");

        Label linearLabel = new Label(0, -5, "Damping", "linearLabel", this, linearDamping);

        CheckBox angleCals = new CheckBox(Editor.leftWidth + 20 + 150 + 500, Editor.lowerY + 105, "angleCals", CheckBoxType.Untextured, this);
        angleCals.setText("Fixed Angles");
        angleCals.baseColor = (Profile.UiThemes.Dark.foregroundDark);

        posX.setUpdateAction((i, text) -> {
            if(!text.isEmpty()) {
                object.setPosition(new Vector2(Integer.parseInt(text), object.getPosition().y));
                saveObjects();
            }
        });

        posY.setUpdateAction((i, text) -> {
            if(!text.isEmpty()) {
                object.setPosition(new Vector2(object.getPosition().x, Integer.parseInt(text)));
                saveObjects();
            }
        });

        angle.setUpdateAction((i, text) -> {
            if(!text.isEmpty()) {
                object.setAngle(Double.parseDouble(text));
                saveObjects();
            }
        });

        width.setUpdateAction((i, text) -> {
            if(!text.isEmpty()) {
                object.setWidth(Integer.parseInt(text));
                saveObjects();
            }
        });

        height.setUpdateAction((i, text) -> {
            if(!text.isEmpty()) {
                object.setHeight(Integer.parseInt(text));
                saveObjects();
            }
        });

        texture.setUpdateAction((Input i, String text) -> {
            if (!text.isEmpty()) {
                try {
                    Texture texture1 = new Texture(text);
                    object.setTexture(texture1);
                    saveObjects();
                }catch (Exception ignored){}

            }
        });

        dynamic.setClickAction((c, b) -> {
            if(b){
                object.setBodyType(BodyType.DYNAMIC, true);
                saveObjects();
            }else{
                object.setBodyType(BodyType.STATIC, true);
                saveObjects();
            }
        });

        friction.setUpdateAction((Input i, String text) -> {
            if (!text.isEmpty()) {
                object.setFriction(Float.parseFloat(text), true);
                saveObjects();
            }
        });

        density.setUpdateAction((Input i, String text) -> {
            if (!text.isEmpty()) {
                object.setDensity(Float.parseFloat(text), true);
                saveObjects();
            }
        });

        linearDamping.setUpdateAction((Input i, String text) -> {
            if (!text.isEmpty()) {
                object.setLinearDamping(Float.parseFloat(text), true);
                saveObjects();
            }
        });

        angleCals.setClickAction((c, b) -> {
            if(b){
                object.setAngleCals(true, true);
                saveObjects();
            }else{
                object.setAngleCals(false, true);
                saveObjects();
            }
        });


        addComponent(posX);
        addComponent(posXLabel);
        addComponent(posY);
        addComponent(posYLabel);
        addComponent(angle);
        addComponent(angleLabel);
        addComponent(width);
        addComponent(widthLabel);
        addComponent(height);
        addComponent(heightLabel);
        addComponent(texture);
        addComponent(textureLabel);
        addComponent(dynamic);
        addComponent(friction);
        addComponent(frictionLabel);
        addComponent(density);
        addComponent(densityLabel);
        addComponent(linearDamping);
        addComponent(linearLabel);
        addComponent(angleCals);
        addComponent(bodyButton);
    }

    private Button getButton() {
        Button bodyButton = new Button(Editor.leftWidth + 20 + (70*5) + 160, Editor.lowerY + 40, this, "Destroy Physics", "bodyButton");

        bodyButton.setClickAction(n -> {
            if(object.getBody().isActive()) {
                object.destroyPhysicsBody();
                n.setText("Create Physics");
                getComponentById("dynamic").hidden = true;
                getComponentById("friction").hidden = true;
                getComponentById("density").hidden = true;
                getComponentById("linearDamping").hidden = true;
                getComponentById("angleCals").hidden = true;
            }else{
                object.setBodyActive();
                n.setText("Destroy Physics");
                getComponentById("dynamic").hidden = false;
                getComponentById("friction").hidden = false;
                getComponentById("density").hidden = false;
                getComponentById("linearDamping").hidden = false;
                getComponentById("angleCals").hidden = false;
            }
            Editor.saveObjects();
        });
        return bodyButton;
    }

    @Override
    public void render(RenderEvent event) {
        if(object != null) {
            font.renderText(object.getClass().getSimpleName() + object.getObjectId(), new Vector2(Editor.leftWidth + 20, Editor.lowerY+20), Profile.UiThemes.Dark.foregroundDark);

        }
    }

    @Override
    public void click(MouseClickEvent event) {

    }

    @Override
    public void key(KeyEvent event) {
        if(KeyEvent.isKeyDown(GLFW.GLFW_KEY_ENTER)){
            var x = getComponentById("posX");
            var y = getComponentById("posY");
            object.setPosition(new Vector2(Integer.parseInt(x.getText()), Integer.parseInt(y.getText())));
        }
    }
}
