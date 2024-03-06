package com.fams.syllabus_repository.dto;

import com.fams.syllabus_repository.entity.TrainingUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutLineDto extends BaseDto{
    private List<DaySyllabusDto> daySyllabusDtos;
}
