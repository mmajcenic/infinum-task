package hr.infinum.task.controller;

import hr.infinum.task.dto.UserCredentials;
import hr.infinum.task.dto.UserDTO;
import hr.infinum.task.model.City;
import hr.infinum.task.model.User;
import hr.infinum.task.service.TokenService;
import hr.infinum.task.service.UserService;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  private final TokenService tokenService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserDTO create(@RequestBody @Valid final UserCredentials userCredentials,
      final HttpServletResponse response) {
    final var user = userService.create(User.builder()
        .email(userCredentials.getEmail())
        .password(userCredentials.getPassword())
        .build());
    response.addCookie(tokenService.createCookie(user));
    return UserDTO.fromUser(user);
  }

  @PostMapping("/favourites/{cityId}")
  @ResponseStatus(HttpStatus.CREATED)
  public List<City> addFavouriteCity(@PathVariable @Positive(message = "ID must be positive") final Long cityId) {
    return userService.addFavouriteCity(cityId);
  }

  @DeleteMapping("/favourites/{cityId}")
  @ResponseStatus(HttpStatus.OK)
  public List<City> removeFavouriteCity(@PathVariable @Positive(message = "ID must be positive") final Long cityId) {
    return userService.removeFavouriteCity(cityId);
  }

}
