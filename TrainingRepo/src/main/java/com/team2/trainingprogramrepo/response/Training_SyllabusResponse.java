package com.team2.trainingprogramrepo.response;

import com.team2.trainingprogramrepo.entity.TrainingProgram;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Training_SyllabusResponse {
    private TrainingProgram program;
    private List<OrderResponse> syllabusList;
}
