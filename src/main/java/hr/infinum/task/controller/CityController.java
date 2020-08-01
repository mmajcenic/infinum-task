package hr.infinum.task.controller;

import hr.infinum.task.dto.CreateCityRequest;
import hr.infinum.task.model.City;
import hr.infinum.task.service.CityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@Api(tags = "Cities")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

  private final CityService cityService;

  @ApiOperation(value = "Create city", response = City.class)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Constraint violation")
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public City create(@RequestBody @Valid final CreateCityRequest createCityRequest) {
    return cityService.create(createCityRequest.toCity());
  }

  @ApiOperation(value = "Get all cities", response = City[].class)
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<City> getAllCities(@RequestParam(name = "sort_by_favourites",
      defaultValue = "false")
  @ApiParam(name = "sort_by_favourites",
      value = "Specify if cities should be sorted by favourites",
      defaultValue = "false") final boolean sortByFavourites) {
    return sortByFavourites ? cityService.getAllSortedByFavouriteCount() : cityService.getAllSortedByCreationDate();
  }

}
