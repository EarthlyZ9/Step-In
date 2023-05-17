package com.earthlyz9.stepin.controllers;

import com.earthlyz9.stepin.assemblers.ProjectResourceAssembler;
import com.earthlyz9.stepin.dto.project.AbstractProjectDto;
import com.earthlyz9.stepin.dto.project.ProjectDto;
import com.earthlyz9.stepin.dto.project.SimpleProjectDto;
import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.dto.project.ProjectPatchRequest;
import com.earthlyz9.stepin.exceptions.ExceptionResponse;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.exceptions.PermissionDeniedException;
import com.earthlyz9.stepin.exceptions.ValidationExceptionResponse;
import com.earthlyz9.stepin.services.ProjectServiceImpl;
import com.earthlyz9.stepin.utils.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

@Tag(name = "Project", description = "유저가 생성한 프로젝트")
@RestController
@SecurityRequirement(name = "Bearer Token")
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectServiceImpl projectServiceImpl;
    private final ProjectResourceAssembler assembler;

    @Autowired
    public ProjectController(ProjectServiceImpl projectServiceImpl, ProjectResourceAssembler assembler) {
        this.projectServiceImpl = projectServiceImpl;
        this.assembler = assembler;
    }

    @GetMapping("")
    @Operation(summary = "요청을 보내는 유저의 모든 프로젝트를 가져옵니다", responses = {
        @ApiResponse(
            description = "ok",
            responseCode = "200",
            content = @Content(
                mediaType = "application/hal+json",
                array = @ArraySchema(
                    schema = @Schema(implementation = SimpleProjectDto.class)
                )
            )
        )
    })
    public CollectionModel<EntityModel<AbstractProjectDto>> getAllProjects() {
        int requestUserId = AuthUtils.getRequestUserId();

        List<Project> projects = projectServiceImpl.getProjectsByOwnerId(requestUserId);
        List<SimpleProjectDto> collection = projects.stream()
            .map(SimpleProjectDto::toDto).toList();
        return assembler.toCollectionModel(collection);
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "해당 id 의 프로젝트를 가져옵니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/hal+json", schema=@Schema(implementation = ProjectDto.class ))),
        @ApiResponse(description = "not found", responseCode = "404", content = @Content(mediaType = "application/json", schema=@Schema(implementation = ExceptionResponse.class)))
    })
    public EntityModel<AbstractProjectDto> getProjectById(@PathVariable int projectId) throws NotFoundException, PermissionDeniedException {
        Project project = this.projectServiceImpl.getProjectById(projectId);
        project.checkPermission(AuthUtils.getRequestUserId());

        ProjectDto projectOwnerDto = ProjectDto.toDto(project);
        return assembler.toModel(projectOwnerDto);
    }

    @PostMapping ("")
    @Operation(summary = "새로운 빈 프로젝트를 만듭니다", responses = {
        @ApiResponse(description = "created", responseCode = "201", content = @Content(mediaType = "application/hal+json")),
        @ApiResponse(description = "validation error", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationExceptionResponse.class))),
        @ApiResponse(description = "Guest user can only create one project", responseCode = "409", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
    })
    public ResponseEntity<EntityModel<AbstractProjectDto>> createProject() {
        int requestUserId = AuthUtils.getRequestUserId();
        Project newProject = this.projectServiceImpl.createEmptyProject(requestUserId);

        ProjectDto projectOwnerDto = ProjectDto.toDto(newProject);
        projectOwnerDto.setOwner(AuthUtils.getRequestUser());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{projectId}")
            .buildAndExpand(projectOwnerDto.getId()).toUri();

        return ResponseEntity.created(location).body(assembler.toModel(projectOwnerDto));
    }

    @PatchMapping("/{projectId}")
    @Operation(summary = "해당 id 를 가진 프로젝트의 이름을 수정합니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json")),
        @ApiResponse(description = "not found", responseCode = "404", content = @Content(mediaType = "application/json"))
    })
    public EntityModel<AbstractProjectDto> updateProjectById(@PathVariable int projectId,
        @RequestBody ProjectPatchRequest data) throws NotFoundException, PermissionDeniedException {
        Project targetProject = projectServiceImpl.getProjectById(projectId);
        targetProject.checkPermission(AuthUtils.getRequestUserId());

        Project updatedProject = projectServiceImpl.partialUpdateProject(targetProject, data);
        ProjectDto projectOwnerDto = ProjectDto.toDto(updatedProject);
        return assembler.toModel(projectOwnerDto);
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "해당 id 를 가진 프로젝트를 삭제합니다. 하위 모든 정보들도 삭제됩니다", responses = {
        @ApiResponse(description = "ok", responseCode = "204"),
        @ApiResponse(description = "not found", responseCode = "404", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteProjectById(@PathVariable int projectId) throws NotFoundException {
        Project targetProject = projectServiceImpl.getProjectById(projectId);
        targetProject.checkPermission(AuthUtils.getRequestUserId());

        projectServiceImpl.deleteProjectById(targetProject.getId());
        return ResponseEntity.noContent().build();
    }
}
