package io.factorialsystems.msscstore21users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorResponseDTO {
    private String path;
    private HttpStatus status;
    private LocalDateTime dateTime;
    private String message;
}
