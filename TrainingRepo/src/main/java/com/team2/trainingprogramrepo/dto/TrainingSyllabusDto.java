package com.team2.trainingprogramrepo.dto;

import com.team2.trainingprogramrepo.entity.TrainingProgram;
import com.team2.trainingprogramrepo.entity.TrainingSyllabus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrainingSyllabusDto {
        private TrainingProgram trainingProgram;
        private List<TrainingSyllabus> trainingSyllabusList;

        // Getters and setters
    }
