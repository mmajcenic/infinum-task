package com.infinum.task.user.repository.facade.converter;

import com.infinum.task.shared.ORMConverter;
import com.infinum.task.user.model.User;
import com.infinum.task.user.model.UserORM;
import org.springframework.stereotype.Component;

@Component
public class UserORMConverter implements ORMConverter<User, UserORM> {
    @Override
    public UserORM convertFromDomain(final User entity) {
        return UserORM.builder()
                .build();
    }

    @Override
    public User convertToDomain(final UserORM orm) {
        return User.builder()
                .build();
    }
}
