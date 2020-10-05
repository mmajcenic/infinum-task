package com.infinum.task.user.repository.facade;

import com.infinum.task.user.model.User;
import java.util.Optional;

public interface UserRepositoryFacade {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

}
