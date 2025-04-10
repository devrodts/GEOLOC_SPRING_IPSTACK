package com.devrodts.geoloc.geoloc;

import com.devrodts.geoloc.geoloc.dto.GeoLocationResponseDTO;
import com.devrodts.geoloc.geoloc.entity.GeoLocationEntity;
import com.devrodts.geoloc.geoloc.repository.GeoLocationRepository;
import com.devrodts.geoloc.geoloc.service.GeoLocationService;
import com.devrodts.geoloc.geoloc.service.GeoLocationServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeoLocationServiceTest {

    @Mock
    private GeoLocationRepository geoLocationRepository;
    
    @Mock
    private WebClient webClient;
    
    @Mock
    private ObjectMapper objectMapper;
    
    private GeoLocationService geoLocationService;
    
    @BeforeEach
    void setUp() {
        geoLocationService = new GeoLocationServiceImpl(webClient, geoLocationRepository, objectMapper);
        ReflectionTestUtils.setField(geoLocationService, "apiKey", "test_api_key");
        ReflectionTestUtils.setField(geoLocationService, "apiUrl", "http://test-api.com/");
    }
    
    @Test
    void getGeoLocationByIp_ValidIp_CacheHit() {
        
        String testIp = "8.8.8.8";
        GeoLocationEntity cachedLocation = createSampleGeoLocation(testIp);
        
        when(geoLocationRepository.findByIp(testIp)).thenReturn(Optional.of(cachedLocation));
        
        GeoLocationResponseDTO response = geoLocationService.getGeoLocationByIp(testIp);
        
        assertNotNull(response);
        assertEquals(testIp, response.ip());
        assertTrue(response.fromCache());
        verify(geoLocationRepository).findByIp(testIp);
        verify(webClient, never()).get();
    }
    
    @Test
    void getGeoLocationByIp_ValidIp_CacheMiss() throws Exception {
        
        String testIp = "8.8.8.8";
        JsonNode mockJsonNode = mock(JsonNode.class);
        
        when(geoLocationRepository.findByIp(testIp)).thenReturn(Optional.empty());
        
        WebClient.RequestHeadersUriSpec<?> uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        
        doReturn(uriSpec).when(webClient).get();
        doReturn(headersSpec).when(uriSpec).uri(anyString(), anyString(), anyString());
        doReturn(responseSpec).when(headersSpec).retrieve();
        doReturn(Mono.just("{}")).when(responseSpec).bodyToMono(String.class);
        
        when(objectMapper.readTree(anyString())).thenReturn(mockJsonNode);
        when(mockJsonNode.has("error")).thenReturn(false);
        when(mockJsonNode.has(anyString())).thenReturn(true);
        
        GeoLocationResponseDTO response = geoLocationService.getGeoLocationByIp(testIp);
        
        assertNotNull(response);
        verify(geoLocationRepository).findByIp(testIp);
        verify(webClient).get();
        verify(geoLocationRepository).save(any(GeoLocationEntity.class));
    }
    
    private GeoLocationEntity createSampleGeoLocation(String ip) {
        return GeoLocationEntity.builder()
                .ip(ip)
                .continent("North America")
                .continentCode("NA")
                .country("United States")
                .countryCode("US")
                .region("California")
                .regionCode("CA")
                .city("Mountain View")
                .zip("94043")
                .latitude(37.4223)
                .longitude(-122.0847)
                .timestamp(LocalDateTime.now())
                .isp("Google LLC")
                .org("Google LLC")
                .timezone("America/Los_Angeles")
                .build();
    }
}