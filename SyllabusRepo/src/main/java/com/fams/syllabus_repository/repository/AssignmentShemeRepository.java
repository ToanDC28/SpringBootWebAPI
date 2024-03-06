package com.fams.syllabus_repository.repository;

import com.fams.syllabus_repository.entity.AssignmentSheme;
import com.fams.syllabus_repository.entity.OtherSyllabus;
import com.fams.syllabus_repository.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentShemeRepository extends JpaRepository<AssignmentSheme, Long> {
    List<AssignmentSheme> findBySyllabusId(Long id);
    AssignmentSheme findBySyllabus(Syllabus syllabus);
}
