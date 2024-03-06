package com.fams.syllabus_repository.service;

import com.fams.syllabus_repository.dto.DaySyllabusDto;
import com.fams.syllabus_repository.dto.DaySyllabusListDto;
import com.fams.syllabus_repository.dto.TrainingUnitDto;
import com.fams.syllabus_repository.dto.TrainingUnitListDto;
import com.fams.syllabus_repository.entity.DaySyllabus;

import java.util.List;

public interface DaySyllabusService {
    List<DaySyllabusListDto> getDaySyllabusBySyllabusId(Long id);

    DaySyllabusDto createDaySyllabus(Long syllabusId, DaySyllabusDto daySyllabusDto);

    void deleteDaySyllabus(Long syllabusId, Long dayId);
    void deleteDaySyllabusById(Long dayId);

    //DaySyllabusDto createDaySyllabusWithTrainingUnits(Long syllabusId, DaySyllabusDto daySyllabusDto, List<TrainingUnitDto> trainingUnitDtos);
    DaySyllabusDto createDaySyllabusa(Long syllabusId, DaySyllabusDto daySyllabusDto);
    //List<DaySyllabusDto> createOrUpdateDaySyllabusList(Long syllabusId, List<DaySyllabusDto> daySyllabusDtos);
    List<DaySyllabusDto> updateDaySyllabusList(Long syllabusId, List<DaySyllabusDto> daySyllabusDtos);
    List<DaySyllabusDto> createDaySyllabusList(Long syllabusId, List<DaySyllabusDto> daySyllabusDtos);
    //List<DaySyllabusDto> createOrUpdateDaySyllabusList(Long syllabusId, List<DaySyllabusDto> daySyllabusDtos);

    //DaySyllabusDto updateDaySyllabus(Long syllabusId, DaySyllabusDto daySyllabusDto);
    //DaySyllabusDto updateDaySyllabus(Long syllabusId, DaySyllabusDto daySyllabusDto);
    //void updateAllEntities(DaySyllabusDto daySyllabusDto, List<TrainingUnitListDto> trainingUnitListDtos);
}