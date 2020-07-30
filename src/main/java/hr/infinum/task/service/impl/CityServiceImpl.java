package hr.infinum.task.service.impl;

import hr.infinum.task.model.City;
import hr.infinum.task.repository.CityRepository;
import hr.infinum.task.service.CityService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CityServiceImpl implements CityService {

  @Getter
  private final CityRepository repository;

  @Override
  public City incrementFavouriteCount(final City city) {
    city.incrementFavouriteCount();
    return repository.save(city);
  }

  @Override
  public City decrementFavouriteCount(final City city) {
    city.decrementFavouriteCount();
    return repository.save(city);
  }

  @Override
  public City findByName(final String name) {
    return repository.findByNameIgnoreCase(name);
  }
}
