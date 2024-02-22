package org.polyscape.ui.screens;

import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.font.FontMac;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Color;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.component.button.Button;

public final class ProjectScreen extends Screen {
    @Override
    public void onLoad() {
         //titleFont = new FontMac("Segoe UI", 20);
        FontMac font = new FontMac("Segoe UI", 25);
        setFont(font);
        var newProjectButton = new Button(5, 10, this, "New Project","newProjectButton");
        newProjectButton.baseColor = Profile.UiThemes.Dark.accent2;
        newProjectButton.setClickAction(n -> {
            Engine.getScreenManager().setCurrentUi(0, "NewOrEditProject");
        });
        this.addComponent(newProjectButton);
    }

    @Override
    public void render(RenderEvent event) {
        RenderEngine.drawQuad(new Vector2(0,0), Profile.Display.WIDTH, Profile.Display.HEIGHT, Profile.UiThemes.Dark.background);
        RenderEngine.drawLine(new Vector2(0,45), new Vector2(Profile.Display.WIDTH, 45), 2f, Profile.UiThemes.Dark.foregroundDark);
        font.renderText("Previous Projects", new Vector2(5, 70), Profile.UiThemes.Dark.foreground);
    }

    @Override
    public void click(MouseClickEvent event) {

    }

    @Override
    public void key(KeyEvent event) {

    }
}
