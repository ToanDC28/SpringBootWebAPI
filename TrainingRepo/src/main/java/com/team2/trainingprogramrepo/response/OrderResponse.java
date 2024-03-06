package com.team2.trainingprogramrepo.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Long syllabusId;
    private Integer order;
}
