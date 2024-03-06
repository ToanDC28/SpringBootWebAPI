package com.example.ClassRepo.service.impl;

import com.example.ClassRepo.converter.SlotConverter;
import com.example.ClassRepo.dto.SlotDTO;
import com.example.ClassRepo.entity.Slot;
import com.example.ClassRepo.repository.SlotRepository;
import com.example.ClassRepo.service.SlotService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SlotServiceImpl implements SlotService {
    private final SlotRepository slotRepository;

    public SlotServiceImpl(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @Override
    public <S extends Slot> S save(S entity) {
        return slotRepository.save(entity);
    }

    @Override
    public List<SlotDTO> getAllSlot() {
        List<SlotDTO> result = new ArrayList<>();
        List<Slot> slotEntity = slotRepository.findAll();
        for (Slot item : slotEntity) {
            SlotDTO dto = SlotConverter.toDTO(item);
            result.add(dto);
        }
        return result;
    }
    @Override
    public Slot getSlotByClassTime(String time){
        return slotRepository.findSlotByClasstime(time);
    }

    public List<SlotDTO> getSlotsByClassId(Integer classId) {
        // Implement your logic to fetch slots by class ID from the repository
        Optional<Slot> slots = slotRepository.findById(classId);

        // Convert Slot entities to SlotDto objects (you can use SlotConverter)
        List<SlotDTO> slotDtos = slots.stream()
                .map(SlotConverter::toDTO)
                .collect(Collectors.toList());

        return slotDtos;
    }



}
