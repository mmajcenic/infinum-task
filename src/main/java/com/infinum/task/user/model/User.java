package com.infinum.task.user.model;

import com.infinum.task.city.model.City;
import com.infinum.task.user.exception.CityAlreadyFavouredException;
import com.infinum.task.user.exception.CityNotFavouredException;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder
@Data
public class User {

    private Long id;

    private String email;

    private String password;

    private List<City> favouriteCities;

    public boolean hasFavouriteCity(final City city) {
        return favouriteCities.contains(city);
    }

    public void addFavouriteCity(final City city) {
        if (this.hasFavouriteCity(city)) {
            throw new CityAlreadyFavouredException(city);
        }
        this.favouriteCities.add(city);
        city.incrementFavouriteCount();
    }

    public void removeFavouriteCity(final City city) {
        if (!this.hasFavouriteCity(city)) {
            throw new CityNotFavouredException(city);
        }
        this.favouriteCities.remove(city);
        city.decrementFavouriteCount();
    }

}
