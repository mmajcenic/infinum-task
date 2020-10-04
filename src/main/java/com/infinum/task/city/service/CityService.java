package com.infinum.task.city.service;

import com.infinum.task.city.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
public interface CityService {

    City findById(Long cityId);

    Page<City> findAll(Pageable pageable);

    City create(City city);

}
