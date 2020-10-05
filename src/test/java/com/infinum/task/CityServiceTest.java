package com.infinum.task;

import com.infinum.task.city.model.City;
import com.infinum.task.city.service.CityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CityServiceTest {

    private static final long ID = 1L;

    @Autowired
    private CityService cityService;

    @Test
    @DisplayName("Test fetching of all cities")
    void testFindAll() {
        final Pageable pageable = PageRequest.of(0, 5,
                Sort.by(Sort.Direction.DESC, "name"));
        Assertions.assertEquals(5, cityService.findAll(pageable).getNumberOfElements());
        final City city = cityService.findById(ID);
        Assertions.assertEquals(ID, city.getId());
    }

}
