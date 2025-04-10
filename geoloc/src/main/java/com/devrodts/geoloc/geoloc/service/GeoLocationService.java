package com.devrodts.geoloc.geoloc.service;
import com.devrodts.geoloc.geoloc.dto.GeoLocationResponseDTO;



public interface GeoLocationService {
    GeoLocationResponseDTO getGeoLocationByIp(String ip);
    GeoLocationResponseDTO refreshGeoLocationByIp(String ip);
}