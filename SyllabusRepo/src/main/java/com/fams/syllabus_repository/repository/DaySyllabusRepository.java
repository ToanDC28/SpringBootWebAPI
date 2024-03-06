package com.fams.syllabus_repository.repository;

import com.fams.syllabus_repository.entity.DaySyllabus;
import com.fams.syllabus_repository.entity.Syllabus;
import com.fams.syllabus_repository.entity.TrainingUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DaySyllabusRepository extends JpaRepository<DaySyllabus, Long> {
    Optional<DaySyllabusRepository> findByDay(String day);
    List<DaySyllabus> findBySyllabusId(Long id);
    void deleteDaySyllabusBySyllabus(Syllabus syllabus);
}
