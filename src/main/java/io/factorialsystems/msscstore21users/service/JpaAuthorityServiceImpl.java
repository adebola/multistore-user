package io.factorialsystems.msscstore21users.service;


import io.factorialsystems.msscstore21users.dto.AuthorityDTO;
import io.factorialsystems.msscstore21users.entity.Authority;
import io.factorialsystems.msscstore21users.exception.BusinessEntityExistsException;
import io.factorialsystems.msscstore21users.exception.BusinessEntityNotFoundException;
import io.factorialsystems.msscstore21users.repository.AuthorityRepository;
import io.factorialsystems.msscstore21users.security.TenantContext;
import io.factorialsystems.msscstore21users.utils.PageRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class JpaAuthorityServiceImpl implements JpaAuthorityService {
    public static final String CREATE_AUTHORITY = "Create-Authority";
    public static final String EDIT_AUTHORITY = "Edit-Authority";

    private final JpaAuditService jpaAuditService;
    private final AuthorityRepository authorityRepository;

    @Override
    public Page<AuthorityDTO> findAll(Integer pageNumber, Integer pageSize) {
        log.info("Retrieving List of Roles from database");

        Pageable pageable = PageRequestBuilder.build(pageNumber, pageSize);
        final Page<Authority> authorityPage = authorityRepository.findAll(pageable);
        return authorityPage.map(Authority::toDTO);
    }

    @Override
    public void createAuthority(AuthorityDTO authority) {
        log.info("Creating Authority {}", authority);

        final String tenantId = TenantContext.getCurrentTenant();

        if (tenantId == null) {
            throw new IllegalStateException("No Store submitted in request to Creating Authority");
        }

        authorityRepository.findByAuthorityAndTenantId(authority.getAuthority(), tenantId)
                .ifPresentOrElse((value) -> {
                    final String s = String.format("Role %s already exists", value.getAuthority());
                    log.error(s);
                    throw new BusinessEntityExistsException(s);
                }, () -> {
                    log.info("Creating UserAuthority {}", authority);
                    authorityRepository.save(authority.createEntity());
                    jpaAuditService.audit(CREATE_AUTHORITY, String.format("Role %s created", authority.getAuthority()));
                });
    }

    @Override
    public void editAuthority(String id, AuthorityDTO authority) {
        final String tenantId = TenantContext.getCurrentTenant();

        if (tenantId == null) {
            throw new IllegalStateException("No Store submitted in request to Creating Authority");
        }

        authorityRepository.findByIdAndTenantId(id, tenantId).ifPresentOrElse((value) -> {
           value.setAuthority(authority.getAuthority());
           value.setDescription(authority.getDescription());

            log.info("Modifying UserAuthority {}", authority);
            authorityRepository.save(value);
            jpaAuditService.audit(EDIT_AUTHORITY, String.format("Role %s modified", authority.getAuthority()));
        }, () -> {
            final String s = String.format("Role Id %s Name %s does not Exist", id, authority.getAuthority());
            log.error(s);
            throw new BusinessEntityNotFoundException(s);
        });
    }

    @Override
    public Page<AuthorityDTO> findAllByTenant(int pageNumber, int pageSize) {
        final String tenantId = TenantContext.getCurrentTenant();

        if (tenantId == null) {
            throw new IllegalStateException("No Store found");
        }

        return localFindByTenantId(pageNumber, pageSize, tenantId);
    }

    @Override
    public AuthorityDTO findById(String id) {
        return authorityRepository
                .findById(id)
                .map(Authority::toDTO).orElseThrow(() -> new BusinessEntityNotFoundException(String.format("Role with id %s not found", id)));
    }

    @Override
    public AuthorityDTO findByIdAndTenantId(String id) {
        final String tenantId = TenantContext.getCurrentTenant();

        if (tenantId == null) {
            throw new IllegalStateException("No Store found");
        }

        return authorityRepository
                .findByIdAndTenantId(id, tenantId)
                .map(Authority::toDTO).orElseThrow(() -> new BusinessEntityNotFoundException(String.format("Role with id %s not found", id)));
    }

    @Override
    public AuthorityDTO findByAuthorityAndTenantId(String authority) {
        final String tenantId = TenantContext.getCurrentTenant();

        if (tenantId == null) {
            throw new IllegalStateException("No Store found");
        }

        return authorityRepository
                .findByAuthorityAndTenantId(authority, tenantId)
                .map(Authority::toDTO).orElseThrow(() -> new BusinessEntityNotFoundException(String.format("Role with name %s not found", authority)));
    }

    @Override
    public Page<AuthorityDTO> findByTenantId(Integer pageNumber, Integer pageSize, String id) {
        return localFindByTenantId(pageNumber, pageSize, id);
    }

    @Override
    public Page<AuthorityDTO> search(Integer pageNumber, Integer pageSize, String search) {
        final String tenantId = TenantContext.getCurrentTenant();

        if (tenantId == null) {
            throw new IllegalStateException("No Store found");
        }

        log.info("Searching Authority by {}", search);
        Pageable pageable = PageRequestBuilder.build(pageNumber, pageSize);
        final Page<Authority> authorityPage = authorityRepository.findByAuthorityContainingAndTenantId(search, tenantId, pageable);
        return authorityPage.map(Authority::toDTO);
    }

    private Page<AuthorityDTO> localFindByTenantId(Integer pageNumber, Integer pageSize, String id) {
        log.info("Retrieving List of Roles for Tenant {}", id);
        Pageable pageable = PageRequestBuilder.build(pageNumber, pageSize);
        final Page<Authority> authorityPage = authorityRepository.findByTenantId(id, pageable);
        return authorityPage.map(Authority::toDTO);
    }
}
