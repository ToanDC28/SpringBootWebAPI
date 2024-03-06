package com.team2.trainingprogramrepo.controller;

import com.team2.trainingprogramrepo.dto.*;
import com.team2.trainingprogramrepo.entity.TrainingProgram;
import com.team2.trainingprogramrepo.exception.BadRequestException;
import com.team2.trainingprogramrepo.exception.DateTimeException;
import com.team2.trainingprogramrepo.request.*;
import com.team2.trainingprogramrepo.response.ResponseObject;
import com.team2.trainingprogramrepo.response.Training_SyllabusResponse;
import com.team2.trainingprogramrepo.service.TrainingProgramService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/trainingProgram")
public class TrainingProgramController {

    private static final Logger LOGGER = LogManager.getLogger(TrainingProgramController.class);

    @Autowired
    private TrainingProgramService trainingProgramService;

    @PostMapping("/list")
    public Page<TrainingProgramRequest> getAllTrainingProgramsPaginationList(@RequestBody PageRequestDto dto) {
        if (dto == null) return null;

        LOGGER.info("get list successfully");

        return trainingProgramService.getAllTrainingPrograms(dto);
    }

    @PostMapping("syllabus")
    public Training_SyllabusResponse getTrainingSyllabusByProgramId(@RequestBody GetIDRequest request) {
        LOGGER.info("get training syllabus successfully");

        return trainingProgramService.findTrainingSyllabusByProgramId(request.getId());
    }

    @PostMapping("/classes")
    public ResponseEntity<ResponseObject> getClassesByTrainingProgramId(@RequestBody GetIDRequest request) {
        List<ClassDto> classes = trainingProgramService.getClassesByTrainingProgramId(request.getId());

        if (classes == null || classes.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(1, "No classes found", null));

        LOGGER.info("get classes successfully");

        return ResponseEntity.ok(new ResponseObject(0, "get classes successfully", classes));
    }

    @PostMapping("/syllabuses")
    public ResponseEntity<ResponseObject> getSyllabusesByTrainingProgramId(@RequestBody GetIDRequest request) {
        List<SyllabusDto> syllabuses = trainingProgramService.getSyllabusesByTrainingProgramId(request.getId());

        if (syllabuses == null || syllabuses.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(1, "No syllabuses found", null));

        LOGGER.info("get syllabuses successfully");

        return ResponseEntity.ok(new ResponseObject(0, "get syllabuses successfully", syllabuses));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<TrainingProgramRequest>> searchTrainingPrograms(@RequestBody SearchRequestWrapper requestWrapper) throws ParseException {
        PageRequestDto searchRequest = requestWrapper.getSearchRequest();

        String keyword = searchRequest.getKeyword();
        String searchType = searchRequest.getSearchType();

        Page<TrainingProgramRequest> searchResults = trainingProgramService.searchTrainingProgram(keyword, searchType, searchRequest);

        LOGGER.info("search successfully");

        return ResponseEntity.ok(searchResults);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createTrainingProgram(@RequestBody TrainingProgramCreateRequest trainingProgram) {
        if (trainingProgram == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(1, "Training program empty or null", null));

        try {
            int result = trainingProgramService.createTrainingProgram(trainingProgram);

            if (result > 0) {
                trainingProgram.setCreatedDate(new Date(System.currentTimeMillis()));
                trainingProgram.setModifiedDate(new Date(System.currentTimeMillis()));

                LOGGER.info("create successfully");

                return ResponseEntity
                        .ok(new ResponseObject(0, "Create training program successfully", trainingProgram));
            }

        } catch (Exception e) {
            LOGGER.error("create failed");

            throw new BadRequestException(e.getMessage());
//            return ResponseEntity.internalServerError().body(new ResponseObject(1, e.getMessage(), null));
        }

        LOGGER.error("create failed");

        return ResponseEntity.badRequest()
                .body(new ResponseObject(1, "Create training program failed", null));
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseObject> deleteTrainingProgram(@RequestBody GetIDRequest request) {
        if (request.getId() == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(1, "Training program id empty or null", null));

        trainingProgramService.deleteTrainingProgram(request.getId());

            return ResponseEntity
                    .ok(new ResponseObject(0, "Delete training program successfully for training id:" +
                            request.getId(), null));


    }

    @PostMapping("/duplicate")
    public ResponseEntity<ResponseObject> duplicateTrainingProgram(@RequestBody GetIDRequest request) {
        if (request.getId() == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(1, "Training program id empty or null", null));

        try {
            TrainingProgram result = trainingProgramService.duplicateTrainingProgram(request.getId());
            if (result != null)
                return ResponseEntity
                        .ok(new ResponseObject(0, "Duplicate training program successfully", result));
        } catch (Exception e) {
            LOGGER.error("duplicate failed");

            throw new BadRequestException(e.getMessage());
//            return ResponseEntity.internalServerError().body(new ResponseObject(1, e.getMessage(), null));
        }

        return ResponseEntity.badRequest()
                .body(new ResponseObject(1, "Duplicate training program failed", null));
    }

    @PostMapping("/get_training_for_service")
    public ResponseEntity<TrainingProgram> getTrainingProgramForClass(@RequestBody GetIDRequest request) {

        return ResponseEntity.ok(
                trainingProgramService.getTrainingDetailForClass(request)
        );
    }


    @PostMapping("/active")
    public List<TrainingProgramRequest> getTrainingProgramsWithStatusActive(@RequestBody PageRequestDto dto) {
        String keyword = dto.getKeyword();

        LOGGER.info("get active training programs successfully");

        return trainingProgramService.getTrainingProgramsWithStatusActive(keyword);
    }

    @PostMapping("/deactivate")
    public ResponseEntity<ResponseObject> deactivateTrainingProgram(@RequestBody GetIDRequest request) {
        if (request.getId() == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(1, "Training program id empty or null", null));
        try {
            int result = trainingProgramService.deactivateTrainingProgram(request.getId());
            if (result > 0)
                return ResponseEntity
                        .ok(new ResponseObject(0, "Deactivate training program successfully", request.getId()));
        } catch (Exception e) {
            LOGGER.error("deactivate error");
            throw new BadRequestException(e.getMessage());
        }

        LOGGER.error("deactivate error");

        return ResponseEntity.badRequest()
                .body(new ResponseObject(1, "Deactivate training program failed", null));
    }

    @PostMapping("/activate")
    public ResponseEntity<ResponseObject> activateTrainingProgram(@RequestBody GetIDRequest request) {
        if (request.getId() == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(1, "Training program id empty or null", null));
        try {
            int result = trainingProgramService.activateTrainingProgram(request.getId());
            if (result > 0)
                return ResponseEntity
                        .ok(new ResponseObject(0, "Activate training program successfully", request.getId()));
        } catch (Exception e) {
            LOGGER.error("activate failed");

            throw new BadRequestException(e.getMessage());
        }

        LOGGER.error("activate failed");

        return ResponseEntity.badRequest()
                .body(new ResponseObject(1, "Activate training program failed", null));
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateTrainingProgram(
            @RequestBody TrainingProgramUpdateRequest trainingProgramUpdateRequest
    ) {

        if (trainingProgramUpdateRequest == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(1, "Training program empty or null", null));


        try {
            TrainingProgram updatedTrainingProgram = trainingProgramService.updateTrainingProgram(trainingProgramUpdateRequest);

            LOGGER.info("update successfully");

            return ResponseEntity
                    .ok(new ResponseObject(0, "Update training program successfully", updatedTrainingProgram));
        } catch (Exception e) {
            LOGGER.error("update failed");
            throw new BadRequestException(e.getMessage());
        }

    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObject> importFile(@RequestBody MultipartFile file) {
        String message = "";

        try {
            int check = trainingProgramService.importFile(file);

            if (check <= 0) {
                message = "Invalid file format: " + file.getOriginalFilename();

                LOGGER.error(message);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(1, message, null));
            }

            List<TrainingProgram> data = trainingProgramService.getAllTrainingPrograms();
            message = "Uploaded the file successfully: " + file.getOriginalFilename();

            LOGGER.info(message);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(0, message, data));
        } catch (DateTimeException e) {
            message = e.getMessage();

            LOGGER.error(message);

            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject(1, null, message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";

            LOGGER.error(message);

            throw new BadRequestException(message);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(1, null, message));
        }
    }
}
