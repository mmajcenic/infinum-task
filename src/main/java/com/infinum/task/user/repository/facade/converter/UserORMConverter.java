package com.infinum.task.user.repository.facade.converter;

import com.infinum.task.city.model.CityORM;
import com.infinum.task.city.repository.facade.converter.CityORMConverter;
import com.infinum.task.shared.ORMConverter;
import com.infinum.task.user.model.User;
import com.infinum.task.user.model.UserORM;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserORMConverter implements ORMConverter<User, UserORM> {

    private final CityORMConverter cityORMConverter;

    @Override
    public UserORM convertFromDomain(final User entity) {
        final List<CityORM> cityORMList = Optional.ofNullable(entity.getFavouriteCities())
                .map(favouriteCities -> favouriteCities
                        .stream()
                        .map(cityORMConverter::convertFromDomain)
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);

        return UserORM.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .favouriteCities(cityORMList)
                .build();
    }

    @Override
    public User convertToDomain(final UserORM orm) {
        return User.builder()
                .id(orm.getId())
                .email(orm.getEmail())
                .password(orm.getPassword())
                .favouriteCities(orm.getFavouriteCities()
                        .stream()
                        .map(cityORMConverter::convertToDomain)
                        .collect(Collectors.toList()))
                .build();
    }
}
