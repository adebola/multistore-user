package io.factorialsystems.msscstore21users.service;

import io.factorialsystems.msscstore21users.dto.AuthorityDTO;
import io.factorialsystems.msscstore21users.entity.Authority;
import io.factorialsystems.msscstore21users.repository.AuthorityRepository;
import io.factorialsystems.msscstore21users.security.JwtTokenWrapper;
import io.factorialsystems.msscstore21users.security.TenantContext;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@CommonsLog
@SpringBootTest
class JpaAuthorityServiceImplTest {

    @Autowired
    private JpaAuthorityService authorityService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    void findAll() {
        final Page<AuthorityDTO> all = authorityService.findAll(1, 20);
        assertThat(all.getTotalElements()).isGreaterThan(1);
        AuthorityDTO  authority = all.getContent().get(0);
        assertThat(authority.getAuthority()).isNotEmpty();
        assertThat(authority.getId()).isNotEmpty();
        log.info(authority.toString());

    }

    @Test
    @Transactional
    @Rollback
    void createAuthority() {

        try (MockedStatic<TenantContext> t = Mockito.mockStatic(TenantContext.class)) {
            final String id = "875008ad-66ae-46ad-bd9b-c88dfa173206";
            t.when(TenantContext::getCurrentTenant).thenReturn(id);
            assertThat(TenantContext.getCurrentTenant()).isEqualTo(id);
            log.info(TenantContext.getCurrentTenant());


            try (MockedStatic<JwtTokenWrapper> j = Mockito.mockStatic(JwtTokenWrapper.class)) {
                final String userName = "__debug__";
                t.when(JwtTokenWrapper::getUserName).thenReturn(userName);
                assertThat(JwtTokenWrapper.getUserName()).isEqualTo(userName);
                t.when(JwtTokenWrapper::getTenant).thenReturn(id);
                assertThat(JwtTokenWrapper.getTenant()).isEqualTo(id);

                AuthorityDTO authority = AuthorityDTO.builder()
                        .authority("ROLE_TEST")
                        .build();

                authorityService.createAuthority(authority);

                Optional<Authority> authority1 = authorityRepository.findByAuthorityAndTenantId(authority.getAuthority(), TenantContext.getCurrentTenant());
                assertThat(authority1.isPresent()).isTrue();
                Authority authority2 = authority1.get();
                assertThat(authority2.getAuthority()).isEqualTo(authority.getAuthority());
                assertThat(authority2.getTenantId()).isEqualTo(TenantContext.getCurrentTenant());
                assertThat(authority2.getCreatedBy()).isEqualTo(JwtTokenWrapper.getUserName());
                log.info(authority2.toString());
            }
        }
    }

    @Test
    @Rollback
    @Transactional
    void editAuthority() {
        try (MockedStatic<TenantContext> t = Mockito.mockStatic(TenantContext.class)) {
            final String id = "92f0e27e-f9a8-11ef-a5fc-ece4b0c4c58e";
            t.when(TenantContext::getCurrentTenant).thenReturn(id);
            assertThat(TenantContext.getCurrentTenant()).isEqualTo(id);
            log.info(TenantContext.getCurrentTenant());


            try (MockedStatic<JwtTokenWrapper> j = Mockito.mockStatic(JwtTokenWrapper.class)) {
                final String userName = "__debug__";
//                final String userName = "Admin";
                t.when(JwtTokenWrapper::getUserName).thenReturn(userName);
                assertThat(JwtTokenWrapper.getUserName()).isEqualTo(userName);
                t.when(JwtTokenWrapper::getTenant).thenReturn(id);
                assertThat(JwtTokenWrapper.getTenant()).isEqualTo(id);

                final String authorityId = "ca4b4b9a-f9af-11ef-a5fc-ece4b0c4c58e";
                final AuthorityDTO byIdAndTenantId = authorityService.findByIdAndTenantId(authorityId);
                assertThat(byIdAndTenantId).isNotNull();
                assertThat(byIdAndTenantId.getId()).isEqualTo(authorityId);

                final String authorityName = "ROLE_TEST_2";
//                final String authorityName = "MULTISTORE_ADMIN";

                byIdAndTenantId.setAuthority(authorityName);
                log.info(byIdAndTenantId.toString());
                authorityService.editAuthority(authorityId, byIdAndTenantId);

                final AuthorityDTO byIdAndTenantId1 = authorityService.findByIdAndTenantId(authorityId);
                assertThat(byIdAndTenantId1).isNotNull();
                assertThat(byIdAndTenantId1.getAuthority()).isEqualTo(authorityName);
                assertThat(byIdAndTenantId1.getId()).isEqualTo(authorityId);

                log.info(byIdAndTenantId.toString());
                log.info(byIdAndTenantId1.toString());
            }
        }

    }

    @Test
    void findAllByTenant() {
        final String id = "92f0e27e-f9a8-11ef-a5fc-ece4b0c4c58e";

        try (MockedStatic<TenantContext> t = Mockito.mockStatic(TenantContext.class)) {
            t.when(TenantContext::getCurrentTenant).thenReturn(id);
            assertThat(TenantContext.getCurrentTenant()).isEqualTo(id);
            log.info(TenantContext.getCurrentTenant());

            final Page<AuthorityDTO> all = authorityService.findAllByTenant(1, 20);
            assertThat(all.getTotalElements()).isGreaterThan(1);
            AuthorityDTO  authority = all.getContent().get(0);
            assertThat(authority.getAuthority()).isNotEmpty();
            assertThat(authority.getId()).isNotEmpty();
            log.info(authority.toString());
        }
    }

    @Test
    void findById() {
        final String id = "ca4b4b9a-f9af-11ef-a5fc-ece4b0c4c58e";
        final AuthorityDTO byId = authorityService.findById(id);
        assertThat(byId).isNotNull();
        assertThat(byId.getId()).isEqualTo(id);
        log.info(byId.toString());
    }

    @Test
    void findByIdAndTenantId() {
        final String id = "ca4b4b9a-f9af-11ef-a5fc-ece4b0c4c58e";
        final String tenantId = "92f0e27e-f9a8-11ef-a5fc-ece4b0c4c58e";

        try (MockedStatic<TenantContext> t = Mockito.mockStatic(TenantContext.class)) {
            t.when(TenantContext::getCurrentTenant).thenReturn(tenantId);
            assertThat(TenantContext.getCurrentTenant()).isEqualTo(tenantId);
            log.info(TenantContext.getCurrentTenant());

            final AuthorityDTO byId = authorityService.findByIdAndTenantId(id);

            assertThat(byId).isNotNull();
            assertThat(byId.getId()).isEqualTo(id);
            log.info(byId.toString());
        }
    }

    @Test
    void findByAuthorityAndTenantId() {

        final String authority = "MULTISTORE_ADMIN";
        final String tenantId = "92f0e27e-f9a8-11ef-a5fc-ece4b0c4c58e";

        try (MockedStatic<TenantContext> t = Mockito.mockStatic(TenantContext.class)) {
            t.when(TenantContext::getCurrentTenant).thenReturn(tenantId);
            assertThat(TenantContext.getCurrentTenant()).isEqualTo(tenantId);
            log.info(TenantContext.getCurrentTenant());

            final AuthorityDTO byId = authorityService.findByAuthorityAndTenantId(authority);

            assertThat(byId).isNotNull();
            assertThat(byId.getAuthority()).isEqualTo(authority);
            log.info(byId.toString());
        }

    }
}