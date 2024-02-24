package org.polyscape.project;


import com.google.gson.Gson;
import org.polyscape.Loader;
import org.polyscape.project.model.ProjectConfig;
import org.polyscape.project.model.ProjectInfo;

import java.io.*;
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

    public void saveProjectConfig(){
        try {
            FileWriter fw = new FileWriter(projectConfig);

            Gson gson = new Gson();

            gson.toJson(config, fw);

            Comparator<ProjectInfo> compareInfo = Comparator.comparingLong((ProjectInfo o) -> o.lastOpened).reversed();

            config.projects.sort(compareInfo);

            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void loadProjects() throws IOException {
        FileReader fr = new FileReader(projectConfig);

        Gson gson = new Gson();

        this.config = gson.fromJson(fr, ProjectConfig.class);

        Comparator<ProjectInfo> compareInfo = Comparator.comparingLong((ProjectInfo o) -> o.lastOpened).reversed();

        if(config != null) {
            config.projects.sort(compareInfo);
        }
        fr.close();
    }
}
