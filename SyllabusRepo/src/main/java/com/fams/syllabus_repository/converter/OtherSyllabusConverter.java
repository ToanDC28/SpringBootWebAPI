package com.fams.syllabus_repository.converter;

import com.fams.syllabus_repository.dto.OtherSyllabusCreateUpdateDto;
import com.fams.syllabus_repository.dto.OtherSyllabusDto;
import com.fams.syllabus_repository.entity.AssignmentSheme;
import com.fams.syllabus_repository.entity.OtherSyllabus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OtherSyllabusConverter {
    public static OtherSyllabusDto toDTO(OtherSyllabus entity){
        OtherSyllabusDto dto = new OtherSyllabusDto();
        dto.setId(entity.getId());
        dto.setTraining(entity.getTraining());
        dto.setReTest(entity.getReTest());
        dto.setMarking(entity.getMarking());
        dto.setWaiverCriteria(entity.getWaiverCriteria());
        dto.setOther(entity.getOther());
        dto.setCreatedDate(LocalDate.now());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifyDate(LocalDate.now());
        dto.setModifyBy(entity.getModifyBy());
        return dto;
    }
    public static OtherSyllabus toEntity(OtherSyllabusDto dto){
        OtherSyllabus entity = new OtherSyllabus();
        //entity.setId(dto.getId());
        entity.setTraining(dto.getTraining());
        entity.setReTest(dto.getReTest());
        entity.setMarking(dto.getMarking());
        entity.setWaiverCriteria(dto.getWaiverCriteria());
        entity.setOther(dto.getOther());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        return entity;
    }
    public static OtherSyllabus toEntity(OtherSyllabusDto dto, OtherSyllabus entity){
        //entity.setId(dto.getId());
        entity.setTraining(dto.getTraining());
        entity.setReTest(dto.getReTest());
        entity.setMarking(dto.getMarking());
        entity.setWaiverCriteria(dto.getWaiverCriteria());
        entity.setOther(dto.getOther());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        return entity;
    }

    public static OtherSyllabusCreateUpdateDto toCreateUpdateDTO(AssignmentSheme entity, OtherSyllabus otherSyllabus) {
        OtherSyllabusCreateUpdateDto dto = new OtherSyllabusCreateUpdateDto();
        dto.setAssignmentShemeDtos(AssignmentShemeConverter.toDTO(entity));
        dto.setOtherSyllabusDtos(toDTO(otherSyllabus));
        return dto;
    }
}
