package com.fams.syllabus_repository.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseObjectDto {

    // 0 for success, 1 for failure
    private int code;

    private String message;

    private Object data;
}
