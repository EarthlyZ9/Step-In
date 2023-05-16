package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.entities.SocialProviderType;
import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.entities.UserRole;
import com.earthlyz9.stepin.exceptions.DuplicateInstanceException;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.repositories.UserRepository;
import com.earthlyz9.stepin.utils.UserUtils;
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

    @Override
    public User getUserByEmail(String email) throws NotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new NotFoundException("user with the provided username does not exist");
        }
        return user.get();
    }

    @Override
    public User getUserBySocialProvider(SocialProviderType socialProviderType, String socialId) throws NotFoundException {
        Optional<User> user = userRepository.findBySocialProviderTypeAndSocialId(socialProviderType, socialId);
        if (user.isEmpty()) throw new NotFoundException("user with the provided social id and social provider type does not exist");
        return user.get();

    }

    @Override
    public User createSocialUser(User newUser) {
        newUser.setId(0);
        newUser.setRole(UserRole.USER);
        newUser.setIsActive(true);
        User savedUser = userRepository.save(newUser);
        return savedUser;
    }

    @Override
    public User createBasicUser(User newUser) throws DuplicateInstanceException {
        try {
            User existingUser = getUserByEmail(newUser.getEmail());
            if (existingUser.getSocialId() != null) {
                throw new DuplicateInstanceException("This account is already registered as a social account");
            } else {
                throw new DuplicateInstanceException("Account with the provided email already exists");
            }
        } catch (NotFoundException e) {
            newUser.setId(0);
            newUser.setRole(UserRole.USER);
            newUser.setIsActive(true);
            User savedUser = userRepository.save(newUser);
            return savedUser;
        }
    }

    @Override
    public User createGuestUser() {
        User newUser = new User();
        newUser.setId(0);
        newUser.setRole(UserRole.GUEST);
        newUser.setIsActive(true);
        newUser.setNickname("Guest");
        newUser.setEmail(UserUtils.generateGuestTempEmail());
        return userRepository.save(newUser);
    }
}
