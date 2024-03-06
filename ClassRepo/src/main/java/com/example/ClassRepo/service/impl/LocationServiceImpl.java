package com.example.ClassRepo.service.impl;

import com.example.ClassRepo.entity.Location;
import com.example.ClassRepo.repository.LocationRepository;
import com.example.ClassRepo.service.LocationService;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {


    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {this.locationRepository = locationRepository;}

    @Override
    public Location getLocationsByLocation(String location) {
        return locationRepository.findLocationByLocation(location);
    }
    @Override
    public <S extends Location> S save(S entity) {
        return locationRepository.save(entity);
    }
}
