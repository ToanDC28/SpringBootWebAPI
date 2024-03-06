package com.fams.syllabus_repository.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterRequest {
    //private List<String> outputStandard;
    private List<String> status;
}
