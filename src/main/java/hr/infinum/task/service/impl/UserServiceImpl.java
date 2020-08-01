package hr.infinum.task.service.impl;

import hr.infinum.task.exception.CityAlreadyFavouredException;
import hr.infinum.task.exception.CityNotFavouredException;
import hr.infinum.task.model.City;
import hr.infinum.task.model.User;
import hr.infinum.task.repository.UserRepository;
import hr.infinum.task.security.TokenAuthenticatedUser;
import hr.infinum.task.service.CityService;
import hr.infinum.task.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  @Getter
  private final UserRepository repository;

  private final CityService cityService;

  private final PasswordEncoder passwordEncoder;

  @Override
  public List<City> addFavouriteCity(final Long cityId) {
    final var city = cityService.findById(cityId);
    final var user = getUserFromSecurityContext();
    if (user.hasFavouriteCity(city)) {
      throw new CityAlreadyFavouredException(city);
    }
    user.addFavouriteCity(cityService.incrementFavouriteCount(city));
    return repository.save(user).getFavouriteCities();
  }

  @Override
  public List<City> removeFavouriteCity(final Long cityId) {
    final var city = cityService.findById(cityId);
    final var user = getUserFromSecurityContext();
    if (!user.hasFavouriteCity(city)) {
      throw new CityNotFavouredException(city);
    }
    user.removeFavouriteCity(cityService.decrementFavouriteCount(city));
    return repository.save(user).getFavouriteCities();
  }

  @Override
  public User findByEmail(final String email) {
    return repository.findByEmail(email);
  }

  @Override
  public User create(final User entity) {
    Optional.ofNullable(entity.getPassword())
        .ifPresent(password -> entity.setPassword(passwordEncoder.encode(entity.getPassword())));
    return UserService.super.create(entity);
  }

  private User getUserFromSecurityContext() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(Authentication::getPrincipal)
        .filter(principal -> principal instanceof TokenAuthenticatedUser)
        .map(principal -> (TokenAuthenticatedUser) principal)
        .flatMap(tokenAuthenticatedUser -> repository.findById(tokenAuthenticatedUser.getUserId()))
        .orElseThrow(() -> new IllegalStateException("Failed to fetch user from security context"));
  }

}
