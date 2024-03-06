//package com.fams.syllabus_repository.Service.Impl;
//
//import com.fams.syllabus_repository.dto.DaySyllabusDto;
//import com.fams.syllabus_repository.dto.DaySyllabusListDto;
//import com.fams.syllabus_repository.dto.TrainingContentDto;
//import com.fams.syllabus_repository.dto.TrainingUnitListDto;
//import com.fams.syllabus_repository.entity.DaySyllabus;
//import com.fams.syllabus_repository.entity.Syllabus;
//import com.fams.syllabus_repository.entity.TrainingContent;
//import com.fams.syllabus_repository.entity.TrainingUnit;
//import com.fams.syllabus_repository.repository.DaySyllabusRepository;
//import com.fams.syllabus_repository.repository.SyllabusRepository;
//import com.fams.syllabus_repository.service.impl.DaySyllabusServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.testng.Assert.assertNotNull;
//import static org.testng.Assert.assertNull;
//
//public class DaySyllabusServiceImplTest {
//    @InjectMocks
//    private DaySyllabusServiceImpl daySyllabusService;
//
//    @Mock
//    private DaySyllabusRepository daySyllabusRepository;
//    @Mock
//    private SyllabusRepository syllabusRepository;
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
//    @Test
//    public void testCreateDaySyllabus() {
//        // Mock input data
//        Long syllabusId = 1L;
//        DaySyllabusDto daySyllabusDto = new DaySyllabusDto();
//        daySyllabusDto.setDay("Monday");
//
//        // Mock behavior của repository
//        Syllabus syllabus = new Syllabus();
//        syllabus.setId(syllabusId);
//        when(syllabusRepository.findById(syllabusId)).thenReturn(Optional.of(syllabus));
//
//        DaySyllabus savedDaySyllabus = new DaySyllabus();
//        savedDaySyllabus.setDay(daySyllabusDto.getDay());
//        when(daySyllabusRepository.save(any())).thenReturn(savedDaySyllabus);
//
//        // Gọi phương thức cần kiểm tra
//        DaySyllabusDto result = daySyllabusService.createDaySyllabus(syllabusId, daySyllabusDto);
//
//        // Kiểm tra kết quả
//        assertNotNull(result);
//        assertEquals(daySyllabusDto.getDay(), result.getDay());
//    }
//    @Test
//    public void testDeleteDaySyllabus() {
//        // Mock input data
//        Long syllabusId = 1L;
//        Long dayId = 2L;
//
//        // Mock behavior của repository
//        Syllabus syllabus = new Syllabus();
//        when(syllabusRepository.findById(syllabusId)).thenReturn(Optional.of(syllabus));
//
//        DaySyllabus daySyllabus = new DaySyllabus();
//        when(daySyllabusRepository.findById(dayId)).thenReturn(Optional.of(daySyllabus));
//
//        // Gọi phương thức cần kiểm tra
//        daySyllabusService.deleteDaySyllabus(syllabusId, dayId);
//
//        // Kiểm tra xem phương thức đã gọi các phương thức repository delete chưa
//        verify(daySyllabusRepository, times(1)).delete(daySyllabus);
//    }
////    @Test
////    public void testTrainingUnitListToDtoList() {
////        // Tạo danh sách các đối tượng TrainingUnit
////        List<TrainingUnit> trainingUnits = new ArrayList<>();
////        TrainingUnit trainingUnit1 = new TrainingUnit();
////        //trainingUnit1.setDayNumber("day 1");
////        trainingUnit1.setUnitName("Unit 1");
////        trainingUnit1.setUnitNumber("day1");
////        trainingUnits.add(trainingUnit1);
////
////        TrainingUnit trainingUnit2 = new TrainingUnit();
////        //trainingUnit2.setDayNumber("day 2");
////        trainingUnit2.setUnitName("Unit 2");
////        trainingUnit2.setUnitNumber("102");
////        trainingUnits.add(trainingUnit2);
////
////        // Gọi hàm chuyển đổi
////        List<TrainingUnitListDto> trainingUnitListDtos = DaySyllabusServiceImpl.trainingUnitListToDtoList(trainingUnits);
////
////        // Kiểm tra kết quả
////        assertNotNull(trainingUnitListDtos);
////        assertEquals(2, trainingUnitListDtos.size());
////
////        // Kiểm tra phần tử đầu tiên trong danh sách kết quả
////        TrainingUnitListDto dto1 = trainingUnitListDtos.get(0);
////        assertEquals("day 1", dto1.getDayNumber());
////        assertEquals("Unit 1", dto1.getUnitName());
////        assertEquals("day1", dto1.getUnitNumber());
////        assertNotNull(dto1.getCreatedDate());
////        assertNotNull(dto1.getModifyDate());
////
////        // Kiểm tra phần tử thứ hai trong danh sách kết quả
////        TrainingUnitListDto dto2 = trainingUnitListDtos.get(1);
////        assertEquals("day 2", dto2.getDayNumber());
////        assertEquals("Unit 2", dto2.getUnitName());
////        assertEquals("102", dto2.getUnitNumber());
////        assertNotNull(dto2.getCreatedDate());
////        assertNotNull(dto2.getModifyDate());
////    }
//    @Test
//    public void testToEntityDaySyllabusDto() {
//        // Tạo một đối tượng DaySyllabusDto
//        DaySyllabusDto daySyllabusDto = new DaySyllabusDto();
//        daySyllabusDto.setDay("Monday");
//
//        // Gọi hàm chuyển đổi
//        DaySyllabus daySyllabus = DaySyllabusServiceImpl.toEntity(daySyllabusDto);
//
//        // Kiểm tra kết quả
//        assertNotNull(daySyllabus);
//        assertNull(daySyllabus.getId()); // Vì id không được đặt trong hàm toEntity
//        assertEquals("Monday", daySyllabus.getDay());
//        assertNotNull(daySyllabus.getCreatedDate());
//        assertNotNull(daySyllabus.getModifyDate());
//    }
//
//    @Test
//    public void testToEntityTrainingUnitListDto() {
//        // Tạo một đối tượng TrainingUnitListDto
//        TrainingUnitListDto trainingUnitDto = new TrainingUnitListDto();
//        trainingUnitDto.setUnitName("Unit 1");
//        trainingUnitDto.setUnitNumber("101");
//        trainingUnitDto.setCreatedBy("ducanh");
//        trainingUnitDto.setModifyBy("ducanh");
//
//        // Tạo một danh sách TrainingContentDto
//        List<TrainingContentDto> contentDtos = new ArrayList<>();
//        TrainingContentDto contentDto1 = new TrainingContentDto();
//        contentDto1.setName("Content 1");
//        contentDtos.add(contentDto1);
//        contentDto1.setCreatedBy("ducanh");
//        contentDto1.setModifyBy("ducanh");
//
//        TrainingContentDto contentDto2 = new TrainingContentDto();
//        contentDto2.setName("Content 2");
//        contentDtos.add(contentDto2);
//        contentDto2.setCreatedBy("ducanh");
//        contentDto2.setModifyBy("ducanh");
//
//        trainingUnitDto.setTrainingContent(contentDtos);
//
//        // Gọi hàm chuyển đổi
//        TrainingUnit trainingUnit = DaySyllabusServiceImpl.toEntity(trainingUnitDto);
//
//        // Kiểm tra kết quả
//        assertNotNull(trainingUnit);
//        assertNull(trainingUnit.getId()); // Vì id không được đặt trong hàm toEntity
//        //assertEquals("day 1", trainingUnit.getDayNumber());
//        assertEquals("Unit 1", trainingUnit.getUnitName());
//        assertEquals("101", trainingUnit.getUnitNumber());
//        //assertNotNull(trainingUnit.getCreatedDate());
//        assertNotNull(trainingUnit.getModifyDate());
//        assertNotNull("ducanh", trainingUnit.getCreatedBy());
//        assertNotNull("ducanh", trainingUnit.getModifyBy());
//
//        // Kiểm tra danh sách TrainingContent
//        List<TrainingContent> trainingContents = trainingUnit.getTrainingContents();
//        assertNotNull(trainingContents);
//        assertEquals(2, trainingContents.size());
//
//        // Kiểm tra nội dung của TrainingContent
//        TrainingContent content1 = trainingContents.get(0);
//        assertEquals("Content 1", content1.getName());
//        assertEquals(trainingUnit, content1.getTrainingUnit());
//
//        TrainingContent content2 = trainingContents.get(1);
//        assertEquals("Content 2", content2.getName());
//        assertEquals(trainingUnit, content2.getTrainingUnit());
//    }
//}
