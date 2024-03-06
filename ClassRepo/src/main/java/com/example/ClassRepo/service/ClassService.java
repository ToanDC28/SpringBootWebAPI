package com.example.ClassRepo.service;

import com.example.ClassRepo.dto.ClassDto;
import com.example.ClassRepo.dto.PagingClassDto;
import com.example.ClassRepo.entity.Class;
import com.example.ClassRepo.exceptions.ClassDoesNotExistException;
import com.example.ClassRepo.exceptions.ResourceNotFoundException;
import com.example.ClassRepo.exceptions.SlotDoesNotExistException;
import com.example.ClassRepo.reponses.ClassResponse;
import com.example.ClassRepo.request.ClassCreateRequest;
import com.example.ClassRepo.request.ClassUpdateRequest;
import com.example.ClassRepo.request.GetIdRequest;
import com.example.ClassRepo.request.StatusUpdateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;


import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface ClassService {
    void createClass(ClassCreateRequest request) throws ParseException, SlotDoesNotExistException, JsonProcessingException, ResourceNotFoundException;

    Page<ClassDto> searchClass(String keyword, String searchType, ClassDto classDto, PagingClassDto searchRequest);


    ClassResponse getClassById(GetIdRequest request) throws SlotDoesNotExistException, JsonProcessingException, ClassNotFoundException, ResourceNotFoundException;


    Page<ClassDto> getAllClasses(PagingClassDto dto);
    //Cập nhật thông tin của class
    void updateClass(ClassUpdateRequest classUpdateRequest) throws ClassDoesNotExistException, ParseException, JsonProcessingException;
    void UpdateStatus(StatusUpdateRequest request) throws ClassDoesNotExistException, JsonProcessingException;
    void deleteClass(Integer class_id) throws ClassDoesNotExistException;

}
