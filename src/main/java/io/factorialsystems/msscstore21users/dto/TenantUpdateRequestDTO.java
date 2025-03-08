package io.factorialsystems.msscstore21users.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TenantUpdateRequestDTO {
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    private String secret;
    private Boolean disabled;
}
