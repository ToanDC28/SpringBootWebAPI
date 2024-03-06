package com.fams.syllabus_repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeAllocationDto extends BaseDto{
    private int assignment;
    private int concept;
    private int gulde;
    private int test;
    private int exam;
}
