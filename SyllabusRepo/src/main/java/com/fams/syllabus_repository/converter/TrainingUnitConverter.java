package com.fams.syllabus_repository.converter;

import com.fams.syllabus_repository.dto.SyllabusDto;
import com.fams.syllabus_repository.dto.TrainingContentDto;
import com.fams.syllabus_repository.dto.TrainingUnitDto;
import com.fams.syllabus_repository.dto.TrainingUnitListDto;
import com.fams.syllabus_repository.entity.Syllabus;
import com.fams.syllabus_repository.entity.TrainingContent;
import com.fams.syllabus_repository.entity.TrainingUnit;
import com.fams.syllabus_repository.repository.TrainingContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainingUnitConverter {
//    @Autowired
//    private  TrainingContentRepository trainingContentRepository;

    private static ApplicationContext context;

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        TrainingUnitConverter.context = context;
    }


    public static TrainingUnitDto toDTO(TrainingUnit entity) {
        TrainingUnitDto dto = new TrainingUnitDto();
        dto.setId(entity.getId());
        //dto.setTopic(entity.getTopic());
        //dto.setDayNumber(entity.getDayNumber());
        dto.setUnitNumber(entity.getUnitNumber());
        dto.setUnitName(entity.getUnitName());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifyDate(entity.getModifyDate());
        dto.setModifyBy(dto.getModifyBy());
        //dto.setSyllabus(entity.getSyllabus());
        return dto;
    }

    public static TrainingUnit toEntity(TrainingUnitDto dto) {
        TrainingUnit entity = new TrainingUnit();
        //entity.setId(dto.getId());
        //entity.setTopic(dto.getTopic());
        //entity.setDayNumber(dto.getDayNumber());
        entity.setUnitNumber(dto.getUnitNumber());
        entity.setUnitName(dto.getUnitName());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        //entity.setSyllabus(dto.getSyllabus());
        return entity;
    }

    public static TrainingUnit toEntity(TrainingUnitDto dto, TrainingUnit entity) {
        //TrainingUnit entity = new TrainingUnit();
        //entity.setId(dto.getId());
        //entity.setTopic(dto.getTopic());
        //entity.setDayNumber(dto.getDayNumber());
        entity.setUnitNumber(dto.getUnitNumber());
        entity.setUnitName(dto.getUnitName());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        //entity.setSyllabus(dto.getSyllabus());
        return entity;
    }

    public static TrainingUnit toEntityUnitContent(TrainingUnitListDto dto, TrainingUnit existingTrainingUnit) {
        if (existingTrainingUnit == null) {
            existingTrainingUnit = new TrainingUnit();
            existingTrainingUnit.setCreatedDate(LocalDate.now());
        }

        //existingTrainingUnit.setDayNumber(dto.getDayNumber());
        existingTrainingUnit.setUnitName(dto.getUnitName());
        existingTrainingUnit.setUnitNumber(dto.getUnitNumber());
        existingTrainingUnit.setModifyDate(LocalDate.now());

        List<TrainingContent> trainingContents = new ArrayList<>();
        for (TrainingContentDto contentDto : dto.getTrainingContent()) {
            TrainingContent existingTrainingContent = null;
            if (contentDto.getId() != null) {
                TrainingContentRepository trainingContentRepository = context.getBean(TrainingContentRepository.class);
                existingTrainingContent = trainingContentRepository.findById(contentDto.getId()).orElse(null);
            }

            assert existingTrainingContent != null;
            TrainingContent updatedTrainingContent = TrainingContentConverter.toEntity(contentDto, existingTrainingContent);
            updatedTrainingContent.setTrainingUnit(existingTrainingUnit);
            trainingContents.add(updatedTrainingContent);
        }

        existingTrainingUnit.setTrainingContents(trainingContents);

        return existingTrainingUnit;
    }
}
