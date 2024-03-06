package com.fams.syllabus_repository.dto;

import com.fams.syllabus_repository.request.FilterRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagingSyllabusDto {
    private List<SyllabusDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private String message;
    private String search;
    //private String sort;
    //private FilterRequest filter;
//    private LocalDate selectedDateStr;
//    private LocalDate selectedEndDateStr;
}
