package com.fams.syllabus_repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingMaterialsDto extends BaseDto{
//    private Long materialsId;
    private String title;
    private String s3Location;
    private byte[] trainingMaterialData;
}
