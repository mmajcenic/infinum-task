package com.infinum.task.user.service.impl;

import com.infinum.task.city.model.City;
import com.infinum.task.security.model.TokenAuthenticatedUser;
import com.infinum.task.user.model.User;
import com.infinum.task.user.repository.facade.UserRepositoryFacade;
import com.infinum.task.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepositoryFacade userRepositoryFacade;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User addFavouriteCity(final City city) {
        final User user = getUserFromSecurityContext();
        user.addFavouriteCity(city);
        return userRepositoryFacade.save(user);
    }

    @Override
    public User removeFavouriteCity(final City city) {
        final User user = getUserFromSecurityContext();
        user.removeFavouriteCity(city);
        return userRepositoryFacade.save(user);
    }

    @Override
    public User findByEmail(final String email) {
        return userRepositoryFacade.findByEmail(email);
    }

    @Override
    public User create(final User entity) {
        entity.encodePassword(passwordEncoder);
        return userRepositoryFacade.save(entity);
    }

    private User getUserFromSecurityContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof TokenAuthenticatedUser)
                .map(principal -> (TokenAuthenticatedUser) principal)
                .map(tokenAuthenticatedUser -> userRepositoryFacade.findById(tokenAuthenticatedUser.getUserId()))
                .orElseThrow(() -> new IllegalStateException("Failed to fetch user from security context"));
    }

}
