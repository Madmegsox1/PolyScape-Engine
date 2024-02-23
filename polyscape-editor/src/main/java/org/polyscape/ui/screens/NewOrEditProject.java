package org.polyscape.ui.screens;

import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.project.model.ProjectInfo;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.button.Button;
import org.polyscape.ui.component.input.Input;

public final class NewOrEditProject extends Screen {

    ProjectInfo info;

    @Override
    public void model() {
        info = getModel();
        getComponentById("projectName").setText(info.projectName);
    }

    @Override
    public void onLoad() {
        FontMac font = new FontMac("Segoe UI", 25);
        setFont(font);

        Button backButton = new Button(5, 10, this, "Back To Projects", "backButton");
        backButton.baseColor = Profile.UiThemes.Dark.accent;
        backButton.setClickAction(n -> {
            Engine.getScreenManager().setCurrentUi(0, "ProjectScreen");
        });
        addComponent(backButton);

        Input projectName = new Input(5, 70, 200, 25, 2f, "projectName", this);
        projectName.setBaseColor(Profile.UiThemes.Dark.foreground);
        addComponent(projectName);
    }

    @Override
    public void render(RenderEvent event) {
        RenderEngine.drawQuad(new Vector2(0, 0), Profile.Display.WIDTH, Profile.Display.HEIGHT, Profile.UiThemes.Dark.background);

        if(model !=  null){
            font.renderText("Editing Project Config: " + info.projectName, new Vector2(5, 60), Profile.UiThemes.Dark.foreground);
        }

    }

    @Override
    public void click(MouseClickEvent event) {

    }

    @Override
    public void key(KeyEvent event) {

    }
}
