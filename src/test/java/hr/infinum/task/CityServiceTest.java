package hr.infinum.task;

import hr.infinum.task.service.CityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CityServiceTest {

  private static final long ID = 1L;

  @Autowired
  private CityService cityService;

  @Test
  @DisplayName("Test fetching of all cities")
  public void testGetList() {
    Assertions.assertEquals(10, cityService.getAllSortedByCreationDate().size());
    cityService.incrementFavouriteCount(cityService.findById(ID));
    final var sortedByFavourite = cityService.getAllSortedByFavouriteCount();
    Assertions.assertEquals(10, sortedByFavourite.size());
    Assertions.assertEquals(ID, sortedByFavourite.get(0).getId());
    cityService.decrementFavouriteCount(cityService.findById(ID));
  }

  @Test
  @DisplayName("Test increment and decrement of favourite count")
  public void testIncrementAndDecrement() {
    cityService.incrementFavouriteCount(cityService.findById(ID));
    Assertions.assertEquals(1, cityService.findById(ID).getFavouriteCount());
    cityService.decrementFavouriteCount(cityService.findById(ID));
    Assertions.assertEquals(0, cityService.findById(ID).getFavouriteCount());
  }

}
