package com.earthlyz9.stepin.controllers;

import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.entities.ProjectPatchRequest;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.services.ProjectServiceImpl;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectServiceImpl projectServiceImpl;

    @Autowired
    public ProjectController(ProjectServiceImpl projectServiceImpl) {
        this.projectServiceImpl = projectServiceImpl;
    }

    @GetMapping("")
    public List<Project> getAllProjects() {
        return this.projectServiceImpl.getProjects();
    }

    @GetMapping("/{projectId}")
    public Project getProjectById(@PathVariable int projectId) throws NotFoundException {
        return this.projectServiceImpl.getProjectById(projectId);
    }

    @PostMapping ("")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project newProject = this.projectServiceImpl.createProject(project);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}")
            .buildAndExpand(newProject.getId()).toUri();
        return ResponseEntity.created(location).body(newProject);
    }

    @PatchMapping("/{projectId}")
    public Project updateProjectById(@PathVariable int projectId, @RequestBody ProjectPatchRequest data) {
        Project updatedProject = projectServiceImpl.partialUpdateProject(projectId, data);
        return updatedProject;
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable int projectId) throws NotFoundException{
        projectServiceImpl.deleteProjectById(projectId);
        return ResponseEntity. noContent().build();
    }


}
