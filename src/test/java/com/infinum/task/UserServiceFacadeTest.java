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
import com.infinum.task.user.web.facade.UserServiceFacade;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceFacadeTest {

    private static final String EMAIL = "test@test.hr";

    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceFacade userServiceFacade;

    @Autowired
    private UserRepositoryFacade userRepositoryFacade;

    @Autowired
    private CityService cityService;

    private City city;

    @BeforeAll
    void setup() {
        this.city = cityService.create(City.builder()
                .name("Camelot")
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
                .build());

        setMocks(user);

        final long id = city.getId();

        userServiceFacade.addFavouriteCity(city.getId());
        Assertions.assertEquals(1, getUser().getFavouriteCities().size());
        Assertions.assertThrows(CityAlreadyFavouredException.class, () -> userServiceFacade.addFavouriteCity(id));
        userServiceFacade.removeFavouriteCity(city.getId());
        Assertions.assertEquals(0, getUser().getFavouriteCities().size());
        Assertions.assertThrows(CityNotFavouredException.class, () -> userServiceFacade.removeFavouriteCity(id));
    }

    @Transactional
    @Test
    @DisplayName("Test concurrent update")
    void testConcurrentUpdate() throws Exception {

        final ExecutorService executorService = Executors.newFixedThreadPool(100);

        final AtomicInteger atomicInteger = new AtomicInteger(1);

        IntStream.range(0, 100)
                .forEach(i -> executorService.execute(() -> {
                    final User user = userService.create(User.builder()
                            .email(EMAIL.concat(String.valueOf(atomicInteger.getAndIncrement())))
                            .password("password")
                            .build());
                    setMocks(user);
                    userServiceFacade.addFavouriteCity(city.getId());
                }));

        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);

        Assertions.assertTrue(executorService.isTerminated());

        Assertions.assertEquals(100, getCity().getFavouriteCount());

    }

    private User getUser() {
        return userRepositoryFacade.findByEmail(EMAIL).orElseThrow(UserNotFoundException::new);
    }

    private City getCity() {
        return cityService.findById(city.getId());
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
