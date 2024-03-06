package com.team2.trainingprogramrepo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TrainingProgramRequest {

    private Integer trainingProgramId;

    private String trainingProgramCode;

    private String name;

    private String createdDate;

    private String modifiedBy;

    private String modifiedDate;

    private String createdBy;

    private String topicCode;

    private String status;

    private int duration;
}
