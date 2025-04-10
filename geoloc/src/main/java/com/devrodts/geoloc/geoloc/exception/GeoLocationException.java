package com.devrodts.geoloc.geoloc.exception;


public class GeoLocationException extends RuntimeException {
    
    private final String errorCode;
    
    public GeoLocationException(String message) {
        super(message);
        this.errorCode = "INTERNAL_ERROR";
    }
    
    public GeoLocationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Cria uma exceção para endereço IP inválido.
     * 
     * @return GeoLocationException com mensagem apropriada
     */
    public static GeoLocationException invalidIpAddress() {
        return new GeoLocationException("Endereço IP inválido", "INVALID_IP");
    }
    
    public static GeoLocationException apiError(String message) {
        return new GeoLocationException(message, "API_ERROR");
    }
    
    public static GeoLocationException serviceUnavailable() {
        return new GeoLocationException("Serviço temporariamente indisponível", "SERVICE_UNAVAILABLE");
    }
} 