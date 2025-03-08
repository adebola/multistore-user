package io.factorialsystems.msscstore21users.controller;

import io.factorialsystems.msscstore21users.dto.AuthorityDTO;
import io.factorialsystems.msscstore21users.service.JpaAuthorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authority")
public class AuthorityController {
    private final JpaAuthorityService authorityService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER')")
    public ResponseEntity<Page<AuthorityDTO>> findAll(@RequestParam int pageNumber, @RequestParam int pageSize) {
        log.info("Find All Authorities in the system PageNumber {}, PageSize {}", pageNumber, pageSize);
        return ResponseEntity.ok(authorityService.findAll(pageNumber, pageSize));
    }

    @GetMapping("/tenant")
    @PreAuthorize("hasAnyRole('STORE_ADMIN', 'STORE_USER')")
    public ResponseEntity<Page<AuthorityDTO>> findAllByTenant(@RequestParam int pageNumber, @RequestParam int pageSize) {
        log.info("Find All Authorities PageNumber {}, PageSize {}", pageNumber, pageSize);
        return ResponseEntity.ok(authorityService.findAllByTenant(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER', 'STORE_ADMIN', 'STORE_USER')")
    public ResponseEntity<AuthorityDTO> findById(@PathVariable("id") String id) {
        log.info("Find Authority : {}", id);
        return ResponseEntity.ok(authorityService.findById(id));
    }
}
