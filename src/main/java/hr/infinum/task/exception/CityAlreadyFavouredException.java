package hr.infinum.task.exception;

import hr.infinum.task.model.City;

public class CityAlreadyFavouredException extends RuntimeException {

  public CityAlreadyFavouredException(final City city) {
    super("Provided city is already a favourite: ".concat(city.getName()));
  }
}
