package io.factorialsystems.msscstore21users.dto;


import io.factorialsystems.msscstore21users.entity.Authority;
import io.factorialsystems.msscstore21users.security.JwtTokenWrapper;
import io.factorialsystems.msscstore21users.security.TenantContext;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDTO {
    @Null(message = "User DTO id cannot be set it will be generated automatically")
    private String id;
    private String authority;
    private Instant createdAt;
    private String createdBy;
    private String description;

    public Authority createEntity() {
        return Authority.builder()
                .id(id)
                .authority(authority)
                .disabled(Boolean.FALSE)
                .tenantId(TenantContext.getCurrentTenant())
                .createdBy(JwtTokenWrapper.getUserId())
                .description(description)
                .build();
    }
}
