package io.factorialsystems.msscstore21users.dto;

import io.factorialsystems.msscstore21users.entity.User;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private Instant createdOn;
    private String email;
    private Set<AuthorityDTO> authorities;

    public User toEntity() {
        return User.builder()
                .id(id)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .createdOn(createdOn)
                .email(email)
                .authorities(
                        authorities.stream()
                                .map(AuthorityDTO::createEntity)
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
