package com.infinum.task.user.exception;

import com.infinum.task.city.model.City;

public class CityAlreadyFavouredException extends RuntimeException {

    public CityAlreadyFavouredException(final City city) {
        super("Provided city is already a favourite: ".concat(city.getName()));
    }
}
