package com.fams.syllabus_repository.service;

import com.fams.syllabus_repository.dto.SyllabusDto;
import com.fams.syllabus_repository.dto.TrainingContentDto;
import com.fams.syllabus_repository.dto.TrainingUnitDto;

import java.util.List;

public interface TrainingContentService {
    List<TrainingContentDto> getAllTraniningContent();
    TrainingContentDto createContent(Long trainingUnitId, TrainingContentDto trainingContentDto);
    void deleteTrainingContentById(Long contentId);
}
