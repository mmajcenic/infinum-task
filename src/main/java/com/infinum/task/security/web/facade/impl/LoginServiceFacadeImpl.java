package com.infinum.task.security.web.facade.impl;

import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.security.service.LoginService;
import com.infinum.task.security.service.TokenService;
import com.infinum.task.security.web.facade.LoginServiceFacade;
import com.infinum.task.user.model.User;
import com.infinum.task.user.model.UserDTO;
import com.infinum.task.user.web.facade.converter.UserDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
public class LoginServiceFacadeImpl implements LoginServiceFacade {

    private final LoginService loginService;

    private final TokenService tokenService;

    private final UserDTOConverter userDTOConverter;

    @Override
    public UserDTO login(final UserCredentials userCredentials, final HttpServletResponse response) {
        final User user = loginService.login(userCredentials);
        response.addCookie(tokenService.createCookie(user));
        return userDTOConverter.convertFromDomain(user);
    }

}
