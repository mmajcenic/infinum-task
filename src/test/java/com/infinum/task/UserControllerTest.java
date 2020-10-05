package com.infinum.task;

import com.infinum.task.city.model.City;
import com.infinum.task.helper.JsonHelper;
import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.user.exception.CityAlreadyFavouredException;
import com.infinum.task.user.exception.CityNotFavouredException;
import com.infinum.task.user.model.UserDTO;
import com.infinum.task.user.web.UserController;
import com.infinum.task.user.web.facade.UserServiceFacade;
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
import javax.servlet.http.Cookie;

import static com.infinum.task.security.service.impl.TokenServiceImpl.TOKEN_COOKIE_NAME;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@TestInstance(Lifecycle.PER_CLASS)
@Import(TokenTestConfiguration.class)
class UserControllerTest {

    private static final String EMAIL = "test@test.hr";

    private static final Long ID = 1L;

    private static final City TEST_CITY = City.builder()
            .id(ID)
            .name("test_city")
            .build();

    private static final UserDTO TEST_USER = UserDTO.builder()
            .email(EMAIL)
            .build();

    @MockBean
    private UserServiceFacade userServiceFacade;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test user creation")
    void testCreateUser() throws Exception {
        final String password = "password";

        final UserCredentials user = UserCredentials.builder()
                .email(EMAIL)
                .password(password)
                .build();

        Mockito.when(userServiceFacade.create(Mockito.eq(user), Mockito.any()))
                .thenReturn(TEST_USER);

        mockMvc.perform(userCredentialsPost(UserCredentials.builder()
                .email(EMAIL)
                .password(password)
                .build()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(EMAIL));

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
                .email(EMAIL)
                .build()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo("Password is required")));
    }

    @Test
    @DisplayName("Test add favourite city")
    void testAddFavouriteCity() throws Exception {

        Mockito.when(userServiceFacade.addFavouriteCity(Mockito.eq(TEST_CITY.getId())))
                .thenReturn(TEST_USER);

        mockMvc.perform(createAddRequest())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(EMAIL));

        Mockito.when(userServiceFacade.addFavouriteCity(TEST_CITY.getId()))
                .thenThrow(new CityAlreadyFavouredException(TEST_CITY));

        mockMvc.perform(createAddRequest())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Provided city is already a favourite: ".concat(TEST_CITY.getName())));
    }

    @Test
    @DisplayName("Test add favourite city")
    void testRemoveFavouriteCity() throws Exception {
        Mockito.when(userServiceFacade.removeFavouriteCity(TEST_CITY.getId()))
                .thenReturn(TEST_USER);

        mockMvc.perform(createRemoveRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL));

        Mockito.when(userServiceFacade.removeFavouriteCity(TEST_CITY.getId()))
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
