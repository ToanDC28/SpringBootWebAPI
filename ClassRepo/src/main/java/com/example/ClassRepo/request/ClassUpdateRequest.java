package com.example.ClassRepo.request;

import com.example.ClassRepo.entity.TrainingForClass;
import com.example.ClassRepo.reponses.TrainingForClassResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassUpdateRequest {
    private Integer class_id;
    private String className;
    private String classCode;
    private String location;
    private String modifyBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private String startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy/MM/dd")
    private String endDate;
    private List<String> schedule;
    private String classTime;
    private TrainingForClassRequest trainingForClassList;
    private Integer admin_id;
    private String fsu;
}
