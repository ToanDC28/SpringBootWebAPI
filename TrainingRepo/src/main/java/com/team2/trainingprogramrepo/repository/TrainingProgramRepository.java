package com.team2.trainingprogramrepo.repository;

import com.team2.trainingprogramrepo.dto.TrainingSyllabusDto;
import com.team2.trainingprogramrepo.entity.TrainingProgram;
import com.team2.trainingprogramrepo.entity.TrainingSyllabus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Integer> {
    //    @Query("SELECT tr FROM TrainingProgram tr WHERE tr.name LIKE %:keyword%")
//    List<TrainingProgram> findByKeyword(@Param("keyword") String keyword);

    Page<TrainingProgram> findAll(Pageable pageable);
    Page<TrainingProgram> findAll(Specification<TrainingProgram> spec, Pageable pageable);

    TrainingProgram findTopByOrderByTrainingProgramIdDesc();
    TrainingProgram findTrainingProgramByTrainingProgramId(Integer id);
    @Query("SELECT ts FROM TrainingSyllabus ts JOIN FETCH ts.trainingProgram tp WHERE tp.trainingProgramId = :programId")
    List<TrainingSyllabus> findTrainingSyllabusByProgramId(@Param("programId") Integer programId);
    List<TrainingProgram> findAll(Specification<TrainingProgram> spec);
}



