package com.infinum.task.city.model;

import com.infinum.task.shared.PersistenceEntity;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CityDTO implements PersistenceEntity {

    Long id;

    String name;

    String description;

    Integer population;

    Integer favouriteCount;

}
