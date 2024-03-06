//package com.team2.trainingprogramrepo.service;
//
//import com.team2.trainingprogramrepo.dto.*;
//import com.team2.trainingprogramrepo.entity.TrainingProgram;
//import com.team2.trainingprogramrepo.entity.TrainingSyllabus;
//import com.team2.trainingprogramrepo.exception.DuplicateRecordException;
//import com.team2.trainingprogramrepo.repository.TrainingProgramRepository;
//import com.team2.trainingprogramrepo.repository.TrainingSyllabusRepository;
//import com.team2.trainingprogramrepo.response.Training_SyllabusResponse;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TrainingProgramServiceTest {
//
//    @Mock
//    private TrainingProgramRepository trainingProgramRepository;
//    @Mock
//    private RestTemplate restTemplate;
//    @Mock
//    private TrainingSyllabusRepository trainingSyllabusRepository;
//    @InjectMocks
//    private TrainingProgramService trainingProgramService;
//    @Mock
//    private Properties properties;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        properties = new Properties();
//
//    }
//
//    @Test
//    public void getAllTrainingPrograms() {
//        List<TrainingProgram> trainingPrograms = new ArrayList<>();
//        trainingPrograms.add(new TrainingProgram());
//
//        when(trainingProgramRepository.findAll()).thenReturn(trainingPrograms);
//
//        List<TrainingProgram> actualTrainingPrograms = trainingProgramService.getAllTrainingPrograms();
//
//        assertEquals(trainingPrograms, actualTrainingPrograms);
//    }
//
//
////    @Test
////    public void createTrainingProgramWithException() {
////        TrainingProgram trainingProgram = new TrainingProgram();
////        TrainingProgramCreateRequest trainingProgramCreateRequest = new TrainingProgramCreateRequest();
////
////        when(trainingProgramRepository.save(trainingProgram)).thenThrow(new RuntimeException());
////
////        assertThrows(IllegalStateException.class, () -> trainingProgramService.createTrainingProgram(trainingProgramCreateRequest));
////    }
//
//
//
//    @Test
//    public void duplicateTrainingProgramWithValidTrainingProgramId() {
//        // Given
//        Integer trainingProgramId = 1;
//        TrainingProgram existedTrainingProgram = new TrainingProgram();
//        existedTrainingProgram.setTrainingProgramId(trainingProgramId);
//        existedTrainingProgram.setTrainingProgramCode("TP-001");
//        existedTrainingProgram.setName("Training Program 1");
//        existedTrainingProgram.setCreatedDate(new java.sql.Date(2022, 01, 01));
//        existedTrainingProgram.setModifiedBy("admin");
//        existedTrainingProgram.setTopicCode("TC-001");
//        existedTrainingProgram.setStatus("Active");
//
//        when(trainingProgramRepository.findById(trainingProgramId)).thenReturn(java.util.Optional.of(existedTrainingProgram));
//
//        // When
//        TrainingProgram duplicatedTrainingProgram = trainingProgramService.duplicateTrainingProgram(trainingProgramId);
//
//        // Then
//        assertEquals("TP-001", duplicatedTrainingProgram.getTrainingProgramCode());
//        assertEquals("Training Program 1 (duplicate)", duplicatedTrainingProgram.getName());
//        assertEquals(existedTrainingProgram.getCreatedDate(), duplicatedTrainingProgram.getCreatedDate());
//        assertEquals(existedTrainingProgram.getModifiedBy(), duplicatedTrainingProgram.getModifiedBy());
//        assertEquals(existedTrainingProgram.getTopicCode(), duplicatedTrainingProgram.getTopicCode());
//        assertEquals(existedTrainingProgram.getStatus(), duplicatedTrainingProgram.getStatus());
//    }
//
//    @Test
//    public void duplicateTrainingProgramWithNonexistentTrainingProgramId() {
//        // Given
//        Integer trainingProgramId = 1;
//
//        when(trainingProgramRepository.findById(trainingProgramId)).thenReturn(java.util.Optional.empty());
//
//        // When
//        assertThrows(IllegalStateException.class, () -> trainingProgramService.duplicateTrainingProgram(trainingProgramId));
//    }
//
//    @Test
//    public void deactivateTrainingProgramWithNullTrainingProgramId() {
//        // When
//        int result = trainingProgramService.deactivateTrainingProgram(null);
//
//        // Then
//        assertEquals(0, result);
//    }
//
//    @Test
//    public void deactivateTrainingProgramWithNonexistentTrainingProgramId() {
//        // Given
//        Integer trainingProgramId = 1;
//
//        when(trainingProgramRepository.findById(trainingProgramId)).thenReturn(java.util.Optional.empty());
//
//        // When
//        assertThrows(IllegalStateException.class, () -> trainingProgramService.deactivateTrainingProgram(trainingProgramId));
//    }
//
//
//    @Test
//    public void activateTrainingProgramWithNullTrainingProgramId() {
//        // When
//        int result = trainingProgramService.activateTrainingProgram(null);
//
//        // Then
//        assertEquals(0, result);
//    }
//
//    @Test
//    public void activateTrainingProgramWithNonexistentTrainingProgramId() {
//        // Given
//        Integer trainingProgramId = 1;
//
//        when(trainingProgramRepository.findById(trainingProgramId)).thenReturn(java.util.Optional.empty());
//
//        // When
//        assertThrows(IllegalStateException.class, () -> trainingProgramService.activateTrainingProgram(trainingProgramId));
//    }
//
//
////    @Test
////    public void importTrainingProgramFromFileWithValidCsvData() throws IOException, ParseException {
////        // Given
////        String csvData = "id,trainingProgramCode,name,createdDate,modifiedDate,createdBy,modifiedBy,startDate,topicCode,endDate,status\n" +
////                "1,TP-001,Training Program 1,2023-10-23,2023-10-24,admin,admin,2023-10-23,TC-001,2023-10-24,Active";
////        ByteArrayInputStream is = new ByteArrayInputStream(csvData.getBytes());
////
////        TrainingProgram trainingProgram = new TrainingProgram();
////        trainingProgram.setTrainingProgramId(1);
////        trainingProgram.setTrainingProgramCode("TP-001");
////        trainingProgram.setName("Training Program 1");
////        trainingProgram.setCreatedDate(java.sql.Date.valueOf("2023-10-23"));
////        trainingProgram.setModifiedDate(java.sql.Date.valueOf("2023-10-24"));
////        trainingProgram.setCreatedBy("admin");
////        trainingProgram.setModifiedBy("admin");
////        trainingProgram.setTopicCode("TC-001");
////        trainingProgram.setStatus("Active");
////
////        List<TrainingProgram> trainingPrograms = new ArrayList<>();
////        trainingPrograms.add(trainingProgram);
////
////        when(trainingProgramRepository.findById(1)).thenReturn(Optional.empty());
////
////        // When
////        List<TrainingProgram> actualTrainingPrograms = trainingProgramService.importTrainingProgramFromFile(is);
////
////        // Then
////        assertEquals(trainingPrograms, actualTrainingPrograms);
////    }
//
//    @Test
//    public void testImportFile() throws IOException {
//        MultipartFile file = new MockMultipartFile("file", "hello.csv", "text/csv", "Hello, World!".getBytes());
//
//        when(trainingProgramRepository.saveAll(any())).thenReturn(Collections.emptyList());
//
//        int result = trainingProgramService.importFile(file);
//
//        assertEquals(1, result);
//    }
//
//    @Test
//    public void testImportFileWithException() {
//        MultipartFile file = new MockMultipartFile("file", "hello.csv", "text/csv", "Hello, World!".getBytes());
//
//        when(trainingProgramRepository.saveAll(any())).thenThrow(new RuntimeException("fail to store csv data"));
//
//        assertThrows(RuntimeException.class, () -> trainingProgramService.importFile(file));
//    }
//
//    @Test
//    public void importFileWithInvalidFileType() throws IOException {
//        // Given
//        MockMultipartFile file = new MockMultipartFile("training_programs.pdf", "application/pdf", "application/pdf",
//                new ByteArrayInputStream("".getBytes()));
//
//        // When
//        int result = trainingProgramService.importFile(file);
//
//        // Then
//        assertEquals(0, result);
//    }
//
////    @Test
////    public void importTrainingProgramFromFileWithDuplicateRecord() throws IOException {
////        // Given
////        String csvData = "id,trainingProgramCode,name,createdDate,modifiedDate,createdBy,modifiedBy,startDate,topicCode,endDate,status\n" +
////                "1,TP-001,Training Program 1,2023-10-23,2023-10-24,admin,admin,2023-10-23,TC-001,2023-10-24,Active";
////        ByteArrayInputStream is = new ByteArrayInputStream(csvData.getBytes());
////
////        TrainingProgram trainingProgram = new TrainingProgram();
////        trainingProgram.setTrainingProgramId(1);
////        trainingProgram.setTrainingProgramCode("TP-001");
////        trainingProgram.setName("Training Program 1");
////        trainingProgram.setCreatedDate(new java.sql.Date(2023, 10, 23));
////        trainingProgram.setModifiedDate(new java.sql.Date(2023, 10, 24));
////        trainingProgram.setCreatedBy("admin");
////        trainingProgram.setModifiedBy("admin");
////        trainingProgram.setTopicCode("TC-001");
////        trainingProgram.setStatus("Active");
////
////        when(trainingProgramRepository.findById(1)).thenReturn(Optional.of(trainingProgram));
////
////        // When
////        assertThrows(DuplicateRecordException.class, () -> trainingProgramService.importTrainingProgramFromFile(is));
////    }
//
//
//    @Test
//    public void testCreateTrainingProgram_NullInput() {
//        int result = trainingProgramService.createTrainingProgram(null);
//
//        assertEquals(0, result);
//        verify(trainingProgramRepository, never()).save(any(TrainingProgram.class));
//    }
//
//
////    @Test
////    public void shouldReturnListOfClassesForTrainingProgramId() {
////        // Mock the restTemplate to return a list of classes.
////        ClassDto[] mockClasses = new ClassDto[]{
////                ClassDto.builder().trainingProgramId(1).build(),
////                ClassDto.builder().trainingProgramId(2).build()
////        };
////        when(restTemplate.getForObject(properties.getProperty("class-service-url"), ClassDto[].class))
////                .thenReturn(mockClasses);
////
////        // Set the trainingProgramId.
////        Integer trainingProgramId = 1;
////
////        // Call the getClassesByTrainingProgramId() method.
////        List<ClassDto> resultClasses = trainingProgramService.getClassesByTrainingProgramId(trainingProgramId);
////
////        // Assert that the resultClasses contains a single class with the expected trainingProgramId.
////        assertThat(resultClasses).hasSize(1);
////        assertThat(resultClasses.get(0).getTrainingProgramId()).isEqualTo(trainingProgramId);
////    }
////
////    @Test
////    public void testGetClassesByTrainingProgramId_WithNullId_ReturnsNull() {
////        // Arrange
////        Integer trainingProgramId = null;
////
////        // Act
////        List<ClassDto> result = trainingProgramService.getClassesByTrainingProgramId(trainingProgramId);
////
////        // Assert
////        Assertions.assertNull(result);
////    }
//
//
//    @Test
//    public void testCreateTrainingProgram_WithNullTrainingProgram_ReturnsFailure() {
//        // Arrange
//        TrainingProgramCreateRequest trainingProgram = null;
//
//        // Act
//        int result = trainingProgramService.createTrainingProgram(trainingProgram);
//
//        // Assert
//        Assertions.assertEquals(0, result);
//        verify(trainingProgramRepository, never()).save(any());
//    }
//
//
//
//
//    @Test
//    public void testDuplicateTrainingProgram() {
//        // Arrange
//        Integer trainingProgramId = 123;
//        TrainingProgram existingProgram = new TrainingProgram();
//        existingProgram.setTrainingProgramId(trainingProgramId);
//
//        when(trainingProgramRepository.findById(trainingProgramId)).thenReturn(Optional.of(existingProgram));
//
//        // Act
//        TrainingProgram result = trainingProgramService.duplicateTrainingProgram(trainingProgramId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(existingProgram.getTrainingProgramCode(), result.getTrainingProgramCode());
//        assertEquals(existingProgram.getName() + " (duplicate)", result.getName());
//        verify(trainingProgramRepository, times(1)).save(any(TrainingProgram.class));
//    }
//
//    @Test
//    public void testSearchTrainingProgram() throws ParseException {
//        // Create assumptions
//        PageRequestDto dto = new PageRequestDto();
//        dto.setPageNo(1);
//        dto.setPageSize(10);
//        dto.setSortByColumn("createdDate");
//        dto.setSort(Sort.Direction.ASC);
//
//        String keyword = "Program";
//        String searchType = "name";
//        String status = "Active";
//        String startDate = "2023-01-01";
//        String endDate = "2023-01-31";
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.ASC, "createdDate"));
//        List<TrainingProgram> trainingProgramList = new ArrayList<>();
//        // Add training programs to the trainingProgramList
//
//        when(trainingProgramRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(trainingProgramList));
//
//        // Call the method
//        Page<TrainingProgramRequest> result = trainingProgramService.searchTrainingProgram(keyword, searchType, dto);
//
//        // Verify the result
//        verify(trainingProgramRepository, times(1)).findAll(any(Specification.class), eq(pageable));
//        // Verify that the values in the result are appropriate according to the simulated data
//
//        // Assert keyword search functionality
//        ArgumentCaptor<Specification> specCaptor = ArgumentCaptor.forClass(Specification.class);
//        verify(trainingProgramRepository).findAll(specCaptor.capture(), eq(pageable));
//        Specification<TrainingProgram> capturedSpec = specCaptor.getValue();
//        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
//        CriteriaQuery criteriaQuery = Mockito.mock(CriteriaQuery.class);
//        Root<TrainingProgram> root = Mockito.mock(Root.class);
//        Predicate predicate = capturedSpec.toPredicate(root, criteriaQuery, criteriaBuilder);
//
//        // Assert the generated predicate based on the keyword and searchType
//        Predicate expectedPredicate;
//        if ("name".equalsIgnoreCase(searchType)) {
//            expectedPredicate = criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
//        } else if ("topicCode".equalsIgnoreCase(searchType)) {
//            expectedPredicate = criteriaBuilder.like(root.get("topicCode"), "%" + keyword + "%");
//        } else if ("trainingProgramCode".equalsIgnoreCase(searchType)) {
//            expectedPredicate = criteriaBuilder.like(root.get("trainingProgramCode"), "%" + keyword + "%");
//        } else {
//            expectedPredicate = criteriaBuilder.conjunction();
//        }
//        assertEquals(expectedPredicate, predicate);
//    }
//
//    @Test
//    public void testGetAllTrainingPrograms() {
//        PageRequestDto dto = new PageRequestDto();
//        dto.setPageNo(1);
//        dto.setPageSize(10);
//        dto.setSortByColumn("createdDate");
//        dto.setSort(Sort.Direction.ASC);
//
//        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.ASC, "createdDate"));
//
//        List<TrainingProgram> trainingProgramList = new ArrayList<>();
//        // Thêm training programs vào danh sách trainingProgramList
//
//        when(trainingProgramRepository.findAll(eq(pageable))).thenReturn(new PageImpl<>(trainingProgramList));
//
//        // Gọi phương thức
//        Page<TrainingProgramRequest> result = trainingProgramService.getAllTrainingPrograms(dto);
//
//        // Kiểm tra kết quả
//        verify(trainingProgramRepository, times(1)).findAll(eq(pageable));
//        // Kiểm tra các giá trị trong result có phù hợp với dữ liệu đã mô phỏng
//    }
//
//
//    @Test
//    public void testImportFileWithNonCsv() {
//
//        MockMultipartFile nonCsvFile = new MockMultipartFile("file", "data.txt", "text/plain", "Some text content".getBytes());
//
//
//        int result = trainingProgramService.importFile(nonCsvFile);
//
//        assertEquals(0, result);
//        verify(trainingProgramRepository, never()).save(any(TrainingProgram.class));
//    }
//
//    @Test
//    public void testSearchTrainingProgramByTopicCode() throws ParseException {
//        // Create assumptions
//        PageRequestDto dto = new PageRequestDto();
//        dto.setPageNo(1);
//        dto.setPageSize(10);
//        dto.setSortByColumn("createdDate");
//        dto.setSort(Sort.Direction.ASC);
//
//        String keyword = "TOPIC123";
//        String searchType = "topicCode";
//        String status = "Active";
//        String startDate = "2023-01-01";
//        String endDate = "2023-01-31";
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.ASC, "createdDate"));
//        List<TrainingProgram> trainingProgramList = new ArrayList<>();
//        // Add training programs to the trainingProgramList
//
//        when(trainingProgramRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(trainingProgramList));
//
//        // Call the method
//        Page<TrainingProgramRequest> result = trainingProgramService.searchTrainingProgram(keyword, searchType, dto);
//
//        // Verify the result
//        verify(trainingProgramRepository, times(1)).findAll(any(Specification.class), eq(pageable));
//        // Verify that the values in the result are appropriate according to the simulated data
//
//        // Assert the keyword search functionality for topicCode
//        ArgumentCaptor<Specification> specCaptor = ArgumentCaptor.forClass(Specification.class);
//        verify(trainingProgramRepository).findAll(specCaptor.capture(), eq(pageable));
//        Specification<TrainingProgram> capturedSpec = specCaptor.getValue();
//        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
//        CriteriaQuery criteriaQuery = Mockito.mock(CriteriaQuery.class);
//        Root<TrainingProgram> root = Mockito.mock(Root.class);
//        Predicate predicate = capturedSpec.toPredicate(root, criteriaQuery, criteriaBuilder);
//
//        // Assert the generated predicate based on the keyword and searchType
//        Predicate expectedPredicate = criteriaBuilder.like(root.get("topicCode"), "%TOPIC123%");
//        assertEquals(expectedPredicate, predicate);
//    }
//
//    @Test
//    public void testSearchTrainingProgramByTrainingProgramCode() throws ParseException {
//        // Create assumptions
//        PageRequestDto dto = new PageRequestDto();
//        dto.setPageNo(1);
//        dto.setPageSize(10);
//        dto.setSortByColumn("createdDate");
//        dto.setSort(Sort.Direction.ASC);
//
//        String keyword = "TP123";
//        String searchType = "trainingProgramCode";
//        String status = "Active";
//        String startDate = "2023-01-01";
//        String endDate = "2023-01-31";
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.ASC, "createdDate"));
//        List<TrainingProgram> trainingProgramList = new ArrayList<>();
//        // Add training programs to the trainingProgramList
//
//        when(trainingProgramRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(trainingProgramList));
//
//        // Call the method
//        Page<TrainingProgramRequest> result = trainingProgramService.searchTrainingProgram(keyword, searchType, dto);
//
//        // Verify the result
//        verify(trainingProgramRepository, times(1)).findAll(any(Specification.class), eq(pageable));
//        // Verify that the values in the result are appropriate according to the simulated data
//
//        // Assert the keyword search functionality for trainingProgramCode
//        ArgumentCaptor<Specification> specCaptor = ArgumentCaptor.forClass(Specification.class);
//        verify(trainingProgramRepository).findAll(specCaptor.capture(), eq(pageable));
//        Specification<TrainingProgram> capturedSpec = specCaptor.getValue();
//        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
//        CriteriaQuery criteriaQuery = Mockito.mock(CriteriaQuery.class);
//        Root<TrainingProgram> root = Mockito.mock(Root.class);
//        Predicate predicate = capturedSpec.toPredicate(root, criteriaQuery, criteriaBuilder);
//
//        // Assert the generated predicate based on the keyword and searchType
//        Predicate expectedPredicate = criteriaBuilder.like(root.get("trainingProgramCode"), "%TP123%");
//        assertEquals(expectedPredicate, predicate);
//    }
//
//    @Test
//    public void testMapToTrainingProgramDtoList() {
//        // Tạo danh sách chương trình đào tạo
//        List<TrainingProgram> trainingPrograms = new ArrayList<>();
//
//        // Tạo đối tượng SimpleDateFormat
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        // Tạo đối tượng TrainingProgram và thiết lập các thuộc tính
//        TrainingProgram program1 = new TrainingProgram();
//        program1.setTrainingProgramId(1);
//        program1.setTrainingProgramCode("TP001");
//        program1.setName("Training Program 1");
//        program1.setStatus("Active");
//        program1.setCreatedDate(new java.sql.Date(new java.util.Date().getTime()));
//        program1.setCreatedBy("John");
//        program1.setModifiedBy("Mary");
//        program1.setModifiedDate(new java.sql.Date(new java.util.Date().getTime()));
//        program1.setTopicCode("TC001");
//        // Thêm vào danh sách chương trình đào tạo
//        trainingPrograms.add(program1);
//
//        // Ánh xạ danh sách chương trình đào tạo
//        List<TrainingProgramRequest> result = mapToTrainingProgramDtoList(trainingPrograms);
//
//        // Kiểm tra kích thước của danh sách kết quả
//        Assertions.assertEquals(1, result.size());
//
//        // Kiểm tra giá trị của các thuộc tính trong đối tượng TrainingProgramRequest
//        TrainingProgramRequest result1 = result.get(0);
//        Assertions.assertEquals(1, result1.getTrainingProgramId());
//        Assertions.assertEquals("TP001", result1.getTrainingProgramCode());
//        Assertions.assertEquals("Training Program 1", result1.getName());
//        Assertions.assertEquals("Active", result1.getStatus());
//        Assertions.assertEquals(dateFormat.format(new Date()), result1.getCreatedDate());
//        Assertions.assertEquals("John", result1.getCreatedBy());
//        Assertions.assertEquals("Mary", result1.getModifiedBy());
//        Assertions.assertEquals(dateFormat.format(new Date()), result1.getModifiedDate());
//        Assertions.assertEquals("TC001", result1.getTopicCode());
//        // Kiểm tra giá trị của thuộc tính duration
//        // ...
//    }
//
//    private List<TrainingProgramRequest> mapToTrainingProgramDtoList(List<TrainingProgram> trainingPrograms) {
//        if (trainingPrograms == null)
//            throw new IllegalArgumentException("Argument cannot be null");
//
//        if (trainingPrograms.isEmpty()) return List.of();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        return trainingPrograms.stream().map(trainingProgram -> {
//            TrainingProgramRequest trainingProgramRequest = new TrainingProgramRequest();
//            trainingProgramRequest.setTrainingProgramId(trainingProgram.getTrainingProgramId());
//            trainingProgramRequest.setTrainingProgramCode(trainingProgram.getTrainingProgramCode());
//            trainingProgramRequest.setName(trainingProgram.getName());
//            trainingProgramRequest.setStatus(trainingProgram.getStatus());
//            trainingProgramRequest.setCreatedDate(dateFormat.format(trainingProgram.getCreatedDate()));
//            trainingProgramRequest.setCreatedBy(trainingProgram.getCreatedBy());
//            trainingProgramRequest.setModifiedBy(trainingProgram.getModifiedBy());
//            trainingProgramRequest.setModifiedDate(dateFormat.format(trainingProgram.getModifiedDate()));
//            trainingProgramRequest.setTopicCode(trainingProgram.getTopicCode());
//            // Ánh xạ giá trị cho thuộc tính duration
//            // ...
//
//            return trainingProgramRequest;
//        }).collect(Collectors.toList());
//    }
//
//    @Test
//    public void testDuplicateTrainingProgramWithNullId() {
//        // Test case: When trainingProgramId is null
//        Integer trainingProgramId = null;
//
//        TrainingProgram duplicatedProgram = trainingProgramService.duplicateTrainingProgram(trainingProgramId);
//
//        Assertions.assertNull(duplicatedProgram);
//    }
//
////    @Test
////    public void testDeleteTrainingProgramWithNullId() {
////        // Test case: When trainingProgramId is null
////        Integer trainingProgramId = null;
////
////        int result = trainingProgramService.deleteTrainingProgram(trainingProgramId);
////
////        Assertions.assertEquals(0, result);
////    }
//
//    @Test
//    public void testDeactivateTrainingProgramWithNullId() {
//        // Test case: When trainingProgramId is null
//        Integer trainingProgramId = null;
//
//        int result = trainingProgramService.deactivateTrainingProgram(trainingProgramId);
//
//        Assertions.assertEquals(0, result);
//    }
//
//    @Test
//    public void testActivateTrainingProgramWithNullId() {
//        // Test case: When trainingProgramId is null
//        Integer trainingProgramId = null;
//
//        int result = trainingProgramService.activateTrainingProgram(trainingProgramId);
//
//        Assertions.assertEquals(0, result);
//    }
//
//    @Test
//    public void testCreateTrainingProgram() {
//        // Arrange
//        TrainingProgramCreateRequest request = new TrainingProgramCreateRequest();
//        request.setTrainingProgramCode("TP001");
//        request.setName("Training Program 1");
//        request.setCreatedBy("John");
//        request.setTopicCode("TOP001");
//        request.setStatus("PLANNING");
//        request.setSyllabusList(Collections.singletonList(new SyllabusRequest(1, 1001L)));
//
//        Date currentDate = new Date(System.currentTimeMillis());
//        when(trainingProgramRepository.save(any(TrainingProgram.class))).thenReturn(new TrainingProgram());
//        when(trainingSyllabusRepository.save(any(TrainingSyllabus.class))).thenReturn(new TrainingSyllabus());
//
//        // Act
//        int result = trainingProgramService.createTrainingProgram(request);
//
//        // Assert
//        assertEquals(1, result);
//        verify(trainingProgramRepository, times(1)).save(any(TrainingProgram.class));
//        verify(trainingSyllabusRepository, times(1)).save(any(TrainingSyllabus.class));
//    }
//
//    //    @Test
////    public void testFindTrainingSyllabusByProgramId() {
////        // Create a mock of the trainingProgramRepository
////        TrainingProgramRepository trainingProgramRepository = mock(TrainingProgramRepository.class);
////
////        // Create a sample programId
////        Integer programId = 123;
////
////        // Create a sample list of TrainingSyllabus
////        List<TrainingSyllabus> syllabusList = new ArrayList<>();
////
////        // Create an instance of the TrainingProgramService and pass the mock repository
////
////
////        // Set up the mock behavior of the trainingProgramRepository
////        when(trainingProgramRepository.findTrainingSyllabusByProgramId(programId)).thenReturn(syllabusList);
////
////        // Call the method being tested
////        Training_SyllabusResponse response = trainingProgramService.findTrainingSyllabusByProgramId(programId);
////
////        // Verify the expected behavior
////
////        // Verify that the trainingProgramRepository's method was called with the correct programId
////        verify(trainingProgramRepository).findTrainingSyllabusByProgramId(programId);
////
////        // Verify that the returned response has an empty syllabusList
////        assertEquals(0, response.getSyllabusList().size());
////    }
//
//    @Test
//    public void testDeactivateTrainingProgram_Success() {
//        // Create a training program ID for testing
//        Integer trainingProgramId = 123;
//
//        // Create a mock TrainingProgram object
//        TrainingProgram trainingProgram = new TrainingProgram();
//        trainingProgram.setTrainingProgramId(trainingProgramId);
//        trainingProgram.setStatus("INACTIVE");
//
//        // Mock the behavior of the trainingProgramRepository
//        when(trainingProgramRepository.findById(trainingProgramId)).thenReturn(Optional.of(trainingProgram));
//
//        // Call the method being tested
//        int result = trainingProgramService.deactivateTrainingProgram(trainingProgramId);
//
//        // Verify the expected behavior
//
//        // Verify that the trainingProgramRepository's findById method was called with the correct trainingProgramId
//        verify(trainingProgramRepository).findById(trainingProgramId);
//
//        // Verify that the trainingProgramRepository's save method was called with the updated trainingProgram
//        verify(trainingProgramRepository).save(trainingProgram);
//
//        // Verify that the method returned 1
//        assertEquals(1, result);
//
//        // Verify that the trainingProgram's status is now "ACTIVE"
//        assertEquals("INACTIVE", trainingProgram.getStatus());
//    }
//
//    @Test
//    public void testDeactivateTrainingProgram_NullId() {
//        // Call the method being tested with a null trainingProgramId
//        int result = trainingProgramService.deactivateTrainingProgram(null);
//
//        // Verify that the method returned 0
//        assertEquals(0, result);
//
//        // Verify that the trainingProgramRepository's findById method was not called
//        verify(trainingProgramRepository, never()).findById(any());
//    }
//
//    @Test
//    public void testDeactivateTrainingProgram_Exception() {
//        // Create a training program ID for testing
//        Integer trainingProgramId = 123;
//
//        // Mock the behavior of the trainingProgramRepository to throw an exception
//        when(trainingProgramRepository.findById(trainingProgramId)).thenThrow(new RuntimeException("DB connection error"));
//
//        // Call the method being tested and verify that it throws an IllegalStateException
//        assertThrows(IllegalStateException.class, () -> trainingProgramService.deactivateTrainingProgram(trainingProgramId));
//
//        // Verify that the trainingProgramRepository's findById method was called with the correct trainingProgramId
//        verify(trainingProgramRepository).findById(trainingProgramId);
//
//        // Verify that the trainingProgramRepository's save method was not called
//        verify(trainingProgramRepository, never()).save(any());
//    }
//
//    @Test
//    public void testActivateTrainingProgram_Success() {
//        // Create a training program ID for testing
//        Integer trainingProgramId = 123;
//
//        // Create a mock TrainingProgram object
//        TrainingProgram trainingProgram = new TrainingProgram();
//        trainingProgram.setTrainingProgramId(trainingProgramId);
//        trainingProgram.setStatus("INACTIVE");
//
//        // Mock the behavior of the trainingProgramRepository
//        when(trainingProgramRepository.findById(trainingProgramId)).thenReturn(Optional.of(trainingProgram));
//
//        // Call the method being tested
//        int result = trainingProgramService.activateTrainingProgram(trainingProgramId);
//
//        // Verify the expected behavior
//
//        // Verify that the trainingProgramRepository's findById method was called with the correct trainingProgramId
//        verify(trainingProgramRepository).findById(trainingProgramId);
//
//        // Verify that the trainingProgramRepository's save method was called with the updated trainingProgram
//        verify(trainingProgramRepository).save(trainingProgram);
//
//        // Verify that the method returned 1
//        assertEquals(1, result);
//
//        // Verify that the trainingProgram's status is now "ACTIVE"
//        assertEquals("ACTIVE", trainingProgram.getStatus());
//    }
//
//    @Test
//    public void testActivateTrainingProgram_NullId() {
//        // Call the method being tested with a null trainingProgramId
//        int result = trainingProgramService.activateTrainingProgram(null);
//
//        // Verify that the method returned 0
//        assertEquals(0, result);
//
//        // Verify that the trainingProgramRepository's findById method was not called
//        verify(trainingProgramRepository, never()).findById(any());
//    }
//
//    @Test
//    public void testActivateTrainingProgram_Exception() {
//        // Create a training program ID for testing
//        Integer trainingProgramId = 123;
//
//        // Mock the behavior of the trainingProgramRepository to throw an exception
//        when(trainingProgramRepository.findById(trainingProgramId)).thenThrow(new RuntimeException("DB connection error"));
//
//        // Call the method being tested and verify that it throws an IllegalStateException
//        assertThrows(IllegalStateException.class, () -> trainingProgramService.activateTrainingProgram(trainingProgramId));
//
//        // Verify that the trainingProgramRepository's findById method was called with the correct trainingProgramId
//        verify(trainingProgramRepository).findById(trainingProgramId);
//
//        // Verify that the trainingProgramRepository's save method was not called
//        verify(trainingProgramRepository, never()).save(any());
//    }
//
//    @Test
//    public void testUpdateTrainingProgram_Success() {
//        // Create a sample TrainingProgramCreateRequest
//        TrainingProgramUpdateRequest request = new TrainingProgramUpdateRequest();
//        request.setId(123);
//        request.setName("Sample Training Program");
//        request.setModifiedBy("John Doe");
//        request.setTopicCode("TC001");
//        request.setStatus("ACTIVE");
//
//        // Create a list of sample syllabus requests
//        List<SyllabusRequest> syllabusList = Arrays.asList(
//                new SyllabusRequest(1, 1L),
//                new SyllabusRequest(2, 2L)
//        );
//        request.setSyllabusList(syllabusList);
//
//        // Create a mock TrainingProgram object
//        TrainingProgram trainingProgram = new TrainingProgram();
//        trainingProgram.setTrainingProgramId(123);
//        trainingProgram.setTrainingProgramCode("TP001");
//        trainingProgram.setName("Sample Training Program");
//        trainingProgram.setModifiedBy("John Doe");
//        trainingProgram.setTopicCode("TC001");
//        trainingProgram.setStatus("INACTIVE");
//
//        // Mock the behavior of the trainingProgramRepository
//        when(trainingProgramRepository.findById(123)).thenReturn(Optional.of(trainingProgram));
//
//        // Call the method being tested
//        TrainingProgram result = trainingProgramService.updateTrainingProgram(request);
//
//        // Verify the expected behavior
//
//        // Verify that the trainingProgramRepository's findById method was called with the correct trainingProgramId
//        verify(trainingProgramRepository).findById(123);
//
//        // Verify that the trainingProgramRepository's save method was called with the updated trainingProgram
//        verify(trainingProgramRepository).save(trainingProgram);
//
//        // Verify that the trainingSyllabusRepository's deleteByTrainingProgram method was called
//        verify(trainingSyllabusRepository).deleteByTrainingProgram(trainingProgram);
//
//        // Verify that the trainingSyllabusRepository's save method was called twice for each syllabus in the request
//        verify(trainingSyllabusRepository, times(2)).save(any(TrainingSyllabus.class));
//
//        // Verify that the returned result is the updated trainingProgram
//        assertEquals(trainingProgram, result);
//    }
//
//    @Test
//    public void testUpdateTrainingProgram_NotFound() {
//        // Create a sample TrainingProgramCreateRequest
//        TrainingProgramUpdateRequest request = new TrainingProgramUpdateRequest();
//        request.setId(123);
//        // ...
//
//        // Mock the behavior of the trainingProgramRepository to return empty optional
//        when(trainingProgramRepository.findById(123)).thenReturn(Optional.empty());
//
//        // Call the method being tested and verify that it throws an IllegalStateException
//        assertThrows(IllegalStateException.class, () -> trainingProgramService.updateTrainingProgram(request));
//
//        // Verify that the trainingProgramRepository's findById method was called with the correct trainingProgramId
//        verify(trainingProgramRepository).findById(123);
//
//        // Verify that the trainingProgramRepository's save method was not called
//        verify(trainingProgramRepository, never()).save(any());
//    }
//
//    @Test
//    public void testUpdateTrainingProgram_InvalidStatus() {
//        // Create a sample TrainingProgramCreateRequest with an invalid status
//        TrainingProgramUpdateRequest request = new TrainingProgramUpdateRequest();
//        request.setId(123);
//        // ...
//        request.setStatus("INVALID_STATUS");
//
//        // Create a mock TrainingProgram object
//        TrainingProgram trainingProgram = new TrainingProgram();
//        trainingProgram.setTrainingProgramId(123);
//        trainingProgram.setStatus("INACTIVE");
//
//        // Mock the behavior of the trainingProgramRepository
//        when(trainingProgramRepository.findById(123)).thenReturn(Optional.of(trainingProgram));
//
//        // Call the method being tested and verify that it throws an IllegalArgumentException
//        assertThrows(IllegalArgumentException.class, () -> trainingProgramService.updateTrainingProgram(request));
//
//        // Verify that the trainingProgramRepository's findById method was called with the correct trainingProgramId
//        verify(trainingProgramRepository).findById(123);
//
//        // Verify that the trainingProgramRepository's save method was not called
//        verify(trainingProgramRepository, never()).save(any());
//    }
//
//
//    @Test
//    public void testImportFile_InvalidFileType() throws IOException {
//        // Create a sample file with an invalid content type (not text/csv)
//        MockMultipartFile file = new MockMultipartFile("file.txt", "file.txt",
//                "text/plain", "Some text".getBytes(StandardCharsets.UTF_8));
//
//        // Call the method being tested
//        int result = trainingProgramService.importFile(file);
//
//        // Verify that the trainingProgramRepository's saveAll method was not called
//        verify(trainingProgramRepository, never()).saveAll(any());
//
//        // Verify that the returned result is 0
//        assertEquals(0, result);
//    }
//
//    //    @Test
////    public void testImportFile_IOError() throws IOException {
////        // Create a mock MultipartFile
////        MockMultipartFile file = mock(MockMultipartFile.class);
////
////        // Mock the behavior of file.getInputStream to throw an IOException
////        when(file.getInputStream()).thenThrow(new IOException("IO Error"));
////
////        // Call the method being tested and verify that it throws a RuntimeException
////        assertThrows(RuntimeException.class, () -> trainingProgramService.importFile(file));
////
////        // Verify that the trainingProgramRepository's saveAll method was not called
////        verify(trainingProgramRepository, never()).saveAll(any());
////    }
////    @Test
////    public void testGetTrainingProgramsWithStatusActive() {
////        // Arrange
////        String keyword = "o";
////        Specification<TrainingProgram> spec = Specification.where((root, query, builder) -> builder.equal(root.get("status"), "ACTIVE"));
////
////        List<TrainingProgram> sampleTrainingPrograms = createSampleTrainingPrograms();
////        when(trainingProgramRepository.findAll(eq(spec))).thenReturn(sampleTrainingPrograms);
////
////        // Act
////        List<TrainingProgramRequest> result = trainingProgramService.getTrainingProgramsWithStatusActive(keyword);
////
////        // Assert
////        assertEquals(2, result.size()); // Replace 2 with the expected size of the result
////        // You can add more assertions to check the content of the result if needed
////    }
////
////    // Define a method to create sample TrainingProgram objects
////    private List<TrainingProgram> createSampleTrainingPrograms() {
////        List<TrainingProgram> trainingPrograms = new ArrayList<>();
////
////        TrainingProgram program1 = new TrainingProgram();
////        program1.setTrainingProgramId(1);
////        program1.setTrainingProgramCode("TP001");
////        program1.setName("Training Program 1");
////        program1.setStatus("ACTIVE");
////        program1.setCreatedBy("da");
////        program1.setTopicCode("sad");
////        program1.setModifiedBy("aa");
////        // Set other properties for program1
////
////        TrainingProgram program2 = new TrainingProgram();
////        program2.setTrainingProgramId(2);
////        program2.setTrainingProgramCode("TP002");
////        program2.setName("Training Program 2");
////        program2.setStatus("ACTIVE");
////        program2.setCreatedBy("da");
////        program2.setTopicCode("sad");
////        program2.setModifiedBy("aa");
////        trainingPrograms.add(program1);
////        trainingPrograms.add(program2);
////
////        return trainingPrograms;
////    }
//    @Test
//    public void testFindTrainingSyllabusByProgramId() {
//        // Mock the repository and define the data to be returned
//        when(trainingProgramRepository.findTrainingSyllabusByProgramId(any(Integer.class)))
//                .thenReturn(Arrays.asList(new TrainingSyllabus(/* Initialize with valid data */)));
//
//        // Create the service instance
//
//        // Perform the method call
//        Training_SyllabusResponse response = trainingProgramService.findTrainingSyllabusByProgramId(1); // Provide a valid programId
//
//        // Assert the results
//        assertNotNull(response);
//        assertEquals(1, response.getSyllabusList().size());
//        // Add more specific assertions for the response data
//    }
//}
//
//
//
//
//
//
//
//
