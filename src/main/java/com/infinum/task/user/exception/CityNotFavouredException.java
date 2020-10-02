package com.infinum.task.user.exception;

import com.infinum.task.city.model.City;

public class CityNotFavouredException extends RuntimeException {

    public CityNotFavouredException(final City city) {
        super("Provided city is not a favourite: ".concat(city.getName()));
    }

}
