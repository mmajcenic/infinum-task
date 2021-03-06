package com.infinum.task.security.web;

import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.security.web.facade.LoginServiceFacade;
import com.infinum.task.user.model.UserDTO;
import com.infinum.task.security.service.LoginService;
import com.infinum.task.security.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@Api(tags = "Login")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

  private final LoginServiceFacade loginServiceFacade;

  @ApiOperation(value = "Login", response = UserDTO.class)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Constraint violation"),
      @ApiResponse(code = 401, message = "Authentication failed")
  })
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public UserDTO login(@RequestBody @NotNull @Valid final UserCredentials userCredentials,
      final HttpServletResponse response) {
    return loginServiceFacade.login(userCredentials, response);
  }

}
