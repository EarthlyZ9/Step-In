package com.earthlyz9.stepin.repositories;

import com.earthlyz9.stepin.entities.Step;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepRepository extends JpaRepository<Step, Integer> {
    List<Step> findByProjectId(Integer projectId);
}
