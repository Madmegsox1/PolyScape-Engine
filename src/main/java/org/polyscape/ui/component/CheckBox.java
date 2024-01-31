package org.polyscape.ui.component;

import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.rendering.sprite.SpriteSheet;
import org.polyscape.ui.IScreen;

/**
 * @author Madmegsox1
 * @since 31/01/2024
 */

public class CheckBox extends Component {

    public int state = 0;
    public CheckBox(int x, int y, IScreen screen) {
        super(x, y, 100, 100, screen);

        SpriteSheet checkBox = new SpriteSheet("CheckBoxSheet", 100, 100);
        setTextured(true);
        setSpriteSheet(checkBox);
        setTexture(0);
    }

    @Override
    public void draw(RenderEvent event) {
        RenderEngine.drawQuadTexture(pos, width, height, texture);
    }

    @Override
    public void onClick(MouseClickEvent event) {
        state++;
        if(state > 2){
            state = 0;
        }

        setTexture(state);
    }

    @Override
    public void onKey(KeyEvent event) {

    }

    @Override
    public String getName() {
        return "CheckBox";
    }
}
