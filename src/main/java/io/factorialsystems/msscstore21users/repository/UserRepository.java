package io.factorialsystems.msscstore21users.repository;

import io.factorialsystems.msscstore21users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Page<User> findByTenantId(String tenantId, Pageable pageable);
    Optional<User>  findByIdAndTenantId(String id, String tenantId);
}
