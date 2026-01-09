package org.polyscape.ui.screens;

import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.project.model.ProjectInfo;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.rendering.sprite.SpriteSheet;
import org.polyscape.rendering.sprite.SpriteSheetManager;
import org.polyscape.ui.Screen;
import org.polyscape.ui.UiEngine;
import org.polyscape.ui.component.button.Button;


public class SpriteSheetList extends Screen  {
    public static ProjectInfo info;

    private int buttonY = 30;

    private SpriteSheet currentSpriteSheet = null;

    @Override
    public void model() {
        info = this.getModel();

        components.clear();
        SpriteSheetManager.getSpriteSheets().forEach(spriteSheet -> {
            Button bt = new Button(5, buttonY, this, spriteSheet.getSpriteSheetId()+" | "+ spriteSheet.getFileName(), "SHButton:" + spriteSheet.getSpriteSheetId());
            buttonY += 10;
            buttonY += font.getHeight(spriteSheet.getSpriteSheetId()+" | "+ spriteSheet.getFileName());
            bt.baseColor = Profile.UiThemes.Dark.foregroundDark;

            bt.setClickAction(n -> {
                if(currentSpriteSheet != null) {
                    getComponentById("SHButton:" + currentSpriteSheet.getSpriteSheetId()).foregroundColor = Profile.UiThemes.Dark.foreground;
                }

                n.foregroundColor = Color.BLUE;
                currentSpriteSheet = spriteSheet;
                UiEngine.getScreenManager().setCurrentUi(1, "SpriteSheets");
                UiEngine.getScreenManager().setScreenModel(1, currentSpriteSheet);

            });
            addComponent(bt);
        });

        Button newSpriteSheet = new Button(5, Profile.Display.HEIGHT - 160, this, "Add Sprite Sheet", "addSpriteSheet");
        newSpriteSheet.setClickAction(n -> {
            UiEngine.getScreenManager().setCurrentUi(1, "SpriteSheets");
            UiEngine.getScreenManager().setScreenModel(1, null);

        });

        addComponent(newSpriteSheet);

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

    @Override
    public void onLoad() {
        FontMac font = new FontMac("Segoe UI", 25);
        setFont(font);
        buttonY = 30;

    }
}
