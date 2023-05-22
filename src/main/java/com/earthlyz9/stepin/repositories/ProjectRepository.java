package com.earthlyz9.stepin.repositories;

import com.earthlyz9.stepin.entities.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByOwnerIdOrderByUpdatedAtDesc(Integer ownerId);
    List<Project> findByOwnerId(Integer ownerId);
}
