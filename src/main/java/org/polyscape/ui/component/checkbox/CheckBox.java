package org.polyscape.ui.component.checkbox;

import org.polyscape.Profile;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.rendering.sprite.SpriteSheet;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.Component;
import org.polyscape.ui.events.ComponentClickEvent;

/**
 * @author Madmegsox1
 * @since 31/01/2024
 */

public class CheckBox extends Component {

    public int state = 0;

    public CheckBoxType type;

    public ICheckBoxClick clickAction;

    public Color baseColor;


    public CheckBox(int x, int y, String id,CheckBoxType type, Screen screen) {
        super(x, y, 25, 25, screen, id);
        this.type = type;
        baseColor = Profile.UiThemes.Dark.foreground;
        if(type == CheckBoxType.Textured) {
            SpriteSheet checkBox = new SpriteSheet("CheckBoxSheet", 100, 100);
            setTextured(true);
            setSpriteSheet(checkBox);
            setTexture(0);
        }
    }

    @Override
    public void draw(RenderEvent event) {
        if(type == CheckBoxType.Textured) {
            RenderEngine.drawQuadTexture(pos, width, height, texture);
        }else if(type == CheckBoxType.Untextured){
            RenderEngine.drawQuadLines(pos, width, height, 2f, baseColor);

            if(state > 0){
                RenderEngine.drawQuad(new Vector2(pos.x + 5, pos.y + 5), width - 10, height - 10, baseColor);
            }
        }

        screen.font.renderText(this.text, new Vector2(pos.x + width + 5, pos.y + height), baseColor);

    }

    public void setClickAction(ICheckBoxClick action){
        this.clickAction = action;
    }

    @Override
    public void onClick(MouseClickEvent event) {
    }

    @Override
    public void onKey(KeyEvent event) {

    }

    @Override
    public void onComponentClick(ComponentClickEvent event) {
        if(event.action == 0){
            state++;
            if(state > 2 || (type == CheckBoxType.Untextured && state > 1)){
                state = 0;
            }
            if(type == CheckBoxType.Textured) {
                setTexture(state);
            }

            if(clickAction != null){
                clickAction.run(this, state > 0);
            }
        }
    }

    @Override
    public String getName() {
        return "CheckBox";
    }
}
