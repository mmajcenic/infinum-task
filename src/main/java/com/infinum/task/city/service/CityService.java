package com.infinum.task.city.service;

import com.infinum.task.city.model.City;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
public interface CityService {

    City findById(Long cityId);

    List<City> getAllSortedByCreationDate();

    List<City> getAllSortedByFavouriteCount();

}
