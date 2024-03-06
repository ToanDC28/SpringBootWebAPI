package com.fams.syllabus_repository.service;

import com.fams.syllabus_repository.dto.OtherListDto;
import com.fams.syllabus_repository.dto.OtherSyllabusCreateUpdateDto;
import com.fams.syllabus_repository.dto.OtherSyllabusDto;
import com.fams.syllabus_repository.entity.AssignmentSheme;
import com.fams.syllabus_repository.entity.OtherSyllabus;
import com.fams.syllabus_repository.entity.TimeAllocation;

import java.util.List;

public interface OtherSyllabusService {
    List<OtherSyllabusDto> getOrderSyllabusBySyllabusId(Long id);

    OtherSyllabusDto createOrderSyllabus(Long syllabusId, OtherSyllabusDto otherSyllabusDto);

    //OtherListDto OtherListDto(Long syllabusId);
    OtherSyllabusDto updateOtherSyllabusBySyllabusId(Long syllabusId, OtherSyllabusDto otherSyllabusDto);
    OtherSyllabusDto saveOrUpdateOtherSyllabus(Long syllabusId, OtherSyllabusDto otherSyllabusDto);
    OtherSyllabusCreateUpdateDto createOrUpdateSchemesBySyllabusId(Long syllabusId, OtherSyllabusCreateUpdateDto dto);
}
