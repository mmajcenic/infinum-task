package com.infinum.task.user.service;

import com.infinum.task.city.model.City;
import com.infinum.task.user.model.User;

public interface UserService {

    User addFavouriteCity(User user, City city);

    User removeFavouriteCity(User user, City city);

    User create(User user);

}
