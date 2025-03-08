package io.factorialsystems.msscstore21users.repository;

import io.factorialsystems.msscstore21users.entity.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TenantRepository extends JpaRepository<Tenant, String> {

    @Query("SELECT t FROM Tenant t WHERE t.id LIKE %:search% OR t.name LIKE %:search%")
    Page<Tenant> search(Pageable pageable, @Param("search") String search);
}
