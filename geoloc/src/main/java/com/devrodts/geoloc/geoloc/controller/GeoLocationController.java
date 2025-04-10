package com.devrodts.geoloc.geoloc.controller;

import com.devrodts.geoloc.geoloc.dto.GeoLocationResponseDTO;
import com.devrodts.geoloc.geoloc.service.GeoLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/geolocation")
@Tag(name = "GeoLocation API", description = "API for IP address geolocation services")
public class GeoLocationController {

    private final GeoLocationService geoLocationService;

    public GeoLocationController(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }

    @Operation(
            summary = "Get geolocation by IP",
            description = "Retrieve geolocation information for a specific IP address"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved geolocation data"),
            @ApiResponse(responseCode = "404", description = "Geolocation data not found")
    })
    @GetMapping("/ip/{ip}")
    public ResponseEntity<GeoLocationResponseDTO> getGeoLocationByIp(
            @Parameter(description = "IP address to lookup", example = "192.168.1.1")
            @PathVariable String ip) {
        GeoLocationResponseDTO response = geoLocationService.getGeoLocationByIp(ip);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Refresh geolocation data",
            description = "Force refresh of geolocation information for a specific IP address"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Geolocation data refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid IP address format")
    })
    @GetMapping("/ip/{ip}/refresh")
    public ResponseEntity<GeoLocationResponseDTO> refreshGeoLocationByIp(
            @Parameter(description = "IP address to refresh", example = "8.8.8.8")
            @PathVariable String ip) {
        GeoLocationResponseDTO response = geoLocationService.refreshGeoLocationByIp(ip);
        return ResponseEntity.ok(response);
    }
}