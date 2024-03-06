package com.fams.syllabus_repository.request;

import com.fams.syllabus_repository.dto.DaySyllabusDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSyllabusRequest {
    private Long syllabusId;
    private DaySyllabusDto daySyllabusDto;
}
