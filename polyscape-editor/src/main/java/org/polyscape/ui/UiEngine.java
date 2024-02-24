package org.polyscape.ui;

import org.polyscape.Engine;
import org.polyscape.Profile;
import org.polyscape.event.EventBus;
import org.polyscape.rendering.Display;
import org.polyscape.rendering.RenderEngine;
import org.polyscape.rendering.Renderer;
import org.polyscape.ui.screens.Editor;
import org.polyscape.ui.screens.NewOrEditProject;
import org.polyscape.ui.screens.ProjectScreen;

public final class UiEngine extends Engine {


    public void init(){
        Profile.Display.BACKGROUND_COLOR = new float[]{27/255f, 27/255f,27/255f, 1.0f};

        eventBus = new EventBus();
        display = new Display("Polyscape - Editor");
        display.init(false);

        renderer = new Renderer(display);
        renderer.init();

        renderEngine = new RenderEngine();
        screenManager = new ScreenManager();
        loadScreens();
    }

    public void destroyEngine(){
        display.destroyWindow();
        renderer.destroy = true;
        screenManager.clearAll();
        screenManager = null;
        eventBus = null;
        display = null;
        renderer = null;
        renderEngine = null;
    }

    public void render(){
        renderEngine.render(renderer, display);
    }

    public void loadScreens(){
        screenManager.addScreen("ProjectScreen", new ProjectScreen());
        screenManager.addScreen("NewOrEditProject", new NewOrEditProject());
        screenManager.addScreen("Editor", new Editor());
    }
}
