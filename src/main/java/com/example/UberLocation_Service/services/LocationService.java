package com.example.UberLocation_Service.services;

import com.example.UberLocation_Service.dto.DriverLocationDto;

import java.util.List;

public interface LocationService {
    public Boolean saveDriverLocation(String driverId,Double latitude,Double longitude);
    public List<DriverLocationDto> getNearByDrivers(Double latitude,Double longitude);
}
