package com.example.ClassRepo.service;

import com.example.ClassRepo.dto.SlotDTO;
import com.example.ClassRepo.entity.Slot;
import com.example.ClassRepo.exceptions.SlotDoesNotExistException;
import com.example.ClassRepo.request.SlotUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface SlotService {

    <S extends Slot> S save(S entity);

    List<SlotDTO> getAllSlot();
//    List<SlotDTO> getSlotsByClassId(Integer classId);


    Slot getSlotByClassTime(String time);

    List<SlotDTO> getSlotsByClassId(Integer classId);

    //Update Slot
}
