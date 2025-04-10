package com.devrodts.geoloc.geoloc.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
    String message,
    String code,
    LocalDateTime timestamp
) {
    public static ErrorResponseDTO of(String message, String code) {
        return new ErrorResponseDTO(message, code, LocalDateTime.now());
    }
} 
