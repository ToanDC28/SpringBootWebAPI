package com.team2.trainingprogramrepo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClassDto {
    private Integer id;
    private String classCode;
    private String className;
    private String location;
    private String trainingProgramId;
    private String createBy;
    private String createDate;
    private String modifyBy;
    private String modifyDate;
    private String trainer_id;
    private String admin_id;
    private String fsu;
    private Integer durationInDays;
    private String classTime;
    private List<String> schedule;
    private String startDate;
    private String endDate;
    private String approveBy;
    private String approveDate;
}
