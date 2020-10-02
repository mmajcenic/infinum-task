package com.infinum.task.city.model;

import lombok.Builder;
import lombok.Value;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

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

}
