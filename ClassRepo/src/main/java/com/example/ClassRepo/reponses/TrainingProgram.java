package com.example.ClassRepo.reponses;

import lombok.Data;

@Data
public class TrainingProgram {
    private Integer trainingProgramId;

    private String trainingProgramCode;

    private String name;

    private String createdDate;

    private String modifiedDate;

    private String createdBy;

    private String modifiedBy;

    private String topicCode;

    private String status;
}
