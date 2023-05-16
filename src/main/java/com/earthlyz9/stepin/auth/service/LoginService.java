package com.earthlyz9.stepin.auth.service;

import com.earthlyz9.stepin.auth.CustomUserDetails;
import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
    private final UserServiceImpl userServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String email) throws NotFoundException {
        User user;

        user = userServiceImpl.getUserByEmail(email);

        return new CustomUserDetails(
            user.getId(),
            user.getEmail(),
            user.getPassword(),
            user.getRole().name(),
            user
        );
    }
}
