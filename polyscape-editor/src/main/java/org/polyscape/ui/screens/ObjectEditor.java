package org.polyscape.ui.screens;

import org.jbox2d.dynamics.BodyType;
import org.lwjgl.glfw.GLFW;
import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.object.BaseObject;
import org.polyscape.rendering.elements.Texture;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.checkbox.CheckBox;
import org.polyscape.ui.component.checkbox.CheckBoxType;
import org.polyscape.ui.component.input.Input;

public class ObjectEditor extends Screen {

    BaseObject object;

    @Override
    public void model() {
        object = getModel();
        getComponentById("posX").setText(String.valueOf((int) object.getPosition().x));
        getComponentById("posY").setText(String.valueOf((int) object.getPosition().y));
        getComponentById("angle").setText(String.valueOf((int) Math.toDegrees(object.getAngle())));
        getComponentById("width").setText(String.valueOf(object.getWidth()));
        getComponentById("height").setText(String.valueOf(object.getHeight()));
        var txt = object.getTexture();
        if(txt != null){
            var loaded = Texture.loadedTextures.get(txt.getTexture());
            if(!loaded.isEmpty()) {
                getComponentById("texture").setText(loaded);
            }
        }

        var dynamic = (CheckBox) getComponentById("dynamic");
        dynamic.state = object.getBody().m_type == BodyType.DYNAMIC ? 1 : 0;

    }

    @Override
    public void onLoad() {
        FontMac font = new FontMac("Segoe UI", 22);
        setFont(font);
        Input posX = new Input(Editor.leftWidth + 20, Editor.lowerY + 40, 70, 30, 2f, "posX", this);
        posX.setText("X");

        Input posY = new Input(Editor.leftWidth + 20 + 70 + 20, Editor.lowerY + 40, 70, 30, 2f, "posY", this);
        posY.setText("Y");

        Input angle = new Input(Editor.leftWidth + 20 + (70*2) + 40, Editor.lowerY + 40, 70, 30, 2f, "angle", this);
        posY.setText("Angle");

        Input width = new Input(Editor.leftWidth + 20 + (70*3) + 100, Editor.lowerY + 40, 70, 30, 2f, "width", this);
        posY.setText("Width");

        Input height = new Input(Editor.leftWidth + 20 + (70*4) + 120, Editor.lowerY + 40, 70, 30, 2f, "height", this);
        posY.setText("height");

        Input texture = new Input(Editor.leftWidth + 20, Editor.lowerY + 80, 150, 30, 2f, "texture", this);
        texture.setText("Texture");

        CheckBox dynamic = new CheckBox(Editor.leftWidth + 20 + 150 + 20, Editor.lowerY + 80, "dynamic", CheckBoxType.Untextured, this);
        dynamic.setText("Dynamic Object");
        dynamic.baseColor = (Profile.UiThemes.Dark.foregroundDark);

        posX.setUpdateAction((i, text) -> {
            if(!text.isEmpty()) {
                object.setPosition(new Vector2(Integer.parseInt(text), object.getPosition().y));
            }
        });

        posY.setUpdateAction((i, text) -> {
            if(!text.isEmpty()) {
                object.setPosition(new Vector2(object.getPosition().x, Integer.parseInt(text)));
            }
        });

        angle.setUpdateAction((i, text) -> {
            if(!text.isEmpty()) {
                object.setAngle(Double.parseDouble(text));
            }
        });

        width.setUpdateAction((i, text) -> {
            if(!text.isEmpty()) {
                object.setWidth(Integer.parseInt(text));
            }
        });

        height.setUpdateAction((i, text) -> {
            if(!text.isEmpty()) {
                object.setHeight(Integer.parseInt(text));
            }
        });

        texture.setUpdateAction((Input i, String text) -> {
            if (!text.isEmpty()) {
                try {
                    Texture texture1 = new Texture(text);
                    object.setTexture(texture1);
                }catch (Exception ignored){}

            }
        });

        dynamic.setClickAction((c, b) -> {
            if(b){
                object.setUpPhysicsBody(BodyType.DYNAMIC);
            }else{
                object.setUpPhysicsBody(BodyType.STATIC);
            }
        });


        addComponent(posX);
        addComponent(posY);
        addComponent(angle);
        addComponent(width);
        addComponent(height);
        addComponent(texture);
        addComponent(dynamic);
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
