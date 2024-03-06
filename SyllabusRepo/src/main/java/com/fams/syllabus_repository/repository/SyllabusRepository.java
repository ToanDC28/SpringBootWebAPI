package com.fams.syllabus_repository.repository;

import com.fams.syllabus_repository.entity.Syllabus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {
    Optional<Syllabus> findBySyllabusCode(String topicName);
    Page<Syllabus> findAll(Specification<Syllabus> spec, Pageable pageable);
    boolean existsBySyllabusName(String syllabusName);

    List<Syllabus> findBySyllabusNameAndVersionAndTechnicalRequirementAndCourseObjectives(String syllabusName, String version, String technicalRequirement, String courseObjectives);
    //List<Syllabus> findBySyllabusId(Long id);
}
