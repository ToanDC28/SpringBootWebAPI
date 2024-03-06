package com.team2.trainingprogramrepo.response;

import com.team2.trainingprogramrepo.dto.SyllabusDto;
import com.team2.trainingprogramrepo.dto.UserDto;
import com.team2.trainingprogramrepo.entity.TrainingProgram;
import lombok.Data;

import java.util.List;

@Data
public class TrainingProgramForClass {
    private List<User_Syllabus> userSyllabus;
    private TrainingProgram trainingProgram;
}
