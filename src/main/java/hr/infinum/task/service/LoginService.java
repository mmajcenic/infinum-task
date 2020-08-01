package hr.infinum.task.service;

import hr.infinum.task.dto.UserCredentials;
import hr.infinum.task.model.User;

public interface LoginService {

  User login(UserCredentials userCredentials);

}
