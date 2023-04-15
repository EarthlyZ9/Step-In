package com.earthlyz9.stepin.controllers;

import com.earthlyz9.stepin.JsonViews;
import com.earthlyz9.stepin.entities.Category;
import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.entities.ProjectPatchRequest;
import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.services.CategoryServiceImpl;
import com.earthlyz9.stepin.services.ProjectServiceImpl;
import com.earthlyz9.stepin.services.UserServiceImpl;
import com.fasterxml.jackson.annotation.JsonView;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final CategoryServiceImpl categoryServiceImpl;

    @Autowired
    public ProjectController(ProjectServiceImpl projectServiceImpl, CategoryServiceImpl categoryServiceImpl) {
        this.projectServiceImpl = projectServiceImpl;
        this.categoryServiceImpl = categoryServiceImpl;
    }

    @GetMapping("")
    @JsonView(JsonViews.List.class)
    public List<Project> getAllProjects() {
        return this.projectServiceImpl.getProjects();
    }

    @GetMapping("/{projectId}")
    @JsonView(JsonViews.Retrieve.class)
    public Project getProjectById(@PathVariable int projectId) throws NotFoundException {
        Project project = this.projectServiceImpl.getProjectById(projectId);
//        project.setOwner();
        return project;
    }

    @PostMapping ("")
    @JsonView(JsonViews.Retrieve.class)
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Project newProject = this.projectServiceImpl.createProject(project, email);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{projectId}")
            .buildAndExpand(newProject.getId()).toUri();
        return ResponseEntity.created(location).body(newProject);
    }

    @PatchMapping("/{projectId}")
    @JsonView(JsonViews.Retrieve.class)
    public Project updateProjectById(@PathVariable int projectId, @RequestBody ProjectPatchRequest data) {
        Project updatedProject = projectServiceImpl.partialUpdateProject(projectId, data);
        return updatedProject;
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable int projectId) throws NotFoundException{
        projectServiceImpl.deleteProjectById(projectId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/categories")
    @JsonView(JsonViews.Retrieve.class)
    public ResponseEntity<Category> createCategoryUnderProject(@PathVariable int projectId, @RequestBody Category category) throws NotFoundException {
        Category newCategory = categoryServiceImpl.createCategory(category, projectId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/categories/" + newCategory.getId())
            .buildAndExpand(newCategory.getId()).toUri();
        return ResponseEntity.created(location).body(newCategory);
    }


}
