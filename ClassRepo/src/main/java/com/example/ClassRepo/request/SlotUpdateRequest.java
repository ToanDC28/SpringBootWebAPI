package com.example.ClassRepo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlotUpdateRequest {
    private Integer id;
    private Time startTime;
    private Time endTime;
}
