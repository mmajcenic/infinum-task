package hr.infinum.task.exception;

import hr.infinum.task.model.City;

public class CityNotFavouredException extends RuntimeException {

  public CityNotFavouredException(final City city) {
    super("Provided city is not a favourite: ".concat(city.getName()));
  }

}
