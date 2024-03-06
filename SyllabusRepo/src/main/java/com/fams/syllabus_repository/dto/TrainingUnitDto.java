package com.fams.syllabus_repository.dto;

import com.fams.syllabus_repository.entity.Syllabus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingUnitDto extends BaseDto{
//    private Long id;
    //private String topic;
    //private String dayNumber;
    private String unitName;
    private String unitNumber;
    //private Syllabus syllabus;
    //private List<TrainingContentDto> trainingContent;
}
