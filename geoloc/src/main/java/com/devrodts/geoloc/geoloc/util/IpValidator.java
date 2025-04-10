package com.devrodts.geoloc.geoloc.util;


import com.devrodts.geoloc.geoloc.exception.GeoLocationException;

import java.util.regex.Pattern;

/**
 * Utilitário para validação de endereços IP.
 */
public class IpValidator {
    
    private static final Pattern IPV4_PATTERN = 
        Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");
    
    private static final Pattern IPV6_PATTERN = 
        Pattern.compile("^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    
    private IpValidator() {
        // Classe utilitária não deve ser instanciada
    }
    
    /**
     * Valida se uma string é um endereço IP válido (IPv4 ou IPv6).
     * 
     * @param ip String a ser validada
     * @throws GeoLocationException se o IP for inválido
     */
    public static void validateIp(String ip) {
        if (ip == null || ip.isBlank() || (!IPV4_PATTERN.matcher(ip).matches() && !IPV6_PATTERN.matcher(ip).matches())) {
            throw GeoLocationException.invalidIpAddress();
        }
    }
}