package com.example.ClassRepo.request;

import lombok.Data;

import java.util.List;

@Data
public class TrainingForClassRequest {
    private List<User_SyllabusRequest> userSyllabus;
    private Integer trainingProgramID;
}
