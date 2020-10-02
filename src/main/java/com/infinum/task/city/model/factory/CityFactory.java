package com.infinum.task.city.model.factory;

import com.infinum.task.city.model.City;
import com.infinum.task.city.model.CityDTO;
import com.infinum.task.city.model.CityORM;
import com.infinum.task.shared.EntityFactory;
import org.springframework.stereotype.Component;

@Component
public class CityFactory implements EntityFactory<City, CityORM, CityDTO> {

    @Override
    public City fromDto(CityDTO dto) {
        return null;
    }

    @Override
    public City fromOrm(CityORM orm) {
        return null;
    }

    @Override
    public CityORM toOrm(City entity) {
        return null;
    }

    @Override
    public CityDTO toDto(City entity) {
        return null;
    }
}
