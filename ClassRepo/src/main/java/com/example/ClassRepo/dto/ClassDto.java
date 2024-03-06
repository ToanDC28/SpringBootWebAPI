package com.example.ClassRepo.dto;

import com.example.ClassRepo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassDto {
    private Integer id;
    private String classCode; //Sử dụng như class ID
    private String className;
    private String location;

    private String trainingProgramId;
    private String createBy;
    private String createDate;

    private String modifyBy;
    private String modifyDate;

    private String admin_id;
    private String fsu;
    private Integer durationInDays;
    private String classTime;
    private List<String> schedule;

    private String startDate;
    private String endDate;

    private String approveBy;
    private String approveDate;

    private Status status;
}
