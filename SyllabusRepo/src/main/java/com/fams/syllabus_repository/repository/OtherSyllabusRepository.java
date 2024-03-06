package com.fams.syllabus_repository.repository;

import com.fams.syllabus_repository.entity.OtherSyllabus;
import com.fams.syllabus_repository.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OtherSyllabusRepository extends JpaRepository<OtherSyllabus, Long> {
    List<OtherSyllabus> findBySyllabusId(Long id);
    Optional<OtherSyllabus> findByTraining(String findByTrainingPriciples);

    //Optional<OtherSyllabus> findBySyllabus(Syllabus syllabus);
    OtherSyllabus findBySyllabus(Syllabus syllabus);
}
