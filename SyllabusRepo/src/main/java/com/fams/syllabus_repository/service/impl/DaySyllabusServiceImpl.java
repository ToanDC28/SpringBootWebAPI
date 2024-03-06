package com.fams.syllabus_repository.service.impl;

import com.fams.syllabus_repository.converter.DaySyllabusConverter;
import com.fams.syllabus_repository.converter.TrainingContentConverter;
import com.fams.syllabus_repository.dto.DaySyllabusDto;
import com.fams.syllabus_repository.dto.DaySyllabusListDto;
import com.fams.syllabus_repository.dto.TrainingContentDto;
import com.fams.syllabus_repository.dto.TrainingUnitListDto;
import com.fams.syllabus_repository.entity.DaySyllabus;
import com.fams.syllabus_repository.entity.Syllabus;
import com.fams.syllabus_repository.entity.TrainingContent;
import com.fams.syllabus_repository.entity.TrainingUnit;
import com.fams.syllabus_repository.enums.DeliveryType;
import com.fams.syllabus_repository.exceptions.DaySyllabusNotFoundException;
import com.fams.syllabus_repository.exceptions.SyllabusNotFoundException;
import com.fams.syllabus_repository.exceptions.TrainingContentNotFoundException;
import com.fams.syllabus_repository.exceptions.TrainingUnitNotFoundException;
import com.fams.syllabus_repository.repository.DaySyllabusRepository;
import com.fams.syllabus_repository.repository.SyllabusRepository;
import com.fams.syllabus_repository.repository.TrainingContentRepository;
import com.fams.syllabus_repository.repository.TrainingUnitRepository;
import com.fams.syllabus_repository.service.DaySyllabusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DaySyllabusServiceImpl implements DaySyllabusService {
    @Autowired
    private DaySyllabusRepository daySyllabusRepository;
    @Autowired
    private SyllabusRepository syllabusRepository;
    @Autowired
    private TrainingUnitRepository trainingUnitRepository;
    @Autowired
    private TrainingContentRepository trainingContentRepository;

    @Override
    public List<DaySyllabusListDto> getDaySyllabusBySyllabusId(Long id) {
        List<DaySyllabus> daySyllabus = daySyllabusRepository.findBySyllabusId(id);

        return daySyllabus.stream().map(day -> {
            DaySyllabusListDto unitDto = toDTO(day);

            List<TrainingUnitListDto> unitDtoList = day.getTrainingUnit().stream()
                    .map(content -> toDTO(content))
                    .collect(Collectors.toList());

            unitDto.setTrainingUnitListDtos(unitDtoList);

            return unitDto;
        }).collect(Collectors.toList());
    }

    @Override
    public DaySyllabusDto createDaySyllabus(Long syllabusId, DaySyllabusDto daySyllabusDto) {
        DaySyllabus daySyllabus = DaySyllabusConverter.toEntity(daySyllabusDto);

        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new DaySyllabusNotFoundException("Day with associated syllabus not found"));
        daySyllabus.setSyllabus(syllabus);

        DaySyllabus newDay = daySyllabusRepository.save(daySyllabus);

        return DaySyllabusConverter.toDTO(newDay);
    }

    @Override
    @Transactional
    public void deleteDaySyllabus(Long syllabusId, Long dayId) {
        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new SyllabusNotFoundException("Syllabus with associated day not found"));
        DaySyllabus daySyllabus = daySyllabusRepository.findById(dayId)
                .orElseThrow(() -> new DaySyllabusNotFoundException("DaySyllabus not found"));

        // Xóa DaySyllabus và TrainingUnit liên quan sẽ bị xóa theo
        daySyllabusRepository.delete(daySyllabus);
    }
    @Override
    @Transactional
    public void deleteDaySyllabusById(Long dayId) {
        DaySyllabus daySyllabus = daySyllabusRepository.findById(dayId)
                .orElseThrow(() -> new DaySyllabusNotFoundException("DaySyllabus not found"));

        // Deleting the DaySyllabus and related TrainingUnits
        daySyllabusRepository.delete(daySyllabus);
    }


    @Override
    public DaySyllabusDto createDaySyllabusa(Long syllabusId, DaySyllabusDto daySyllabusDto) {
        DaySyllabus daySyllabus = toEntity(daySyllabusDto);

        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new DaySyllabusNotFoundException("Day with associated syllabus not found"));
        daySyllabus.setSyllabus(syllabus);

        List<TrainingUnit> trainingUnits = new ArrayList<>();
        for (TrainingUnitListDto trainingUnitDto : daySyllabusDto.getTrainingUnitListDtos()) {
            TrainingUnit trainingUnit = toEntity(trainingUnitDto);
            trainingUnit.setDaySyllabus(daySyllabus); // Liên kết với DaySyllabus
            trainingUnits.add(trainingUnit);
        }

        daySyllabus.setTrainingUnit(trainingUnits); // Đặt danh sách TrainingUnit vào DaySyllabus

        daySyllabusDto.setTrainingUnitListDtos(daySyllabusDto.getTrainingUnitListDtos());

        DaySyllabus newDay = daySyllabusRepository.save(daySyllabus);

        return DaySyllabusConverter.toDTOCreate(newDay);
    }

    @Override
    public List<DaySyllabusDto> updateDaySyllabusList(Long syllabusId, List<DaySyllabusDto> daySyllabusDtos) {
        List<DaySyllabusDto> updatedDaySyllabusDtos = new ArrayList<>();

        for (DaySyllabusDto daySyllabusDto : daySyllabusDtos) {
            DaySyllabus daySyllabus;
            if (daySyllabusDto.getId() == null || daySyllabusDto.getId() == 0) {
                daySyllabus = new DaySyllabus();
            } else {
                daySyllabus = daySyllabusRepository.findById(daySyllabusDto.getId())
                        .orElseThrow(() -> new DaySyllabusNotFoundException("DaySyllabus not found"));
            }

            daySyllabus.setDay(daySyllabusDto.getDay());
            if (daySyllabus.getId() == null || daySyllabus.getId() == 0) {
                daySyllabus.setCreatedDate(LocalDate.now());
            }
            daySyllabus.setCreatedBy(daySyllabusDto.getCreatedBy());
            daySyllabus.setModifyDate(LocalDate.now());
            daySyllabus.setModifyBy(daySyllabusDto.getModifyBy());
            daySyllabus.setSyllabus(syllabusRepository.findById(syllabusId)
                    .orElseThrow(() -> new SyllabusNotFoundException("Syllabus not found")));

            daySyllabus = daySyllabusRepository.save(daySyllabus);

            for (TrainingUnitListDto unitDto : daySyllabusDto.getTrainingUnitListDtos()) {
                TrainingUnit trainingUnit;
                if (unitDto.getId() == null || unitDto.getId() == 0) {
                    trainingUnit = new TrainingUnit();
                } else {
                    trainingUnit = trainingUnitRepository.findById(unitDto.getId())
                            .orElseThrow(() -> new TrainingUnitNotFoundException("TrainingUnit not found"));
                }

                trainingUnit.setUnitNumber(unitDto.getUnitNumber());
                trainingUnit.setUnitName(unitDto.getUnitName());
                if (trainingUnit.getId() == null || trainingUnit.getId() == 0) {
                    trainingUnit.setCreatedDate(LocalDate.now());
                }
                trainingUnit.setCreatedBy(unitDto.getCreatedBy());
                trainingUnit.setModifyDate(LocalDate.now());
                trainingUnit.setModifyBy(unitDto.getModifyBy());
                trainingUnit.setDaySyllabus(daySyllabus);
                trainingUnit = trainingUnitRepository.save(trainingUnit);

                for (TrainingContentDto contentDto : unitDto.getTrainingContent()) {
                    TrainingContent trainingContent;
                    if (contentDto.getId() == null || contentDto.getId() == 0) {
                        trainingContent = new TrainingContent();
                    } else {
                        trainingContent = trainingContentRepository.findById(contentDto.getId())
                                .orElseThrow(() -> new TrainingContentNotFoundException("TrainingContent not found"));
                    }

                    trainingContent.setName(contentDto.getName());
                    trainingContent.setOutputStandard(contentDto.getOutputStandard());
                    trainingContent.setTrainingTime(contentDto.getTrainingTime());
                    trainingContent.setDeliveryType(DeliveryType.valueOf(contentDto.getDeliveryType()));
                    trainingContent.setMethod(contentDto.getMethod());
                    trainingContent.setTrainingUnit(trainingUnit);
                    if (trainingContent.getId() == null || trainingContent.getId() == 0) {
                        trainingContent.setCreatedDate(LocalDate.now());
                    }
                    trainingContent.setCreatedBy(contentDto.getCreatedBy());
                    trainingContent.setModifyDate(LocalDate.now());
                    trainingContent.setModifyBy(contentDto.getModifyBy());
                    trainingContentRepository.save(trainingContent);
                }
            }

            // Lưu hoặc cập nhật daySyllabus
            daySyllabus = daySyllabusRepository.save(daySyllabus);

            // Chuyển đổi daySyllabus thành DTO và thêm vào danh sách kết quả
            updatedDaySyllabusDtos.add(DaySyllabusConverter.toDTOCreate(daySyllabus));
        }

        return updatedDaySyllabusDtos;
    }






    @Override
    public List<DaySyllabusDto> createDaySyllabusList(Long syllabusId, List<DaySyllabusDto> daySyllabusDtos) {
        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new DaySyllabusNotFoundException("Syllabus not found"));

        List<DaySyllabusDto> createdDaySyllabusDtos = new ArrayList<>();
        for (DaySyllabusDto daySyllabusDto : daySyllabusDtos) {
            DaySyllabus daySyllabus = toEntity(daySyllabusDto);
            daySyllabus.setSyllabus(syllabus);

            List<TrainingUnit> trainingUnits = new ArrayList<>();
            for (TrainingUnitListDto trainingUnitDto : daySyllabusDto.getTrainingUnitListDtos()) {
                TrainingUnit trainingUnit = toEntity(trainingUnitDto);
                trainingUnit.setDaySyllabus(daySyllabus);
                trainingUnits.add(trainingUnit);
            }

            daySyllabus.setTrainingUnit(trainingUnits);

            DaySyllabus newDay = daySyllabusRepository.save(daySyllabus);
            createdDaySyllabusDtos.add(DaySyllabusConverter.toDTOCreate(newDay));
        }

        return createdDaySyllabusDtos;
    }

    public static List<TrainingUnitListDto> trainingUnitListToDtoList(List<TrainingUnit> trainingUnits) {
        List<TrainingUnitListDto> trainingUnitListDtos = new ArrayList<>();

        for (TrainingUnit trainingUnit : trainingUnits) {
            TrainingUnitListDto trainingUnitDto = new TrainingUnitListDto();
            //trainingUnitDto.setDayNumber(trainingUnit.getDayNumber());
            trainingUnitDto.setUnitName(trainingUnit.getUnitName());
            trainingUnitDto.setUnitNumber(trainingUnit.getUnitNumber());
            trainingUnitDto.setCreatedDate(LocalDate.now());
            trainingUnitDto.setModifyDate(LocalDate.now());

            // Nếu cần chuyển đổi thêm các trường khác, thêm vào đây

            trainingUnitListDtos.add(trainingUnitDto);
        }

        return trainingUnitListDtos;
    }




    public static DaySyllabusListDto toDTO(DaySyllabus entity) {
        DaySyllabusListDto dto = new DaySyllabusListDto();
        dto.setId(entity.getId());
        dto.setDay(entity.getDay());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifyBy(entity.getModifyBy());
        dto.setModifyDate(entity.getModifyDate());
        return dto;
    }

    public static TrainingUnitListDto toDTO(TrainingUnit entity) {
        TrainingUnitListDto dto = new TrainingUnitListDto();
        dto.setId(entity.getId());
        //dto.setTopic(entity.getTopic());
        //dto.setDayNumber(entity.getDayNumber());
        dto.setUnitNumber(entity.getUnitNumber());
        dto.setUnitName(entity.getUnitName());
        //dto.setSyllabus(entity.getSyllabus());
        List<TrainingContentDto> trainingContentList = entity.getTrainingContents().stream()
                .map(content -> TrainingContentConverter.toDto(content))
                .collect(Collectors.toList());
        dto.setTrainingContent(trainingContentList);
        return dto;
    }

    public static DaySyllabus toEntity(DaySyllabusDto dto){
        DaySyllabus entity = new DaySyllabus();
        //entity.setId(dto.getId());
        entity.setDay(dto.getDay());
        entity.setCreatedDate(LocalDate.now());
        entity.setModifyDate(LocalDate.now());
        return entity;
    }

    public static TrainingUnit toEntity(TrainingUnitListDto dto) {
        TrainingUnit entity = new TrainingUnit();
        //entity.setDayNumber(dto.getDayNumber());
        entity.setUnitName(dto.getUnitName());
        entity.setUnitNumber(dto.getUnitNumber());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());

        // Tạo danh sách TrainingContent
        List<TrainingContent> trainingContents = new ArrayList<>();
        for (TrainingContentDto contentDto : dto.getTrainingContent()) {
            TrainingContent content = TrainingContentConverter.toEntity(contentDto);
            content.setTrainingUnit(entity);
            trainingContents.add(content);
        }
        entity.setTrainingContents(trainingContents);
        return entity;
    }

    public static TrainingUnit toEntityUnitList(TrainingUnitListDto dto, TrainingUnit entity) {
        //TrainingUnit entity = new TrainingUnit();
        //entity.setId(dto.getId());
        //entity.setTopic(dto.getTopic());
        //entity.setDayNumber(dto.getDayNumber());
        entity.setUnitNumber(dto.getUnitNumber());
        entity.setUnitName(dto.getUnitName());
        entity.setCreatedDate(LocalDate.now());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifyDate(LocalDate.now());
        entity.setModifyBy(dto.getModifyBy());
        //entity.setSyllabus(dto.getSyllabus());
        return entity;
    }
//    public static TrainingUnit toEntityUnitContent(TrainingUnitListDto dto, TrainingUnit existingTrainingUnit) {
//        if (existingTrainingUnit == null) {
//            existingTrainingUnit = new TrainingUnit();
//            existingTrainingUnit.setCreatedDate(LocalDate.now());
//        }
//
//        //existingTrainingUnit.setDayNumber(dto.getDayNumber());
//        existingTrainingUnit.setUnitName(dto.getUnitName());
//        existingTrainingUnit.setUnitNumber(dto.getUnitNumber());
//        existingTrainingUnit.setModifyDate(LocalDate.now());
//
//        List<TrainingContent> trainingContents = new ArrayList<>();
//        for (TrainingContentDto contentDto : dto.getTrainingContent()) {
//            TrainingContent existingTrainingContent = null;
//            if (contentDto.getId() != null) {
//                existingTrainingContent = trainingContentRepository.findById(contentDto.getId())
//                        .orElse(null);
//            }
//
//            assert existingTrainingContent != null;
//            TrainingContent updatedTrainingContent = TrainingContentConverter.toEntity(contentDto, existingTrainingContent);
//            updatedTrainingContent.setTrainingUnit(existingTrainingUnit);
//            trainingContents.add(updatedTrainingContent);
//        }
//
//        existingTrainingUnit.setTrainingContents(trainingContents);
//
//        return existingTrainingUnit;
//    }

}
