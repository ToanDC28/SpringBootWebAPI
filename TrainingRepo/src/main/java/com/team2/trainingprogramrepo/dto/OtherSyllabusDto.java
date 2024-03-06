package com.team2.trainingprogramrepo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtherSyllabusDto extends BaseDto{
//    private Long id;
    private String training;
    private String reTest;
    private String marking;
    private String waiverCriteria;
    private String other;
}
