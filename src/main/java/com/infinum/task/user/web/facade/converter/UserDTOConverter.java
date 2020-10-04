package com.infinum.task.user.web.facade.converter;

import com.infinum.task.shared.DTOConverter;
import com.infinum.task.user.model.User;
import com.infinum.task.user.model.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserDTOConverter implements DTOConverter<User, UserDTO> {
    @Override
    public UserDTO convertFromDomain(final User entity) {
        return UserDTO.builder()
                .build();
    }

    @Override
    public User convertToDomain(final UserDTO dto) {
        return User.builder()
                .build();
    }
}
