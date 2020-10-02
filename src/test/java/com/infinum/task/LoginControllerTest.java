package com.infinum.task;

import static com.infinum.task.security.service.impl.TokenServiceImpl.TOKEN_COOKIE_NAME;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.infinum.task.helper.JsonHelper;
import com.infinum.task.controller.LoginController;
import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.security.exception.NonMatchingPasswordException;
import com.infinum.task.security.exception.UserNotFoundException;
import com.infinum.task.security.TokenAuthenticationEntryPoint;
import com.infinum.task.security.service.LoginService;
import com.infinum.task.security.service.TokenService;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("unused")
@WebMvcTest(LoginController.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LoginControllerTest {

  @MockBean
  private LoginService loginService;

  @MockBean
  private TokenService tokenService;

  @MockBean
  private TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Login test")
  public void loginTest() throws Exception {
    final var email = "test@test.hr";

    final var user = User.builder()
        .email(email)
        .id(1L)
        .build();

    Mockito.when(tokenService.createCookie(user))
        .thenReturn(new Cookie(TOKEN_COOKIE_NAME, "signedToken"));

    final var userCredentials = UserCredentials.builder()
        .email(email)
        .password("password")
        .build();

    Mockito.when(loginService.login(userCredentials))
        .thenReturn(user);

    mockMvc.perform(post("/api/v1/login")
        .with(csrf())
        .content(JsonHelper.toJson(userCredentials))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(email)));

    mockMvc.perform(post("/api/v1/login")
        .with(csrf())
        .content(JsonHelper.toJson(UserCredentials.builder()
            .password("password")
            .build()))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(equalTo("Email is required")));

    mockMvc.perform(post("/api/v1/login")
        .with(csrf())
        .content(JsonHelper.toJson(UserCredentials.builder()
            .email("test@test.gr")
            .build()))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(equalTo("Password is required")));

    mockMvc.perform(post("/api/v1/login")
        .with(csrf())
        .content(JsonHelper.toJson(UserCredentials.builder()
            .email("test")
            .password("password")
            .build()))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(equalTo("Invalid email")));

    final var userNotFoundException = new UserNotFoundException();
    final var nonMatchingPasswordException = new NonMatchingPasswordException();
    Mockito.when(loginService.login(userCredentials))
        .thenThrow(userNotFoundException, nonMatchingPasswordException);

    mockMvc.perform(post("/api/v1/login")
        .with(csrf())
        .content(JsonHelper.toJson(userCredentials))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string(equalTo(userNotFoundException.getMessage())));

    mockMvc.perform(post("/api/v1/login")
        .with(csrf())
        .content(JsonHelper.toJson(userCredentials))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string(equalTo(nonMatchingPasswordException.getMessage())));
  }

}
