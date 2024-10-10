package org.polyscape.ui.screens;

import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.logic.LogicContainer;
import org.polyscape.logic.LogicManager;
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

    private int selectedId = -1;


    @Override
    public void model() {
        this.info = getModel();

        components.clear();
        LogicManager.getLogics().forEach(n -> {
            Button b = getLogicButton(n);
            buttonY += 10;
            buttonY += font.getHeight(b.getText());
            b.baseColor = Profile.UiThemes.Dark.foregroundDark;
            addComponent(b);
        });

        Button newLogicButton = getLogicNewButton();
        addComponent(newLogicButton);
    }

    private Button getLogicNewButton(){
        Button newLogic = new Button(5, Profile.Display.HEIGHT - 140, this, "Add Logic", "addLogicButton");
        newLogic.setClickAction(n -> {
            UiEngine.getScreenManager().setCurrentUi(1, "LogicEdit");
            UiEngine.getScreenManager().setScreenModel(1, null);
        });

        return newLogic;
    }

    private Button getLogicButton(LogicContainer logicContainer) {
        Button button = new Button(5, buttonY, this, logicContainer.logicId()+"|"+logicContainer.logicName(), "LogicButton:" + logicContainer.logicId());
        button.setClickAction(n -> {
            if(selectedId >= 0) {
                getComponentById("LogicButton:" + selectedId).foregroundColor = Profile.UiThemes.Dark.foreground;
            }
            selectedId = logicContainer.logicId();
            n.foregroundColor = Color.BLUE;
            UiEngine.getScreenManager().setCurrentUi(1, "LogicEdit");
            UiEngine.getScreenManager().setScreenModel(1, logicContainer);
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
