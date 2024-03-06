//package com.fams.syllabus_repository.Service.Impl;
//import com.fams.syllabus_repository.dto.OtherListDto;
//import com.fams.syllabus_repository.dto.OtherSyllabusDto;
//import com.fams.syllabus_repository.entity.AssignmentSheme;
//import com.fams.syllabus_repository.entity.OtherSyllabus;
//import com.fams.syllabus_repository.entity.Syllabus;
//import com.fams.syllabus_repository.entity.TimeAllocation;
//import com.fams.syllabus_repository.exceptions.OtherSyllabusNotFoundException;
//import com.fams.syllabus_repository.repository.AssignmentShemeRepository;
//import com.fams.syllabus_repository.repository.OtherSyllabusRepository;
//import com.fams.syllabus_repository.repository.SyllabusRepository;
//import com.fams.syllabus_repository.repository.TimeAllocationRepositoty;
//import com.fams.syllabus_repository.service.OtherSyllabusService;
//import com.fams.syllabus_repository.service.impl.OtherSyllabusImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//@ExtendWith(MockitoExtension.class)
//public class OtherSyllabusServiceImplTest {
//    @Mock
//    private TimeAllocationRepositoty timeAllocationRepositoty;
//
//    @Mock
//    private SyllabusRepository syllabusRepository;
//
//    @Mock
//    private AssignmentShemeRepository assignmentShemeRepository;
//
//    @Mock
//    private OtherSyllabusRepository otherSyllabusRepository;
//
//    @InjectMocks
//    private OtherSyllabusImpl otherSyllabusService;
//
//
////    @Test
////    void getOtherSyllabusBySyllabusId_ShouldReturnOtherSyllabusDtoList() {
////        // Arrange
////        Long syllabusId = 1L;
////
////        OtherSyllabus otherSyllabus1 = new OtherSyllabus();
////        otherSyllabus1.setId(1L);
////        otherSyllabus1.setTrainingPriciples("Principles 1");
////        otherSyllabus1.setSyllabus(new Syllabus());
////
////        OtherSyllabus otherSyllabus2 = new OtherSyllabus();
////        otherSyllabus2.setId(2L);
////        otherSyllabus2.setTrainingPriciples("Principles 2");
////        otherSyllabus2.setSyllabus(new Syllabus());
////
////        List<OtherSyllabus> otherSyllabusList = new ArrayList<>();
////        otherSyllabusList.add(otherSyllabus1);
////        otherSyllabusList.add(otherSyllabus2);
////
////        when(otherSyllabusRepository.findBySyllabusId(syllabusId)).thenReturn(otherSyllabusList);
////
////        // Act
////        List<OtherSyllabusDto> result = otherSyllabusService.getOrderSyllabusBySyllabusId(syllabusId);
////
////        // Assert
////        assertEquals(2, result.size());
////        assertEquals(otherSyllabus1.getId(), result.get(0).getId());
////        assertEquals(otherSyllabus1.getTrainingPriciples(), result.get(0).getTrainingPriciples());
////        assertEquals(otherSyllabus2.getId(), result.get(1).getId());
////        assertEquals(otherSyllabus2.getTrainingPriciples(), result.get(1).getTrainingPriciples());
////
////        verify(otherSyllabusRepository, times(1)).findBySyllabusId(syllabusId);
////    }
//
////    @Test
////    void createOtherListDto_ShouldReturnOtherListDto() {
////        // Arrange
////        Long syllabusId = 1L;
////
////        // Mock TimeAllocation data
////        TimeAllocation timeAllocation1 = new TimeAllocation();
////        timeAllocation1.setId(1L);
////        // Set other properties for timeAllocation1
////
////        TimeAllocation timeAllocation2 = new TimeAllocation();
////        timeAllocation2.setId(2L);
////        // Set other properties for timeAllocation2
////
////        List<TimeAllocation> timeAllocations = new ArrayList<>();
////        timeAllocations.add(timeAllocation1);
////        timeAllocations.add(timeAllocation2);
////
////        when(timeAllocationRepositoty.findBySyllabusId(syllabusId)).thenReturn(timeAllocations);
////
////        // Mock AssignmentScheme data
////        AssignmentSheme assignmentSheme1 = new AssignmentSheme();
////        assignmentSheme1.setId(1L);
////        // Set other properties for assignmentSheme1
////
////        AssignmentSheme assignmentSheme2 = new AssignmentSheme();
////        assignmentSheme2.setId(2L);
////        // Set other properties for assignmentSheme2
////
////        List<AssignmentSheme> assignmentSchemes = new ArrayList<>();
////        assignmentSchemes.add(assignmentSheme1);
////        assignmentSchemes.add(assignmentSheme2);
////
////        when(assignmentShemeRepository.findBySyllabusId(syllabusId)).thenReturn(assignmentSchemes);
////
////        // Mock OtherSyllabus data
////        OtherSyllabus otherSyllabus1 = new OtherSyllabus();
////        otherSyllabus1.setId(1L);
////        // Set other properties for otherSyllabus1
////
////        OtherSyllabus otherSyllabus2 = new OtherSyllabus();
////        otherSyllabus2.setId(2L);
////        // Set other properties for otherSyllabus2
////
////        List<OtherSyllabus> otherSyllabuses = new ArrayList<>();
////        otherSyllabuses.add(otherSyllabus1);
////        otherSyllabuses.add(otherSyllabus2);
////
////        when(otherSyllabusRepository.findBySyllabusId(syllabusId)).thenReturn(otherSyllabuses);
////
////        // Act
////        OtherListDto result = otherSyllabusService.OtherListDto(syllabusId);
////
////        // Assert
////        assertEquals(timeAllocations.size(), result.getTimeAllocationDtoList().size());
////        assertEquals(assignmentSchemes.size(), result.getAssignmentShemeDtos().size());
////        assertEquals(otherSyllabuses.size(), result.getOtherSyllabusDtos().size());
////
////        verify(timeAllocationRepositoty, times(1)).findBySyllabusId(syllabusId);
////        verify(assignmentShemeRepository, times(1)).findBySyllabusId(syllabusId);
////        verify(otherSyllabusRepository, times(1)).findBySyllabusId(syllabusId);
////    }
//
//    @Test
//    void createOtherSyllabus_WithExistingSyllabus_ShouldReturnOtherSyllabusDto() {
//        // Arrange
//        Long syllabusId = 1L;
//        OtherSyllabusDto otherSyllabusDto = new OtherSyllabusDto();
//        // Set properties for otherSyllabusDto
//
//        Syllabus syllabus = new Syllabus();
//        // Set properties for syllabus
//
//        OtherSyllabus otherSyllabus = new OtherSyllabus();
//        // Set properties for otherSyllabus
//
//        when(syllabusRepository.findById(syllabusId)).thenReturn(Optional.of(syllabus));
//        when(otherSyllabusRepository.save(any(OtherSyllabus.class))).thenReturn(otherSyllabus);
//
//        // Act
//        OtherSyllabusDto result = otherSyllabusService.createOrderSyllabus(syllabusId, otherSyllabusDto);
//
//        // Assert
//        assertEquals(otherSyllabus.getId(), result.getId());
//        // Assert other properties of result against otherSyllabusDto
//
//        verify(syllabusRepository, times(1)).findById(syllabusId);
//        verify(otherSyllabusRepository, times(1)).save(any(OtherSyllabus.class));
//    }
//
//    @Test
//    void createOtherSyllabus_WithNonExistingSyllabus_ShouldThrowException() {
//        // Arrange
//        Long syllabusId = 1L;
//        OtherSyllabusDto otherSyllabusDto = new OtherSyllabusDto();
//        // Set properties for otherSyllabusDto
//
//        when(syllabusRepository.findById(syllabusId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(OtherSyllabusNotFoundException.class, () -> {
//            otherSyllabusService.createOrderSyllabus(syllabusId, otherSyllabusDto);
//        });
//
//        verify(syllabusRepository, times(1)).findById(syllabusId);
//        verify(otherSyllabusRepository, never()).save(any(OtherSyllabus.class));
//    }
//}
