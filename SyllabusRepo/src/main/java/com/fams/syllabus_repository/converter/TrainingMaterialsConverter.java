package com.fams.syllabus_repository.converter;

import com.fams.syllabus_repository.dto.TrainingMaterialsDto;
import com.fams.syllabus_repository.entity.TrainingMaterials;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class TrainingMaterialsConverter {
    public static TrainingMaterialsDto toDTO(TrainingMaterials entity){
        TrainingMaterialsDto dto = new TrainingMaterialsDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setS3Location(entity.getS3Location());
        dto.setCreatedDate(LocalDate.now());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifyDate(LocalDate.now());
        dto.setModifyBy(entity.getModifyBy());
        return dto;
    }

    public static TrainingMaterials toEntity(TrainingMaterialsDto dto){
        TrainingMaterials entity = new TrainingMaterials();
        //dto.setId(entity.getId());
        entity.setTitle(dto.getTitle());
        entity.setS3Location(dto.getS3Location());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        return entity;
    }


}
