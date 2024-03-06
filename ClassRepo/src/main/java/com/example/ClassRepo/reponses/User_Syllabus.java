package com.example.ClassRepo.reponses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User_Syllabus {
    private UserDto user;
    private SyllabusDto syllabus;
}
