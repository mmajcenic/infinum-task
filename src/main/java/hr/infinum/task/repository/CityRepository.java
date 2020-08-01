package hr.infinum.task.repository;

import hr.infinum.task.model.City;
import java.util.List;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

  List<City> findAllByOrderByCreatedAtDesc();

  List<City> findAllByOrderByFavouriteCountDesc();

  City findByNameIgnoreCase(String name);

  @NonNull
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
  City getById(@NonNull Long id);

}
