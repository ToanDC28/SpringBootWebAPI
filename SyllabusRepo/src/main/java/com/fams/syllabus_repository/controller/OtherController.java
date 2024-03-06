package com.fams.syllabus_repository.controller;

import com.fams.syllabus_repository.dto.*;
import com.fams.syllabus_repository.repository.TrainingMaterialsRepositoty;
import com.fams.syllabus_repository.request.RequestIdDto;
import com.fams.syllabus_repository.request.UpdateOtherSyllabusRequest;
import com.fams.syllabus_repository.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/syllabus")
@RequiredArgsConstructor
public class OtherController {
    private static final Logger logger = LogManager.getLogger(SyllabusController.class);
    //private static final Logger logInfo = (Logger) LoggerFactory.getLogger(SyllabusController.class);
    @Autowired
    private OtherSyllabusService otherSyllabusService;

    @PostMapping("/other-management")
    public ResponseEntity<OtherSyllabusCreateUpdateDto> createOrUpdateSchemesBySyllabusId(
            @RequestBody OtherSyllabusCreateUpdateDto dto
    ) {
        OtherSyllabusCreateUpdateDto createdOrUpdated = otherSyllabusService.createOrUpdateSchemesBySyllabusId(dto.getId(), dto);
        if (createdOrUpdated != null && createdOrUpdated.getId() == null) {
            createdOrUpdated.setId(dto.getId());
        }
        return new ResponseEntity<>(createdOrUpdated, HttpStatus.CREATED);
    }

}
