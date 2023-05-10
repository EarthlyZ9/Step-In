package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.dto.project.ProjectCreateRequest;
import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.dto.project.ProjectPatchRequest;
import java.util.List;

public interface ProjectService {

    List<Project> getProjectsByOwnerId(Integer ownerId);

    Project getProjectById(Integer projectId);

    Project createProject(ProjectCreateRequest newProject, Integer userId);

    Project partialUpdateProject(Project targetProject, ProjectPatchRequest projectPatchRequest);

    void deleteProjectById(Integer projectId);
    void deleteAllProjects();

}
