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
public class TrainingUnitListDto extends BaseDto {
    //    private Long id;
    //private String topic;
    private String unitName;
    private String unitNumber;
    //private Syllabus syllabus;
    private List<TrainingContentDto> trainingContent;
}
