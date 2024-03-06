package com.fams.syllabus_repository.converter;

import com.fams.syllabus_repository.dto.DaySyllabusDto;
import com.fams.syllabus_repository.dto.TrainingContentDto;
import com.fams.syllabus_repository.dto.TrainingUnitListDto;
import com.fams.syllabus_repository.entity.DaySyllabus;
import com.fams.syllabus_repository.entity.TrainingContent;
import com.fams.syllabus_repository.entity.TrainingUnit;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DaySyllabusConverter {
    public static DaySyllabusDto toDTO(DaySyllabus entity){
        DaySyllabusDto dto = new DaySyllabusDto();
        dto.setId(entity.getId());
        dto.setDay(entity.getDay());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifyDate(entity.getCreatedDate());
        dto.setModifyBy(entity.getModifyBy());
        //dto.setTrainingUnitListDtos(entity.getTrainingUnit());
        return dto;
    }


    public static DaySyllabus toEntity(DaySyllabusDto dto){
        DaySyllabus entity = new DaySyllabus();
        //entity.setId(dto.getId());
        entity.setDay(dto.getDay());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        return entity;
    }
    public static DaySyllabusDto toDTOCreate(DaySyllabus entity) {
        DaySyllabusDto dto = new DaySyllabusDto();
        dto.setId(entity.getId());
        dto.setDay(entity.getDay());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifyDate(entity.getModifyDate());
        dto.setModifyBy(entity.getModifyBy());

        List<TrainingUnit> trainingUnits = entity.getTrainingUnit();
        List<TrainingUnitListDto> trainingUnitListDtos = new ArrayList<>();
        for (TrainingUnit trainingUnit : trainingUnits) {
            TrainingUnitListDto trainingUnitListDto = new TrainingUnitListDto();
            trainingUnitListDto.setId(trainingUnit.getId());
            //trainingUnitListDto.setDayNumber(trainingUnit.getDayNumber());
            trainingUnitListDto.setUnitName(trainingUnit.getUnitName());
            trainingUnitListDto.setUnitNumber(trainingUnit.getUnitNumber());
            trainingUnitListDto.setCreatedDate(trainingUnit.getCreatedDate());
            trainingUnitListDto.setCreatedBy(trainingUnit.getCreatedBy());
            trainingUnitListDto.setModifyDate(trainingUnit.getModifyDate());
            trainingUnitListDto.setModifyBy(trainingUnit.getModifyBy());

            List<TrainingContentDto> trainingContentDtos = new ArrayList<>();
            for (TrainingContent content : trainingUnit.getTrainingContents()) {
                TrainingContentDto contentDto = new TrainingContentDto();
                contentDto.setName(content.getName());
                contentDto.setDeliveryType(content.getDeliveryType().toString());
                contentDto.setOutputStandard(content.getOutputStandard());
                contentDto.setTrainingTime(content.getTrainingTime());
                contentDto.setMethod(content.getMethod());
                contentDto.setId(content.getId());
                contentDto.setCreatedDate(content.getCreatedDate());
                contentDto.setCreatedBy(content.getCreatedBy());
                contentDto.setModifyDate(content.getModifyDate());
                contentDto.setModifyBy(content.getModifyBy());
                trainingContentDtos.add(contentDto);
            }
            trainingUnitListDto.setTrainingContent(trainingContentDtos);

            trainingUnitListDtos.add(trainingUnitListDto);
        }
        dto.setTrainingUnitListDtos(trainingUnitListDtos);
        return dto;
    }

    public static DaySyllabus toEntity(DaySyllabusDto dto, DaySyllabus entity){
        //entity.setId(dto.getId());
        entity.setDay(dto.getDay());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        return entity;
    }
}
