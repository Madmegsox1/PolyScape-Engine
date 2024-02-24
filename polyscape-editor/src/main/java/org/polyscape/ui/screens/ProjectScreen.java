package org.polyscape.ui.screens;

import org.polyscape.Engine;
import org.polyscape.Loader;
import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.project.model.ProjectConfig;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.button.Button;

public final class ProjectScreen extends Screen {

    private ProjectConfig config;

    @Override
    public void onLoad() {
        //titleFont = new FontMac("Segoe UI", 20);

        config = Loader.projectLoader.config;

        FontMac font = new FontMac("Segoe UI", 25);
        setFont(font);
        var newProjectButton = new Button(5, 10, this, "New Project", "newProjectButton");
        newProjectButton.baseColor = Profile.UiThemes.Dark.accent2;
        newProjectButton.setClickAction(n -> {
            Engine.getScreenManager().setCurrentUi(0, "NewOrEditProject");
            Engine.getScreenManager().setScreenModel(0, null);
        });

        this.addComponent(newProjectButton);

        if (config != null && !config.projects.isEmpty()) {
            int y = 90;
            for (var p : config.projects) {
                Button openProjectButton = new Button(5, y, this, "Edit Project", "openProjectButton:" + p.projectName);
                openProjectButton.baseColor = Profile.UiThemes.Dark.accent;
                openProjectButton.setClickAction((Button n) -> {
                    Engine.getScreenManager().setCurrentUi(0, "NewOrEditProject");
                    Engine.getScreenManager().setScreenModel(0, p);
                });

                this.addComponent(openProjectButton);
                y += font.getHeight(p.projectName) + 50;
            }
        }
    }

    @Override
    public void render(RenderEvent event) {
        RenderEngine.drawQuad(new Vector2(0, 0), Profile.Display.WIDTH, Profile.Display.HEIGHT, Profile.UiThemes.Dark.background);
        RenderEngine.drawLine(new Vector2(0, 45), new Vector2(Profile.Display.WIDTH, 45), 4f, Profile.UiThemes.Dark.foregroundDark);
        font.renderText("Previous Projects:", new Vector2(5, 70), Profile.UiThemes.Dark.foreground);

        if (config != null && !config.projects.isEmpty()) {
            float y = 70;
            for (var p : config.projects) {
                y += font.getHeight(p.projectName) + 50;
                RenderEngine.drawLine(new Vector2(0, y - 50), new Vector2(Profile.Display.WIDTH, y - 50), 1f, Profile.UiThemes.Dark.foregroundDark);
                font.renderText(p.projectName, new Vector2(5, y), Profile.UiThemes.Dark.foreground);
                font.renderText(p.projectPath, new Vector2(Profile.Display.WIDTH - font.getWidth(p.projectPath), y), Profile.UiThemes.Dark.foregroundDark);
            }
        }
    }

    @Override
    public void click(MouseClickEvent event) {

    }

    @Override
    public void key(KeyEvent event) {

    }
}
