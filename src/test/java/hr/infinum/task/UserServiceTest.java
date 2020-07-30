package hr.infinum.task;

import hr.infinum.task.exception.CityAlreadyFavouredException;
import hr.infinum.task.exception.CityNotFavouredException;
import hr.infinum.task.model.City;
import hr.infinum.task.model.User;
import hr.infinum.task.repository.UserRepository;
import hr.infinum.task.service.CityService;
import hr.infinum.task.service.UserService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

  private static final String EMAIL = "test@test.hr";

  private static final String CITY_NAME = "Camelot";

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CityService cityService;

  private City city;

  @BeforeAll
  public void setup() {
    this.city = cityService.create(City.builder()
        .name(CITY_NAME)
        .description("A mythical city")
        .population(1000)
        .build());
  }

  @Transactional
  @Test
  @DisplayName("Test adding and removing of favourite cities")
  public void addingAndRemovingOfCities() {

    final var user = userService.create(User.builder()
        .email(EMAIL)
        .password("password")
        .build());

    setMocks(user);

    userService.addFavouriteCity(CITY_NAME);
    Assertions.assertEquals(1, getUser().getFavouriteCities().size());
    Assertions.assertThrows(CityAlreadyFavouredException.class, () -> userService.addFavouriteCity(CITY_NAME));
    userService.removeFavouriteCity(CITY_NAME);
    Assertions.assertEquals(0, getUser().getFavouriteCities().size());
    Assertions.assertThrows(CityNotFavouredException.class, () -> userService.removeFavouriteCity(CITY_NAME));
    userService.addFavouriteCity(getCity());
    Assertions.assertEquals(1, getUser().getFavouriteCities().size());
    userService.removeFavouriteCity(getCity());
    Assertions.assertEquals(0, getUser().getFavouriteCities().size());
  }

  @Transactional
  @Test
  @DisplayName("Test concurrent update")
  public void testConcurrentUpdate() throws Exception {

    final var executorService = Executors.newFixedThreadPool(50);

    final var atomicInteger = new AtomicInteger(1);

    IntStream.range(0, 100)
        .forEach(i -> executorService.execute(() -> {
          final var user = userService.create(User.builder()
              .email(EMAIL.concat(String.valueOf(atomicInteger.getAndIncrement())))
              .password("password")
              .build());
          setMocks(user);
          userService.addFavouriteCity(city);
        }));

    executorService.shutdown();
    executorService.awaitTermination(30, TimeUnit.SECONDS);

    Assertions.assertTrue(executorService.isTerminated());

    Assertions.assertEquals(100, getCity().getFavouriteCount());

  }

  private User getUser() {
    return userRepository.findByEmail(EMAIL);
  }

  private City getCity() {
    return cityService.findByName(CITY_NAME);
  }

  private void setMocks(final User user) {
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(authentication.getPrincipal()).thenReturn(user);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

}
