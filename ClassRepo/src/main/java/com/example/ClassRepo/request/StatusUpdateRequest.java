package com.example.ClassRepo.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatusUpdateRequest {
    private Integer id;
    private String status;
    private String By;
}
