package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.entities.Step;
import com.earthlyz9.stepin.entities.StepPatchRequest;
import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.repositories.StepRepository;
import java.util.List;
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
    public List<Step> getStepsByProjectId(Integer projectId) {
        // TODO: valid project id?
        return stepRepository.findByProjectId(projectId);
    }

    @Override
    public Step getStepById(Integer stepId) {
        Optional<Step> step = stepRepository.findById(stepId);
        if (step.isEmpty()) throw new NotFoundException("step with the provided id does not exist");
        return step.get();
    }

    @Override
    @Transactional
    public Step createStep(Step newStep, Integer projectId) throws NotFoundException {
        int stepCount = stepRepository.findAll().size();

        Project currentProject = projectServiceImpl.getProjectById(projectId);
        newStep.setId(0);
        newStep.setProjectId(projectId);
        newStep.setNumber(stepCount + 1);
        Step step = stepRepository.save(newStep);
        step.setProject(currentProject);
        return step;
    }

    @Override
    @Transactional
    public Step partialUpdateStep(Integer stepId, StepPatchRequest newStep) throws NotFoundException {
        Step object = getStepById(stepId);
        object.setName(newStep.getName());
        return stepRepository.save(object);
    }

    @Override
    @Transactional
    public void deleteStepById(Integer stepId) throws NotFoundException{
        Step instance = getStepById(stepId);
        stepRepository.deleteById(stepId);
    }
}
