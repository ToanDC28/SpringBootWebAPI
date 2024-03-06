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
public class OtherListDto {
    //private List<TimeAllocationDto> timeAllocationDtoList;
    private List<AssignmentShemeDto> assignmentShemeDtos;
    private List<OtherSyllabusDto> otherSyllabusDtos;
}
