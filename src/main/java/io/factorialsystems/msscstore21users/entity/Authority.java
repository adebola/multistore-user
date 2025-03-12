package io.factorialsystems.msscstore21users.entity;

import io.factorialsystems.msscstore21users.dto.AuthorityDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@ToString
@Table(name = "authorities")
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String id;

    @Column(name = "authority")
    private String authority;

    @Column(name = "tenant_id")
    private String tenantId;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    private Boolean disabled = false;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<User> users;


    public Authority(String id, String authority, String tenantId) {
        this.id = id;
        this.authority = authority;
        this.tenantId = tenantId;
        this.disabled = false;
    }

    @Transient
    public AuthorityDTO toDTO() {
        return AuthorityDTO.builder()
                .id(id)
                .authority(authority)
                .build();
    }
}