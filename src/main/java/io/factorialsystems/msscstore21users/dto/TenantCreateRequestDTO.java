package io.factorialsystems.msscstore21users.dto;


import io.factorialsystems.msscstore21users.entity.Tenant;
import io.factorialsystems.msscstore21users.security.JwtTokenWrapper;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TenantCreateRequestDTO {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private String secret;


    public Tenant toEntity() {

        return Tenant.builder()
                .name(name)
                .description(description)
                .createdBy(JwtTokenWrapper.getUserName())
                .disabled(false)
//                .createdAt(Instant.now())
                .build();
    }
}
