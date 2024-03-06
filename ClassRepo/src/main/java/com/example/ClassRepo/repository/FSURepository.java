package com.example.ClassRepo.repository;

import com.example.ClassRepo.entity.FSU;
import com.example.ClassRepo.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FSURepository extends JpaRepository<FSU, Integer> {
    FSU findFSUByName(String name);
}
