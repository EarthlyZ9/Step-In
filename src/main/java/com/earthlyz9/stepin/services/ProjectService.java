package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.entities.ProjectPatchRequest;
import java.util.List;

public interface ProjectService {

    List<Project> getProjects();

    Project getProjectById(Integer projectId);

    Project createProject(Project newProject);

    Project partialUpdateProject(Integer projectId, ProjectPatchRequest newProject);

    void deleteProjectById(Integer projectId);

}
