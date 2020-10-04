package com.infinum.task.city.repository.facade.impl;

import com.infinum.task.city.model.City;
import com.infinum.task.city.repository.CityRepository;
import com.infinum.task.city.repository.facade.converter.CityORMConverter;
import com.infinum.task.city.repository.facade.CityRepositoryFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CityRepositoryFacadeImpl implements CityRepositoryFacade {

    private final CityORMConverter cityORMConverter;

    private final CityRepository cityRepository;

    @Override
    public Page<City> findAll(final Pageable pageable) {
        return cityRepository.findAll(pageable)
                .map(cityORMConverter::convertToDomain);
    }

    @Override
    public City findByName(final String name) {
        return null;
    }

    @Override
    public City getById(final Long id) {
        return null;
    }

    @Override
    public City save(final City city) {
        return null;
    }

}
