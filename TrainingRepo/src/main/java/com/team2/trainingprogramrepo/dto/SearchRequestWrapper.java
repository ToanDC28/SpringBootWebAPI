package com.team2.trainingprogramrepo.dto;

import com.team2.trainingprogramrepo.request.TrainingProgramRequest;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchRequestWrapper {
    private TrainingProgramRequest trainingProgramRequest;
    private PageRequestDto searchRequest;
}
