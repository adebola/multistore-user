package io.factorialsystems.msscstore21users.service;

import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@CommonsLog
@SpringBootTest
class JpaTenantServiceImplTest {

    @Autowired
    private JpaTenantService tenantService;

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void findAll() {
    }

    @Test
    void search() {
        var results = tenantService.search(1, 20, "multi");
        assertEquals(1, results.getTotalElements());
        var dto = results.getContent().get(0);
        log.info(dto.toString());
    }

    @Test
    void findById() {
    }

    @Test
    void disable() {
    }

    @Test
    void enable() {
    }
}