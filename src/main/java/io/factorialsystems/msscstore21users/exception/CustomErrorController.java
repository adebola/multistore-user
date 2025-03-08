package io.factorialsystems.msscstore21users.exception;


import io.factorialsystems.msscstore21users.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class CustomErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException rex, WebRequest webRequest) {
        final String message = String.format("caused by %s", rex.getMessage());
        log.error(message);

        return ResponseEntity.badRequest().body(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .dateTime(LocalDateTime.now())
                        .message(message)
                        .path(webRequest.getDescription(false))
                        .build()
        );
    }

    @ExceptionHandler(BusinessEntityNotFoundException.class)
    ResponseEntity<ErrorResponseDTO> handleNotFoundException(BusinessEntityNotFoundException nfe, WebRequest webRequest) {
        final String message = String.format("caused by %s", nfe.getMessage());
        log.error(message);

        return ResponseEntity.badRequest().body(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .dateTime(LocalDateTime.now())
                        .message(message)
                        .path(webRequest.getDescription(false))
                        .build()
        );
    }

    @ExceptionHandler(BusinessEntityExistsException.class)
    ResponseEntity<ErrorResponseDTO> handleAlreadyExistsException(BusinessEntityExistsException bee, WebRequest webRequest) {
        final String message = String.format("caused by %s", bee.getMessage());
        log.error(message);

        return ResponseEntity.badRequest().body(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .dateTime(LocalDateTime.now())
                        .message(message)
                        .path(webRequest.getDescription(false))
                        .build()
        );
    }
}
