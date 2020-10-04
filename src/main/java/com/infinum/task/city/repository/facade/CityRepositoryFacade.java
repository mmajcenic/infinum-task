package com.infinum.task.city.repository.facade;

import com.infinum.task.city.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityRepositoryFacade {

    Page<City> findAll(Pageable pageable);

    City findByName(String name);

    City getById(Long id);

    City save(City city);

}
