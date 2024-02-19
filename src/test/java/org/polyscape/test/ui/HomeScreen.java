package org.polyscape.test.ui;

import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.button.Button;
import org.polyscape.ui.component.checkbox.CheckBox;
import org.polyscape.ui.component.checkbox.CheckBoxType;

/**
 * @author Madmegsox1
 * @since 31/01/2024
 */

public class HomeScreen extends Screen {

    @Override
    public void onLoad() {
        FontMac font = new FontMac("Segoe UI", 30);
        addComponent(new CheckBox(200, 200, "CheckBox1",CheckBoxType.Untextured,this));

        setFont(font);
        CheckBox c = (CheckBox) this.getComponentById("CheckBox1");
        c.setText("Click me");
        c.setClickAction((checkBox, selected) -> {
            if(selected){
                checkBox.setText("Well done you selected me!");
            }else{
                checkBox.setText("Click me");
            }
        });


        addComponent(new Button(200, 300, this,"Button1"));

    }

    @Override
    public void render(RenderEvent event) {
        //RenderEngine.drawQuad(new Vector2(0, 0), Profile.Display.WIDTH, Profile.Display.HEIGHT, new Color(200, 200, 200));
    }

    @Override
    public void click(MouseClickEvent event) {

    }

    @Override
    public void key(KeyEvent event) {

    }

}
