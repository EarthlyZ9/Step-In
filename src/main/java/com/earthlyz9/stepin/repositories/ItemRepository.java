package com.earthlyz9.stepin.repositories;

import com.earthlyz9.stepin.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {

}
