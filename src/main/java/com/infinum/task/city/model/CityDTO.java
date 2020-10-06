package com.infinum.task.city.model;

import com.infinum.task.shared.DataEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class CityDTO extends RepresentationModel<CityDTO> implements DataEntity {

    private Long id;

    private String name;

    private String description;

    private Integer population;

    private Integer favouriteCount;

}
