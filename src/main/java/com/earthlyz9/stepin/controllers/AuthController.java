package com.earthlyz9.stepin.controllers;

import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.dto.UserSignupRequest;
import com.earthlyz9.stepin.exceptions.ValidationError;
import com.earthlyz9.stepin.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증과 관련된 동작")
public class AuthController {
    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder) {
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/basic/sign-up")
    public User basicSignUp(@RequestBody UserSignupRequest userSignupRequest) throws ValidationError {
        if (!userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword()))
            throw new ValidationError("Password does not match confirmation password");

        String encodedPassword = passwordEncoder.encode(userSignupRequest.getPassword());

        User newUser = userSignupRequest.toEntity(encodedPassword);

        User savedUser = userServiceImpl.createBasicUser(newUser);

        return savedUser;
    }
}
