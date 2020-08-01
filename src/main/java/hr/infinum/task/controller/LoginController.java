package hr.infinum.task.controller;

import hr.infinum.task.dto.UserCredentials;
import hr.infinum.task.dto.UserDTO;
import hr.infinum.task.service.LoginService;
import hr.infinum.task.service.TokenService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

  private final LoginService loginService;

  private final TokenService tokenService;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public UserDTO login(@RequestBody @NotNull @Valid final UserCredentials userCredentials,
      final HttpServletResponse response) {
    final var user = loginService.login(userCredentials);
    response.addCookie(tokenService.createCookie(user));
    return UserDTO.fromUser(user);
  }

}
