package com.fams.syllabus_repository.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseObject<T> {
    private int statusCode;
    private String message;
    private T  data;
    private String error;
}
