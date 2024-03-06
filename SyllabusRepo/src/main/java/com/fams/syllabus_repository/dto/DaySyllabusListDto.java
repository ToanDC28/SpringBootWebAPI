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
public class DaySyllabusListDto extends BaseDto{
//    private Long id;
    private String day;
    private List<TrainingUnitListDto> trainingUnitListDtos;
}
