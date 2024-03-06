package com.fams.syllabus_repository.repository;

import com.fams.syllabus_repository.entity.TrainingContent;
import com.fams.syllabus_repository.entity.TrainingUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingContentRepository extends JpaRepository<TrainingContent, Long> {
    Optional<TrainingContent> findByName(String content);
    void deleteTrainingContentByTrainingUnit(TrainingUnit trainingUnit);
    TrainingContent findTrainingContentByTrainingUnit(TrainingUnit trainingUnit);

}
