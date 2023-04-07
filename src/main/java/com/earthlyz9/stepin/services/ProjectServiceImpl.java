package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.entities.ProjectPatchRequest;
import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.repositories.ProjectRepository;
import com.earthlyz9.stepin.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserServiceImpl userServiceImpl) {
        this.projectRepository = projectRepository;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Integer projectId) throws NotFoundException {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isEmpty()) throw new NotFoundException("Project with the provided id doesn't exist");
        return project.get();
    }

    @Override
    @Transactional
    public Project createProject(Project newProject) {
        // TODO: get current user
        User currentUser = userServiceImpl.getUserById(1);
        newProject.setId(0);
        newProject.setOwnerId(currentUser.getId());
        Project project = projectRepository.save(newProject);
        project.setOwner(currentUser);
        return project;
    }

    @Override
    public Project partialUpdateProject(Integer projectId, ProjectPatchRequest newProject) {
        Project object = getProjectById(projectId);
        object.setName(newProject.getName());
        return projectRepository.save(object);
    }

    @Override
    @Transactional
    public void deleteProjectById(Integer projectId) throws NotFoundException {
        // TODO: check cascading deletion for all categories and items
        Project instance = getProjectById(projectId);
        projectRepository.deleteById(projectId);
    }
}