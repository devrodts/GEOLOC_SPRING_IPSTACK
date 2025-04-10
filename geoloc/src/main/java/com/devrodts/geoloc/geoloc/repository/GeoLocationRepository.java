package com.devrodts.geoloc.geoloc.repository;

import com.devrodts.geoloc.geoloc.entity.GeoLocationEntity;

import java.util.Optional;

public interface GeoLocationRepository {

    void save(GeoLocationEntity entity);
    Optional<GeoLocationEntity> findByIp(String ip);
    void delete(String ip);
} 