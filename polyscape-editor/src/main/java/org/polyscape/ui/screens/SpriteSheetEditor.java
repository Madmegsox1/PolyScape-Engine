package org.polyscape.ui.screens;

import org.polyscape.event.EventMetadata;
import org.polyscape.event.IEvent;
import org.polyscape.font.FontMac;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.rendering.sprite.SpriteSheet;
import org.polyscape.rendering.sprite.SpriteSheetManager;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.button.Button;
import org.polyscape.ui.component.input.Input;
import org.polyscape.ui.component.lable.Label;
import org.polyscape.ui.events.ScreenChangeEvent;


public final class SpriteSheetEditor extends Screen {
    public SpriteSheetEditor() {
        IEvent<ScreenChangeEvent> screenChangeEvent = n -> {
            if(n.oldScreen != null && n.oldScreen.getName().equals("SpriteSheets")){
                Editor.setRenderLevel(true);
            }
        };

        ScreenChangeEvent.addEvent(screenChangeEvent, new EventMetadata(ScreenChangeEvent.class, 0));
    }

    SpriteSheet spriteSheet;
    @Override
    public void model() {
        this.spriteSheet = getModel();
        getComponentById("fileName").setText(this.spriteSheet.getFileName());
        getComponentById("chunkWidth").setText(String.valueOf(this.spriteSheet.getChunkWidth()));
        getComponentById("chunkHeight").setText(String.valueOf(this.spriteSheet.getChunkHeight()));
        Editor.setRenderLevel(false);
        Editor.cameraVector = new Vector2(-100,0);
        Editor.cameraZoom = spriteSheet.width / 20f;
    }

    @Override
    public void onLoad() {
        components.clear();
        FontMac font = new FontMac("Segoe UI", 22);
        setFont(font);

        Input fileName = new Input(Editor.leftWidth + 20, Editor.lowerY + 40, 500, 30, 2f, "fileName", this);
        Label fileLabel = new Label(0, -5, "File Name", "fileNameLabel", this,fileName);

        Input chunkWidth = new Input(Editor.leftWidth + 20, Editor.lowerY + 40 + 55, 70, 30, 2f, "chunkWidth", this);
        Label chunckWidthLabel = new Label(0, -5, "Chunk Width", "chunkWidthLabel", this, chunkWidth);

        Input chunkHeight = new Input((int) chunkWidth.getX() + 70 + 40, Editor.lowerY + 40 + 55, 70, 30, 2f, "chunkHeight", this);
        Label chunkHeightLabel = new Label(0, -5, "Chunk Height", "chunkHeightLabel", this, chunkHeight);

        Button saveSpriteSheet = getButton(fileName, chunkWidth, chunkHeight);

        components.add(saveSpriteSheet);

        components.add(chunkWidth);
        components.add(chunckWidthLabel);

        components.add(chunkHeight);
        components.add(chunkHeightLabel);

        components.add(fileName);
        components.add(fileLabel);

    }

    private Button getButton(Input fileName, Input chunkWidth, Input chunkHeight) {
        Button saveSpriteSheet = new Button(Editor.leftWidth + 20, Editor.lowerY + 80 + 55, this, "Save", "SaveSpriteSheet");

        saveSpriteSheet.setClickAction(n -> {
            if(model == null) {
                spriteSheet = new SpriteSheet(fileName.getText(), chunkWidth.parseInputInt(), chunkHeight.parseInputInt());
                SpriteSheetManager.addSpriteSheet(spriteSheet);
            }
            else{
                int id = spriteSheet.getSpriteSheetId();
                spriteSheet = new SpriteSheet(fileName.getText(), chunkWidth.parseInputInt(), chunkHeight.parseInputInt());
                spriteSheet.setSpriteSheetId(id);
                SpriteSheetManager.addSpriteSheet(spriteSheet);
            }

            model = spriteSheet;
            Editor.saveObjects();

            Editor.setRenderLevel(false);
            Editor.cameraVector = new Vector2(-50,0);
            Editor.cameraZoom = spriteSheet.width / 20f;
        });
        return saveSpriteSheet;
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
