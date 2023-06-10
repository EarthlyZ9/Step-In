package com.earthlyz9.stepin.oauth2.service;

import com.earthlyz9.stepin.entities.SocialProviderType;
import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.exceptions.ConflictException;
import com.earthlyz9.stepin.exceptions.InternalServerErrorException;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.oauth2.OAuthAttributes;
import com.earthlyz9.stepin.services.UserServiceImpl;
import java.net.URI;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class OAuth2Service {

    private final ClientRegistration clientRegistration;
    private final RestTemplate restTemplate;

    private final UserServiceImpl userServiceImpl;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    public OAuth2Service(ClientRegistration clientRegistration, UserServiceImpl userServiceImpl) {
        this.clientRegistration = clientRegistration;
        this.restTemplate = new RestTemplate();
        this.userServiceImpl = userServiceImpl;
    }

    public String obtainAccessToken(String authorizationCode) {
        ResponseEntity<Map<String, Object>> responseEntity = this.getAccessTokenResponseEntity(authorizationCode);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = responseEntity.getBody();
            return (String) responseBody.get("access_token");
        } else {
            // Access Token을 가져오지 못한 경우에 대한 예외 처리
            throw new RuntimeException("Failed to obtain access token.");
        }
    }

    public User getUser(String accessToken) throws ConflictException {
        Map<String, Object> userAttributes = getUserInfoAttributes(accessToken);
        OAuthAttributes oAuthAttributes = getOAuthAttributes(userAttributes);
        SocialProviderType socialType = getSocialProviderType(clientRegistration.getRegistrationId());

        User user;

        try {
            user = userServiceImpl.getUserBySocialProvider(socialType,
                oAuthAttributes.getOauth2UserInfo().getId());
        } catch (NotFoundException e) {
            return saveUser(oAuthAttributes, socialType);
        }
        return user;
    }

    private ResponseEntity<Map<String, Object>>  getAccessTokenResponseEntity(String authorizationCode) {
        String  tokenUri = clientRegistration.getProviderDetails().getTokenUri();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tokenUri);
        URI uri = builder.build().toUri();

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add(OAuth2ParameterNames.GRANT_TYPE, clientRegistration.getAuthorizationGrantType().getValue());
        parameters.add(OAuth2ParameterNames.REDIRECT_URI, clientRegistration.getRedirectUri());
        parameters.add(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());
        parameters.add(OAuth2ParameterNames.CODE, authorizationCode);
        parameters.add(OAuth2ParameterNames.CLIENT_SECRET, clientRegistration.getClientSecret());

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, headers);
        try {
            return restTemplate.exchange(uri, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            throw new InternalServerErrorException();
        }
    }

    private ResponseEntity<Map<String, Object>> getUserInfoResponseEntity(String accessToken) {
        String userInfoEndpointUri = clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(userInfoEndpointUri);
        URI uri = builder.build().toUri();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        // GET 요청
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> responseEntity = new RestTemplate().exchange(uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});
        return responseEntity;
    }

    private Map<String, Object> getUserInfoAttributes(String accessToken) {
        ResponseEntity<Map<String, Object>> responseEntity = this.getUserInfoResponseEntity(accessToken);

        Map<String, Object> attributes;

        // 응답에서 사용자 정보 추출
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            attributes =  responseEntity.getBody();
        } else {
            // 사용자 정보를 가져오지 못한 경우에 대한 예외 처리
            throw new RuntimeException("Failed to retrieve user info.");
        }

        return attributes;
    }

    private OAuthAttributes getOAuthAttributes(Map<String, Object> attributes) {
        SocialProviderType socialType = getSocialProviderType(clientRegistration.getRegistrationId());
        String userNameAttributeName = clientRegistration
            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);
        return extractAttributes;
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

    /**
     * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 User 객체 생성 후 반환
     * 생성된 User 객체를 DB에 저장 : socialType, socialId, email, role 값만 있는 상태
     */
    private User saveUser(OAuthAttributes attributes, SocialProviderType socialType) throws ConflictException {
        User user = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        User createdSocialUser;

        try {
            System.out.println(user.getEmail());
            User existingUser = userServiceImpl.getUserByEmail(user.getEmail());
            if (existingUser.getSocialId() == null)
                throw new ConflictException("This email is already registered via basic sign up");

            throw new ConflictException(
                "This email is already registered under social provider: "
                    + existingUser.getSocialProviderType().name());

        } catch (NotFoundException e) {
            createdSocialUser = userServiceImpl.createSocialUser(user);
        }

        return createdSocialUser;
    }
}
