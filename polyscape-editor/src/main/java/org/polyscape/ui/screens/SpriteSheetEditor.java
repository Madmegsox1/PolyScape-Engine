package org.polyscape.ui.screens;

import org.polyscape.font.FontMac;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.rendering.sprite.SpriteSheet;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.button.Button;
import org.polyscape.ui.component.input.Input;

import java.io.File;

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

        Input chunkWidth = new Input((int) fileName.getX() + 20, Editor.lowerY + 40, 70, 30, 2f, "chunkWidth", this);
        Input chunkHeight = new Input((int) chunkWidth.getX() + 20, Editor.lowerY + 40, 70, 30, 2f, "chunkHeight", this);

        Button saveSpriteSheet = new Button(Editor.leftWidth + 20, Editor.lowerY + 40, this, "Save", "SaveSpriteSheet");

        saveSpriteSheet.setClickAction(n -> {
            spriteSheet = new SpriteSheet(fileName.getText(), chunkWidth.parseInputInt(), chunkHeight.parseInputInt());
        });
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
