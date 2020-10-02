package com.infinum.task.user.service.impl;

import com.infinum.task.city.model.City;
import com.infinum.task.city.model.factory.CityFactory;
import com.infinum.task.user.repository.UserRepository;
import com.infinum.task.security.model.TokenAuthenticatedUser;
import com.infinum.task.user.model.User;
import com.infinum.task.user.model.factory.UserFactory;
import com.infinum.task.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final UserFactory userFactory;

    private final CityFactory cityFactory;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<City> addFavouriteCity(final City city) {
        final User user = getUserFromSecurityContext();
        user.addFavouriteCity(city);
        return persistUser(user);
    }

    @Override
    public List<City> removeFavouriteCity(final City city) {
        final User user = getUserFromSecurityContext();
        user.removeFavouriteCity(city);
        return persistUser(user);
    }

    @Override
    public User findByEmail(final String email) {
        return userFactory.fromOrm(repository.findByEmail(email));
    }

    @Override
    public User create(final User entity) {
        Optional.ofNullable(entity.getPassword())
                .ifPresent(password -> entity.setPassword(passwordEncoder.encode(entity.getPassword())));
        return userFactory.fromOrm(repository.save(userFactory.toOrm(entity)));
    }

    private User getUserFromSecurityContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof TokenAuthenticatedUser)
                .map(principal -> (TokenAuthenticatedUser) principal)
                .flatMap(tokenAuthenticatedUser -> repository.findById(tokenAuthenticatedUser.getUserId()))
                .map(userFactory::fromOrm)
                .orElseThrow(() -> new IllegalStateException("Failed to fetch user from security context"));
    }

    private List<City> persistUser(final User user) {
        return cityFactory.fromOrm(repository.save(userFactory.toOrm(user))
                .getFavouriteCities());
    }

}
