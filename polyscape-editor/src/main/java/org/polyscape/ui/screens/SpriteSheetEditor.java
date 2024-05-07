package org.polyscape.ui.screens;

import org.polyscape.font.FontMac;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.rendering.sprite.SpriteSheet;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.input.Input;

public final class SpriteSheetEditor extends Screen {

    SpriteSheet spriteSheet;
    @Override
    public void model() {
        this.spriteSheet = getModel();
    }

    @Override
    public void onLoad() {
        components.clear();
        FontMac font = new FontMac("Segoe UI", 22);
        setFont(font);

        Input fileName = new Input(Editor.leftWidth + 20, Editor.lowerY + 40, 70, 30, 2f, "fileName", this);


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
