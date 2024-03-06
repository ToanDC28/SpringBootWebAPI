package com.fams.syllabus_repository.service;

import com.fams.syllabus_repository.dto.PagingSyllabusDto;
import com.fams.syllabus_repository.dto.SyllabusDetailDto;
import com.fams.syllabus_repository.dto.SyllabusDto;
import com.fams.syllabus_repository.entity.Syllabus;
import com.fams.syllabus_repository.exceptions.SyllabusNotFoundException;
import com.fams.syllabus_repository.request.FilterRequest;
import com.fams.syllabus_repository.request.RequestIdDto;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface SyllabusService {
    List<SyllabusDto> getAllSyllabus();
    SyllabusDetailDto getSyllabusById(Long id);
    SyllabusDto createAndUpdate(SyllabusDto syllabusDto);

    PagingSyllabusDto getAllSyllabusAndSearch(int pageNo, int pageSize, String keyword);
     SyllabusDto duplicateSyllabus(Long id);

//    void importSyllabusFromCSV(MultipartFile file);
    void importSyllabusFromCSV(MultipartFile file, String createdBy, String modifiedBy, String importOption);

    //void importDataFromCsv(MultipartFile file);

    Syllabus getSyllabusEntityById(Long id);
    void deleteSyllabusById(Long id) throws SyllabusNotFoundException;

    List<SyllabusDto> getListSyllabusFOrService(List<RequestIdDto> request);
    void deleteSyllabus(Long syllabusId);
}
