package com.infinum.task.city.model;

import com.infinum.task.shared.PersistenceEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Builder
@Value
public class CityDTO extends RepresentationModel<CityDTO> implements PersistenceEntity {

    Long id;

    String name;

    String description;

    Integer population;

    Integer favouriteCount;

}
