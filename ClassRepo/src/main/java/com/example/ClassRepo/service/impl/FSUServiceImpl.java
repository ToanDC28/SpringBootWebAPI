package com.example.ClassRepo.service.impl;

import com.example.ClassRepo.entity.FSU;
import com.example.ClassRepo.repository.FSURepository;
import com.example.ClassRepo.service.FSUService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FSUServiceImpl implements FSUService {


    private FSURepository fsuRepository;

    public FSUServiceImpl(FSURepository fsuRepository) {this.fsuRepository = fsuRepository;}

    @Override
    public <S extends FSU> S save(S entity) {
        return fsuRepository.save(entity);
    }

    @Override
    public FSU getFSUsByFsu(String fsu) {
        return fsuRepository.findFSUByName(fsu);
    }

    @Override
    public List<FSU> getAll() {
        return fsuRepository.findAll();
    }
}
