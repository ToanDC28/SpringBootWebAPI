package com.example.ClassRepo.converter;

import com.example.ClassRepo.dto.ClassDto;
import com.example.ClassRepo.dto.SlotDTO;
import com.example.ClassRepo.entity.Class;
import com.example.ClassRepo.entity.Slot;

public class SlotConverter {
    public static SlotDTO toDTO(Slot entity){
        if (entity == null) {
            return null; // or handle this case as needed
        }

        SlotDTO dto = new SlotDTO();

        dto.setId(entity.getSlotId());

        // Add null checks for start time and end time
        if (entity.getClasstime() != null) {
            dto.setClassTime(entity.getClasstime());
        }


        return dto;
    }
    public static Slot toEntity(SlotDTO dto){
        Slot entity = new Slot();
        entity.setClasstime(dto.getClassTime());



        return entity;
    }

    public static Slot toEntity(SlotDTO dto, Slot entity){
        entity.setClasstime(dto.getClassTime());


        return entity;
    }
}
