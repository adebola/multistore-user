package io.factorialsystems.msscstore21users.service;

import io.factorialsystems.msscstore21users.dto.TenantCreateRequestDTO;
import io.factorialsystems.msscstore21users.dto.TenantResponseDTO;
import io.factorialsystems.msscstore21users.dto.TenantUpdateRequestDTO;
import org.springframework.data.domain.Page;

public interface JpaTenantService {
    void save(TenantCreateRequestDTO tenant);
    void update(String id, TenantUpdateRequestDTO tenant);
    Page<TenantResponseDTO> findAll(int pageNumber, int pageSize);
    Page<TenantResponseDTO> search(int pageNumber, int pageSize, String search);
    TenantResponseDTO findById(String id);
    void disable(String id);
    void enable(String id);
}
