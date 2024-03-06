package com.team2.trainingprogramrepo.response;

import com.team2.trainingprogramrepo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClassResponse {
    private Integer class_id;
    private String className;
    private String createBy;
    private String createDate;

    private String modifyBy;
    private String classTime;
    private String modifyDate;
    private String status;
    private String location;
    private String classCode;

    private String startDate;

    private String endDate;

    private List<String> schedule;
    private String approveBy;

    private String approveDate;
    private List<Integer> trainer_id;
    private Integer admin_id;
    private String fsu;
    private Integer trainingProgramId;
    private Integer durationInDays;

}
