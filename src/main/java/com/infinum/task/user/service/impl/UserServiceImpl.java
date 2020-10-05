package com.infinum.task.user.service.impl;

import com.infinum.task.city.model.City;
import com.infinum.task.user.model.User;
import com.infinum.task.user.repository.facade.UserRepositoryFacade;
import com.infinum.task.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepositoryFacade userRepositoryFacade;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User addFavouriteCity(final User user,
                                 final City city) {
        user.addFavouriteCity(city);
        return userRepositoryFacade.save(user);
    }

    @Override
    public User removeFavouriteCity(final User user,
                                    final City city) {
        user.removeFavouriteCity(city);
        return userRepositoryFacade.save(user);
    }

    @Override
    public User create(final User entity) {
        entity.encodePassword(passwordEncoder);
        return userRepositoryFacade.save(entity);
    }

}
