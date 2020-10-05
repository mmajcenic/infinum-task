package com.infinum.task.user.web.facade.converter;

import com.infinum.task.city.model.City;
import com.infinum.task.city.model.CityDTO;
import com.infinum.task.city.web.facade.converter.CityDTOConverter;
import com.infinum.task.shared.DTOConverter;
import com.infinum.task.user.model.User;
import com.infinum.task.user.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserDTOConverter implements DTOConverter<User, UserDTO> {

    private final CityDTOConverter cityDTOConverter;

    @Override
    public UserDTO convertFromDomain(final User entity) {
        final List<CityDTO> cityDTOList = Optional.ofNullable(entity.getFavouriteCities())
                .map(favouriteCities -> favouriteCities.stream()
                        .map(cityDTOConverter::convertFromDomain)
                        .collect(Collectors.toUnmodifiableList()))
                .orElseGet(ArrayList::new);

        return UserDTO.builder()
                .email(entity.getEmail())
                .favouriteCities(cityDTOList)
                .build();
    }

    @Override
    public User convertToDomain(final UserDTO dto) {
        final List<City> cityList = Optional.ofNullable(dto.getFavouriteCities())
                .map(favouriteCities -> favouriteCities.stream()
                        .map(cityDTOConverter::convertToDomain)
                        .collect(Collectors.toCollection(ArrayList::new)))
                .orElseGet(ArrayList::new);
        return User.builder()
                .email(dto.getEmail())
                .favouriteCities(cityList)
                .build();
    }
}
