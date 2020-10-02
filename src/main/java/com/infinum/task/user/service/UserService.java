package com.infinum.task.user.service;

import java.util.List;
import com.infinum.task.city.model.City;
import com.infinum.task.user.model.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService {

  List<City> addFavouriteCity(City city);

  List<City> removeFavouriteCity(City city);

  User findByEmail(String email);

  User create(User user);

}
