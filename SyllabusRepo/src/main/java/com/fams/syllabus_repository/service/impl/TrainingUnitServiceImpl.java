package com.fams.syllabus_repository.service.impl;

import com.fams.syllabus_repository.converter.TrainingContentConverter;
import com.fams.syllabus_repository.converter.TrainingUnitConverter;
import com.fams.syllabus_repository.dto.TrainingContentDto;
import com.fams.syllabus_repository.dto.TrainingUnitDto;
import com.fams.syllabus_repository.dto.TrainingUnitListDto;
import com.fams.syllabus_repository.entity.DaySyllabus;
import com.fams.syllabus_repository.entity.TrainingUnit;
import com.fams.syllabus_repository.exceptions.TrainingUnitNotFoundException;
import com.fams.syllabus_repository.repository.DaySyllabusRepository;
import com.fams.syllabus_repository.repository.SyllabusRepository;
import com.fams.syllabus_repository.repository.TrainingUnitRepository;
import com.fams.syllabus_repository.service.TrainingUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingUnitServiceImpl implements TrainingUnitService {
    @Autowired
    private SyllabusRepository syllabusRepository;
    @Autowired
    private TrainingUnitRepository trainingUnitRepository;
    @Autowired
    private DaySyllabusRepository daySyllabusRepository;

    @Override
    public List<TrainingUnitListDto> getTrainingUnitBySyllabusId(Long id) {
        List<TrainingUnit> reviews = trainingUnitRepository.findByDaySyllabusId(id);

        return reviews.stream().map(review -> {
            TrainingUnitListDto unitDto = toDTO(review);

            List<TrainingContentDto> contentDtoList = review.getTrainingContents().stream()
                    .map(content -> TrainingContentConverter.toDto(content))
                    .collect(Collectors.toList());

            unitDto.setTrainingContent(contentDtoList);

            return unitDto;
        }).collect(Collectors.toList());
    }

    @Override
    public TrainingUnitDto createUnit(Long daySyllabusId, TrainingUnitDto trainingUnitDto) {
        TrainingUnit trainingUnit = TrainingUnitConverter.toEntity(trainingUnitDto);

        DaySyllabus syllabus = daySyllabusRepository.findById(daySyllabusId)
                .orElseThrow(() -> new TrainingUnitNotFoundException("Unit with associated day not found"));
        trainingUnit.setDaySyllabus(syllabus);

        TrainingUnit newTrainingUnit = trainingUnitRepository.save(trainingUnit);

        return TrainingUnitConverter.toDTO(newTrainingUnit);
    }

    @Override
    public void deleteTrainingUnitById(Long unitId) {
        TrainingUnit trainingUnit = trainingUnitRepository.findById(unitId)
                .orElseThrow(() -> new TrainingUnitNotFoundException("Training unit not found"));

        // Deleting the DaySyllabus and related TrainingUnits
        trainingUnitRepository.delete(trainingUnit);
    }

    public static TrainingUnitListDto toDTO(TrainingUnit entity) {
        TrainingUnitListDto dto = new TrainingUnitListDto();
        dto.setId(entity.getId());
        //dto.setTopic(entity.getTopic());
        //dto.setDayNumber(entity.getDayNumber());
        dto.setUnitNumber(entity.getUnitNumber());
        dto.setUnitName(entity.getUnitName());
        //dto.setSyllabus(entity.getSyllabus());
        return dto;
    }
}
