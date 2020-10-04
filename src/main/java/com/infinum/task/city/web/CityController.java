package com.infinum.task.city.web;

import com.infinum.task.city.model.CityDTO;
import com.infinum.task.city.model.CreateCityRequest;
import com.infinum.task.city.web.facade.CityServiceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@Api(tags = "Cities")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    private final CityServiceFacade cityServiceFacade;

    @ApiOperation(value = "Create city", response = CityDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Constraint violation")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CityDTO create(@RequestBody @Valid final CreateCityRequest createCityRequest) {
        return cityServiceFacade.createCity(createCityRequest);
    }

    @ApiOperation(value = "Get all cities", response = CityDTO[].class)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CityDTO> getAllCities(@RequestParam(name = "sort_by_favourites",
            defaultValue = "false")
                                      @ApiParam(name = "sort_by_favourites",
                                              value = "Specify if cities should be sorted by favourites",
                                              defaultValue = "false") final boolean sortByFavourites,
                                      @RequestParam("page") int page,
                                      @RequestParam("size") int size) {
        return cityServiceFacade.getAllCities(sortByFavourites, page, size);
    }

}
