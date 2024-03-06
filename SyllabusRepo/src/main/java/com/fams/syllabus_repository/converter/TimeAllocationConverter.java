package com.fams.syllabus_repository.converter;

import com.fams.syllabus_repository.dto.TimeAllocationDto;
import com.fams.syllabus_repository.entity.TimeAllocation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TimeAllocationConverter {
    public static TimeAllocationDto toDTO(TimeAllocation entity){
        TimeAllocationDto dto = new TimeAllocationDto();
        dto.setId(entity.getId());
        dto.setAssignment(entity.getAssignment());
        dto.setConcept(entity.getConcept());
        dto.setGulde(entity.getGulde());
        dto.setTest(entity.getTest());
        dto.setExam(entity.getExam());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifyDate(entity.getModifyDate());
        dto.setModifyBy(entity.getModifyBy());
        return dto;
    }
    public static TimeAllocation toEntity(TimeAllocationDto dto){
        TimeAllocation entity = new TimeAllocation();
        entity.setAssignment(dto.getAssignment());
        entity.setConcept(dto.getConcept());
        entity.setGulde(dto.getGulde());
        entity.setTest(dto.getTest());
        entity.setExam(dto.getExam());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        return entity;
    }

    public static TimeAllocation toEntity(TimeAllocationDto dto, TimeAllocation entity){
        entity.setAssignment(dto.getAssignment());
        entity.setConcept(dto.getConcept());
        entity.setGulde(dto.getGulde());
        entity.setTest(dto.getTest());
        entity.setExam(dto.getExam());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        return entity;
    }
}
