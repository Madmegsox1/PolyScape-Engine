package org.polyscape.ui.screens;

import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.object.ObjectManager;
import org.polyscape.project.model.ProjectInfo;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.UiEngine;
import org.polyscape.ui.component.button.Button;

public class ObjectList extends Screen {

    ProjectInfo projectInfo;

    int buttonY = 30;
    @Override
    public void model() {
        projectInfo = getModel();
        Editor editor = (Editor) UiEngine.getScreenManager().getUi("Editor");
        components.clear();

        for (var obj : ObjectManager.getObjects()) {
            Button button = new Button(5, buttonY, this, obj.getClass().getSimpleName() + obj.getObjectId(), "ObjectButton:" + obj.getObjectId());
            button.setClickAction(n -> {
                editor.setSelectedId(obj.getObjectId());
            });
            buttonY += 10;
            buttonY += font.getHeight(button.getText());
            button.baseColor = Profile.UiThemes.Dark.foregroundDark;
            addComponent(button);

        }

    }

    @Override
    public void onLoad() {
        buttonY = 30;
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
