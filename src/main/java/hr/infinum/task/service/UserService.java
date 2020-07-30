package hr.infinum.task.service;

import hr.infinum.task.model.City;
import hr.infinum.task.model.User;
import java.util.List;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService extends EntityCreator<User> {

  List<City> addFavouriteCity(String cityName);

  List<City> removeFavouriteCity(String cityName);

  List<City> addFavouriteCity(City city);

  List<City> removeFavouriteCity(City city);

}
