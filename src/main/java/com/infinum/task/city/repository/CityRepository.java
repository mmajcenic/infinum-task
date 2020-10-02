package com.infinum.task.city.repository;

import java.util.List;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import com.infinum.task.city.model.CityORM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<CityORM, Long> {

  List<CityORM> findAllByOrderByCreatedAtDesc();

  List<CityORM> findAllByOrderByFavouriteCountDesc();

  CityORM findByNameIgnoreCase(String name);

  @NonNull
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
  CityORM getById(@NonNull Long id);

}
