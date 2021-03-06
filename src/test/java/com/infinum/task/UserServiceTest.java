package com.infinum.task;

import com.infinum.task.city.model.City;
import com.infinum.task.city.service.CityService;
import com.infinum.task.security.exception.UserNotFoundException;
import com.infinum.task.security.model.TokenAuthenticatedUser;
import com.infinum.task.user.exception.CityAlreadyFavouredException;
import com.infinum.task.user.exception.CityNotFavouredException;
import com.infinum.task.user.model.User;
import com.infinum.task.user.repository.facade.UserRepositoryFacade;
import com.infinum.task.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {

    private static final String EMAIL = "test@test.hr";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepositoryFacade userRepositoryFacade;

    @Autowired
    private CityService cityService;

    private City city;

    @BeforeAll
    void setup() {
        this.city = cityService.create(City.builder()
                .name("Testy town")
                .description("A mythical city")
                .population(1000)
                .favouriteCount(0)
                .build());
    }

    @Transactional
    @Test
    @DisplayName("Test adding and removing of favourite cities")
    void addingAndRemovingOfCities() {

        final User user = userService.create(User.builder()
                .email(EMAIL)
                .password("password")
                .favouriteCities(new ArrayList<>())
                .build());

        setMocks(user);

        userService.addFavouriteCity(user, city);
        Assertions.assertEquals(1, getUser().getFavouriteCities().size());
        Assertions.assertThrows(CityAlreadyFavouredException.class, () -> userService.addFavouriteCity(user, city));
        userService.removeFavouriteCity(user, city);
        Assertions.assertEquals(0, getUser().getFavouriteCities().size());
        Assertions.assertThrows(CityNotFavouredException.class, () -> userService.removeFavouriteCity(user, city));
    }

    private User getUser() {
        return userRepositoryFacade.findByEmail(EMAIL).orElseThrow(UserNotFoundException::new);
    }

    private void setMocks(final User user) {
        final TokenAuthenticatedUser tokenAuthenticatedUser = TokenAuthenticatedUser.builder()
                .email(user.getEmail())
                .userId(user.getId())
                .build();
        final Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(tokenAuthenticatedUser);
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

}
