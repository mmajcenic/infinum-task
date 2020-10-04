package com.infinum.task.user.repository.facade;

import com.infinum.task.user.model.User;

public interface UserRepositoryFacade {

    User save(User user);

    User findByEmail(String email);

    User findById(Long id);

}
