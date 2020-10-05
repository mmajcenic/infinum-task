package com.infinum.task.security.service.impl;

import com.infinum.task.security.exception.NonMatchingPasswordException;
import com.infinum.task.security.exception.UserNotFoundException;
import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.security.service.LoginService;
import com.infinum.task.user.model.User;
import com.infinum.task.user.repository.facade.UserRepositoryFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepositoryFacade userRepositoryFacade;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(final UserCredentials userCredentials) {
        final User user = userRepositoryFacade.findByEmail(userCredentials.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (Optional.ofNullable(user.getPassword())
                .filter(password -> passwordMatches(userCredentials.getPassword(), password))
                .isEmpty()) {
            throw new NonMatchingPasswordException();
        }

        return user;
    }

    private boolean passwordMatches(final String plainPassword, final String encryptedPassword) {
        return passwordEncoder.matches(plainPassword, encryptedPassword);
    }

}
