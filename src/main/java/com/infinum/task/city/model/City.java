package com.infinum.task.city.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class City {

    private Long id;

    private String name;

    private String description;

    private Integer population;

    private Integer favouriteCount;

    public void incrementFavouriteCount() {
        favouriteCount++;
    }

    public void decrementFavouriteCount() {
        favouriteCount--;
    }

}
