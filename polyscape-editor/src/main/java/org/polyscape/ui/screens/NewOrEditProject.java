package org.polyscape.ui.screens;

import org.polyscape.Engine;
import org.polyscape.Loader;
import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.project.model.ProjectConfig;
import org.polyscape.project.model.ProjectInfo;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.button.Button;
import org.polyscape.ui.component.checkbox.CheckBox;
import org.polyscape.ui.component.checkbox.CheckBoxType;
import org.polyscape.ui.component.input.Input;

import java.util.ArrayList;
import java.util.Date;

public final class NewOrEditProject extends Screen {

    ProjectInfo info;

    @Override
    public void model() {
        info = getModel();
        getComponentById("projectName").setText(info.projectName);
        getComponentById("saveButton").setText("Save Project");
        if(info.gameType == 0) {
            var topDown = (CheckBox) getComponentById("topDown");
            topDown.state = 1;
        }
        else if(info.gameType == 1){
            var topDown = (CheckBox) getComponentById("sideScroller");
            topDown.state = 1;
        }

    }

    @Override
    public void onLoad() {
        info = null;

        FontMac font = new FontMac("Segoe UI", 25);
        setFont(font);

        Button backButton = new Button(5, 10, this, "Back To Projects", "backButton");
        backButton.baseColor = Profile.UiThemes.Dark.accent;
        backButton.setClickAction(n -> {
            Engine.getScreenManager().setCurrentUi(0, "ProjectScreen");
        });
        addComponent(backButton);

        Input projectName = new Input(5, 130, 200, 25, 2f, "projectName", this);
        projectName.setBaseColor(Profile.UiThemes.Dark.foreground);
        addComponent(projectName);

        CheckBox topdown = new CheckBox(300, 130, "topDown", CheckBoxType.Untextured,this);
        topdown.baseColor = Profile.UiThemes.Dark.foregroundDark;
        topdown.setText("Top Down");


        CheckBox sideScroller = new CheckBox(300, 160, "sideScroller", CheckBoxType.Untextured,this);
        sideScroller.baseColor = Profile.UiThemes.Dark.foregroundDark;
        sideScroller.setText("Side Scroller");
        sideScroller.setClickAction((n, b) -> {
            if (b) {
                topdown.state = 0;
            }
        });

        topdown.setClickAction((n, b) -> {

            if (b) {
                sideScroller.state = 0;
            }
        });


        addComponent(topdown);
        addComponent(sideScroller);

        Button saveButton = new Button(5, 200, this, "Create Project", "saveButton");
        saveButton.baseColor = Profile.UiThemes.Dark.accent2;

        saveButton.setClickAction((n) -> {
            if(info != null){
                Loader.projectLoader.config.projects.remove(info);

                var name = getComponentById("projectName").getText();

                info.projectName = name;
                int gameType = 0;

                if(sideScroller.state > 0){
                    gameType = 1;
                }
                info.gameType = gameType;
                Loader.projectLoader.config.projects.add(info);
            }
            else{
                ProjectInfo projectInfo = new ProjectInfo();
                var name = getComponentById("projectName").getText();
                projectInfo.projectPath = "/" + name;
                projectInfo.projectName = name;
                projectInfo.lastOpened = new Date().getTime();

                int gameType = 0;

                if(sideScroller.state > 0){
                    gameType = 1;
                }

                projectInfo.gameType = gameType;
                if(Loader.projectLoader.config != null) {
                    Loader.projectLoader.config.projects.add(projectInfo);
                }else{
                    Loader.projectLoader.config = new ProjectConfig();
                    Loader.projectLoader.config.projects = new ArrayList<>();
                    Loader.projectLoader.config.projects.add(projectInfo);

                }
            }
            Loader.projectLoader.saveProjectConfig();
            Engine.getScreenManager().setCurrentUi(0, "ProjectScreen");
        });

        addComponent(saveButton);
    }

    @Override
    public void render(RenderEvent event) {
        RenderEngine.drawQuadNew(new Vector2(0, 0), Profile.Display.WIDTH, Profile.Display.HEIGHT, Profile.UiThemes.Dark.background);

        if(model !=  null){
            font.renderText("Editing Project Config: " + info.projectName, new Vector2(200, 30), Profile.UiThemes.Dark.foreground);
        }

        font.renderText("Project Name", new Vector2(5,120), Profile.UiThemes.Dark.foreground);
    }

    @Override
    public void click(MouseClickEvent event) {

    }

    @Override
    public void key(KeyEvent event) {

    }
}
