package com.example.ClassRepo.repository;

import com.example.ClassRepo.entity.Location;
import com.example.ClassRepo.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Location findLocationByLocation(String Location);
}
