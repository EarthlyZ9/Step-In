package com.earthlyz9.stepin.oauth2.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        if (exception instanceof OAuth2AuthenticationException) {
            if (((OAuth2AuthenticationException) exception).getError().getErrorCode()
                .equals("409")) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write(
                    "{\"code\": 409,\"message\": \"" + exception.getMessage()
                        + "\", \"timestamp\": \"" + LocalDateTime.now() + "\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(
                    "{\"code\": 500,\"message\":\"Something went wrong, please try again\", \"timestamp\": \""
                        + LocalDateTime.now() + "\"}");

            }
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(
                "{\"code\": 500,\"message\":\"Something went wrong, please try again\", \"timestamp\": \""
                    + LocalDateTime.now() + "\"}");
        }
    }
}
