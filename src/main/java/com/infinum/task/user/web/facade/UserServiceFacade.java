package com.infinum.task.user.web.facade;

import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.user.model.UserDTO;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;

@Transactional
public interface UserServiceFacade {

    UserDTO create(UserCredentials userCredentials, HttpServletResponse response);

    UserDTO addFavouriteCity(Long cityId);

    UserDTO removeFavouriteCity(Long cityId);

    UserDTO getById(Long id);

}
