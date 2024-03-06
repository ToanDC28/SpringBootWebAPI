package com.example.ClassRepo.repository;

import com.example.ClassRepo.entity.Class;
import com.example.ClassRepo.entity.TrainingForClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingForClassRepository extends JpaRepository<TrainingForClass, Integer> {
    List<TrainingForClass> findAllByMyClass(Class myclass);
    List<TrainingForClass> findAllBySyllabusId(Long id);
    List<TrainingForClass> findAllByTrainingProgramId(Integer id);

    List<TrainingForClass> findAllByUserId(Integer id);
}
