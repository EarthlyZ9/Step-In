package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.dto.project.ProjectCreateRequest;
import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.dto.project.ProjectPatchRequest;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.repositories.ProjectRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> getProjectsByOwnerId(Integer ownerId) {
        List<Project> projects = projectRepository.findByOwnerId(ownerId);
        return projects;
    }

    @Override
    public Project getProjectById(Integer projectId) throws NotFoundException {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isEmpty()) throw new NotFoundException("Project with the provided id doesn't exist");
        return project.get();
    }

    @Override
    @Transactional
    public Project createProject(ProjectCreateRequest newProject, Integer userId) {
        Project entity = Project.builder().id(0).ownerId(userId).name(newProject.getName()).build();
        return projectRepository.save(entity);
    }

    @Override
    @Transactional
    public Project partialUpdateProject(Project targetProject, ProjectPatchRequest newProject) {
        targetProject.setName(newProject.getName());
        return projectRepository.save(targetProject);
    }

    @Override
    @Transactional
    public void deleteProjectById(Integer projectId) throws NotFoundException {
        projectRepository.deleteById(projectId);
    }

    @Override
    @Transactional
    public void deleteAllProjects() {
        projectRepository.deleteAll();
    }
}
