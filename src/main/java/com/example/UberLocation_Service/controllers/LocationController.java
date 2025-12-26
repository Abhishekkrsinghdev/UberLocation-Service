package com.example.UberLocation_Service.controllers;

import com.example.UberLocation_Service.dto.DriverLocationDto;
import com.example.UberLocation_Service.dto.NearbyDriversRequestDto;
import com.example.UberLocation_Service.dto.SaveDriverLocationRequestDto;
import com.example.UberLocation_Service.services.LocationService;
import org.springframework.data.geo.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/location")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService){
        this.locationService=locationService;
    }

    @PostMapping("/drivers")
    public ResponseEntity<Boolean> saveDriverLocation(@RequestBody SaveDriverLocationRequestDto saveDriverLocationRequestDto){
        try {
            Boolean response=locationService.saveDriverLocation(saveDriverLocationRequestDto.getDriverId(), saveDriverLocationRequestDto.getLatitude(), saveDriverLocationRequestDto.getLongitude());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/nearby/drivers")
    public ResponseEntity<List<DriverLocationDto>> getNearbyDrivers(@RequestBody NearbyDriversRequestDto nearbyDriversRequestDto){
       try{
           List<DriverLocationDto> drivers = locationService.getNearByDrivers(nearbyDriversRequestDto.getLatitude(), nearbyDriversRequestDto.getLongitude());
           return new ResponseEntity<>(drivers, HttpStatus.OK);
       } catch (Exception e) {
           System.out.println(e.getMessage());
           return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
}
