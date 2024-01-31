package org.polyscape.ui;

import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.component.CheckBox;

/**
 * @author Madmegsox1
 * @since 31/01/2024
 */

public class HomeScreen extends Screen{

    @Override
    public void onLoad() {
        addComponent(new CheckBox(200, 200, this));
    }

    @Override
    public void render(RenderEvent event) {
        RenderEngine.drawQuad(new Vector2(100, 100), 500, 500, Color.WHITE);
    }

    @Override
    public void click(MouseClickEvent event) {

    }

    @Override
    public void key(KeyEvent event) {

    }

}
