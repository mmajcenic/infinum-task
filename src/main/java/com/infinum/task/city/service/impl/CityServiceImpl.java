package com.infinum.task.city.service.impl;

import com.infinum.task.city.model.City;
import com.infinum.task.city.repository.facade.CityRepositoryFacade;
import com.infinum.task.city.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CityServiceImpl implements CityService {

    private final CityRepositoryFacade cityRepositoryFacade;

    @Override
    public City findById(final Long cityId) {
        return cityRepositoryFacade.getById(cityId);
    }

    @Override
    public Page<City> findAll(final Pageable pageable) {
        return cityRepositoryFacade.findAll(pageable);
    }

    @Override
    public City create(final City city) {
        return cityRepositoryFacade.save(city);
    }

}
