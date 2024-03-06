package com.team2.trainingprogramrepo.dto;

import com.team2.trainingprogramrepo.enums.OutputStandard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyllabusDto {
    private Long id;
    private String createdDate;
    private String createdBy;
    private String modifyDate;
    private String modifyBy;
    private String syllabusName;
    private String syllabusCode;
    private int duration;
    private String version;
    private int attendeeNumber;
    private String technicalRequirement;
    private List<OutputStandard> outputStandards;
    private String courseObjectives;
    private String level;
    private String publicStatus;
}
