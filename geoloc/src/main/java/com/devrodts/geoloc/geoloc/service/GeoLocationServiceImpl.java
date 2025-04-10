package com.devrodts.geoloc.geoloc.service;

import com.devrodts.geoloc.geoloc.dto.GeoLocationResponseDTO;
import com.devrodts.geoloc.geoloc.entity.GeoLocationEntity;
import com.devrodts.geoloc.geoloc.exception.GeoLocationException;
import com.devrodts.geoloc.geoloc.repository.GeoLocationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GeoLocationServiceImpl implements GeoLocationService {

    private static final Logger log = LoggerFactory.getLogger(GeoLocationServiceImpl.class);

    private final WebClient webClient;
    private final GeoLocationRepository geoLocationRepository;
    private final ObjectMapper objectMapper;

    @Value("${ipstack.api.key}")
    private String apiKey;

    @Value("${ipstack.api.url:http://api.ipstack.com/}")
    private String apiUrl;

    public GeoLocationServiceImpl(
            WebClient webClient,
            GeoLocationRepository geoLocationRepository,
            ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.geoLocationRepository = geoLocationRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public GeoLocationResponseDTO getGeoLocationByIp(String ip) {
        log.info("Buscando geolocalização para o IP: {}", ip);

        Optional<GeoLocationEntity> cachedEntity = geoLocationRepository.findByIp(ip);

        if (cachedEntity.isPresent()) {
            log.info("Dados encontrados no cache para o IP: {}", ip);
            return mapToDto(cachedEntity.get(), true);
        }

        log.info("Dados não encontrados no cache, buscando da API para o IP: {}", ip);
        return refreshGeoLocationByIp(ip);
    }

    @Override
    public GeoLocationResponseDTO refreshGeoLocationByIp(String ip) {
        log.info("Atualizando geolocalização para o IP: {}", ip);

        try {

            String response = webClient.get()
                    .uri(apiUrl + "{ip}?access_key={apiKey}", ip, apiKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response == null || response.isEmpty()) {
                throw new GeoLocationException("Resposta vazia da API para o IP: " + ip);
            }


            JsonNode jsonNode = objectMapper.readTree(response);


            if (jsonNode.has("error")) {
                String errorInfo = jsonNode.get("error").get("info").asText();
                throw new GeoLocationException("Erro na API: " + errorInfo);
            }


            GeoLocationEntity entity = parseApiResponse(jsonNode);

            // Salva no cache
            geoLocationRepository.save(entity);

            return mapToDto(entity, false);
        } catch (Exception e) {
            log.error("Erro ao buscar geolocalização para o IP: {}", ip, e);
            throw new GeoLocationException("Erro ao buscar geolocalização: " + e.getMessage(), e.getMessage());
        }
    }

    private GeoLocationEntity parseApiResponse(JsonNode jsonNode) {
        return GeoLocationEntity.builder()
                .ip(getJsonTextValue(jsonNode, "ip"))
                .continent(getJsonTextValue(jsonNode, "continent_name"))
                .continentCode(getJsonTextValue(jsonNode, "continent_code"))
                .country(getJsonTextValue(jsonNode, "country_name"))
                .countryCode(getJsonTextValue(jsonNode, "country_code"))
                .region(getJsonTextValue(jsonNode, "region_name"))
                .regionCode(getJsonTextValue(jsonNode, "region_code"))
                .city(getJsonTextValue(jsonNode, "city"))
                .zip(getJsonTextValue(jsonNode, "zip"))
                .latitude(getJsonDoubleValue(jsonNode, "latitude"))
                .longitude(getJsonDoubleValue(jsonNode, "longitude"))
                .timestamp(LocalDateTime.now())
                .isp(getJsonTextValue(jsonNode, "connection", "isp"))
                .org(getJsonTextValue(jsonNode, "connection", "organization"))
                .timezone(getJsonTextValue(jsonNode, "time_zone", "code"))
                .build();
    }

    private String getJsonTextValue(JsonNode node, String... path) {
        JsonNode current = node;
        for (String key : path) {
            if (current == null || !current.has(key)) {
                return null;
            }
            current = current.get(key);
        }
        return current != null ? current.asText() : null;
    }

    private Double getJsonDoubleValue(JsonNode node, String field) {
        return node.has(field) ? node.get(field).asDouble() : null;
    }

    private GeoLocationResponseDTO mapToDto(GeoLocationEntity entity, boolean fromCache) {
        return GeoLocationResponseDTO.builder()
                .ip(entity.ip())
                .continent(entity.continent())
                .continentCode(entity.continentCode())
                .country(entity.country())
                .countryCode(entity.countryCode())
                .region(entity.region())
                .regionCode(entity.regionCode())
                .city(entity.city())
                .zip(entity.zip())
                .latitude(entity.latitude())
                .longitude(entity.longitude())
                .timestamp(entity.timestamp())
                .isp(entity.isp())
                .org(entity.org())
                .timezone(entity.timezone())
                .fromCache(fromCache)
                .build();
    }
}