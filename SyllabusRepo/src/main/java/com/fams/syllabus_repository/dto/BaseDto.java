package com.fams.syllabus_repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDto {
    private Long id;
    private LocalDate createdDate;
    private String createdBy;
    private LocalDate modifyDate;
    private String modifyBy;
}
