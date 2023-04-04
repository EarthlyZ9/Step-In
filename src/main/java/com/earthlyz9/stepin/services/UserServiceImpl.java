package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new NotFoundException("Project with the provided id doesn't exist");
        return user.get();
    }
}
