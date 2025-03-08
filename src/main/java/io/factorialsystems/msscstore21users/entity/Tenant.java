package io.factorialsystems.msscstore21users.entity;

import io.factorialsystems.msscstore21users.dto.TenantResponseDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;

import java.time.Instant;

@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name = "tenants")
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {
    @Id
    @UuidGenerator
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "secret", nullable = false)
    private String secret;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "disabled", nullable = false)
    private Boolean disabled;

    @Transient
    public TenantResponseDTO toDTO() {
        return TenantResponseDTO.builder()
                .id(id)
                .name(name)
                .description(description)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .disabled(disabled)
                .build();

    }
}
