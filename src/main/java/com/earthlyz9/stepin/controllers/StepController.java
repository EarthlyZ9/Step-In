package com.earthlyz9.stepin.controllers;

import com.earthlyz9.stepin.JsonViews;
import com.earthlyz9.stepin.entities.Step;
import com.earthlyz9.stepin.entities.StepPatchRequest;
import com.earthlyz9.stepin.entities.Item;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.services.StepServiceImpl;
import com.earthlyz9.stepin.services.ItemServiceImpl;
import com.fasterxml.jackson.annotation.JsonView;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/steps")
@Tag(name = "Step", description = "프로젝트 하위의 단계")
public class StepController {

    private final StepServiceImpl stepServiceImpl;
    private final ItemServiceImpl itemServiceImpl;

    @Autowired
    public StepController(StepServiceImpl stepServiceImpl, ItemServiceImpl itemServiceImpl) {
        this.stepServiceImpl = stepServiceImpl;
        this.itemServiceImpl = itemServiceImpl;
    }

    @GetMapping("")
    @JsonView(JsonViews.List.class)
    public List<Step> getAllSteps() {
        return stepServiceImpl.getSteps();
    }

    @GetMapping("/{stepId}")
    @JsonView(JsonViews.Retrieve.class)
    public Step getStepById(@PathVariable int stepId) throws NotFoundException {
        return stepServiceImpl.getStepById(stepId);
    }

    @PatchMapping("/{stepId}")
    @JsonView(JsonViews.Retrieve.class)
    public Step updateStepById(@PathVariable int stepId, @RequestBody
    StepPatchRequest data) throws NotFoundException {
        Step updatedStep = stepServiceImpl.partialUpdateStep(stepId, data);
        return updatedStep;
    }

    @DeleteMapping("/{stepId}")
    public ResponseEntity<Void> deleteStepById(@PathVariable int stepId) throws NotFoundException {
        stepServiceImpl.deleteStepById(stepId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{stepId}/items")
    @JsonView(JsonViews.Retrieve.class)
    public ResponseEntity<Item> createItem(@PathVariable int stepId, @RequestBody Item item) throws NotFoundException {
        Item newItem = itemServiceImpl.createItem(item, stepId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{itemId}")
            .buildAndExpand(newItem.getId()).toUri();
        return ResponseEntity.created(location).body(newItem);
    }

}
