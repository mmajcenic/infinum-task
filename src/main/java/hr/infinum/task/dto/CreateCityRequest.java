package hr.infinum.task.dto;

import hr.infinum.task.model.City;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateCityRequest {

  @NotEmpty(message = "Name is required")
  String name;

  @NotEmpty(message = "Description is required")
  String description;

  @PositiveOrZero(message = "Population must not be negative")
  @NotNull(message = "Population is required")
  Integer population;

  public City toCity() {
    return City.builder()
        .name(name)
        .description(description)
        .population(population)
        .build();
  }

}
