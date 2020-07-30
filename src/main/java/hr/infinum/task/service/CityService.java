package hr.infinum.task.service;

import hr.infinum.task.model.City;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CityService extends EntityCreator<City> {

  City incrementFavouriteCount(City city);

  City decrementFavouriteCount(City city);

  City findByName(String name);

}
