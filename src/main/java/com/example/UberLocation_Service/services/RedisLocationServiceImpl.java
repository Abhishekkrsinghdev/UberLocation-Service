package com.example.UberLocation_Service.services;

import com.example.UberLocation_Service.dto.DriverLocationDto;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisLocationServiceImpl implements LocationService{
    private static final String DRIVER_GEO_OPS_KEY="drivers";
    private static final Double SEARCH_RADIUS=5.0;
    private final StringRedisTemplate stringRedisTemplate;

    public RedisLocationServiceImpl(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate=stringRedisTemplate;
    }

    @Override
    public Boolean saveDriverLocation(String driverId, Double latitude, Double longitude) {
        GeoOperations<String,String> geoOps=stringRedisTemplate.opsForGeo();
        geoOps.add(
                DRIVER_GEO_OPS_KEY,
                new RedisGeoCommands.GeoLocation<>(
                        driverId,
                        new Point(
                               longitude,
                               latitude
                        )
                )
        );
        return true;
    }

    @Override
    public List<DriverLocationDto> getNearByDrivers(Double latitude, Double longitude) {
        GeoOperations<String, String> geoOps = stringRedisTemplate.opsForGeo();
        Distance radius = new Distance(SEARCH_RADIUS, Metrics.KILOMETERS);
        Circle within = new Circle(new Point(longitude, latitude), radius);

        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeCoordinates()
                .includeDistance()
                .sortAscending();

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoOps.radius(DRIVER_GEO_OPS_KEY, within, args);
        List<DriverLocationDto> drivers = new ArrayList<>();
        if (results != null) {
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {
                drivers.add(DriverLocationDto.builder()
                        .driverId(result.getContent().getName())
                        .latitude(result.getContent().getPoint().getY())
                        .longitude(result.getContent().getPoint().getX())
                        .build());
            }
        }
        return drivers;
    }
}
