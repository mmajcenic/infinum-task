package com.infinum.task.user.web.facade.impl;

import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.security.service.TokenService;
import com.infinum.task.user.model.User;
import com.infinum.task.user.model.UserDTO;
import com.infinum.task.user.service.UserService;
import com.infinum.task.user.web.facade.UserServiceFacade;
import com.infinum.task.user.web.facade.converter.UserDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
public class UserServiceFacadeImpl implements UserServiceFacade {

    private final UserService userService;

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
}
