package com.fams.syllabus_repository.request;

import com.fams.syllabus_repository.dto.OtherSyllabusDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOtherSyllabusRequest {
    private Long syllabusId;
    private OtherSyllabusDto otherSyllabusDto;
}
