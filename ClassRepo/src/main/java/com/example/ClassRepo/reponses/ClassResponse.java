package com.example.ClassRepo.reponses;

import com.example.ClassRepo.entity.TrainingForClass;
import com.example.ClassRepo.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Date createDate;

    private String modifyBy;
    private String classTime;
    private Date modifyDate;
    private Status status;
    private String location;
    private String classCode;

    private Date startDate;

    private Date endDate;

    private List<Date> schedule;
    private String approveBy;

    private Date approveDate;
    private TrainingForClassResponse trainingForClass;
    private UserDto admin_id;
    private String fsu;
    private Integer durationInDays;

}
