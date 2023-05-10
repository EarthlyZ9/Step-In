package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.dto.step.StepCreateRequest;
import com.earthlyz9.stepin.entities.Step;
import com.earthlyz9.stepin.dto.step.StepPatchRequest;
import java.util.List;

public interface StepService {
    List<Step> getStepsByProjectId(Integer projectId);

    Step getStepById(Integer stepId);

    Step createStep(StepCreateRequest newStep, Integer projectId);

    Step partialUpdateStep(Integer stepId, StepPatchRequest newStep);

    void deleteStepById(Integer stepId);

}
