package com.team2.trainingprogramrepo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SyllabusRequest {
    private Integer order;
    private Long syllabusId;
}
