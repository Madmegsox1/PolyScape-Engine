package org.polyscape.project;


import com.google.gson.Gson;
import org.polyscape.Loader;
import org.polyscape.project.model.ProjectConfig;
import org.polyscape.project.model.ProjectInfo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;

public class ProjectLoader {
    private final String projectPath = Loader.polyscapePath + "/projects/";

    private final String projectConfigFileName = projectPath + "projectConfig.json";

    private File projectConfig = new File(projectConfigFileName);
    public ProjectConfig config;


    public void initPath() throws IOException {
        File projectFile = new File(projectPath);
        if(!projectFile.exists()){
            projectFile.mkdirs();
        }
        if(!projectConfig.exists()){
            projectConfig.createNewFile();
        }

    }

    public void loadProjects() throws IOException {
        FileReader fr = new FileReader(projectConfig);

        Gson gson = new Gson();

        this.config = gson.fromJson(fr, ProjectConfig.class);

        Comparator<ProjectInfo> compareInfo = Comparator.comparingLong((ProjectInfo o) -> o.lastOpened).reversed();

        config.projects.sort(compareInfo);

        fr.close();
    }
}
