package com.example.ClassRepo.reponses;

import lombok.Data;

import java.util.List;

@Data
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
