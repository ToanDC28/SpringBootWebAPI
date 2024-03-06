package com.example.ClassRepo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayUpdateRequest {
    private Integer classid;
    private Date date;
}
