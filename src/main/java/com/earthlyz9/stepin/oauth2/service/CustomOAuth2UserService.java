package com.earthlyz9.stepin.oauth2.service;

import com.earthlyz9.stepin.entities.SocialProviderType;
import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.exceptions.ConflictException;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.oauth2.CustomOAuth2User;
import com.earthlyz9.stepin.oauth2.OAuthAttributes;
import com.earthlyz9.stepin.services.UserServiceImpl;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserServiceImpl userServiceImpl;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException, ConflictException {
        System.out.println("OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // OAuth 서비스에서 가져온 유저 정보를 담고 있음

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // registrationId = google or kakao or naver
        SocialProviderType socialType = getSocialProviderType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)

        // socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        User createdUser;
        try {
            createdUser = getUser(extractAttributes, socialType);
        } catch (ConflictException e) {
            throw new OAuth2AuthenticationException(new OAuth2Error("409"), e);
        }

        // DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성해서 반환
        return new CustomOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
            attributes,
            extractAttributes.getNameAttributeKey(), // userNameAttributeName
            createdUser.getId(),
            createdUser.getEmail(),
            createdUser.getRole()
        );
    }

    private SocialProviderType getSocialProviderType(String registrationId) {
        if(NAVER.equals(registrationId)) {
            return SocialProviderType.NAVER;
        }
        if(KAKAO.equals(registrationId)) {
            return SocialProviderType.KAKAO;
        }
        return SocialProviderType.GOOGLE;
    }

    private User getUser(OAuthAttributes attributes, SocialProviderType socialType) throws ConflictException {
        User user;

        try {
            user = userServiceImpl.getUserBySocialProvider(socialType,
                attributes.getOauth2UserInfo().getId());
        } catch (NotFoundException e) {
            return saveUser(attributes, socialType);
        }
        return user;
    }

    /**
     * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 User 객체 생성 후 반환
     * 생성된 User 객체를 DB에 저장 : socialType, socialId, email, role 값만 있는 상태
     */
    private User saveUser(OAuthAttributes attributes, SocialProviderType socialType) throws ConflictException {
        User user = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        User createdSocialUser = null;

        try {
            System.out.println(attributes.getOauth2UserInfo());
            User existingUser = userServiceImpl.getUserByEmail(user.getEmail());
            System.out.println(existingUser.getEmail());
            if (existingUser.getSocialId() == null)
                throw new ConflictException("This email is already registered via basic sign up");

            // 카카오 소셜 로그인 + 이메일 제공했을 때 -> 이미 해당 이메일로 다른 소셜 로그인을 했을 수도 있음
            if (socialType == SocialProviderType.KAKAO && !existingUser.getEmail()
                .contains("@socialUser.com")) {
                throw new ConflictException(
                    "This email is already registered under social provider: "
                        + existingUser.getSocialProviderType().name());
            }

        } catch (NotFoundException e) {
            createdSocialUser = userServiceImpl.createSocialUser(user);
        }

        return createdSocialUser;
    }
}
