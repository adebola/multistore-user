package io.factorialsystems.msscstore21users.controller;


import io.factorialsystems.msscstore21users.dto.UserDTO;
import io.factorialsystems.msscstore21users.service.JpaUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final JpaUserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable String id) {
        log.info("Find User : {}", id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(@RequestParam int pageNumber, @RequestParam int pageSize) {
        log.info("Find All Tenants PageNumber {}, PageSize {}", pageNumber, pageSize);
        return ResponseEntity.ok(userService.findAll(pageNumber, pageSize));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @Valid @RequestBody UserDTO user) {
        log.info("Updating tenant: {}", user);
        userService.editUser(id, user);
    }
}