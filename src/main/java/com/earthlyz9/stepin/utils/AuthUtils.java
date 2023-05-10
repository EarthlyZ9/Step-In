package com.earthlyz9.stepin.utils;

import com.earthlyz9.stepin.auth.CustomUserDetails;
import com.earthlyz9.stepin.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    public static User getRequestUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return currentUserDetails.getUserObj();
    }

    public static int getRequestUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return currentUserDetails.getId();
    }

    public static String getRequestUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
