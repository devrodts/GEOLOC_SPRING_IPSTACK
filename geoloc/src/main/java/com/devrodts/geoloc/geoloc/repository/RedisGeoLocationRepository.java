package com.devrodts.geoloc.geoloc.repository;
import com.devrodts.geoloc.geoloc.entity.GeoLocationEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
public class RedisGeoLocationRepository implements GeoLocationRepository {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY_PREFIX = "geo:";
    private static final Duration CACHE_TTL = Duration.ofHours(24);
    
    public RedisGeoLocationRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    public void save(GeoLocationEntity entity) {
        String key = buildKey(entity.ip());
        redisTemplate.opsForValue().set(key, entity, CACHE_TTL);
    }
    
    @Override
    public Optional<GeoLocationEntity> findByIp(String ip) {
        String key = buildKey(ip);
        Object cachedValue = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(cachedValue)
                .map(value -> (GeoLocationEntity) value);
    }
    
    @Override
    public void delete(String ip) {
        String key = buildKey(ip);
        redisTemplate.delete(key);
    }
    
    private String buildKey(String ip) {
        return CACHE_KEY_PREFIX + ip;
    }
}
