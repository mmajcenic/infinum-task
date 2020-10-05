package com.infinum.task.security.service;

import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.user.model.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LoginService {

    User login(UserCredentials userCredentials);

}
