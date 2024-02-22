package org.polyscape.project;

import org.pkl.core.Evaluator;
import org.pkl.core.ModuleSource;
import org.pkl.core.PModule;
import org.pkl.core.PObject;

import java.util.List;

@SuppressWarnings("unchecked")
public class ProjectLoader {

    private final String projectPath = "projectHistory.pkl";

    public void loadProject(String projectName) {
        PModule project;
        try (var evaluator =
                     Evaluator.preconfigured()) {
            project = evaluator.evaluate(
                    ModuleSource.modulePath(projectPath));
        }

        var projects = (PObject) project.get("projectLoader");
        assert projects != null;
        var previousProjects = (List<PObject>) projects.get("previousProjects");
    }
}
