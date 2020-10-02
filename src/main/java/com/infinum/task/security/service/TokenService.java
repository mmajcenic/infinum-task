package com.infinum.task.security.service;

import com.infinum.task.security.model.TokenAuthenticatedUser;
import com.infinum.task.user.model.User;
import javax.servlet.http.Cookie;

public interface TokenService {

    String generateToken(User user);

    Cookie createCookie(User user);

    TokenAuthenticatedUser createTokenUser(String token);

    boolean isNotExpired(String token);

}
