package io.factorialsystems.msscstore21users.repository;

import io.factorialsystems.msscstore21users.entity.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    Page<Authority> findByTenantId(String tenantId, Pageable pageable);
    Optional<Authority> findByAuthorityAndTenantId(String authority, String tenantId);
    Optional<Authority> findByIdAndTenantId(String id, String tenantId);
    List<Authority> findAllById(Iterable<String> ids);
    Page<Authority> findByAuthorityContainingAndTenantId(String authority, String tenantId, Pageable pageable);
}
