package io.factorialsystems.msscstore21users.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;

@Slf4j
public class JwtTokenWrapper {
    private static final String SYSTEM_NAME = "__not_logged_in__";
    private static final String SYSTEM_EMAIL = "notloggedin@factorialsystems.io";

    private static Map<String, Object> getClaims () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? ((Jwt)authentication.getPrincipal()).getClaims() : null;
    }

    public static String getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? ((Jwt)authentication.getPrincipal()).getTokenValue() : null;
    }

    public static String getUserName() {
        Map<String, Object> claims = getClaims();
        return claims == null ?  SYSTEM_NAME : (String) claims.get("user");
    }

    public static String getTenant() {
        Map<String, Object> claims = getClaims();
        return claims != null ? (String) claims.get("tenant") : null;
    }

}
