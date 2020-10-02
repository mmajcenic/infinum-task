package com.infinum.task;

import static com.infinum.task.security.service.impl.TokenServiceImpl.TOKEN_COOKIE_NAME;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.infinum.task.configuration.TokenTestConfiguration;
import com.infinum.task.helper.JsonHelper;
import com.infinum.task.controller.CityController;
import com.infinum.task.city.model.CreateCityRequest;
import com.infinum.task.city.service.CityService;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SuppressWarnings("unused")
@WebMvcTest(CityController.class)
@TestInstance(Lifecycle.PER_CLASS)
@Import(TokenTestConfiguration.class)
public class CityControllerTest {

  private static final City TEST_CITY_1 = City.builder()
      .id(1L)
      .name("test1")
      .description("test1")
      .population(1)
      .favouriteCount(1)
      .createdAt(LocalDateTime.of(2020, 10, 10, 0, 0, 0))
      .lastModifiedAt(LocalDateTime.of(2020, 10, 10, 0, 0, 0))
      .build();

  private static final City TEST_CITY_2 = City.builder()
      .id(2L)
      .name("test1")
      .description("test1")
      .population(1)
      .favouriteCount(2)
      .createdAt(LocalDateTime.of(2020, 10, 9, 0, 0, 0))
      .lastModifiedAt(LocalDateTime.of(2020, 10, 9, 0, 0, 0))
      .build();

  @MockBean
  private CityService cityService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Test city creation")
  public void testCreateCity() throws Exception {
    final var cityName = "test city";

    Mockito.when(cityService.create(City.builder()
        .name(cityName)
        .description("test")
        .population(1)
        .build()))
        .thenReturn(City.builder()
            .id(1L)
            .favouriteCount(0)
            .name(cityName)
            .description("test")
            .population(1)
            .build());

    mockMvc.perform(createPostRequest(CreateCityRequest.builder()
        .name(cityName)
        .description("test")
        .population(1)
        .build()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value(cityName));

    mockMvc.perform(createPostRequest(CreateCityRequest.builder()
        .description("a")
        .population(2)
        .build()))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(equalTo("Name is required")));

    mockMvc.perform(createPostRequest(CreateCityRequest.builder()
        .name("a")
        .population(2)
        .build()))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(equalTo("Description is required")));

    mockMvc.perform(createPostRequest(CreateCityRequest.builder()
        .name("a")
        .description("a")
        .build()))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(equalTo("Population is required")));
  }

  @Test
  @DisplayName("Test get all cities default")
  public void testGetAllCitiesDefault() throws Exception {
    Mockito.when(cityService.getAllSortedByCreationDate())
        .thenReturn(List.of(TEST_CITY_1, TEST_CITY_2));
    mockMvc.perform(createGetRequest("/api/v1/cities"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("[0].id").value(TEST_CITY_1.getId()));
  }

  @Test
  @DisplayName("Test get all cities by favourite count")
  public void testGetAllCitiesByFavourite() throws Exception {
    Mockito.when(cityService.getAllSortedByFavouriteCount())
        .thenReturn(List.of(TEST_CITY_2, TEST_CITY_1));
    mockMvc.perform(createGetRequest("/api/v1/cities?sort_by_favourites=true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("[0].id").value(TEST_CITY_2.getId()));
  }

  private MockHttpServletRequestBuilder createPostRequest(final CreateCityRequest createCityRequest) throws Exception {
    return post("/api/v1/cities")
        .cookie(new Cookie(TOKEN_COOKIE_NAME, "signedToken"))
        .with(csrf())
        .content(JsonHelper.toJson(createCityRequest))
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
        .accept(MediaType.APPLICATION_JSON);
  }

  private MockHttpServletRequestBuilder createGetRequest(final String path) {
    return get(path)
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
        .accept(MediaType.APPLICATION_JSON);
  }

}
