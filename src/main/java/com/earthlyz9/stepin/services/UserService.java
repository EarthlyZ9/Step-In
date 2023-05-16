package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.entities.SocialProviderType;
import com.earthlyz9.stepin.entities.User;

public interface UserService {

    User getUserById(Integer userId);

    User getUserByEmail(String email);

    User getUserBySocialProvider(SocialProviderType socialProviderType, String socialId);

    User createSocialUser(User newUser);

    User createBasicUser(User newUser);

    User createGuestUser();

    void deleteGuestUserById(Integer userId);

}
