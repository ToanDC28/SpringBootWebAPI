package com.example.ClassRepo.controller;

import com.example.ClassRepo.dto.ClassDto;
import com.example.ClassRepo.dto.PagingClassDto;
import com.example.ClassRepo.entity.FSU;
import com.example.ClassRepo.exceptions.BadRequestException;
import com.example.ClassRepo.exceptions.ResourceNotFoundException;
import com.example.ClassRepo.exceptions.SlotDoesNotExistException;
import com.example.ClassRepo.reponses.ClassResponse;
import com.example.ClassRepo.request.*;
import com.example.ClassRepo.service.FSUService;
import com.example.ClassRepo.service.impl.ClassServiceImpl;
import com.example.ClassRepo.service.impl.FSUServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.ClassRepo.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

import com.example.ClassRepo.entity.Class;
import com.example.ClassRepo.exceptions.ClassDoesNotExistException;
import com.example.ClassRepo.reponses.ResponseObject;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/class")
public class ClassController {

    @Autowired
    private ClassServiceImpl classService;

    @Autowired
    private FSUServiceImpl fsuService;


    @PostMapping("/searchClass")
    public ResponseEntity<ResponseObject> searchClasses(@RequestBody SearchRequestWrapper requestWrapper) {
        PagingClassDto searchRequest = requestWrapper.getSearchRequest();

        String keyword = searchRequest.getKeyword();
        String searchType = "className";

        Page<ClassDto> searchResults = classService.searchClass(keyword, searchType, null, searchRequest);

        return ResponseEntity.ok(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .data(searchResults)
                .build()
        );
    }


    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createClass(@RequestBody ClassCreateRequest request) {
        classService.validateBlank(request);

        try {
            classService.createClass(request);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .code(HttpStatus.OK.value())
                            .message("success")
                            .build()
            );
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }


    @PostMapping("admin/list")
    public ResponseEntity<ResponseObject> getAllUserPagniationList(@RequestBody PagingClassDto dto) {
        if (dto == null) return null;
        return ResponseEntity.ok(ResponseObject.builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .data(classService.getAllClasses(dto))
                .build()
        );
    }

    @PostMapping("/class-detail")
    public ResponseEntity<ClassResponse> getClassById(@RequestBody GetIdRequest getIdRequest) throws JsonProcessingException, ClassNotFoundException, ResourceNotFoundException {
        ClassResponse classDto = classService.getClassById(getIdRequest);
        if (classDto != null) {
            return ResponseEntity.ok(classDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/admin/updateClass")
    public ResponseEntity<ResponseObject> updateClass(@RequestBody ClassUpdateRequest classUpdateRequest) throws ClassDoesNotExistException, ParseException, JsonProcessingException {
        classService.updateClass(classUpdateRequest);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .build()
        );
    }

    @PostMapping("/admin/update-status")
    public ResponseEntity<ResponseObject> updateStatusClass(@RequestBody StatusUpdateRequest request) throws ClassDoesNotExistException, JsonProcessingException {
        classService.UpdateStatus(request);
        return ResponseEntity.ok(
                com.example.ClassRepo.reponses.ResponseObject.builder()
                        .code(HttpStatus.OK.value())
                        .message("success")
                        .build()
        );
    }

    @GetMapping("/get-FSU")
    public ResponseEntity<List<FSU>> getAllFSU() {
        List<FSU> fsu = fsuService.getAll();
        if (fsu != null) {
            return ResponseEntity.ok(fsu);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deleteClass(@RequestBody GetIdRequest request) throws ClassDoesNotExistException {
        try {
            classService.deleteClass(request.getId());
            return ResponseEntity.ok(ResponseObject.builder()
                    .code(HttpStatus.OK.value())
                    .message("Class deleted successfully")
                    .build()
            );
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseObject.builder()
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build()
                    );
        }
    }
    @PostMapping("/get_syllabus_in_use_forService")
    public ResponseEntity<Integer> getSyllabus(@RequestBody SyllabusIDRequest request) throws ClassDoesNotExistException {
        return ResponseEntity.ok(
          classService.getSyllabusInClass(request)
        );
    }

    @PostMapping("/get_training_in_use_forService")
    public ResponseEntity<Integer> getTraining(@RequestBody GetIdRequest request) throws ClassDoesNotExistException {
        return ResponseEntity.ok(
                classService.getTrainingInUse(request)
        );
    }

    @PostMapping("/get_user_in_class_for_service")
    public ResponseEntity<Integer> getUserForService(@RequestBody GetIdRequest request) throws ClassDoesNotExistException {
        return ResponseEntity.ok(
                classService.getUserInUse(request)
        );
    }



}





