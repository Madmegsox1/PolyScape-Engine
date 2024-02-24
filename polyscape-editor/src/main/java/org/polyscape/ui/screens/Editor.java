package org.polyscape.ui.screens;

import org.polyscape.Profile;
import org.polyscape.project.model.ProjectInfo;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.elements.Vector2;
import org.polyscape.rendering.events.KeyEvent;
import org.polyscape.rendering.events.MouseClickEvent;
import org.polyscape.rendering.events.RenderEvent;
import org.polyscape.ui.Screen;
import org.polyscape.ui.UiEngine;

public final class Editor extends Screen {
    ProjectInfo info;

    @Override
    public void model() {
        info = getModel();
        UiEngine.getDisplay().setTitle("Polyscape - Editing " + info.projectName);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void render(RenderEvent event) {
        RenderEngine.drawQuad(new Vector2(0, 0), Profile.Display.WIDTH, Profile.Display.HEIGHT, Profile.UiThemes.Dark.background);

    }

    @Override
    public void click(MouseClickEvent event) {

    }

    @Override
    public void key(KeyEvent event) {

    }
}
