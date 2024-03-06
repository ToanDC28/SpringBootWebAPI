package com.example.ClassRepo.service;

import com.example.ClassRepo.dto.SlotDTO;
import com.example.ClassRepo.entity.FSU;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FSUService {
    <S extends FSU> S save(S entity);

    FSU getFSUsByFsu(String fsu);


    List<FSU> getAll();
}
