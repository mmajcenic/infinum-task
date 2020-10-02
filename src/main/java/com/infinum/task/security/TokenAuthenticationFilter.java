package com.infinum.task.security;


import static com.infinum.task.security.service.impl.TokenServiceImpl.TOKEN_COOKIE_NAME;

import com.infinum.task.security.model.TokenAuthenticatedUser;
import com.infinum.task.security.service.TokenService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private final TokenService tokenService;

  @Override
  protected void doFilterInternal(@NonNull final HttpServletRequest httpServletRequest,
      @NonNull final HttpServletResponse httpServletResponse,
      @NonNull final FilterChain filterChain) throws IOException, ServletException {

    try {
      Optional.ofNullable(WebUtils.getCookie(httpServletRequest, TOKEN_COOKIE_NAME))
          .map(Cookie::getValue)
          .filter(tokenService::isNotExpired)
          .map(tokenService::createTokenUser)
          .ifPresentOrElse(this::initializeSecurityContext,
              SecurityContextHolder::clearContext);
    } catch (final Exception e) {
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

  private void initializeSecurityContext(final TokenAuthenticatedUser tokenAuthenticatedUser) {
    final var authenticationToken = new UsernamePasswordAuthenticationToken(tokenAuthenticatedUser,
        null,
        tokenAuthenticatedUser.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

}
