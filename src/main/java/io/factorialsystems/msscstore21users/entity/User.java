package io.factorialsystems.msscstore21users.entity;


import io.factorialsystems.msscstore21users.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private String id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "created_on")
    private Instant createdOn;

    private Boolean locked;
    private Boolean enabled;
    private String password;
    private String email;

    @Column(name = "avatar_image_url")
    private String avatarImageUrl;

    @Column(name = "tenant_id")
    private String tenantId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    @ToString.Exclude
    private Set<Authority> authorities;

    @Transient
    public UserDTO toShortDTO() {
        return UserDTO.builder()
                .id(id)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .createdOn(createdOn)
                .email(email)
                .avatarImageUrl(avatarImageUrl)
                .build();
    }

    @Transient
    public UserDTO toLongDTO() {
        return UserDTO.builder()
                .id(id)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .createdOn(createdOn)
                .email(email)
                .avatarImageUrl(avatarImageUrl)
                .authorities(
                        authorities.stream()
                                .map(Authority::toDTO)
                                .collect(Collectors.toSet())
                ).build();
    }
}
