package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.dto.project.ProjectPatchRequest;
import com.earthlyz9.stepin.entities.UserRole;
import com.earthlyz9.stepin.exceptions.ConflictException;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.repositories.ProjectRepository;
import com.earthlyz9.stepin.utils.AuthUtils;
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
        List<Project> projects = projectRepository.findByOwnerIdOrderByUpdatedAtDesc(ownerId);
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
    public Project createEmptyProject(Integer userId) throws ConflictException {

        if (AuthUtils.getRequestUser().getRole().equals(UserRole.GUEST)) {
            int count = projectRepository.findByOwnerId(userId).size();
            if (count == 1) throw new ConflictException("Guest user can only create one project");
        }

        Project newProject = new Project();
        newProject.setId(0);
        newProject.setOwnerId(userId);
        newProject.setName("Untitled");

        return projectRepository.save(newProject);
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
