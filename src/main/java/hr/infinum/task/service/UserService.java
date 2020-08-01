package hr.infinum.task.service;

import hr.infinum.task.model.City;
import hr.infinum.task.model.User;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService extends EntityCreator<User> {

  List<City> addFavouriteCity(Long cityId);

  List<City> removeFavouriteCity(Long cityId);

  User findByEmail(String email);

}
