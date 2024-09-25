package org.polyscape.ui.screens;

import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.object.Level;
import org.polyscape.object.ObjectManager;
import org.polyscape.object.RenderProperty;
import org.polyscape.project.model.ProjectInfo;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.rendering.sprite.SpriteSheet;
import org.polyscape.ui.Screen;
import org.polyscape.ui.UiEngine;
import org.polyscape.ui.component.button.Button;

import java.util.Objects;

public class SpriteSheetList extends Screen  {
    public static ProjectInfo info;

    private int buttonY = 30;

    private SpriteSheet currentSpriteSheet = null;

    @Override
    public void model() {
        info = this.getModel();

        components.clear();
        // todo change this as sprite sheets shouldnt just be binded to objects they should be stored in there own collection
        ObjectManager.getAllObject().stream().map(RenderProperty::getSpriteSheet).filter(Objects::nonNull).forEach(spriteSheet -> {
            Button bt = new Button(5, buttonY, this,spriteSheet.getFileName(), "SHButton:" + spriteSheet.getFileName());
            buttonY += 10;
            buttonY += font.getHeight(spriteSheet.getFileName());
            bt.baseColor = Profile.UiThemes.Dark.foregroundDark;

            bt.setClickAction(n -> {
                if(currentSpriteSheet != null) {
                    getComponentById("SHButton:" + currentSpriteSheet.getFileName()).foregroundColor = Profile.UiThemes.Dark.foregroundDark;
                }

                n.foregroundColor = Color.BLUE;
                currentSpriteSheet = spriteSheet;

            });
            addComponent(bt);
        });

        Button newSpriteSheet = new Button(5, Profile.Display.HEIGHT - 140, this, "Add Sprite Sheet", "addSpriteSheet");
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

    }
}
