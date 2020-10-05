package com.infinum.task.city.model;

import com.infinum.task.shared.DomainEntity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class City implements DomainEntity<City> {

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

    @Override
    public boolean sameIdentityAs(final City other) {
        return other != null && id.equals(other.id);
    }
}
