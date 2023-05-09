package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.dto.ProjectPatchRequest;
import java.util.List;

public interface ProjectService {

    List<Project> getProjectsByOwnerId(Integer ownerId);

    Project getProjectById(Integer projectId);

    Project createProject(Project newProject, String username);

    Project partialUpdateProject(Project targetProject, ProjectPatchRequest projectPatchRequest);

    void deleteProjectById(Integer projectId);
    void deleteAllProjects();

}
