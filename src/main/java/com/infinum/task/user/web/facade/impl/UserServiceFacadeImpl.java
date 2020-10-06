package com.infinum.task.user.web.facade.impl;

import com.infinum.task.city.model.City;
import com.infinum.task.city.repository.facade.CityRepositoryFacade;
import com.infinum.task.security.exception.UserNotFoundException;
import com.infinum.task.security.model.TokenAuthenticatedUser;
import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.security.service.TokenService;
import com.infinum.task.user.model.User;
import com.infinum.task.user.model.UserDTO;
import com.infinum.task.user.repository.facade.UserRepositoryFacade;
import com.infinum.task.user.service.UserService;
import com.infinum.task.user.web.facade.UserServiceFacade;
import com.infinum.task.user.web.facade.converter.UserDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
public class UserServiceFacadeImpl implements UserServiceFacade {

    private final UserService userService;

    private final UserRepositoryFacade userRepositoryFacade;

    private final CityRepositoryFacade cityRepositoryFacade;

    private final UserDTOConverter userDTOConverter;

    private final TokenService tokenService;

    @Override
    public UserDTO create(final UserCredentials userCredentials,
                          final HttpServletResponse response) {
        final User user = userService.create(User.builder()
                .email(userCredentials.getEmail())
                .password(userCredentials.getPassword())
                .build());
        response.addCookie(tokenService.createCookie(user));
        return userDTOConverter.convertFromDomain(user);
    }

    @Override
    public UserDTO addFavouriteCity(final Long cityId) {
        final User user = getUserFromSecurityContext();
        final City city = cityRepositoryFacade.getById(cityId);
        return userDTOConverter.convertFromDomain(userService.addFavouriteCity(user, city));
    }

    @Override
    public UserDTO removeFavouriteCity(final Long cityId) {
        final User user = getUserFromSecurityContext();
        final City city = cityRepositoryFacade.getById(cityId);
        return userDTOConverter.convertFromDomain(userService.removeFavouriteCity(user, city));
    }

    @Override
    public UserDTO getById(final Long id) {
        return userDTOConverter.convertFromDomain(userRepositoryFacade.findById(id)
                .orElseThrow(UserNotFoundException::new));
    }

    private User getUserFromSecurityContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof TokenAuthenticatedUser)
                .map(principal -> (TokenAuthenticatedUser) principal)
                .flatMap(tokenAuthenticatedUser -> userRepositoryFacade.findById(tokenAuthenticatedUser.getUserId()))
                .orElseThrow(() -> new IllegalStateException("Failed to fetch user from security context"));
    }
}
