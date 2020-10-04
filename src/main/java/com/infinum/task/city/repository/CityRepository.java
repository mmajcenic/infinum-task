package com.infinum.task.city.repository;

import com.infinum.task.city.model.CityORM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

@Repository
public interface CityRepository extends JpaRepository<CityORM, Long> {

    CityORM findByNameIgnoreCase(String name);

    @NonNull
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
    CityORM getById(@NonNull Long id);

}
