package com.fams.syllabus_repository.converter;

import com.fams.syllabus_repository.dto.TrainingContentDto;
import com.fams.syllabus_repository.entity.TrainingContent;
import com.fams.syllabus_repository.enums.DeliveryType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TrainingContentConverter {
    public static TrainingContentDto toDto(TrainingContent entity) {
        TrainingContentDto dto = new TrainingContentDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDeliveryType(entity.getDeliveryType().toString());
        dto.setTrainingTime(entity.getTrainingTime());
        dto.setOutputStandard(entity.getOutputStandard());
        dto.setMethod(entity.getMethod());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifyDate(entity.getModifyDate());
        dto.setModifyBy(dto.getModifyBy());
        return dto;
    }

    public static TrainingContent toEntity(TrainingContentDto dto) {
        TrainingContent entity = new TrainingContent();
        //entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDeliveryType(DeliveryType.valueOf(dto.getDeliveryType()));
        entity.setOutputStandard(dto.getOutputStandard());
        entity.setTrainingTime(dto.getTrainingTime());
        entity.setMethod(dto.getMethod());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        return entity;
    }

    public static TrainingContent toEntity(TrainingContentDto dto, TrainingContent entity) {
        //entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDeliveryType(DeliveryType.valueOf(dto.getDeliveryType()));
        entity.setOutputStandard(dto.getOutputStandard());
        entity.setTrainingTime(dto.getTrainingTime());
        entity.setMethod(dto.getMethod());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        return entity;
    }
}
