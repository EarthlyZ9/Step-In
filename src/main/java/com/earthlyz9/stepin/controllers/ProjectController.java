package com.earthlyz9.stepin.controllers;

import com.earthlyz9.stepin.JsonViews;
import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.entities.ProjectPatchRequest;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.services.ProjectServiceImpl;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Project", description = "유저가 생성한 프로젝트")
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectServiceImpl projectServiceImpl;

    @Autowired
    public ProjectController(ProjectServiceImpl projectServiceImpl) {
        this.projectServiceImpl = projectServiceImpl;
    }

    @GetMapping("")
    @JsonView(JsonViews.List.class)
    @Operation(summary = "요청을 보내는 유저의 모든 프로젝트를 가져옵니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json"))
    })
    public List<Project> getAllProjects() {
        return this.projectServiceImpl.getProjects();
    }

    @GetMapping("/{projectId}")
    @JsonView(JsonViews.Retrieve.class)
    @Operation(summary = "해당 id 의 프로젝트를 가져옵니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json"))
    })
    public Project getProjectById(@PathVariable int projectId) throws NotFoundException {
        Project project = this.projectServiceImpl.getProjectById(projectId);
//        project.setOwner();
        return project;
    }

    @PostMapping ("")
    @JsonView(JsonViews.Retrieve.class)
    @Operation(summary = "새로운 프로젝트를 만듭니다", responses = {
        @ApiResponse(description = "created", responseCode = "201", content = @Content(mediaType = "application/json"))
    })
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
    @Operation(summary = "해당 id 를 가진 프로젝트의 이름을 수정합니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json"))
    })
    public Project updateProjectById(@PathVariable int projectId, @RequestBody ProjectPatchRequest data) {
        Project updatedProject = projectServiceImpl.partialUpdateProject(projectId, data);
        return updatedProject;
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "해당 id 를 가진 프로젝트를 삭제합니다. 하위 모든 정보들도 삭제됩니다", responses = {
        @ApiResponse(description = "no content", responseCode = "204", content = @Content)
    })
    public ResponseEntity<Void> deleteProjectById(@PathVariable int projectId) throws NotFoundException {
        projectServiceImpl.deleteProjectById(projectId);
        return ResponseEntity.noContent().build();
    }
}
