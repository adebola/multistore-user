package io.factorialsystems.msscstore21users.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TenantResponseDTO {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private String createdBy;
    private Boolean disabled;
}
