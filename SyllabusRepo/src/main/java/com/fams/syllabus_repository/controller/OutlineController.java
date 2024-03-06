package com.fams.syllabus_repository.controller;

import com.fams.syllabus_repository.dto.DaySyllabusDto;
import com.fams.syllabus_repository.dto.OutlineRequestDto;
import com.fams.syllabus_repository.repository.TrainingMaterialsRepositoty;
import com.fams.syllabus_repository.request.RequestIdDto;
import com.fams.syllabus_repository.request.UpdateSyllabusRequest;
import com.fams.syllabus_repository.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/syllabus")
@RequiredArgsConstructor
public class OutlineController {
    private static final Logger logger = LogManager.getLogger(SyllabusController.class);
    @Autowired
    private TrainingUnitService trainingUnitService;
    @Autowired
    private TrainingContentService trainingContentService;
    @Autowired
    private DaySyllabusService daySyllabusService;

    @DeleteMapping("/delete-day-syllabus")
    @Transactional
    public String deleteDaySyllabus(@RequestBody RequestIdDto request) {
        try {
            Long dayId = request.getId();
            // action service
            daySyllabusService.deleteDaySyllabusById(dayId);

            // successful
            return "success_view";
        } catch (Exception e) {
            // error
            return "error_view";
        }
    }


    @PostMapping("/outline-management")
    public ResponseEntity<List<DaySyllabusDto>> updateCreateSyllabus(@RequestBody OutlineRequestDto updateRequest) {
        Long id = updateRequest.getId();
        List<DaySyllabusDto> daySyllabusDtos = updateRequest.getData();
        List<DaySyllabusDto> updateDaySyllabusDtos = daySyllabusService.updateDaySyllabusList(id, daySyllabusDtos);
        return new ResponseEntity<>(updateDaySyllabusDtos, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-unit")
    @Transactional
    public String deleteUnit(@RequestBody RequestIdDto deleteRequest) {
        try {
            Long unitId = deleteRequest.getId();
            // action service
            trainingUnitService.deleteTrainingUnitById(unitId);

            // successful
            return "success_view";
        } catch (Exception e) {
            // error
            return "error_view";
        }
    }


    @DeleteMapping("/delete-content")
    @Transactional
    public String deleteContent(@RequestBody RequestIdDto deleteRequest) {
        try {
            Long contentId = deleteRequest.getId();
            // action service
            trainingContentService.deleteTrainingContentById(contentId);

            // successful
            return "success_view";
        } catch (Exception e) {
            // error
            return "error_view";
        }
    }

}
