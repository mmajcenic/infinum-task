package com.infinum.task.user.repository;

import com.infinum.task.user.model.UserORM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserORM, Long> {

    UserORM findByEmail(String email);

}
