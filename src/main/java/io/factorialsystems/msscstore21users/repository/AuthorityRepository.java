package io.factorialsystems.msscstore21users.repository;

import io.factorialsystems.msscstore21users.entity.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
    Page<Authority> findAllByTenantId(String tenantId, Pageable pageable);
    Optional<Authority> findByAuthorityAndTenantId(String authority, String tenantId);
    Optional<Authority> findByIdAndTenantId(String id, String tenantId);

}
