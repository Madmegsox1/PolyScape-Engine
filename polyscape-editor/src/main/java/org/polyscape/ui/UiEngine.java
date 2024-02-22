package org.polyscape.ui;

import org.polyscape.Engine;
import org.polyscape.event.EventBus;
import org.polyscape.rendering.Display;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;
import org.polyscape.ui.screens.NewOrEditProject;
import org.polyscape.ui.screens.ProjectScreen;

public final class UiEngine extends Engine {

    public void init(){
        eventBus = new EventBus();
        display = new Display("Polyscape - Editor");
        display.init(false);

        renderer = new Renderer(display);
        renderer.init();

        renderEngine = new RenderEngine();
        screenManager = new ScreenManager();
        loadScreens();
    }

    public void render(){
        renderEngine.render(renderer, display);
    }

    public void loadScreens(){
        screenManager.addScreen("ProjectScreen", new ProjectScreen());
        screenManager.addScreen("NewOrEditProject", new NewOrEditProject());
    }
}
