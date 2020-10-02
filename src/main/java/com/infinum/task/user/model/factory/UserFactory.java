package com.infinum.task.user.model.factory;

import com.infinum.task.shared.EntityFactory;
import com.infinum.task.user.model.User;
import com.infinum.task.user.model.UserDTO;
import com.infinum.task.user.model.UserORM;
import org.springframework.stereotype.Component;

@Component
public class UserFactory implements EntityFactory<User, UserORM, UserDTO> {

    @Override
    public User fromDto(UserDTO dto) {
        return null;
    }

    @Override
    public User fromOrm(UserORM orm) {
        return null;
    }

    @Override
    public UserORM toOrm(User entity) {
        return null;
    }

    @Override
    public UserDTO toDto(User entity) {
        return null;
    }
}
