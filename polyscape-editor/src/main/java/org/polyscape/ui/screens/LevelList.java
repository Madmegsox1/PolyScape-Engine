package org.polyscape.ui.screens;

import org.polyscape.Loader;
import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.object.Level;
import org.polyscape.object.ObjectManager;
import org.polyscape.project.model.ProjectInfo;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.UiEngine;
import org.polyscape.ui.component.button.Button;

public class LevelList extends Screen {

    public static ProjectInfo info;
    int buttonY = 30;
    @Override
    public void model() {
        info = getModel();


        components.clear();
        for (var lvl : ObjectManager.getLevels()) {
            Button button = getButton(lvl);
            buttonY += 10;
            buttonY += font.getHeight(button.getText());
            button.baseColor = Profile.UiThemes.Dark.foregroundDark;
            addComponent(button);

        }


        getComponentById("LvlButton:" + ObjectManager.getCurrentLevel().getLevelNumber()).foregroundColor = Color.BLUE;

    }

    private Button getButton(Level lvl) {
        Button button = new Button(5, buttonY, this, lvl.getLevelName(), "LvlButton:" + lvl.getLevelNumber());
        button.setClickAction(n -> {
            int previousLvl = ObjectManager.getCurrentLevel().getLevelNumber();
            ObjectManager.loadLevel(lvl.getLevelNumber());

            getComponentById("LvlButton:" + previousLvl).foregroundColor = Profile.UiThemes.Dark.foreground;
            n.foregroundColor = Color.BLUE;
            UiEngine.getScreenManager().setCurrentUi(1, "LevelEditor");
            UiEngine.getScreenManager().setScreenModel(1, lvl);
        });
        return button;
    }

    @Override
    public void onLoad() {
        components.clear();
        FontMac font = new FontMac("Segoe UI", 25);
        setFont(font);

        buttonY = 30;
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
