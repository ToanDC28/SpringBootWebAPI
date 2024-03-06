package com.example.ClassRepo.reponses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseObject {
    // 0 for success, 1 for failure
    private Integer code;
    private String message;
    private Object data;

    public boolean canEqual(Object o) {
        return o instanceof ResponseObject;
    }
}
