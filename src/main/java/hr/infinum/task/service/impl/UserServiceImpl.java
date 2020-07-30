package hr.infinum.task.service.impl;

import hr.infinum.task.exception.CityAlreadyFavouredException;
import hr.infinum.task.exception.CityNotFavouredException;
import hr.infinum.task.model.City;
import hr.infinum.task.model.User;
import hr.infinum.task.repository.UserRepository;
import hr.infinum.task.service.CityService;
import hr.infinum.task.service.UserService;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private static final Map<String, Object> LOCK_PROPERTIES = Map.of("javax.persistence.lock.timeout", "5000");

  @Getter
  private final UserRepository repository;

  private final CityService cityService;

  private final EntityManager entityManager;

  @Override
  public List<City> addFavouriteCity(final String cityName) {
    return this.internalAddFavouriteCity(cityService.findByName(cityName));
  }

  @Override
  public List<City> removeFavouriteCity(final String cityName) {
    return this.removeFavouriteCity(cityService.findByName(cityName));
  }

  @Override
  public List<City> addFavouriteCity(final City city) {
    return internalAddFavouriteCity(findAndLockCity(city));
  }

  @Override
  public List<City> removeFavouriteCity(final City city) {
    final var user = getUserFromSecurityContext();
    if (!user.hasFavouriteCity(city)) {
      throw new CityNotFavouredException(city);
    }
    user.removeFavouriteCity(cityService.decrementFavouriteCount(city));
    return repository.save(user).getFavouriteCities();
  }

  private List<City> internalAddFavouriteCity(final City city) {
    final var user = getUserFromSecurityContext();
    if (user.hasFavouriteCity(city)) {
      throw new CityAlreadyFavouredException(city);
    }
    user.addFavouriteCity(cityService.incrementFavouriteCount(city));
    return repository.save(user).getFavouriteCities();
  }

  private City findAndLockCity(final City city) {
    return entityManager.find(City.class,
        city.getId(),
        LockModeType.PESSIMISTIC_WRITE,
        LOCK_PROPERTIES);
  }

  private User getUserFromSecurityContext() {
    final var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof User) {
      return (User) principal;
    }
    throw new IllegalStateException("Failed to fetch user from security context");
  }

}
