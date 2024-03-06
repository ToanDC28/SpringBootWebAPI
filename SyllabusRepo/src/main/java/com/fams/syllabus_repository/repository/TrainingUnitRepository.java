package com.fams.syllabus_repository.repository;

import com.fams.syllabus_repository.entity.DaySyllabus;
import com.fams.syllabus_repository.entity.TrainingUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainingUnitRepository extends JpaRepository<TrainingUnit, Long> {
    Optional<TrainingUnit> findByUnitName(String unitName);
    List<TrainingUnit> findByDaySyllabusId(Long id);
    TrainingUnit findTrainingUnitByDaySyllabus(DaySyllabus daySyllabus);
    void deleteTrainingUnitByDaySyllabus(DaySyllabus daySyllabus);
}
