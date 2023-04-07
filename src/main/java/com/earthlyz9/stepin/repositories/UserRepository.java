package com.earthlyz9.stepin.repositories;

import com.earthlyz9.stepin.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>  {
    User findByEmail(String username);
}
