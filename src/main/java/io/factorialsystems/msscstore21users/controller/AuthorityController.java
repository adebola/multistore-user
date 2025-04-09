package io.factorialsystems.msscstore21users.controller;

import io.factorialsystems.msscstore21users.dto.AuthorityDTO;
import io.factorialsystems.msscstore21users.service.JpaAuthorityService;
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
@RequestMapping("/api/v1/authority")
public class AuthorityController {
    private final JpaAuthorityService authorityService;

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN')")
    public ResponseEntity<AuthorityDTO> findById(@PathVariable("id") String id) {
        log.info("Find Authority : {}", id);
        return ResponseEntity.ok(authorityService.findById(id));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN')")
    public ResponseEntity<Page<AuthorityDTO>> findAll(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        log.info("Find All Authorities in the system PageNumber {}, PageSize {}", pageNumber, pageSize);
        return ResponseEntity.ok(authorityService.findAll(pageNumber, pageSize));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN', 'STORE_ADMIN', 'STORE_USER_ADMIN')")
    public ResponseEntity<Page<AuthorityDTO>> findByTenant(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        log.info("Find All Authorities by Tenant PageNumber {}, PageSize {}", pageNumber, pageSize);
        return ResponseEntity.ok(authorityService.findAllByTenant(pageNumber, pageSize));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN', 'STORE_ADMIN', 'STORE_USER_ADMIN')")
    public ResponseEntity<Page<AuthorityDTO>> search(@RequestParam Integer pageNumber, @RequestParam Integer pageSize, @RequestParam String search) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        return ResponseEntity.ok(authorityService.search(pageNumber, pageSize, search));
    }


    @GetMapping("/tenant/{id}")
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN')")
    public ResponseEntity<Page<AuthorityDTO>> findByTenantId(@RequestParam Integer pageNumber, @RequestParam Integer pageSize, @PathVariable String id) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        log.info("Find All Roles by Tenant for Admin PageNumber {}, PageSize {}", pageNumber, pageSize);
        return ResponseEntity.ok(authorityService.findByTenantId(pageNumber, pageSize, id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN', 'STORE_ADMIN', 'STORE_USER_ADMIN')")
    public ResponseEntity<AuthorityDTO> findByIdAndTenantId(@PathVariable("id") String id) {
        log.info("Find Authority and Tenant: {}", id);
        return ResponseEntity.ok(authorityService.findByIdAndTenantId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN', 'STORE_ADMIN', 'STORE_USER_ADMIN')")
    public void createAuthority(@Valid @RequestBody AuthorityDTO authority) {
        log.info("Create Authority : {}", authority);
        authorityService.createAuthority(authority);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN', 'STORE_ADMIN', 'STORE_USER_ADMIN')")
    public void editAuthority(@PathVariable("id") String id, @Valid @RequestBody AuthorityDTO authority) {
        log.info("Edit Authority : {}", authority);
        authorityService.editAuthority(id, authority);
    }
}
