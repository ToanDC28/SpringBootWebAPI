package com.team2.trainingprogramrepo.dto;

import com.team2.trainingprogramrepo.request.TrainingProgramRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FmsDto {
    private UserDto userDto;
    private SyllabusDto syllabusDto;
    private ClassDto classDto;
    private TrainingProgramRequest trainingProgramDto;
}
