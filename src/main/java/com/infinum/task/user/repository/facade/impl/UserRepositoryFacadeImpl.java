package com.infinum.task.user.repository.facade.impl;

import com.infinum.task.user.model.User;
import com.infinum.task.user.model.UserORM;
import com.infinum.task.user.repository.UserRepository;
import com.infinum.task.user.repository.facade.UserRepositoryFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserRepositoryFacadeImpl implements UserRepositoryFacade {

    private final UserRepository userRepository;

    private final com.infinum.task.user.repository.facade.converter.UserORMConverter userORMConverter;

    @Override
    public User save(final User user) {
        final UserORM userORM = userRepository.save(userORMConverter
                .convertFromDomain(user));
        return userORMConverter.convertToDomain(userORM);
    }

    @Override
    public User findByEmail(final String email) {
        final UserORM userORM = userRepository.findByEmail(email);
        return userORMConverter.convertToDomain(userORM);
    }

    @Override
    public User findById(final Long id) {
        final UserORM userORM = userRepository.getOne(id);
        return userORMConverter.convertToDomain(userORM);
    }
}
