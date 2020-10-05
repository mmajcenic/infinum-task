package com.infinum.task.city.web.facade.converter;

import com.infinum.task.city.model.City;
import com.infinum.task.city.model.CityDTO;
import com.infinum.task.shared.DTOConverter;
import org.springframework.stereotype.Component;

@Component
public class CityDTOConverter implements DTOConverter<City, CityDTO> {
    @Override
    public CityDTO convertFromDomain(final City entity) {
        return CityDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .population(entity.getPopulation())
                .favouriteCount(entity.getFavouriteCount())
                .build();
    }

    @Override
    public City convertToDomain(final CityDTO dto) {
        return City.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .population(dto.getPopulation())
                .favouriteCount(dto.getFavouriteCount())
                .build();
    }
}
