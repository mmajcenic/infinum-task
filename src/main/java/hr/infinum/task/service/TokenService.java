package hr.infinum.task.service;

import hr.infinum.task.model.User;
import hr.infinum.task.security.TokenAuthenticatedUser;
import javax.servlet.http.Cookie;

public interface TokenService {

  String generateToken(User user);

  Cookie createCookie(User user);

  TokenAuthenticatedUser createTokenUser(String token);

  boolean isNotExpired(String token);

}
