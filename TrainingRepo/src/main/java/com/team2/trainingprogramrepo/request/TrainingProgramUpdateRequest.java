package com.team2.trainingprogramrepo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TrainingProgramUpdateRequest {
    private Integer id;

    private String name;

    private String modifiedBy;

    private String topicCode;

    private String status;

    private List<SyllabusRequest> syllabusList;
}
