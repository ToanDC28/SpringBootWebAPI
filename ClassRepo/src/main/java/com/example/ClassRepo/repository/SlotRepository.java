package com.example.ClassRepo.repository;

import com.example.ClassRepo.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer> {
    Slot findSlotByClasstime(String slot);
}

