package com.earthlyz9.stepin.repositories;

import com.earthlyz9.stepin.entities.SocialProviderType;
import com.earthlyz9.stepin.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>  {
    Optional<User> findByEmail(String username);

    Optional<User> findBySocialProviderTypeAndSocialId(SocialProviderType socialType, String socialId);
}
