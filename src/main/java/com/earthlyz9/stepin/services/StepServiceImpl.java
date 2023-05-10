package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.dto.step.StepCreateRequest;
import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.entities.Step;
import com.earthlyz9.stepin.dto.step.StepPatchRequest;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.exceptions.PermissionDeniedException;
import com.earthlyz9.stepin.repositories.StepRepository;
import com.earthlyz9.stepin.utils.AuthUtils;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StepServiceImpl implements StepService {

    private final StepRepository stepRepository;
    private final ProjectServiceImpl projectServiceImpl;

    @Autowired
    public StepServiceImpl(StepRepository stepRepository, ProjectServiceImpl projectServiceImpl) {
        this.stepRepository = stepRepository;
        this.projectServiceImpl = projectServiceImpl;
    }

    @Override
    public List<Step> getStepsByProjectId(Integer projectId) throws NotFoundException, PermissionDeniedException {
        Project project = projectServiceImpl.getProjectById(projectId);
        if (project.getOwnerId() != AuthUtils.getRequestUserId()) throw new PermissionDeniedException();
        return stepRepository.findByProjectId(projectId);
    }

    @Override
    public Step getStepById(Integer stepId) {
        Optional<Step> step = stepRepository.findById(stepId);
        if (step.isEmpty()) {
            throw new NotFoundException("step with the provided id does not exist");
        }
        return step.get();
    }

    @Override
    @Transactional
    public Step createStep(StepCreateRequest newStep, Integer projectId)
        throws NotFoundException, ValidationException, PermissionDeniedException {
        Project project = projectServiceImpl.getProjectById(projectId);
        int requestUserId = AuthUtils.getRequestUserId();

        if (!Objects.equals(project.getOwnerId(), requestUserId)) {
            throw new PermissionDeniedException();
        }

        int stepCount = stepRepository.findAll().size();

        if (stepCount == 10) {
            throw new ValidationException();
        }

        newStep.setId(0);
        newStep.setProjectId(projectId);
        newStep.setOwnerId(requestUserId);
        newStep.setNumber(stepCount + 1);
        Step step = StepCreateRequest.toEntity(newStep);
        Step savedStep = stepRepository.save(step);

        savedStep.setProject(project);

        return savedStep;
    }

    @Override
    @Transactional
    public Step partialUpdateStep(Integer stepId, StepPatchRequest newStep)
        throws NotFoundException, PermissionDeniedException {
        Step object = getStepById(stepId);
        if (object.getOwnerId() != AuthUtils.getRequestUserId()) throw new PermissionDeniedException();
        object.setName(newStep.getName());
        return stepRepository.save(object);
    }

    @Override
    @Transactional
    public void deleteStepById(Integer stepId) throws NotFoundException {
        stepRepository.deleteById(stepId);
    }
}
