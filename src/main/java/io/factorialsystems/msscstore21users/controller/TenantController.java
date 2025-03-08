package io.factorialsystems.msscstore21users.controller;


import io.factorialsystems.msscstore21users.dto.TenantCreateRequestDTO;
import io.factorialsystems.msscstore21users.dto.TenantResponseDTO;
import io.factorialsystems.msscstore21users.dto.TenantUpdateRequestDTO;
import io.factorialsystems.msscstore21users.service.JpaTenantServiceImpl;
import io.factorialsystems.msscstore21users.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tenant")
public class TenantController {
    private final JpaTenantServiceImpl tenantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_TENANT_ADMIN')")
    public void save(@Valid @RequestBody TenantCreateRequestDTO tenant) {
        log.info("Saving tenant: {}", tenant);
        tenantService.save(tenant);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_TENANT_ADMIN')")
    public void update(@PathVariable String id, @Valid @RequestBody TenantUpdateRequestDTO tenant) {
        log.info("Updating tenant: {}", tenant);
        tenantService.update(id, tenant);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_TENANT_ADMIN')")
    public ResponseEntity<TenantResponseDTO> findById(@PathVariable String id) {
        log.info("Find Tenant : {}", id);
        return ResponseEntity.ok(tenantService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_TENANT_ADMIN')")
    public ResponseEntity<Page<TenantResponseDTO>> findAll(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                           @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        log.info("Find All Tenants PageNumber {}, PageSize {}", pageNumber, pageSize);
        return ResponseEntity.ok(tenantService.findAll(pageNumber, pageSize));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_TENANT_ADMIN')")
    public ResponseEntity<Page<TenantResponseDTO>> search(@RequestParam(value = "search", required = true) String search,
                                                          @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        log.info("Searching for Tenants with search string: {}, PageNumber {}, PageSize {}", search, pageNumber, pageSize);
        return ResponseEntity.ok(tenantService.search(pageNumber, pageSize, search));
    }

    @PutMapping("/{id}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_TENANT_ADMIN')")
    public void disable(@PathVariable String id) {
        log.info("Disabling Tenant : {}", id);
        tenantService.disable(id);
    }

    @PutMapping("/{id}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_TENANT_ADMIN')")
    public void enable(@PathVariable String id) {
        log.info("Enabling Tenant : {}", id);
        tenantService.enable(id);
    }
}
