package com.example.ClassRepo.repository;

import com.example.ClassRepo.entity.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ClassRepository extends JpaRepository<Class, Integer> {

    Page<Class> findAll(Specification<Class> spec, Pageable pageable);
    Optional<Class> findById(Integer id);
    boolean existsClassByClassName(String className);
}

