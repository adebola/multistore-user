package io.factorialsystems.msscstore21users.entity;

import io.factorialsystems.msscstore21users.dto.AuthorityDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "authorities")
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
    @Id
    private String id;
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

    private Boolean disabled;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    private Set<User> users;


    public Authority(String id, String authority, String tenantId) {
        this.id = id;
        this.authority = authority;
        this.tenantId = tenantId;
    }

    @Transient
    public AuthorityDTO toDTO() {
        return AuthorityDTO.builder()
                .id(id)
                .authority(authority)
                .build();
    }
}