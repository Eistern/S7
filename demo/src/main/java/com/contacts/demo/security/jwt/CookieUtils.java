package com.contacts.demo.security.jwt;

import org.springframework.lang.Nullable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
    public static void saveCookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie JWTtoken = new Cookie(name, value);
        JWTtoken.setMaxAge(maxAge);
        JWTtoken.setPath("/");
        response.addCookie(JWTtoken);
    }

    @Nullable
    public static String getValueOf(String name, Cookie[] collection) {
        for (Cookie cookie : collection) {
            if (cookie.getName().equals(name))
                return cookie.getValue();
        }
        return null;
    }
}
