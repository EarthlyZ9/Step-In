package com.earthlyz9.stepin.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.earthlyz9.stepin.repositories.UserRepository;
import com.earthlyz9.stepin.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.name}")
    private String refreshName;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;


    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
            .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
            .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정
            .withClaim(EMAIL_CLAIM, email)
            .sign(Algorithm.HMAC512(
                secretKey));
    }

    public String createRefreshToken(String email) {
        // TODO: encrypt email claim
        Date now = new Date();
        return JWT.create()
            .withSubject(REFRESH_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
            .withClaim(EMAIL_CLAIM, email)
            .sign(Algorithm.HMAC512(secretKey));
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        CookieUtils.addCookie(response, refreshName, refreshToken,
            refreshTokenExpirationPeriod.intValue() / 1000, false, true);
    }


    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        Optional<Cookie> cookie =  CookieUtils.getCookie(request, refreshName);
        return cookie.map(Cookie::getValue);
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
            .filter(refreshToken -> refreshToken.startsWith(BEARER))
            .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractEmail(String token) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                .build() // 반환된 빌더로 JWT verifier 생성
                .verify(token) // accessToken을 검증하고 유효하지 않다면 예외 발생
                .getClaim(EMAIL_CLAIM) // claim(Emial) 가져오기
                .asString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
