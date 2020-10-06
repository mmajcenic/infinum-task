package com.infinum.task.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinum.task.city.model.CityDTO;
import com.infinum.task.shared.DataEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class UserDTO extends RepresentationModel<UserDTO> implements DataEntity {

    private Long id;

    private String email;

    @JsonProperty("favourite_cities")
    private List<CityDTO> favouriteCities;

}
