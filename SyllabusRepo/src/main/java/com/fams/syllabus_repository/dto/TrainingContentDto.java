package com.fams.syllabus_repository.dto;

import com.fams.syllabus_repository.enums.OutputStandard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingContentDto extends BaseDto{
//    private Long id;
    private String name;
    private List<OutputStandard> outputStandard;
    private int trainingTime;
    private String deliveryType;
    private String method;
}
