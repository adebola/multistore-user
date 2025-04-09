package io.factorialsystems.msscstore21users.controller;


import io.factorialsystems.msscstore21users.dto.AuthorityRequestDTO;
import io.factorialsystems.msscstore21users.dto.UserDTO;
import io.factorialsystems.msscstore21users.service.JpaUserService;
import io.factorialsystems.msscstore21users.utils.Constants;
import io.factorialsystems.msscstore21users.utils.JwtTokenWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final JpaUserService userService;

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER')")
    public ResponseEntity<UserDTO> findById(@PathVariable String id) {
        log.info("Find User : {}", id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/self")
    public ResponseEntity<UserDTO> findSelf() {
        return ResponseEntity.ok(userService.findById(JwtTokenWrapper.getUserId()));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN')")
    public ResponseEntity<Page<UserDTO>> findAll(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        log.info("Find All Users PageNumber {}, PageSize {}", pageNumber, pageSize);
        return ResponseEntity.ok(userService.findAll(pageNumber, pageSize));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN', 'STORE_ADMIN', 'STORE_USER_ADMIN')")
    public ResponseEntity<Page<UserDTO>> findByTenant(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        log.info("Find All Users by Tenant PageNumber {}, PageSize {}", pageNumber, pageSize);
        return ResponseEntity.ok(userService.findAllByTenant(pageNumber, pageSize));
    }

    @GetMapping("/tenant/{id}")
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN')")
    public ResponseEntity<Page<UserDTO>> findByTenantId(@RequestParam Integer pageNumber, @RequestParam Integer pageSize, @PathVariable String id) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        log.info("Find All Users by Tenant for Admin PageNumber {}, PageSize {}", pageNumber, pageSize);
        return ResponseEntity.ok(userService.findByTenantId(pageNumber, pageSize, id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN', 'STORE_ADMIN', 'STORE_USER_ADMIN')")
    public ResponseEntity<UserDTO> findByIdAndTenantId(@PathVariable("id") String id) {
        log.info("Find Authority and Tenant: {}", id);
        return ResponseEntity.ok(userService.findByIdAndTenantId(id));
    }

    @PutMapping("/{id}")

    public ResponseEntity<UserDTO> update(@PathVariable String id, @Valid @RequestBody UserDTO user) {
        log.info("UserController: Updating User: {}", user.getUserName());
        return ResponseEntity.ok(userService.editUser(id, user));
    }

    @PostMapping("/{id}/avatar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN', 'STORE_ADMIN', 'STORE_USER_ADMIN')")
    public void uploadAvatar(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        log.info("Updating avatar for user: {}", id);
        userService.uploadAvatar(id, file);
    }

    @PutMapping("/{id}/addrole")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN', 'STORE_ADMIN', 'STORE_USER_ADMIN')")
    public void addRoles(@PathVariable String id, @RequestBody AuthorityRequestDTO roles) {
        log.info("Adding roles: {} to {}", roles.roles(), id);
        userService.addRoles(id, roles.roles());
    }

    @PutMapping("/{id}/removerole")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('MULTISTORE_ADMIN', 'MULTISTORE_USER_ADMIN', 'STORE_ADMIN', 'STORE_USER_ADMIN')")
    public void removeRoles(@PathVariable String id, @RequestBody AuthorityRequestDTO roles) {
        log.info("Removing roles: {} from {}", roles.roles(), id);
        userService.removeRoles(id, roles.roles());
    }
}