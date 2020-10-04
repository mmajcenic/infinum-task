package com.infinum.task.user.web.facade;

import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.user.model.UserDTO;
import javax.servlet.http.HttpServletResponse;

public interface UserServiceFacade {

    UserDTO create(UserCredentials userCredentials, HttpServletResponse response);

}
