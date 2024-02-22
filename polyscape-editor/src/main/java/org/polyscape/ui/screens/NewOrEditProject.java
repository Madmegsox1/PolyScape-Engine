package org.polyscape.ui.screens;

import org.polyscape.font.FontMac;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;

public final class NewOrEditProject extends Screen {
    @Override
    public void onLoad() {
        FontMac font = new FontMac("Segoe UI", 25);
        setFont(font);
    }

    @Override
    public void render(RenderEvent event) {

    }

    @Override
    public void click(MouseClickEvent event) {

    }

    @Override
    public void key(KeyEvent event) {

    }
}
