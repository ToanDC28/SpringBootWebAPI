package com.team2.trainingprogramrepo.response;

import com.team2.trainingprogramrepo.dto.SyllabusDto;
import com.team2.trainingprogramrepo.dto.UserDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User_Syllabus {
    private UserDto user;
    private SyllabusDto syllabus;
}
