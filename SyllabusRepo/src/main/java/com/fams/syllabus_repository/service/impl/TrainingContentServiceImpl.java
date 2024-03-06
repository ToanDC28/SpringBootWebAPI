package com.fams.syllabus_repository.service.impl;

import com.fams.syllabus_repository.converter.TrainingContentConverter;
import com.fams.syllabus_repository.dto.TrainingContentDto;
import com.fams.syllabus_repository.entity.TrainingContent;
import com.fams.syllabus_repository.entity.TrainingUnit;
import com.fams.syllabus_repository.exceptions.TrainingContentNotFoundException;
import com.fams.syllabus_repository.exceptions.TrainingUnitNotFoundException;
import com.fams.syllabus_repository.repository.TrainingContentRepository;
import com.fams.syllabus_repository.repository.TrainingUnitRepository;
import com.fams.syllabus_repository.service.TrainingContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingContentServiceImpl implements TrainingContentService {
    @Autowired
    private TrainingContentRepository trainingContentRepository;
    @Autowired
    private TrainingUnitRepository trainingUnitRepository;

    @Override
    public List<TrainingContentDto> getAllTraniningContent() {
        List<TrainingContentDto> result = new ArrayList<>();
        List<TrainingContent> trainingContents = trainingContentRepository.findAll();
        for (TrainingContent item : trainingContents
        ) {
            TrainingContentDto dto = TrainingContentConverter.toDto(item);
            result.add(dto);
        }
        return result;
    }

    @Override
    public TrainingContentDto createContent(Long trainingUnitId, TrainingContentDto trainingContentDto) {
        TrainingContent trainingContent = TrainingContentConverter.toEntity(trainingContentDto);

        TrainingUnit trainingUnitEntity = trainingUnitRepository.findById(trainingUnitId)
                .orElseThrow(() -> new TrainingUnitNotFoundException("Content with associated unit not found"));

        trainingContent.setTrainingUnit(trainingUnitEntity);

        TrainingContent newTrainingContent = trainingContentRepository.save(trainingContent);

        return TrainingContentConverter.toDto(newTrainingContent);
    }

    @Override
    public void deleteTrainingContentById(Long contentId) {
        TrainingContent trainingContent = trainingContentRepository.findById(contentId)
                .orElseThrow(() -> new TrainingContentNotFoundException("Training content not found"));

        // Deleting the DaySyllabus and related TrainingUnits
        trainingContentRepository.delete(trainingContent);
    }
}
