package com.fams.syllabus_repository.converter;

import com.fams.syllabus_repository.dto.AssignmentShemeDto;
import com.fams.syllabus_repository.dto.OtherSyllabusCreateUpdateDto;
import com.fams.syllabus_repository.entity.AssignmentSheme;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AssignmentShemeConverter {
    public static AssignmentShemeDto toDTO(AssignmentSheme entity){
        AssignmentShemeDto dto = new AssignmentShemeDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setCreatedDate(LocalDate.now());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifyDate(LocalDate.now());
        dto.setModifyBy(entity.getModifyBy());
        return dto;
    }

    public static AssignmentSheme toEntity(AssignmentShemeDto dto){
        AssignmentSheme entity = new AssignmentSheme();
        entity.setDescription(dto.getDescription());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        return entity;
    }
    public static AssignmentSheme toEntity(AssignmentShemeDto dto, AssignmentSheme entity){
        entity.setDescription(dto.getDescription());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        return entity;
    }

}
