package org.polyscape.ui.component.button;

import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.Component;
import org.polyscape.ui.component.checkbox.ICheckBoxClick;
import org.polyscape.ui.events.ComponentClickEvent;

public class Button extends Component {

    private boolean buttonDown;

    public Color pressedColor;

    public IButtonClick clickAction;


    public Button(int x, int y, Screen screen, String text,String compId) {
        super(x, y, 40, 25, screen, compId);
        pressedColor = new Color(100, 100, 100);
        this.setText(text);
    }

    @Override
    public void draw(RenderEvent event) {
        RenderEngine.drawQuad(pos, width, height, (buttonDown) ? pressedColor : baseColor);
    }

    @Override
    public void onClick(MouseClickEvent event) {
        if(!inBounds(event.mX, event.mY) && buttonDown) buttonDown = false;
    }

    @Override
    public void onKey(KeyEvent event) {
    }

    @Override
    public void onComponentClick(ComponentClickEvent event) {
        if(event.action == 0){
            //baseColor = Color.BLACK;
            buttonDown = false;
            clickAction.run(this);
        }

        if(event.action == 1){
            //baseColor = pressedColor;
            buttonDown = true;
        }
    }

    @Override
    public void setText(String text){
        this.text = text;
        // TODO get width
    }

    public void setClickAction(IButtonClick action){
        this.clickAction = action;
    }

    @Override
    public String getName() {
        return "Button";
    }
}
