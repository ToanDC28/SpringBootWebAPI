package com.example.ClassRepo.service;

import com.example.ClassRepo.dto.SlotDTO;
import com.example.ClassRepo.entity.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService  {
   Location getLocationsByLocation(String location);

   <S extends Location> S save(S entity);
}
