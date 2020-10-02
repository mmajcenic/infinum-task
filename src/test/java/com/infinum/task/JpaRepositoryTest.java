package com.infinum.task;

import com.infinum.task.city.repository.CityRepository;
import com.infinum.task.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaRepositoryTest {

  @Autowired
  private CityRepository cityRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testCityPersistence() {
    final var city = cityRepository.save(City.builder()
        .name("Rijeka")
        .description("Description of a city")
        .population(1000)
        .build());

    Assertions.assertEquals(0, city.getFavouriteCount());
    Assertions.assertEquals(city.getCreatedAt(), city.getLastModifiedAt());

    city.setDescription("Another description of a city");
    cityRepository.saveAndFlush(city);

    Assertions.assertTrue(city.getCreatedAt().isBefore(city.getLastModifiedAt()));
  }

  @Test
  @DisplayName("Test user persistence")
  public void testUserPersistence() {
    final var email = "test@test.hr";

    final var user = userRepository.save(User.builder()
        .email(email)
        .password("password")
        .build());

    Assertions.assertEquals(email, userRepository.findByEmail(email).getEmail());
    Assertions.assertEquals(user.getCreatedAt(), user.getLastModifiedAt());

    user.setPassword("password123");
    userRepository.saveAndFlush(user);

    Assertions.assertTrue(user.getCreatedAt().isBefore(user.getLastModifiedAt()));
  }

  @Test
  @DisplayName("Test find city by name")
  public void testFindCityByName() {
    final var city = cityRepository.save(City.builder()
        .name("Osijek")
        .description("Description of a city")
        .population(1000)
        .build());

    Assertions.assertEquals(city, cityRepository.findByNameIgnoreCase("Osijek"));
    Assertions.assertEquals(city, cityRepository.findByNameIgnoreCase("osijek"));
    Assertions.assertNull(cityRepository.findByNameIgnoreCase("osije"));
  }

  @Test
  @DisplayName("Test one to many relation")
  public void testOneToMany() {
    final var email = "test123@test.hr";

    final var user = userRepository.save(User.builder()
        .email(email)
        .password("password")
        .build());
    final var city = cityRepository.save(City.builder()
        .name("Rijeka")
        .description("Description of a city")
        .population(1000)
        .build());

    user.addFavouriteCity(city);

    Assertions.assertEquals(1, userRepository.findByEmail(email).getFavouriteCities().size());

    user.removeFavouriteCity(city);

    Assertions.assertEquals(0, userRepository.findByEmail(email).getFavouriteCities().size());
  }


}
