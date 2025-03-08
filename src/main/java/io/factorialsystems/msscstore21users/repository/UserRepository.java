package io.factorialsystems.msscstore21users.repository;

import io.factorialsystems.msscstore21users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
