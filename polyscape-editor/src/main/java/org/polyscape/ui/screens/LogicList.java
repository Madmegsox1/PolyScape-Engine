package org.polyscape.ui.screens;

import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.logic.LogicContainer;
import org.polyscape.logic.LogicManager;
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

public final class LogicList extends Screen {

    private int buttonY = 30;
    private ProjectInfo info;

    @Override
    public void model() {
        this.info = getModel();

        components.clear();
    }

    private Button getLogicButton(LogicContainer logicContainer) {
        Button button = new Button(5, buttonY, this, , "LvlButton:" + lvl.getLevelNumber());
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
        components.clear();
        FontMac font = new FontMac("Segoe UI", 25);
        setFont(font);

        buttonY = 30;

    }
}
