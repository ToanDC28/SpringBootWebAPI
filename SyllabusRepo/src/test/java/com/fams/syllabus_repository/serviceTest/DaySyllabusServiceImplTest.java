//package com.fams.syllabus_repository.serviceTest;
//import static org.mockito.Mockito.when;
//
//import com.fams.syllabus_repository.dto.DaySyllabusListDto;
//import com.fams.syllabus_repository.entity.DaySyllabus;
//import com.fams.syllabus_repository.entity.TrainingUnit;
//import com.fams.syllabus_repository.repository.DaySyllabusRepository;
//import com.fams.syllabus_repository.service.impl.DaySyllabusServiceImpl;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class DaySyllabusServiceImplTest {
//    @InjectMocks
//    private DaySyllabusServiceImpl daySyllabusService;
//
//    @Mock
//    private DaySyllabusRepository daySyllabusRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void testGetDaySyllabusBySyllabusId() {
//        Long syllabusId = 1L;
//
//        // Create a sample DaySyllabus and its associated TrainingUnits
//        DaySyllabus mockDaySyllabus = new DaySyllabus();
//        mockDaySyllabus.setId(1L);
//        mockDaySyllabus.setDay("Day 1");
//
//        TrainingUnit trainingUnit1 = new TrainingUnit();
//        trainingUnit1.setId(101L);
//        trainingUnit1.setUnitName("Unit 1");
//
//        TrainingUnit trainingUnit2 = new TrainingUnit();
//        trainingUnit2.setId(102L);
//        trainingUnit2.setUnitName("Unit 2");
//
//        mockDaySyllabus.setTrainingUnit(Arrays.asList(trainingUnit1, trainingUnit2));
//
//        // Mock the behavior of daySyllabusRepository
//        when(daySyllabusRepository.findBySyllabusId(syllabusId))
//                .thenReturn(Collections.singletonList(mockDaySyllabus));
//
//        // Call the method under test
//        List<DaySyllabusListDto> result = daySyllabusService.getDaySyllabusBySyllabusId(syllabusId);
//
//        // Assertions
//        assertEquals(1, result.size()); // Ensure there is one DaySyllabus in the result
//
//        DaySyllabusListDto daySyllabusDto = result.get(0);
//        assertEquals(mockDaySyllabus.getId(), daySyllabusDto.getId()); // Verify the ID
//        assertEquals(mockDaySyllabus.getDay(), daySyllabusDto.getDay()); // Verify the name
//
//        // Verify the TrainingUnitListDtos size
//        assertEquals(2, daySyllabusDto.getTrainingUnitListDtos().size());
//
//        // You can add more specific assertions for TrainingUnitListDtos if needed
//    }
//}
