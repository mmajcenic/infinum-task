package com.infinum.task.security.web.facade;

import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.user.model.UserDTO;
import javax.servlet.http.HttpServletResponse;

public interface LoginServiceFacade {

    UserDTO login(UserCredentials userCredentials, HttpServletResponse response);

}
