package io.factorialsystems.msscstore21users.dto;

public record AuditRequestDTO (String action, String message, String userName, String tenantId) { }
