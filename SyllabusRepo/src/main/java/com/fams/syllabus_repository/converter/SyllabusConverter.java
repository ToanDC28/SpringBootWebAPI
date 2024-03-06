package com.fams.syllabus_repository.converter;

import com.fams.syllabus_repository.dto.SyllabusDto;
import com.fams.syllabus_repository.entity.DaySyllabus;
import com.fams.syllabus_repository.entity.Syllabus;
import com.fams.syllabus_repository.entity.TrainingContent;
import com.fams.syllabus_repository.entity.TrainingUnit;
import com.fams.syllabus_repository.enums.OutputStandard;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SyllabusConverter {
    public static SyllabusDto toDTO(Syllabus entity) {
        SyllabusDto dto = new SyllabusDto();
        dto.setId(entity.getId());
        dto.setSyllabusName(entity.getSyllabusName());
        dto.setSyllabusCode(entity.getSyllabusCode());
        dto.setVersion(entity.getVersion());
        dto.setAttendeeNumber(entity.getAttendeeNumber());
        dto.setDuration(entity.getDuration());
        dto.setTechnicalRequirement(entity.getTechnicalRequirement());
        //dto.setOutputStandard(entity.getOutputStandard());
        dto.setCourseObjectives(entity.getCourseObjectives());
        dto.setLevel(entity.getLevel());
        dto.setPublicStatus(entity.getPublicStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifyDate(entity.getModifyDate());
        dto.setModifyBy(entity.getModifyBy());
        dto.setOutputStandards(extractOutputStandards(entity));
        return dto;
    }
    private static List<OutputStandard> extractOutputStandards(Syllabus entity) {
        Set<OutputStandard> outputStandardSet = new HashSet<>();

        for (DaySyllabus daySyllabus : entity.getDaSyllabus()) {
            for (TrainingUnit trainingUnit : daySyllabus.getTrainingUnit()) {
                for (TrainingContent trainingContent : trainingUnit.getTrainingContents()) {
                    outputStandardSet.addAll(trainingContent.getOutputStandard());
                }
            }
        }

        return new ArrayList<>(outputStandardSet);
    }

    public static Syllabus toEntity(SyllabusDto dto) {
        Syllabus entity = new Syllabus();
        //entity.setId(dto.getId());
        entity.setSyllabusName(dto.getSyllabusName());
        entity.setSyllabusCode(dto.getSyllabusCode());
        entity.setVersion(dto.getVersion());
        entity.setAttendeeNumber(dto.getAttendeeNumber());
        entity.setDuration(dto.getDuration());
        entity.setTechnicalRequirement(dto.getTechnicalRequirement());
        //entity.setOutputStandard(dto.getOutputStandard());
        entity.setCourseObjectives(dto.getCourseObjectives());
        entity.setLevel(dto.getLevel());
        entity.setPublicStatus(dto.getPublicStatus());
        //entity.setPublicStatus(dto.getPublicStatus());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        return entity;
    }

    public static Syllabus toEntity(SyllabusDto dto, Syllabus entity) {
        //entity.setId(dto.getId());
        entity.setSyllabusName(dto.getSyllabusName());
        entity.setSyllabusCode(dto.getSyllabusCode());
        entity.setVersion(dto.getVersion());
        entity.setAttendeeNumber(dto.getAttendeeNumber());
        entity.setDuration(dto.getDuration());
        entity.setTechnicalRequirement(dto.getTechnicalRequirement());
        //entity.setOutputStandard(dto.getOutputStandard());
        entity.setCourseObjectives(dto.getCourseObjectives());
        entity.setLevel(dto.getLevel());
        entity.setPublicStatus(dto.getPublicStatus());
        if (entity.getCreatedDate() == null) {
            entity.setCreatedDate(dto.getCreatedDate());
        }
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        return entity;
    }
}
