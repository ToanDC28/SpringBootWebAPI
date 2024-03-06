package com.example.ClassRepo.request;


import com.example.ClassRepo.reponses.TrainingForClassResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClassCreateRequest {

    private String classCode;
    private String className;
    private String createBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm - HH:mm")
    private String classTime;
    private String location;
    private String FSU;
    private TrainingForClassRequest trainingForClassRequest;
    private Integer admin_id;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy/MM/dd")
    private String startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy/MM/dd")
    private String endDate;
    private String status;
    private List<String> schedule;

}
