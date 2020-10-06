package com.infinum.task.city.web.facade.impl;

import com.infinum.task.city.model.City;
import com.infinum.task.city.model.CityDTO;
import com.infinum.task.city.model.CreateCityRequest;
import com.infinum.task.city.service.CityService;
import com.infinum.task.city.web.facade.converter.CityDTOConverter;
import com.infinum.task.city.web.facade.CityServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CityServiceFacadeImpl implements CityServiceFacade {

    private static final Sort DEFAULT_SORT = Sort.by("name").ascending();

    private static final Sort SORT_BY_FAVOURITE = Sort.by("favouriteCount").descending()
            .and(Sort.by("name").ascending());

    private final CityDTOConverter cityDTOConverter;

    private final CityService cityService;

    @Override
    public CityDTO create(final CreateCityRequest createCityRequest) {
        final City city = cityService.create(City.builder()
                .name(createCityRequest.getName())
                .description(createCityRequest.getDescription())
                .population(createCityRequest.getPopulation())
                .favouriteCount(0)
                .build());
        return cityDTOConverter.convertFromDomain(city);
    }

    @Override
    public Page<CityDTO> getAllCities(final boolean sortByFavourites,
                                      final int page,
                                      final int size) {
        final Sort sort = sortByFavourites ? SORT_BY_FAVOURITE
                : DEFAULT_SORT;
        final Pageable pageable = PageRequest.of(page, size, sort);
        final Page<City> cityPage = cityService.findAll(pageable);
        return cityPage.map(cityDTOConverter::convertFromDomain);
    }

    @Override
    public CityDTO getById(final Long id) {
        return cityDTOConverter.convertFromDomain(cityService.findById(id));
    }

}
