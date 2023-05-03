package com.earthlyz9.stepin.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

public class CookieUtils {

//    private static final String COOKIE_DOMAIN = null;

    /**
     * Get specific cookie by name
     * @param request
     * @param name
     * @return optional cookie
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Add cookie
     * @param response
     * @param name name of the cookie
     * @param value value of the cookie
     * @param maxAge max age in seconds
     * @param isSecure secure option
     * @param isHttpOnly http-only cookie
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, boolean isSecure, boolean isHttpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(isHttpOnly);
        cookie.setSecure(isSecure);

        // TODO: add Domain if needed
        response.addCookie(cookie);
    }

    /**
     * Get the target cookie and empty its value
     * @param request
     * @param response
     * @param name
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }
}
