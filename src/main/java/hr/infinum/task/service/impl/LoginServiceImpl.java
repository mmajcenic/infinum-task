package hr.infinum.task.service.impl;

import hr.infinum.task.dto.UserCredentials;
import hr.infinum.task.exception.NonMatchingPasswordException;
import hr.infinum.task.exception.UserNotFoundException;
import hr.infinum.task.model.User;
import hr.infinum.task.service.LoginService;
import hr.infinum.task.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  @Override
  public User login(final UserCredentials userCredentials) {
    final var user = Optional.ofNullable(userService.findByEmail(userCredentials.getEmail()))
        .orElseThrow(UserNotFoundException::new);

    Optional.ofNullable(user.getPassword())
        .filter(password -> passwordMatches(userCredentials.getPassword(), password))
        .orElseThrow(NonMatchingPasswordException::new);

    return user;
  }

  private boolean passwordMatches(final String plainPassword, final String encryptedPassword) {
    return passwordEncoder.matches(plainPassword, encryptedPassword);
  }

}
