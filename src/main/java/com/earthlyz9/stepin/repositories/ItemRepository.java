package com.earthlyz9.stepin.repositories;

import com.earthlyz9.stepin.entities.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByStepId(Integer stepId);
}
