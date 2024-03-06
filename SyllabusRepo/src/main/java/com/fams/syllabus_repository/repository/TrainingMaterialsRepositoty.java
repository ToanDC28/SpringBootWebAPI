package com.fams.syllabus_repository.repository;

import com.fams.syllabus_repository.entity.DaySyllabus;
import com.fams.syllabus_repository.entity.TrainingContent;
import com.fams.syllabus_repository.entity.TrainingMaterials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingMaterialsRepositoty extends JpaRepository<TrainingMaterials, Long> {
    List<TrainingMaterials> findByTrainingContentId(Long id);
    TrainingMaterials findTrainingMaterialsByTrainingContent(TrainingContent trainingContent);
}
