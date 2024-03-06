package com.team2.trainingprogramrepo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResponseObject {

    // 0 for success, 1 for failure
    private int code;

    private String message;

    private Object data;
}
