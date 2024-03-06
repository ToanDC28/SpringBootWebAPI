package com.example.ClassRepo.reponses;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class ScheduleResponse {
    private List<Date> schedule;
    private String slot;
}
