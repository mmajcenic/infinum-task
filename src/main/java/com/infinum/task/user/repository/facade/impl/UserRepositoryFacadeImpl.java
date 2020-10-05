package com.infinum.task.user.repository.facade.impl;

import com.infinum.task.user.model.User;
import com.infinum.task.user.model.UserORM;
import com.infinum.task.user.repository.UserRepository;
import com.infinum.task.user.repository.facade.UserRepositoryFacade;
import com.infinum.task.user.repository.facade.converter.UserORMConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserRepositoryFacadeImpl implements UserRepositoryFacade {

    private final UserRepository userRepository;

    private final UserORMConverter userORMConverter;

    @Override
    public User save(final User user) {
        final UserORM userORM = userRepository.save(userORMConverter
                .convertFromDomain(user));
        return userORMConverter.convertToDomain(userORM);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        final UserORM userORM = userRepository.findByEmail(email);
        return Optional.ofNullable(userORM)
                .map(userORMConverter::convertToDomain);
    }

    @Override
    public Optional<User> findById(final Long id) {
        return userRepository.findById(id)
                .map(userORMConverter::convertToDomain);
    }
}
