package com.fams.syllabus_repository.dto;

import com.fams.syllabus_repository.enums.OutputStandard;
import com.fams.syllabus_repository.enums.PublicStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyllabusDto extends BaseDto{
//    private Long id;
    //private String topicCode;
    private String syllabusName;
    private String syllabusCode;
    private int duration;
    private String version;
    private int attendeeNumber;
    private String technicalRequirement;
    //private String trainer;
    private List<OutputStandard> outputStandards;
    private String courseObjectives;
    private String level;
    public String publicStatus;
}
