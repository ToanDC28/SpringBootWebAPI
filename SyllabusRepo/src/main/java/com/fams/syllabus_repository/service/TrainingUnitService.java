package com.fams.syllabus_repository.service;

import com.fams.syllabus_repository.dto.SyllabusDto;
import com.fams.syllabus_repository.dto.TrainingUnitDto;
import com.fams.syllabus_repository.dto.TrainingUnitListDto;

import java.util.List;

public interface TrainingUnitService {
    List<TrainingUnitListDto> getTrainingUnitBySyllabusId(Long id);
    TrainingUnitDto createUnit(Long syllabusId, TrainingUnitDto trainingUnitDto);
    void deleteTrainingUnitById(Long unitId);
}
