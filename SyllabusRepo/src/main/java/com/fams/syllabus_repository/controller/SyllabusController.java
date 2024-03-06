package com.fams.syllabus_repository.controller;

import com.fams.syllabus_repository.dto.PagingSyllabusDto;
import com.fams.syllabus_repository.dto.SyllabusDetailDto;
import com.fams.syllabus_repository.dto.SyllabusDto;
import com.fams.syllabus_repository.exceptions.SyllabusNotFoundException;
import com.fams.syllabus_repository.repository.TrainingMaterialsRepositoty;
import com.fams.syllabus_repository.request.RequestIdDto;
import com.fams.syllabus_repository.response.ResponseObject;
import com.fams.syllabus_repository.service.SyllabusService;
import com.fams.syllabus_repository.service.TrainingMaterialsService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/syllabus")
@RequiredArgsConstructor
public class SyllabusController {
    private static final Logger logger = LogManager.getLogger(SyllabusController.class);
    @Autowired
    private SyllabusService syllabusService;

    // SYLLABUS
    @PostMapping("/listAll")
    public PagingSyllabusDto getAllSyllabus(@RequestBody PagingSyllabusDto request) {
        int pageNo = request.getPageNo();
        int pageSize = request.getPageSize();
        String search = request.getSearch();

        return syllabusService.getAllSyllabusAndSearch(pageNo, pageSize, search);
    }


    @PostMapping("/syllabusDetail")
    public ResponseObject<Object> syllabusDetail(@RequestBody RequestIdDto request) {
        Long id = request.getId();
        SyllabusDetailDto syllabusDetailDto = syllabusService.getSyllabusById(id);
        return ResponseObject.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Syllabus successfully")
                .data(syllabusDetailDto)
                .build();
    }


    @PostMapping("/general-management")
    public ResponseEntity<SyllabusDto> updateGeneral(@RequestBody SyllabusDto request) {
        SyllabusDto updatedSyllabus = syllabusService.createAndUpdate(request);
        return ResponseEntity.ok(updatedSyllabus);
    }

    @PostMapping("/duplicate")
    public ResponseEntity<SyllabusDto> duplicateSyllabus(@RequestBody RequestIdDto request) {
        Long id = request.getId();
        SyllabusDto duplicatedSyllabus = syllabusService.duplicateSyllabus(id);

        if (duplicatedSyllabus != null) {
            return ResponseEntity.ok(duplicatedSyllabus);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/import-csv")
    public ResponseEntity<String> importSyllabusFromCSV(@RequestParam("file") MultipartFile file,
                                                        @RequestParam("createdBy") String createdBy,
                                                        @RequestParam("modifiedBy") String modifiedBy,
                                                        @RequestParam("option") String option) {
        syllabusService.importSyllabusFromCSV(file, createdBy, modifiedBy, option);
        return ResponseEntity.ok("Syllabus imported successfully.");
    }

    @DeleteMapping("/delete-syllabus")
    public ResponseEntity<String> deleteSyllabusById(@RequestBody RequestIdDto request) {
        try {
            syllabusService.deleteSyllabus(request.getId());
            return new ResponseEntity<>("Syllabus deleted successfully", HttpStatus.OK);
        } catch (SyllabusNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete syllabus", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/get_listSyllabus_for_service")
    public ResponseEntity<List<SyllabusDto>> getAll(@RequestBody List<RequestIdDto> request) {
        return ResponseEntity.ok(
                syllabusService.getListSyllabusFOrService(request)
        );
    }
}
