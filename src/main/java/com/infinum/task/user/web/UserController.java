package com.infinum.task.user.web;

import com.infinum.task.security.model.UserCredentials;
import com.infinum.task.user.model.UserDTO;
import com.infinum.task.user.web.facade.UserServiceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Api(tags = "Users")
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceFacade userServiceFacade;

    @ApiOperation(value = "Create user", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Constraint violation")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody @Valid final UserCredentials userCredentials,
                          final HttpServletResponse response) {
        return userServiceFacade.create(userCredentials, response);
    }

    @ApiOperation(value = "Add favourite city", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Constraint violation"),
            @ApiResponse(code = 401, message = "Authentication failed")
    })
    @PostMapping("/favourites/{cityId}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO addFavouriteCity(@PathVariable
                                    @Positive(message = "ID must be positive")
                                    @ApiParam(name = "cityId",
                                            value = "ID of a city",
                                            required = true) final Long cityId) {
        return userServiceFacade.addFavouriteCity(cityId);
    }

    @ApiOperation(value = "Remove favourite city", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Constraint violation"),
            @ApiResponse(code = 401, message = "Authentication failed")
    })
    @DeleteMapping("/favourites/{cityId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO removeFavouriteCity(@PathVariable
                                       @Positive(message = "ID must be positive")
                                       @ApiParam(name = "cityId",
                                               value = "ID of a city",
                                               required = true) final Long cityId) {
        return userServiceFacade.removeFavouriteCity(cityId);
    }

}
