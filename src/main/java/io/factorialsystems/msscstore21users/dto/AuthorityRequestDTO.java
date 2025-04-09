package io.factorialsystems.msscstore21users.dto;

import jakarta.validation.constraints.NotEmpty;

public record AuthorityRequestDTO(@NotEmpty String[] roles) {}
