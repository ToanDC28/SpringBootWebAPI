package com.fams.syllabus_repository.service.impl;

import com.fams.syllabus_repository.converter.AssignmentShemeConverter;
import com.fams.syllabus_repository.converter.OtherSyllabusConverter;
import com.fams.syllabus_repository.dto.AssignmentShemeDto;
import com.fams.syllabus_repository.dto.OtherSyllabusCreateUpdateDto;
import com.fams.syllabus_repository.dto.OtherSyllabusDto;
import com.fams.syllabus_repository.entity.AssignmentSheme;
import com.fams.syllabus_repository.entity.OtherSyllabus;
import com.fams.syllabus_repository.entity.Syllabus;
import com.fams.syllabus_repository.exceptions.OtherSyllabusNotFoundException;
import com.fams.syllabus_repository.exceptions.SyllabusNotFoundException;
import com.fams.syllabus_repository.repository.AssignmentShemeRepository;
import com.fams.syllabus_repository.repository.OtherSyllabusRepository;
import com.fams.syllabus_repository.repository.SyllabusRepository;
import com.fams.syllabus_repository.repository.TimeAllocationRepositoty;
import com.fams.syllabus_repository.service.OtherSyllabusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OtherSyllabusImpl implements OtherSyllabusService {
    @Autowired
    private OtherSyllabusRepository otherSyllabusRepository;
    @Autowired
    private SyllabusRepository syllabusRepository;
    @Autowired
    private AssignmentShemeRepository assignmentShemeRepository;

    @Override
    public List<OtherSyllabusDto> getOrderSyllabusBySyllabusId(Long id) {
        List<OtherSyllabus> otherSyllabusList = otherSyllabusRepository.findBySyllabusId(id);


        return otherSyllabusList.stream().map(orderSyllabus -> OtherSyllabusConverter.toDTO(orderSyllabus)).collect(Collectors.toList());
    }

//    public OtherListDto OtherListDto(Long syllabusId) {
//        OtherListDto otherListDto = new OtherListDto();
//
//        // Lấy danh sách TimeAllocation liên quan đến syllabusId
////        List<TimeAllocation> timeAllocations = timeAllocationRepositoty.findBySyllabusId(syllabusId);
////        List<TimeAllocationDto> timeAllocationDtos = timeAllocations.stream()
////                .map(TimeAllocationConverter::toDTO)
////                .collect(Collectors.toList());
////        otherListDto.setTimeAllocationDtoList(timeAllocationDtos);
//
//        // Lấy danh sách AssignmentScheme liên quan đến syllabusId
//        AssignmentSheme assignmentSchemes = (AssignmentSheme) assignmentShemeRepository.findBySyllabusId(syllabusId);
//        AssignmentShemeDto assignmentSchemeDtos = assignmentSchemes.stream()
//                .map(AssignmentShemeConverter::toDTO)
//                .collect(Collectors.toList());
//        otherListDto.setAssignmentShemeDtos(assignmentSchemeDtos);
//
//        // Lấy danh sách OtherSyllabus liên quan đến syllabusId
//        List<OtherSyllabus> otherSyllabuses = otherSyllabusRepository.findBySyllabusId(syllabusId);
//        List<OtherSyllabusDto> otherSyllabusDtos = otherSyllabuses.stream()
//                .map(OtherSyllabusConverter::toDTO)
//                .collect(Collectors.toList());
//        otherListDto.setOtherSyllabusDtos(otherSyllabusDtos);
//
//        return otherListDto;
//    }



    @Override
    public OtherSyllabusDto createOrderSyllabus(Long syllabusId, OtherSyllabusDto otherSyllabusDto) {
        OtherSyllabus otherSyllabus = OtherSyllabusConverter.toEntity(otherSyllabusDto);

        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new OtherSyllabusNotFoundException("Another syllabus with associated not found"));

        otherSyllabus.setSyllabus(syllabus);

        OtherSyllabus newOtherSyllabus = otherSyllabusRepository.save(otherSyllabus);

        return OtherSyllabusConverter.toDTO(newOtherSyllabus);
    }
    @Override
    public OtherSyllabusDto updateOtherSyllabusBySyllabusId(Long syllabusId, OtherSyllabusDto otherSyllabusDto) {
        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new SyllabusNotFoundException("Syllabus not found"));

        // Find OtherSyllabus based on ID instead of the entire list of OtherSyllabus in Syllabus
        OtherSyllabus otherSyllabus = otherSyllabusRepository.findById(otherSyllabusDto.getId())
                .orElseThrow(() -> new OtherSyllabusNotFoundException("OtherSyllabus not found for the given ID"));

        // Check to see if OtherSyllabus belongs to the Syllabus that needs to be updated
        if (otherSyllabus.getSyllabus().getId().equals(syllabusId)) {
            // Perform updates to OtherSyllabus information
            OtherSyllabus updatedOtherSyllabus = OtherSyllabusConverter.toEntity(otherSyllabusDto, otherSyllabus);

            // Save OtherSyllabus with changes to the database
            otherSyllabusRepository.save(updatedOtherSyllabus);

            return OtherSyllabusConverter.toDTO(updatedOtherSyllabus);
        } else {
            throw new OtherSyllabusNotFoundException("OtherSyllabus not found for the given Syllabus");
        }
    }
    @Override
    public OtherSyllabusDto saveOrUpdateOtherSyllabus(Long syllabusId, OtherSyllabusDto otherSyllabusDto) {
        // Retrieve the associated syllabus
        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new SyllabusNotFoundException("Syllabus not found"));

        if (otherSyllabusDto.getId() != null && otherSyllabusDto.getId() != 0) {
            // If OtherSyllabus ID exists, perform an update
            OtherSyllabus otherSyllabus = otherSyllabusRepository.findById(otherSyllabusDto.getId())
                    .orElseThrow(() -> new OtherSyllabusNotFoundException("OtherSyllabus not found for the given ID"));

            if (otherSyllabus.getSyllabus().getId().equals(syllabusId)) {
                // Update OtherSyllabus information
                OtherSyllabus updatedOtherSyllabus = OtherSyllabusConverter.toEntity(otherSyllabusDto, otherSyllabus);

                // Save the updated OtherSyllabus to the database
                otherSyllabusRepository.save(updatedOtherSyllabus);

                return OtherSyllabusConverter.toDTO(updatedOtherSyllabus);
            } else {
                throw new OtherSyllabusNotFoundException("OtherSyllabus not associated with the given Syllabus");
            }
        } else {
            // If OtherSyllabus ID does not exist or is 0, create a new OtherSyllabus
            OtherSyllabus newOtherSyllabus = OtherSyllabusConverter.toEntity(otherSyllabusDto);
            newOtherSyllabus.setSyllabus(syllabus);

            OtherSyllabus savedOtherSyllabus = otherSyllabusRepository.save(newOtherSyllabus);

            return OtherSyllabusConverter.toDTO(savedOtherSyllabus);
        }
    }
    @Override
    public OtherSyllabusCreateUpdateDto createOrUpdateSchemesBySyllabusId(Long syllabusId, OtherSyllabusCreateUpdateDto dto) {
        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new SyllabusNotFoundException("Syllabus not found"));

        AssignmentShemeDto assignmentShemeDto = dto.getAssignmentShemeDtos();
        OtherSyllabusDto otherSyllabusDto = dto.getOtherSyllabusDtos();

        AssignmentSheme assignmentSheme = assignmentShemeRepository.findBySyllabus(syllabus);
        if (assignmentSheme == null) {
            assignmentSheme = AssignmentShemeConverter.toEntity(assignmentShemeDto);
            assignmentSheme.setSyllabus(syllabus);
        } else {
            AssignmentSheme originalAssignmentSheme = AssignmentShemeConverter.toEntity(assignmentShemeDto, assignmentSheme);
            originalAssignmentSheme.setModifyDate(LocalDate.now()); // Cập nhật modifyDate
            assignmentSheme = originalAssignmentSheme;
        }
        assignmentShemeRepository.save(assignmentSheme);

        OtherSyllabus otherSyllabus = otherSyllabusRepository.findBySyllabus(syllabus);
        if (otherSyllabus == null) {
            otherSyllabus = OtherSyllabusConverter.toEntity(otherSyllabusDto);
            otherSyllabus.setSyllabus(syllabus);
        } else {
            OtherSyllabus originalOtherSyllabus = OtherSyllabusConverter.toEntity(otherSyllabusDto, otherSyllabus);
            originalOtherSyllabus.setModifyDate(LocalDate.now()); // Cập nhật modifyDate
            otherSyllabus = originalOtherSyllabus;
        }
        otherSyllabusRepository.save(otherSyllabus);

        return OtherSyllabusConverter.toCreateUpdateDTO(assignmentSheme, otherSyllabus);
    }


}
