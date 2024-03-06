package com.team2.trainingprogramrepo.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.team2.trainingprogramrepo.config.SqlDateConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TrainingProgramCreateRequest {
    private Integer id;
    private String trainingProgramCode;

    private String name;

    @JsonDeserialize(converter = SqlDateConverter.class)
    private Date createdDate;

    private String modifiedBy;

    @JsonDeserialize(converter = SqlDateConverter.class)
    private Date modifiedDate;

    private String createdBy;

    private String topicCode;

    private String status;

    private List<SyllabusRequest> syllabusList;
}
