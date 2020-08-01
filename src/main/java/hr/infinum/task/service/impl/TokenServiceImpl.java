package hr.infinum.task.service.impl;

import hr.infinum.task.model.User;
import hr.infinum.task.security.TokenAuthenticatedUser;
import hr.infinum.task.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

  public static final String TOKEN_COOKIE_NAME = "application_jwt";

  private static final String USER_ID_CLAIM = "user_id";

  private final long validity;

  private final String secret;

  public TokenServiceImpl(@Value("${app.token.validity:3600000}") final long validity,
      @Value("${app.token.secret}") final String secret) {
    this.validity = validity;
    this.secret = secret;
  }

  @Override
  public String generateToken(final User user) {
    final var claims = new HashMap<String, Object>();
    claims.put(USER_ID_CLAIM, user.getId());
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getEmail())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + validity))
        .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  @Override
  public Cookie createCookie(final User user) {
    final var token = generateToken(user);
    final var cookie = new Cookie(TOKEN_COOKIE_NAME, token);
    cookie.setMaxAge(Math.toIntExact(validity / 1000));
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    return cookie;
  }

  @Override
  public TokenAuthenticatedUser createTokenUser(final String token) {
    final var claims = getAllClaimsFromToken(token);
    return TokenAuthenticatedUser.builder()
        .email(getEmailFromClaims(claims))
        .userId(getUserIdFromClaims(claims))
        .build();
  }

  @Override
  public boolean isNotExpired(final String token) {
    return getExpirationDateFromToken(token).after(new Date());
  }

  private Date getExpirationDateFromToken(final String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }


  private String getEmailFromClaims(final Claims claims) {
    return claims.getSubject();
  }

  private Long getUserIdFromClaims(final Claims claims) {
    return claims.get(USER_ID_CLAIM, Long.class);
  }


  private <T> T getClaimFromToken(final String token,
      final Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(final String token) {
    return Jwts.parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .getBody();
  }

}
