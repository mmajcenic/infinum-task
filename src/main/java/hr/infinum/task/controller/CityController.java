package hr.infinum.task.controller;

import hr.infinum.task.model.City;
import hr.infinum.task.service.CityService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

  private final CityService cityService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public City create(@RequestBody @Valid final City city) {
    return cityService.create(city);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<City> getAllCities(@RequestParam(name = "sort_by_favourites",
      defaultValue = "false") final boolean sortByFavourites) {
    return sortByFavourites ? cityService.getAllSortedByFavouriteCount() : cityService.getAllSortedByCreationDate();
  }

}
