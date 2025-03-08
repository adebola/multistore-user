package io.factorialsystems.msscstore21users.service;


import io.factorialsystems.msscstore21users.dto.TenantCreateRequestDTO;
import io.factorialsystems.msscstore21users.dto.TenantResponseDTO;
import io.factorialsystems.msscstore21users.dto.TenantUpdateRequestDTO;
import io.factorialsystems.msscstore21users.entity.Tenant;
import io.factorialsystems.msscstore21users.repository.TenantRepository;
import io.factorialsystems.msscstore21users.utils.PageRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaTenantServiceImpl implements JpaTenantService{
    private final TenantRepository tenantRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(TenantCreateRequestDTO tenant) {
        log.info("Saving tenant: {}", tenant);

        Tenant t = tenant.toEntity();
        t.setSecret(bCryptPasswordEncoder.encode(tenant.getSecret()));
        tenantRepository.save(t);
    }

    @Override
    public void update(String id, TenantUpdateRequestDTO tenant) {
        log.info("Updating tenant: {}", tenant);

        Tenant t = tenantRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Tenant with id %s not found", id)));
        Tenant newTenant = Tenant.builder()
                .name(tenant.getName())
                .description(tenant.getDescription())
                .disabled(tenant.getDisabled())
                .createdBy(t.getCreatedBy())
                .createdAt(t.getCreatedAt())
                .build();

        if (tenant.getSecret() == null || tenant.getSecret().isEmpty()) {
            newTenant.setSecret(t.getSecret());
        } else {
           newTenant.setSecret(bCryptPasswordEncoder.encode(tenant.getSecret()));
        }

        tenantRepository.save(newTenant);
    }

    @Override
    public Page<TenantResponseDTO> findAll(int pageNumber, int pageSize) {
        log.info("Find All Tenants PageNumber {}, PageSize {}", pageNumber, pageSize);

        Pageable pageable = PageRequestBuilder.build(pageNumber, pageSize);
        final Page<Tenant> tenantPage = tenantRepository.findAll(pageable);
        return tenantPage.map(Tenant::toDTO);
    }

    @Override
    public Page<TenantResponseDTO> search(int pageNumber, int pageSize, String search) {
        Pageable pageable = PageRequestBuilder.build(pageNumber, pageSize);
        final Page<Tenant> tenantPage = tenantRepository.search(pageable, search);
        return tenantPage.map(Tenant::toDTO);
    }

    @Override
    public TenantResponseDTO findById(String id) {
        log.info("Tenant::FindTenantById : {}", id);
        Tenant tenant = tenantRepository
                .findById(id).orElseThrow(() -> new RuntimeException(String.format("Tenant with id %s not found", id)));
        return tenant.toDTO();
    }

    public void disable(String id) {
        log.info("Disabling Tenant : {}", id);
        Tenant t = tenantRepository
                .findById(id).orElseThrow(() -> new RuntimeException(String.format("Tenant with id %s not found", id)));
        t.setDisabled(true);
        tenantRepository.save(t);
    }

    @Override
    public void enable(String id) {
        log.info("Enabling Tenant : {}", id);
        Tenant t = tenantRepository
                .findById(id).orElseThrow(() -> new RuntimeException(String.format("Tenant with id %s not found", id)));
        t.setDisabled(false);
        tenantRepository.save(t);
    }
}
