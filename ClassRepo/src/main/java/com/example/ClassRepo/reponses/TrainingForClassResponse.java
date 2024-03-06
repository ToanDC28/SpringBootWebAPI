package com.example.ClassRepo.reponses;

import lombok.Data;

import java.util.List;

@Data
public class TrainingForClassResponse {
    private List<User_Syllabus> userSyllabus;
    private TrainingProgram trainingProgram;
}
