package com.earthlyz9.stepin.oauth2.handler;

import com.earthlyz9.stepin.exceptions.ConflictException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException {

        System.out.println("실패");
        System.out.println(exception.getMessage());

        response.setContentType("application/json;charset=UTF-8");

        if (exception.getCause().getClass() == ConflictException.class) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"code\": 409,\"message\": \"" + exception.getCause().getMessage() + "\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"code\": 500,\"message\":\"Something went wrong, please try again\"}");

        }
    }
}
