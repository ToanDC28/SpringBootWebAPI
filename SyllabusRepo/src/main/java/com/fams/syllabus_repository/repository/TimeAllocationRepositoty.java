package com.fams.syllabus_repository.repository;

import com.fams.syllabus_repository.entity.AssignmentSheme;
import com.fams.syllabus_repository.entity.TimeAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeAllocationRepositoty extends JpaRepository<TimeAllocation, Long> {
    List<TimeAllocation> findBySyllabusId(Long id);
}
