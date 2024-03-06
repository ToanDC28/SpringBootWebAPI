package com.team2.trainingprogramrepo.repository;

import com.team2.trainingprogramrepo.entity.TrainingProgram;
import com.team2.trainingprogramrepo.entity.TrainingSyllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TrainingSyllabusRepository extends JpaRepository<TrainingSyllabus, Integer> {
    @Transactional
    void deleteByTrainingProgram(TrainingProgram trainingProgram);
}
