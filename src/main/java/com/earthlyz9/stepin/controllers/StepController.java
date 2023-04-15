package com.earthlyz9.stepin.controllers;

import com.earthlyz9.stepin.JsonViews;
import com.earthlyz9.stepin.entities.Step;
import com.earthlyz9.stepin.entities.StepPatchRequest;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.services.StepServiceImpl;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Tag(name = "Step", description = "프로젝트 하위의 단계")
public class StepController {

    private final StepServiceImpl stepServiceImpl;

    @Autowired
    public StepController(StepServiceImpl stepServiceImpl) {
        this.stepServiceImpl = stepServiceImpl;
    }

    @PostMapping("/projects/{projectId}/steps")
    @JsonView(JsonViews.Retrieve.class)
    @Operation(summary = "해당 id 를 가진 프로젝트 하위에 새로운 스텝을 추가합니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Step> createStepUnderProject(@PathVariable int projectId, @RequestBody Step step) throws NotFoundException {
        Step newStep = stepServiceImpl.createStep(step, projectId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/steps/" + newStep.getId())
            .buildAndExpand(newStep.getId()).toUri();
        return ResponseEntity.created(location).body(newStep);
    }

    @GetMapping("/projects/{projectId}/steps")
    @JsonView(JsonViews.List.class)
    @Operation(summary = "해당 프로젝트 id 하위의 모든 스텝을 가져옵니다 ", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json"))
    })
    public List<Step> getAllSteps(@PathVariable int projectId) {
        return stepServiceImpl.getStepsByProjectId(projectId);
    }

    @GetMapping("/steps/{stepId}")
    @JsonView(JsonViews.Retrieve.class)
    @Operation(summary = "해당 id 를 가진 스텝을 가져옵니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json"))
    })
    public Step getStepById(@PathVariable int stepId) throws NotFoundException {
        return stepServiceImpl.getStepById(stepId);
    }

    @PatchMapping("/steps/{stepId}")
    @JsonView(JsonViews.Retrieve.class)
    @Operation(summary = "해당 id 를 가진 스텝의 이름을 수정합니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json"))
    })
    public Step updateStepById(@PathVariable int stepId, @RequestBody
    StepPatchRequest data) throws NotFoundException {
        Step updatedStep = stepServiceImpl.partialUpdateStep(stepId, data);
        return updatedStep;
    }

    @DeleteMapping("/steps/{stepId}")
    @Operation(summary = "해당 id 를 가진 스텝을 삭제합니다. 관련된 모든 하위 정보들도 삭제됩니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content)
    })
    public ResponseEntity<Void> deleteStepById(@PathVariable int stepId) throws NotFoundException {
        stepServiceImpl.deleteStepById(stepId);
        return ResponseEntity.noContent().build();
    }
}
