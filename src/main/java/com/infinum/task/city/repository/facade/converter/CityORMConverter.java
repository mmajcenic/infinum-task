package com.infinum.task.city.repository.facade.converter;

import com.infinum.task.city.model.City;
import com.infinum.task.city.model.CityORM;
import com.infinum.task.shared.ORMConverter;
import org.springframework.stereotype.Component;

@Component
public class CityORMConverter implements ORMConverter<City, CityORM> {

    @Override
    public CityORM convertFromDomain(final City entity) {
        return CityORM.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .population(entity.getPopulation())
                .favouriteCount(entity.getFavouriteCount())
                .build();
    }

    @Override
    public City convertToDomain(final CityORM orm) {
        return City.builder()
                .id(orm.getId())
                .name(orm.getName())
                .description(orm.getDescription())
                .population(orm.getPopulation())
                .favouriteCount(orm.getFavouriteCount())
                .build();
    }
}
