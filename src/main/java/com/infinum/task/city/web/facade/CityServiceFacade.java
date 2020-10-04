package com.infinum.task.city.web.facade;

import com.infinum.task.city.model.CityDTO;
import com.infinum.task.city.model.CreateCityRequest;
import org.springframework.data.domain.Page;

public interface CityServiceFacade {

    CityDTO createCity(CreateCityRequest createCityRequest);

    Page<CityDTO> getAllCities(boolean sortByFavourites, int page, int size);

}
