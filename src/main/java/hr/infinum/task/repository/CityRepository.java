package hr.infinum.task.repository;

import hr.infinum.task.model.City;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

  List<City> findAllByOrderByCreatedAtDesc();

  List<City> findAllByOrderByFavouriteCountDesc();

  City findByNameIgnoreCase(String name);

}
