//package com.fams.syllabus_repository.Service.Impl;
//
//import com.fams.syllabus_repository.dto.TrainingUnitDto;
//import com.fams.syllabus_repository.dto.TrainingUnitListDto;
//import com.fams.syllabus_repository.entity.DaySyllabus;
//import com.fams.syllabus_repository.entity.TrainingUnit;
//import com.fams.syllabus_repository.exceptions.TrainingUnitNotFoundException;
//import com.fams.syllabus_repository.repository.DaySyllabusRepository;
//import com.fams.syllabus_repository.repository.SyllabusRepository;
//import com.fams.syllabus_repository.repository.TrainingUnitRepository;
//import com.fams.syllabus_repository.service.TrainingUnitService;
//import com.fams.syllabus_repository.service.impl.TrainingUnitServiceImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class TrainingUnitServiceImplTest {
//    @Mock
//    private SyllabusRepository syllabusRepository;
//    @Mock
//    private TrainingUnitRepository trainingUnitRepository;
//    @Mock
//    private DaySyllabusRepository daySyllabusRepository;
//
////    @InjectMocks
////    private TrainingUnitService trainingUnitService = new TrainingUnitServiceImpl(syllabusRepository, trainingUnitRepository, daySyllabusRepository);
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
////    @Test
////    public void testGetTrainingUnitBySyllabusId() {
////        Long syllabusId = 1L;
////        TrainingUnit unit1 = new TrainingUnit();
////        TrainingUnit unit2 = new TrainingUnit();
////
////        List<TrainingUnit> fakeTrainingUnits = new ArrayList<>();
////        fakeTrainingUnits.add(unit1);
////        fakeTrainingUnits.add(unit2);
////
////        when(trainingUnitRepository.findByDaySyllabusId(syllabusId)).thenReturn(fakeTrainingUnits);
////
////        List<TrainingUnitListDto> result = trainingUnitService.getTrainingUnitBySyllabusId(syllabusId);
////
////        assertEquals(2, result.size());
////
////        verify(trainingUnitRepository, times(1)).findByDaySyllabusId(syllabusId);
////    }
//
//
//
////    @Test
////    void createUnit_WithValidData_ShouldReturnTrainingUnitDto() {
////        // Arrange
////        Long daySyllabusId = 1L;
////        String unitName = "Sample unit name";
////
////        TrainingUnitDto trainingUnitDto = new TrainingUnitDto();
////        trainingUnitDto.setUnitName(unitName);
////
////        TrainingUnit trainingUnit = new TrainingUnit();
////        trainingUnit.setUnitName(unitName);
////
////        DaySyllabus daySyllabus = new DaySyllabus();
////        daySyllabus.setId(daySyllabusId);
////
////        when(daySyllabusRepository.findById(daySyllabusId)).thenReturn(Optional.of(daySyllabus));
////        when(trainingUnitRepository.save(any(TrainingUnit.class))).thenReturn(trainingUnit);
////
////        // Act
////        TrainingUnitDto result = trainingUnitService.createUnit(daySyllabusId, trainingUnitDto);
////
////        // Assert
////        assertEquals(unitName, result.getUnitName());
////
////        verify(daySyllabusRepository, times(1)).findById(daySyllabusId);
////        verify(trainingUnitRepository, times(1)).save(any(TrainingUnit.class));
////    }
//
////    @Test
////    void createUnit_WithInvalidDaySyllabusId_ShouldThrowException() {
////        // Arrange
////        Long daySyllabusId = 1L;
////        TrainingUnitDto trainingUnitDto = new TrainingUnitDto();
////
////        when(daySyllabusRepository.findById(daySyllabusId)).thenReturn(Optional.empty());
////
////        // Act and Assert
////        Assertions.assertThrows(TrainingUnitNotFoundException.class,
////                () -> trainingUnitService.createUnit(daySyllabusId, trainingUnitDto));
////
////        verify(daySyllabusRepository, times(1)).findById(daySyllabusId);
////        verify(trainingUnitRepository, never()).save(any());
////    }
//}
