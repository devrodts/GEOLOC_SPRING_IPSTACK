package com.devrodts.geoloc.geoloc.exception;

import com.devrodts.geoloc.geoloc.dto.ErrorResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeoLocationException.class)
    public ResponseEntity<ErrorResponseDTO> handleGeoLocationException(GeoLocationException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(ex.getMessage(), ex.getErrorCode());
        
        HttpStatus status = switch (ex.getErrorCode()) {
            case "INVALID_IP" -> HttpStatus.BAD_REQUEST;
            case "API_ERROR", "SERVICE_UNAVAILABLE" -> HttpStatus.SERVICE_UNAVAILABLE;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        
        return new ResponseEntity<>(errorResponse, status);
    }
    
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorResponseDTO> handleWebClientResponseException(WebClientResponseException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                "Error fetching geolocation data: " + ex.getMessage(),
                "EXTERNAL_API_ERROR"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                "An unexpected error occurred: " + ex.getMessage(),
                "INTERNAL_ERROR"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}