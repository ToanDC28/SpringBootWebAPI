package com.team2.trainingprogramrepo.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.team2.trainingprogramrepo.config.SqlDateConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "training_program")
public class TrainingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer trainingProgramId;

    private String trainingProgramCode;

    private String name;

    @JsonDeserialize(converter = SqlDateConverter.class)
    private Date createdDate;

    @JsonDeserialize(converter = SqlDateConverter.class)
    private Date modifiedDate;

    private String createdBy;

    private String modifiedBy;

    private String topicCode;

    private String status;

}
