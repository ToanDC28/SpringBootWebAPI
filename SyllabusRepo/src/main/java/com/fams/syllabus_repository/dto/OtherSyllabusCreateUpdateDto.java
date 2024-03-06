package com.fams.syllabus_repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtherSyllabusCreateUpdateDto {
    private Long id;
    private AssignmentShemeDto assignmentShemeDtos;
    private OtherSyllabusDto otherSyllabusDtos;
}
