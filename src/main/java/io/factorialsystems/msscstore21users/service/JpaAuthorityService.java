package io.factorialsystems.msscstore21users.service;

import io.factorialsystems.msscstore21users.dto.AuthorityDTO;
import org.springframework.data.domain.Page;

public interface JpaAuthorityService {
    Page<AuthorityDTO> findAll(Integer pageNumber, Integer pageSize);
    void createAuthority(AuthorityDTO authority);
    void editAuthority(String id, AuthorityDTO authority);
    Page<AuthorityDTO> findAllByTenant(int pageNumber, int pageSize);
    AuthorityDTO findById(String id);
    AuthorityDTO findByIdAndTenantId(String id);
    AuthorityDTO findByAuthorityAndTenantId(String authority);
    Page<AuthorityDTO> findByTenantId(Integer pageNumber, Integer pageSize, String id);
    Page<AuthorityDTO> search(Integer pageNumber, Integer pageSize, String search);
}
