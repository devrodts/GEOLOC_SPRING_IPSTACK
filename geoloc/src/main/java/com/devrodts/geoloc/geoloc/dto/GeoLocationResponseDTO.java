package com.devrodts.geoloc.geoloc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

/**
 * DTO para transferência de dados de geolocalização entre camadas.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GeoLocationResponseDTO(
    String ip,
    String continent,
    String continentCode,
    String country,
    String countryCode,
    String region,
    String regionCode,
    String city,
    String zip,
    Double latitude,
    Double longitude,
    LocalDateTime timestamp,
    String isp,
    String org,
    String timezone,
    Boolean fromCache
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String ip;
        private String continent;
        private String continentCode;
        private String country;
        private String countryCode;
        private String region;
        private String regionCode;
        private String city;
        private String zip;
        private Double latitude;
        private Double longitude;
        private LocalDateTime timestamp;
        private String isp;
        private String org;
        private String timezone;
        private Boolean fromCache;

        public Builder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder continent(String continent) {
            this.continent = continent;
            return this;
        }

        public Builder continentCode(String continentCode) {
            this.continentCode = continentCode;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder countryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public Builder regionCode(String regionCode) {
            this.regionCode = regionCode;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder zip(String zip) {
            this.zip = zip;
            return this;
        }

        public Builder latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder isp(String isp) {
            this.isp = isp;
            return this;
        }

        public Builder org(String org) {
            this.org = org;
            return this;
        }

        public Builder timezone(String timezone) {
            this.timezone = timezone;
            return this;
        }

        public Builder fromCache(Boolean fromCache) {
            this.fromCache = fromCache;
            return this;
        }

        public GeoLocationResponseDTO build() {
            return new GeoLocationResponseDTO(
                ip, continent, continentCode, country, countryCode,
                region, regionCode, city, zip, latitude, longitude,
                timestamp, isp, org, timezone, fromCache
            );
        }
    }
}
