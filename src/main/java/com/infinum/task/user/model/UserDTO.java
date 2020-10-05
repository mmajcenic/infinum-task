package com.infinum.task.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinum.task.city.model.CityDTO;
import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder
public class UserDTO {

    String email;

    @JsonProperty("favourite_cities")
    List<CityDTO> favouriteCities;

}
