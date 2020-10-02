package com.infinum.task;

import static com.infinum.task.security.service.impl.TokenServiceImpl.TOKEN_COOKIE_NAME;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.infinum.task.configuration.TokenTestConfiguration;
import com.infinum.task.helper.JsonHelper;
import com.infinum.task.controller.UserController;
import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.user.exception.CityAlreadyFavouredException;
import com.infinum.task.user.exception.CityNotFavouredException;
import com.infinum.task.security.service.TokenService;
import com.infinum.task.user.service.UserService;
import java.util.List;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SuppressWarnings("unused")
@WebMvcTest(UserController.class)
@TestInstance(Lifecycle.PER_CLASS)
@Import(TokenTestConfiguration.class)
public class UserControllerTest {

  private static final City TEST_CITY = City.builder()
      .name("test_city")
      .build();

  @MockBean
  private UserService userService;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Test city creation")
  public void testCreateCity() throws Exception {
    final var email = "test@test.hr";
    final var password = "password";

    final var user = User.builder()
        .email(email)
        .password(password)
        .build();

    Mockito.when(tokenService.createCookie(user))
        .thenReturn(new Cookie(TOKEN_COOKIE_NAME, "signedToken"));

    Mockito.when(userService.create(user))
        .thenReturn(user);

    mockMvc.perform(userCredentialsPost(UserCredentials.builder()
        .email(email)
        .password(password)
        .build()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email").value(email));

    mockMvc.perform(userCredentialsPost(UserCredentials.builder()
        .password(password)
        .build()))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(equalTo("Email is required")));

    mockMvc.perform(userCredentialsPost(UserCredentials.builder()
        .email("invalid_mail")
        .password(password)
        .build()))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(equalTo("Invalid email")));

    mockMvc.perform(userCredentialsPost(UserCredentials.builder()
        .email(email)
        .build()))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(equalTo("Password is required")));
  }

  @Test
  @DisplayName("Test add favourite city")
  public void testAddFavouriteCity() throws Exception {
    final var id = 1L;
    Mockito.when(userService.addFavouriteCity(id))
        .thenReturn(List.of(City.builder()
            .id(id)
            .name("test")
            .description("test")
            .favouriteCount(1)
            .population(10)
            .build()));

    mockMvc.perform(createAddRequest())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("[0].id").value(id));

    Mockito.when(userService.addFavouriteCity(id))
        .thenThrow(new CityAlreadyFavouredException(TEST_CITY));

    mockMvc.perform(createAddRequest())
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Provided city is already a favourite: ".concat(TEST_CITY.getName())));
  }

  @Test
  @DisplayName("Test add favourite city")
  public void testRemoveFavouriteCity() throws Exception {
    final var id = 1L;
    Mockito.when(userService.removeFavouriteCity(id))
        .thenReturn(List.of());

    mockMvc.perform(createRemoveRequest())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));

    Mockito.when(userService.removeFavouriteCity(id))
        .thenThrow(new CityNotFavouredException(TEST_CITY));

    mockMvc.perform(createRemoveRequest())
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Provided city is not a favourite: ".concat(TEST_CITY.getName())));
  }

  private MockHttpServletRequestBuilder createAddRequest() {
    return post("/api/v1/users/favourites/".concat(String.valueOf(1L)))
        .cookie(new Cookie(TOKEN_COOKIE_NAME, "signedToken"))
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
        .accept(MediaType.APPLICATION_JSON);
  }

  private MockHttpServletRequestBuilder createRemoveRequest() {
    return delete("/api/v1/users/favourites/".concat(String.valueOf(1L)))
        .cookie(new Cookie(TOKEN_COOKIE_NAME, "signedToken"))
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
        .accept(MediaType.APPLICATION_JSON);
  }

  private MockHttpServletRequestBuilder userCredentialsPost(final UserCredentials userCredentials) throws Exception {
    return post("/api/v1/users")
        .with(csrf())
        .content(JsonHelper.toJson(userCredentials))
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
        .accept(MediaType.APPLICATION_JSON);
  }

}
