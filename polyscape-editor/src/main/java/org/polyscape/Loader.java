package org.polyscape;

import org.polyscape.project.ProjectLoader;
import org.polyscape.ui.UiEngine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Loader {

    public static final String polyscapePath = System.getProperty("user.dir") + "/Polyscape";

    public static ProjectLoader projectLoader;

    public static UiEngine uiEngine;

    public static void main(String[] args) throws IOException {

        File path = new File(polyscapePath);

        if(!path.exists()){
            path.mkdirs();
        }

        projectLoader = new ProjectLoader();

        projectLoader.initPath();
        projectLoader.loadProjects();

        uiEngine = new UiEngine();

        uiEngine.init();
        UiEngine.getScreenManager().setCurrentUi(0, "ProjectScreen");
        uiEngine.render();
    }
}
