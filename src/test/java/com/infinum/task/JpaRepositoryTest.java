package com.infinum.task;

import com.infinum.task.city.model.CityORM;
import com.infinum.task.city.repository.CityRepository;
import com.infinum.task.user.model.UserORM;
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
class JpaRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCityPersistence() {
        final CityORM city = cityRepository.save(CityORM.builder()
                .name("Rijeka")
                .description("Description of a city")
                .population(1000)
                .favouriteCount(0)
                .build());

        Assertions.assertEquals(0, city.getFavouriteCount());
    }

    @Test
    @DisplayName("Test user persistence")
    void testUserPersistence() {
        final String email = "test@test.hr";

        userRepository.save(UserORM.builder()
                .email(email)
                .password("password")
                .build());

        Assertions.assertEquals(email, userRepository.findByEmail(email).getEmail());
    }

    @Test
    @DisplayName("Test find city by name")
    void testFindCityByName() {
        final CityORM city = cityRepository.save(CityORM.builder()
                .name("Osijek")
                .description("Description of a city")
                .population(1000)
                .favouriteCount(0)
                .build());

        Assertions.assertEquals(city, cityRepository.findByNameIgnoreCase("Osijek"));
        Assertions.assertEquals(city, cityRepository.findByNameIgnoreCase("osijek"));
        Assertions.assertNull(cityRepository.findByNameIgnoreCase("osije"));
    }

    @Test
    @DisplayName("Test one to many relation")
    void testOneToMany() {
        final String email = "test123@test.hr";

        final UserORM user = userRepository.save(UserORM.builder()
                .email(email)
                .password("password")
                .build());
        final CityORM city = cityRepository.save(CityORM.builder()
                .name("Rijeka")
                .description("Description of a city")
                .population(1000)
                .favouriteCount(0)
                .build());

        user.getFavouriteCities().add(city);

        Assertions.assertEquals(1, userRepository.findByEmail(email).getFavouriteCities().size());

        user.getFavouriteCities().remove(city);

        Assertions.assertEquals(0, userRepository.findByEmail(email).getFavouriteCities().size());
    }

}
