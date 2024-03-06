//package com.fams.syllabus_repository.Service.Impl;
//
//import com.fams.syllabus_repository.dto.PagingSyllabusDto;
//import com.fams.syllabus_repository.dto.SyllabusDto;
//import com.fams.syllabus_repository.entity.*;
//import com.fams.syllabus_repository.exceptions.SyllabusNotFoundException;
//import com.fams.syllabus_repository.repository.*;
//import com.fams.syllabus_repository.service.impl.SyllabusServiceImpl;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.data.domain.*;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.persistence.criteria.*;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.time.LocalDate;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//
//@RequiredArgsConstructor
//public class SyllabusServiceImplTest {
//
//
//    @InjectMocks
//    private SyllabusServiceImpl syllabusService;
//
//    @Mock
//    private SyllabusRepository repository;
//
//    @Mock
//    private DaySyllabusRepository daySyllabusRepository;
//
//    @Mock
//    private TrainingUnitRepository trainingUnitRepository;
//
//    @Mock
//    private TrainingContentRepository trainingContentRepository;
//
//    @Mock
//    private OtherSyllabusRepository otherSyllabusRepository;
//    @Mock
//    private CriteriaBuilder criteriaBuilder;
//
//    @Mock
//    private CriteriaQuery<Syllabus> criteriaQuery;
//
//    @Mock
//    private Root<Syllabus> root;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
////    @Test
////    void testGetSyllabusById() {
////        // Tạo một đối tượng Syllabus giả lập
////        Syllabus mockSyllabus = new Syllabus();
////        mockSyllabus.setId(1L);
////        mockSyllabus.setTopicName("Sample Syllabus");
////
////        // Thiết lập hành vi của repository khi gọi phương thức findById
////        when(repository.findById(1L)).thenReturn(Optional.of(mockSyllabus));
////
////        // Gọi phương thức getSyllabusById của service
////        SyllabusDto syllabusDto = syllabusService.getSyllabusById(1L);
////
////        // Kiểm tra kết quả
////        assertEquals(1L, syllabusDto.getId().longValue());
////        assertEquals("Sample Syllabus", syllabusDto.getTopicName());
////        // Kiểm tra các giá trị khác tùy theo cần thiết
////    }
//
////    @Test
////    void testGetAllSyllabusAndSearch() {
////        // Tạo dữ liệu giả lập
////        List<Syllabus> syllabusList = new ArrayList<>();
////        Syllabus syllabus1 = new Syllabus();
////        syllabus1.setId(1L);
////        syllabus1.setTopicName("Syllabus 1");
////        //Thêm các thông tin khác
////        syllabusList.add(syllabus1);
////        //Thêm thêm dữ liệu khác nếu cần
////
////        // Thiết lập hành vi của repository khi gọi phương thức findAll với Specification và Pageable
////        when(repository.findAll(any(Specification.class), any(Pageable.class)))
////                .thenReturn(new PageImpl<>(syllabusList));
////
////        // Gọi phương thức getAllSyllabusAndSearch của service
////        PagingSyllabusDto result = syllabusService.getAllSyllabusAndSearch(0, 10, "keyword", Sort.by(Sort.Order.asc("topicName")), LocalDate.now(), null);
////
////        // Kiểm tra kết quả
////        assertEquals(1, result.getContent().size());
////        // Kiểm tra các giá trị khác tùy theo cần thiết
////    }
//
////    @Test
////    void testSearchSyllabus() {
////        // Tạo một đối tượng Syllabus giả lập
////        Syllabus mockSyllabus = new Syllabus();
////        mockSyllabus.setId(1L);
////        mockSyllabus.setTopicName("Sample Syllabus");
////
////        // Thiết lập hành vi của repository khi gọi phương thức findAll với Specification và Pageable
////        when(repository.findAll(ArgumentMatchers.<Specification<Syllabus>>any(), ArgumentMatchers.<Pageable>any()))
////                .thenReturn(new PageImpl<>(Collections.singletonList(mockSyllabus)));
////
////        // Gọi phương thức searchSyllabus của service
////        Specification<Syllabus> specification = syllabusService.searchSyllabus("keyword", Sort.by(Sort.Order.asc("topicName")), LocalDate.now(), null);
////        Page<Syllabus> result = repository.findAll(specification, PageRequest.of(0, 10));
////
////        // Kiểm tra kết quả
////        assertEquals(1, result.getContent().size());
////        // Kiểm tra các giá trị khác tùy theo cần thiết
////    }
//
////    @Test
////    void testCreateAndUpdate_NewSyllabus() {
////        // Create a SyllabusDto for a new syllabus (ID is null)
////        SyllabusDto newSyllabusDto = new SyllabusDto();
////        newSyllabusDto.setTopicName("New Syllabus");
////
////        // Mock the behavior of the repository for findById
////        when(repository.findById(newSyllabusDto.getId()))
////                .thenReturn(Optional.empty());
////
////        // Mock the behavior of the repository for save
////        Syllabus savedSyllabus = new Syllabus();
////        savedSyllabus.setId(1L); // Assign an ID to the saved syllabus
////        when(repository.save(Mockito.any(Syllabus.class)))
////                .thenReturn(savedSyllabus);
////
////        // Call the method under test
////        SyllabusDto result = syllabusService.createAndUpdate(newSyllabusDto);
////
////        // Assertions
////        assertEquals(savedSyllabus.getId(), result.getId()); // Verify that the ID is assigned
////
////        // You can add more specific assertions for other fields in SyllabusDto if needed
////    }
//
////    @Test
////    void testCreateAndUpdate_UpdateSyllabus() {
////        // Create a SyllabusDto for an existing syllabus (ID is not null)
////        SyllabusDto existingSyllabusDto = new SyllabusDto();
////        existingSyllabusDto.setId(1L);
////        existingSyllabusDto.setTopicName("Updated Syllabus");
////
////        // Mock the behavior of the repository for findById
////        Syllabus existingSyllabus = new Syllabus();
////        existingSyllabus.setId(existingSyllabusDto.getId());
////        when(repository.findById(existingSyllabusDto.getId()))
////                .thenReturn(Optional.of(existingSyllabus));
////
////        // Mock the behavior of the repository for save
////        when(repository.save(Mockito.any(Syllabus.class)))
////                .thenReturn(existingSyllabus);
////
////        // Call the method under test
////        SyllabusDto result = syllabusService.createAndUpdate(existingSyllabusDto);
////
////        // Assertions
////        assertEquals(existingSyllabus.getId(), result.getId()); // Verify that the ID is retained
////
////        // You can add more specific assertions for other fields in SyllabusDto if needed
////    }
//
////    @Test
////    void testGetAllSyllabusSearchSort() {
////        // Define the test parameters
////        int pageNo = 0;
////        int pageSize = 10;
////        String keyword = "example";
////        Sort sort = Sort.by(Sort.Order.asc("name"));
////        LocalDate selectedDate = LocalDate.of(2023, 1, 1);
////        LocalDate selectedEndDate = LocalDate.of(2023, 12, 31);
////
////        // Create a mock Page<Syllabus> with some syllabus entities
////        List<Syllabus> syllabusEntities = new ArrayList<>();
////        syllabusEntities.add(new Syllabus());
////        syllabusEntities.add(new Syllabus());
////        // ... add more syllabus entities for testing
////
////        Page<Syllabus> mockSyllabusPage = new PageImpl<>(syllabusEntities);
////
////        // Mock the behavior of the repository for findAll
////        when(repository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
////                .thenReturn(mockSyllabusPage);
////
////        // Call the method under test
////        PagingSyllabusDto result = syllabusService.getAllSyllabusAndSearch(pageNo, pageSize, keyword, sort, selectedDate, selectedEndDate);
////
////        // Assertions
////        assertEquals(syllabusEntities.size(), result.getContent().size());
////        // You can add more specific assertions for other fields in the result object if needed
////    }
//
////    @Test
////    void testDuplicateSyllabus() {
////        // Define the ID of the original syllabus to be duplicated
////        Long originalSyllabusId = 1L;
////
////        // Create a mock Syllabus entity for the original syllabus
////        Syllabus originalSyllabus = new Syllabus();
////        originalSyllabus.setId(originalSyllabusId);
////        originalSyllabus.setTopicName("Original Syllabus");
////        // ... Set other fields for the original syllabus
////
////        // Mock the behavior of the repository to return the original syllabus
////        when(repository.findById(originalSyllabusId)).thenReturn(Optional.of(originalSyllabus));
////
////        // Create a mock DaySyllabus entity for the original syllabus
////        DaySyllabus originalDaySyllabus = new DaySyllabus();
////        originalDaySyllabus.setDay("day 1");
////        // ... Set other fields for the original DaySyllabus
////
////        // Create a mock TrainingUnit entity for the original DaySyllabus
////        TrainingUnit originalTrainingUnit = new TrainingUnit();
////        originalTrainingUnit.setDayNumber("Day 1");
////        originalTrainingUnit.setUnitName("Unit 1");
////        // ... Set other fields for the original TrainingUnit
////
////        // Create a mock TrainingContent entity for the original TrainingUnit
////        TrainingContent originalTrainingContent = new TrainingContent();
////        originalTrainingContent.setContent("Content 1");
////        originalTrainingContent.setDeliveryType("Type 1");
////        // ... Set other fields for the original TrainingContent
////
////        originalTrainingUnit.getTrainingContents().add(originalTrainingContent);
////        originalDaySyllabus.getTrainingUnit().add(originalTrainingUnit);
////        originalSyllabus.getDaSyllabus().add(originalDaySyllabus);
////
////        // Mock the behavior of repositories to save and return the duplicated entities
////        when(repository.save(any(Syllabus.class))).thenReturn(originalSyllabus);
////        when(daySyllabusRepository.save(any(DaySyllabus.class))).thenReturn(originalDaySyllabus);
////        when(trainingUnitRepository.save(any(TrainingUnit.class))).thenReturn(originalTrainingUnit);
////        when(trainingContentRepository.save(any(TrainingContent.class))).thenReturn(originalTrainingContent);
////
////        // Call the method under test
////        SyllabusDto duplicatedSyllabusDto = syllabusService.duplicateSyllabus(originalSyllabusId);
////
////        // Assertions
////        // You can add assertions to verify that the duplicated syllabus DTO matches your expectations
////        // assertEquals("Copy of Original Syllabus", duplicatedSyllabusDto.getTopicName());
////        // ... Add more assertions for other fields in the DTO
////    }
//
//    //    @Test
////    void testImportDataFromCsv() throws IOException {
////        // Sample CSV data as a string
////        String csvData = "header1,header2,header3,header4,header5,header6,header7,header8,header9,header10,header11,header12,header13,header14,header15,header16,header17,header18,header19\n" +
////                "row1data1,row1data2,row1data3,100,row1data5,row1data6,row1data7,row1data8,row1data9,row1data10,row1data11,row1data12,row1data13,row1data14,row1data15,row1data16,row1data17,row1data18,row1data19\n" +
////                "row2data1,row2data2,row2data3,200,row2data5,row2data6,row2data7,row2data8,row2data9,row2data10,row2data11,row2data12,row2data13,row2data14,row2data15,row2data16,row2data17,row2data18,row2data19";
////
////
////        // Create a mock MultipartFile with the CSV data
////        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
////        MockMultipartFile file = new MockMultipartFile("file", "sample.csv", "text/csv", inputStream);
////
////        // Mock repository save methods
////        when(repository.save(Mockito.any(Syllabus.class))).thenAnswer(invocation -> invocation.getArgument(0));
////        when(daySyllabusRepository.save(Mockito.any(DaySyllabus.class))).thenAnswer(invocation -> invocation.getArgument(0));
////        when(trainingUnitRepository.save(Mockito.any(TrainingUnit.class))).thenAnswer(invocation -> invocation.getArgument(0));
////        when(trainingContentRepository.save(Mockito.any(TrainingContent.class))).thenAnswer(invocation -> invocation.getArgument(0));
////        when(otherSyllabusRepository.save(Mockito.any(OtherSyllabus.class))).thenAnswer(invocation -> invocation.getArgument(0));
////
////        // Call the method under test
////        syllabusService.importDataFromCsv(file);
////
////        // Add assertions as needed
////        // For example, you can verify that certain save methods were called with the expected arguments.
////        Mockito.verify(repository, Mockito.times(2)).save(Mockito.any(Syllabus.class));
////        Mockito.verify(daySyllabusRepository, Mockito.times(2)).save(Mockito.any(DaySyllabus.class));
////        Mockito.verify(trainingUnitRepository, Mockito.times(2)).save(Mockito.any(TrainingUnit.class));
////        Mockito.verify(trainingContentRepository, Mockito.times(2)).save(Mockito.any(TrainingContent.class));
////        Mockito.verify(otherSyllabusRepository, Mockito.times(2)).save(Mockito.any(OtherSyllabus.class));
////    }
//    @Test
//    void testGetSyllabusEntityByIdWhenSyllabusExists() {
//        // Arrange
//        Long syllabusId = 1L;
//        Syllabus syllabus = new Syllabus();
//        when(repository.findById(syllabusId)).thenReturn(Optional.of(syllabus));
//
//        // Act
//        Syllabus result = syllabusService.getSyllabusEntityById(syllabusId);
//
//        // Assert
//        assertEquals(syllabus, result);
//    }
//
//    @Test
//    void testGetSyllabusEntityByIdWhenSyllabusNotExists() {
//        // Arrange
//        Long syllabusId = 1L;
//        when(repository.findById(syllabusId)).thenReturn(Optional.empty());
//
//        // Act
//        Syllabus result = syllabusService.getSyllabusEntityById(syllabusId);
//
//        // Assert
//        assertNull(result);
//    }
//
//    @Test
//    void testDeleteSyllabusByIdWhenSyllabusExists() {
//        // Arrange
//        Long syllabusId = 1L;
//        Syllabus syllabusToDelete = new Syllabus();
//        when(repository.findById(syllabusId)).thenReturn(Optional.of(syllabusToDelete));
//
//        // Act
//        syllabusService.deleteSyllabusById(syllabusId);
//
//        // Assert
//        verify(repository).delete(syllabusToDelete);
//    }
//
//    @Test
//    void testDeleteSyllabusByIdWhenSyllabusNotExists() {
//        // Arrange
//        Long syllabusId = 1L;
//        when(repository.findById(syllabusId)).thenReturn(Optional.empty());
//
//        // Act and Assert
//        try {
//            syllabusService.deleteSyllabusById(syllabusId);
//        } catch (SyllabusNotFoundException exception) {
//            assertEquals("Syllabus Not Found", exception.getMessage());
//        }
//    }
//
//    @Test
//    void testGetAllSyllabus() {
//        // Arrange
//        List<Syllabus> syllabusEntities = new ArrayList<>();
//        syllabusEntities.add(new Syllabus()); // You can add more Syllabus entities for testing
//
//        when(repository.findAll()).thenReturn(syllabusEntities);
//
//        // Act
//        List<SyllabusDto> result = syllabusService.getAllSyllabus();
//
//        // Assert
//        assertEquals(syllabusEntities.size(), result.size());
//        // You can add more specific assertions for SyllabusDto objects if needed
//    }
//
////    @Test
////    void testSearchSyllabuss() {
////        String keyword = "exampleKeyword";
////        Sort sort = Sort.by(Sort.Order.asc("topicName"));
////        LocalDate selectedDate = LocalDate.of(2023, 1, 1);
////        LocalDate selectedEndDate = LocalDate.of(2023, 12, 31);
////
////        Specification<Syllabus> specification = syllabusService.searchSyllabus(keyword, sort, selectedDate, selectedEndDate);
////
////        assertNotNull(specification);
////
////        // Mock some of the methods in the Specification
////        when(criteriaBuilder.or(any(Predicate.class), any(Predicate.class), any(Predicate.class), any(Predicate.class))).thenReturn(mock(Predicate.class));
////        when(criteriaBuilder.between(any(Expression.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(mock(Predicate.class));
////        when(criteriaBuilder.like(any(Expression.class), any(String.class))).thenReturn(mock(Predicate.class));
////
////        // Create a Predicate and test its behavior
////        Predicate predicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);
////
////        // You can add more specific assertions here based on your expected behavior.
////        // For example, you can verify that the created Predicate contains the expected conditions.
////    }
//
////    @Test
////    void testSearchSyllabusSingleDate() {
////        String keyword = "exampleKeyword";
////        Sort sort = Sort.by(Sort.Order.asc("topicName"));
////        LocalDate selectedDate = LocalDate.of(2023, 1, 1);
////
////        Specification<Syllabus> specification = syllabusService.searchSyllabus(keyword, sort, selectedDate, null);
////
////        assertNotNull(specification);
////
////        // Mock some of the methods in the Specification
////        Predicate mockOrPredicate = mock(Predicate.class);
////        Predicate mockEqualPredicate = mock(Predicate.class);
////
////        when(criteriaBuilder.or(
////                any(Predicate.class),
////                any(Predicate.class),
////                any(Predicate.class),
////                any(Predicate.class))
////        ).thenReturn(mockOrPredicate);
////
////        when(criteriaBuilder.equal(
////                any(Expression.class),
////                any(LocalDate.class)
////        )).thenReturn(mockEqualPredicate);
////
////        // Create a Predicate and test its behavior
////        Predicate predicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);
////
////        // You can add assertions to ensure that the correct conditions are applied when a single date is selected.
////    }
//
////    @Test
////    public void testImportSyllabusFromCSV() throws IOException {
////        // Tạo dữ liệu CSV giả mạo
////        String csvData = "Topic 1,Group A,Version 1,100,Everyone,Outline 1,Material 1,Principles 1,High,Draft\n" +
////                "Topic 2,Group B,Version 2,50,Managers,Outline 2,Material 2,Principles 2,Low,Review";
////
////        // Chuyển đổi dữ liệu CSV thành InputStream giả mạo
////        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
////
////        // Tạo đối tượng MultipartFile giả mạo
////        MultipartFile multipartFile = new MockMultipartFile("data.csv", "data.csv", "text/csv", inputStream);
////
////        // Tạo đối tượng Syllabus giả mạo để kiểm tra
////        Syllabus expectedSyllabus1 = new Syllabus();
////        expectedSyllabus1.setTopicName("Topic 1");
////        expectedSyllabus1.setTechnicalGroup("Group A");
////        expectedSyllabus1.setVersion("Version 1");
////        expectedSyllabus1.setAttendeeNumber(100);
////        expectedSyllabus1.setTrainingAudience("Everyone");
////        expectedSyllabus1.setTopicOutline("Outline 1");
////        expectedSyllabus1.setTrainingMaterial("Material 1");
////        expectedSyllabus1.setTrainingPrinciples("Principles 1");
////        expectedSyllabus1.setPriority("High");
////        expectedSyllabus1.setPublicStatus("Draft");
////        expectedSyllabus1.setCreatedDate(LocalDate.now());
////
////        Syllabus expectedSyllabus2 = new Syllabus();
////        expectedSyllabus2.setTopicName("Topic 2");
////        expectedSyllabus2.setTechnicalGroup("Group B");
////        expectedSyllabus2.setVersion("Version 2");
////        expectedSyllabus2.setAttendeeNumber(50);
////        expectedSyllabus2.setTrainingAudience("Managers");
////        expectedSyllabus2.setTopicOutline("Outline 2");
////        expectedSyllabus2.setTrainingMaterial("Material 2");
////        expectedSyllabus2.setTrainingPrinciples("Principles 2");
////        expectedSyllabus2.setPriority("Low");
////        expectedSyllabus2.setPublicStatus("Review");
////        expectedSyllabus2.setCreatedDate(LocalDate.now());
////
////        // Stub repository để trả về đối tượng syllabus giả mạo
////        when(repository.save(expectedSyllabus1)).thenReturn(expectedSyllabus1);
////        when(repository.save(expectedSyllabus2)).thenReturn(expectedSyllabus2);
////
////        // Thực hiện gọi phương thức importSyllabusFromCSV
////        syllabusService.importSyllabusFromCSV(multipartFile);
////
////        // Kiểm tra rằng phương thức đã gọi save() với dữ liệu chính xác
////        // (có thể kiểm tra bằng cách sử dụng Mockito.verify)
////
////        // Lấy danh sách tất cả các syllabus đã được save
////        List<Syllabus> savedSyllabusList = Arrays.asList(expectedSyllabus1, expectedSyllabus2);
////
////        // Kiểm tra rằng repository.save() đã được gọi với đúng số lượng lần và dữ liệu chính xác
////        for (Syllabus savedSyllabus : savedSyllabusList) {
////            assertEquals(savedSyllabus, repository.save(savedSyllabus));
////        }
////    }
////    @Test
////    public void testImportDataFromCsv() {
////        // Tạo dữ liệu CSV ảo để sử dụng trong kiểm tra
////        String csvData = "Topic,TechnicalGroup,Version,AttendeeNumber,TrainingAudience,TopicOutline,TrainingMaterial,TrainingPrinciples,Priority,Day,DayNumber,UnitName,UnitNumber,Content,DeliveryType,Duration,TrainingFormat,Note,TrainingPriciples\n"
////                + "SampleTopic,SampleGroup,1.0,10,All,SampleOutline,SampleMaterial,SamplePrinciples,High,Day1,1,Unit1,1,Content1,Online,1 hour,Format1,Note1,Principles1";
////
////        // Tạo MultipartFile giả với dữ liệu CSV ảo
////        MockMultipartFile mockFile = new MockMultipartFile("file.csv", csvData.getBytes());
////
////        // Gọi phương thức importDataFromCsv
////        syllabusService.importDataFromCsv(mockFile);
////
////        // Kiểm tra xem phương thức repository.save có được gọi đúng cách với dữ liệu từ file CSV hay không
////        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Syllabus.class));
////        Mockito.verify(daySyllabusRepository, Mockito.times(1)).save(Mockito.any(DaySyllabus.class));
////        Mockito.verify(trainingUnitRepository, Mockito.times(1)).save(Mockito.any(TrainingUnit.class));
////        Mockito.verify(trainingContentRepository, Mockito.times(1)).save(Mockito.any(TrainingContent.class));
////        Mockito.verify(otherSyllabusRepository, Mockito.times(1)).save(Mockito.any(OtherSyllabus.class));
////    }
//
//}
