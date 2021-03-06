package com.infinum.task;

import com.infinum.task.helper.JsonHelper;
import com.infinum.task.security.exception.NonMatchingPasswordException;
import com.infinum.task.security.exception.UserNotFoundException;
import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.security.web.LoginController;
import com.infinum.task.security.web.facade.LoginServiceFacade;
import com.infinum.task.user.model.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = TokenTestConfiguration.class)
class LoginControllerTest {

    @MockBean
    private LoginServiceFacade loginServiceFacade;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Login test")
    void loginTest() throws Exception {
        final String email = "test@test.hr";

        final UserDTO user = UserDTO.builder()
                .email(email)
                .build();

        final UserCredentials userCredentials = UserCredentials.builder()
                .email(email)
                .password("password")
                .build();

        Assertions.assertEquals(email, userCredentials.getEmail());

        Mockito.when(loginServiceFacade.login(Mockito.eq(userCredentials), Mockito.any()))
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

        final UserNotFoundException userNotFoundException = new UserNotFoundException();
        final NonMatchingPasswordException nonMatchingPasswordException = new NonMatchingPasswordException();
        Mockito.when(loginServiceFacade.login(Mockito.eq(userCredentials), Mockito.any()))
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
