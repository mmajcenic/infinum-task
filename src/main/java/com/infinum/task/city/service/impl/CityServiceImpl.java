package com.infinum.task.city.service.impl;

import com.infinum.task.city.model.City;
import com.infinum.task.city.model.factory.CityFactory;
import com.infinum.task.city.service.CityService;
import com.infinum.task.city.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CityServiceImpl implements CityService {

    private final CityRepository repository;

    private final CityFactory cityFactory;

    @Override
    public City findById(final Long cityId) {
        return cityFactory.fromOrm(repository.getById(cityId));
    }

    @Override
    public List<City> getAllSortedByCreationDate() {
        return cityFactory.fromOrm(repository.findAllByOrderByCreatedAtDesc());
    }

    @Override
    public List<City> getAllSortedByFavouriteCount() {
        return cityFactory.fromOrm(repository.findAllByOrderByFavouriteCountDesc());
    }

}
