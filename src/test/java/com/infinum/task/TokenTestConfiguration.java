package com.infinum.task;

import static org.mockito.ArgumentMatchers.any;

import com.infinum.task.security.model.TokenAuthenticatedUser;
import com.infinum.task.security.TokenAuthenticationEntryPoint;
import com.infinum.task.security.service.TokenService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TokenTestConfiguration {

  @Bean
  public TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint() {
    return Mockito.mock(TokenAuthenticationEntryPoint.class);
  }

  @Bean
  public TokenService tokenService() {
    final TokenService tokenService = Mockito.mock(TokenService.class);
    Mockito.when(tokenService.isNotExpired(any(String.class)))
        .thenReturn(true);

    Mockito.when(tokenService.createTokenUser(any(String.class)))
        .thenReturn(TokenAuthenticatedUser.builder()
            .userId(1L)
            .email("test@test.hr")
            .build());
    return tokenService;
  }
}
