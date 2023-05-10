package com.earthlyz9.stepin.controllers;

import com.earthlyz9.stepin.assemblers.StepResourceAssembler;
import com.earthlyz9.stepin.dto.step.StepCreateRequest;
import com.earthlyz9.stepin.dto.step.StepDto;
import com.earthlyz9.stepin.dto.step.StepProjectDto;
import com.earthlyz9.stepin.dto.step.StepProjectIdDto;
import com.earthlyz9.stepin.entities.Step;
import com.earthlyz9.stepin.dto.step.StepPatchRequest;
import com.earthlyz9.stepin.exceptions.ExceptionResponse;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.exceptions.PermissionDeniedException;
import com.earthlyz9.stepin.exceptions.ValidationExceptionReponse;
import com.earthlyz9.stepin.services.StepServiceImpl;
import com.earthlyz9.stepin.utils.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Tag(name = "Step", description = "프로젝트 하위의 단계")
public class StepController {

    private final StepServiceImpl stepServiceImpl;
    private final StepResourceAssembler assembler;

    @Autowired
    public StepController(StepServiceImpl stepServiceImpl, StepResourceAssembler assembler) {
        this.stepServiceImpl = stepServiceImpl;
        this.assembler = assembler;
    }

    @PostMapping("/projects/{projectId}/steps")
    @Operation(summary = "해당 id 를 가진 프로젝트 하위에 새로운 스텝을 추가합니다",
        responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = StepProjectDto.class))),
        @ApiResponse(description = "validation error", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationExceptionReponse.class))),
        @ApiResponse(description = "project with the provided id does not exist", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public ResponseEntity<EntityModel<StepDto>> createStepUnderProject(@PathVariable int projectId, @Valid @RequestBody
    StepCreateRequest step) throws NotFoundException, PermissionDeniedException {
        int requestUserId = AuthUtils.getRequestUserId();
        Step newStep = stepServiceImpl.createStep(step, projectId, requestUserId);

        if (newStep.getProject().getOwnerId() != requestUserId) throw new PermissionDeniedException();

        StepProjectDto dto = StepProjectDto.toDto(newStep);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/steps/" + dto.getId())
            .buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(location).body(assembler.toModel(dto));
    }

    @GetMapping("/projects/{projectId}/steps")
    @Operation(summary = "해당 프로젝트 id 하위의 모든 스텝을 가져옵니다 ", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json"))
    })
    public CollectionModel<EntityModel<StepDto>> getAllSteps(@PathVariable int projectId) {
        List<Step> steps = stepServiceImpl.getStepsByProjectId(projectId);
        List<StepProjectIdDto> collection = steps.stream()
            .map(StepProjectIdDto::toDto).toList();
        return assembler.toCollectionModel(collection);
    }

    @GetMapping("/steps/{stepId}")
    @Operation(summary = "해당 id 를 가진 스텝을 가져옵니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json")),
        @ApiResponse(description = "not found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public EntityModel<StepDto> getStepById(@PathVariable int stepId) throws NotFoundException {
        Step step = stepServiceImpl.getStepById(stepId);

        return assembler.toModel(StepProjectDto.toDto(step));
    }

    @PatchMapping("/steps/{stepId}")
    @Operation(summary = "해당 id 를 가진 스텝의 이름을 수정합니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json"))
    })
    public EntityModel<StepDto> updateStepById(@PathVariable int stepId, @Valid @RequestBody
    StepPatchRequest data) throws NotFoundException {
        Step updatedStep = stepServiceImpl.partialUpdateStep(stepId, data);

        return assembler.toModel(StepProjectDto.toDto(updatedStep));
    }

    @DeleteMapping("/steps/{stepId}")
    @Operation(summary = "해당 id 를 가진 스텝을 삭제합니다. 관련된 모든 하위 정보들도 삭제됩니다", responses = {
        @ApiResponse(description = "ok", responseCode = "204"),
        @ApiResponse(description = "not found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
    })
    public ResponseEntity<Void> deleteStepById(@PathVariable int stepId) throws NotFoundException {
        int requestUserId = AuthUtils.getRequestUserId();
        Step targetStep = stepServiceImpl.getStepById(stepId);

        if (requestUserId != targetStep.getOwnerId()) throw new PermissionDeniedException();

        stepServiceImpl.deleteStepById(stepId);
        return ResponseEntity.noContent().build();
    }
}
