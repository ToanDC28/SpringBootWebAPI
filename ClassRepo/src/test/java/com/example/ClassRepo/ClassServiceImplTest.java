//package com.example.ClassRepo;
//
//
//
//import com.example.ClassRepo.dto.ClassDto;
//import com.example.ClassRepo.dto.PagingClassDto;
//import com.example.ClassRepo.entity.Class;
//import com.example.ClassRepo.entity.FSU;
//import com.example.ClassRepo.entity.Location;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//
//import com.example.ClassRepo.exceptions.ClassDoesNotExistException;
//import com.example.ClassRepo.request.ClassUpdateRequest;
//import com.example.ClassRepo.request.StatusUpdateRequest;
//import com.example.ClassRepo.service.FSUService;
//import com.example.ClassRepo.service.LocationService;
//import org.mockito.ArgumentCaptor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.example.ClassRepo.entity.Slot;
//import com.example.ClassRepo.enums.Status;
//import com.example.ClassRepo.exceptions.BadRequestException;
//import com.example.ClassRepo.exceptions.SlotDoesNotExistException;
//import com.example.ClassRepo.reponses.ClassResponse;
//import com.example.ClassRepo.repository.ClassRepository;
//import com.example.ClassRepo.request.ClassCreateRequest;
//import com.example.ClassRepo.service.ClassService;
//import com.example.ClassRepo.service.impl.ClassServiceImpl;
//import com.example.ClassRepo.service.impl.FSUServiceImpl;
//import com.example.ClassRepo.service.impl.LocationServiceImpl;
//import com.example.ClassRepo.service.impl.SlotServiceImpl;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.*;
//
//import org.springframework.data.jpa.domain.Specification;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import javax.persistence.criteria.Predicate;
//
//import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
//
//import static org.junit.Assert.assertThrows;
//
//import static org.mockito.Mockito.*;
//
//public class ClassServiceImplTest {
//
//    @InjectMocks
//    private ClassServiceImpl classService;
//    @Mock
//    private ClassRepository classRepository;
//
//
//    @Value("${date.format}")
//    private String dateFormatPattern;
//
//    @Mock
//    private FSUServiceImpl fsuService;
//
//    @Mock
//    private LocationServiceImpl locationService;
//
//    @Mock
//    private SlotServiceImpl slotService;
//
//    @Mock
//    private Properties properties;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//
//        properties.setProperty("date", "yyyy/MM/dd"); // Set a valid date pattern
//
//        // Mock the properties bean
//        when(properties.getProperty("date")).thenReturn("yyyy/MM/dd");
//
//        // Inject the properties into the service
//        classService.setProperties(properties);
//    }
//
//
//    @Test
//    public void testUpdateClass() throws Exception {
//        // Create a ClassUpdateRequest
//        ClassUpdateRequest request = new ClassUpdateRequest();
//        ClassServiceImpl classService = new ClassServiceImpl();
//        request.setClass_id(1);
//        request.setClassName("Math101");
//        request.setStartDate("2023/11/01");
//        request.setEndDate("2023/12/01");
//        request.setClassCode("UGDUSJ");
//        request.setSchedule(Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03"));
//        request.setTrainer_id(Arrays.asList(1,2,3,4));
//        request.setAdmin_id(2);
//        request.setTrainingProgram_id(3);
//        request.setModifyBy("a");
//
//        // Mock the behavior of your dependencies
//        Slot slot = new Slot();
//        request.setClassTime("9:30 - 11:45");
//        when(slotService.getSlotByClassTime(request.getClassTime())).thenReturn(slot);
//
//        Location location = new Location();
//        request.setLocation("Room 101");
//        when(locationService.getLocationsByLocation(request.getLocation())).thenReturn(location);
//
//        FSU fsu = new FSU();
//        request.setFsu("AF");
//        when(fsuService.getFSUsByFsu(request.getFsu())).thenReturn(fsu);
//
//        // Mock the behavior of repository
//        Class existingClass = new Class();
//        existingClass.setTrainer_id(Arrays.asList(1,2,3,4).toString());
//        existingClass.setStatus(Status.ACTIVE); // Set the initial status to CLOSED
//        existingClass.setClassName("Sample Class");
//        existingClass.setClassName("Class A");
//        existingClass.setClassCode("C001");
//        existingClass.setClassName("Sample Class");
//        existingClass.setCreateBy("John Doe");
//        Slot slot1 = new Slot();
//        slot.setSlotId(1);
//        slot.setClasstime("9:00 - 11:45");
//        existingClass.setClassTime(slot1);
//
//        Location location1 = new Location();
//        location.setId(1);
//        location.setLocation("HCMF1");
//        existingClass.setLocation(location1);
//
//        FSU fsu1 = new FSU();
//        fsu.setId(1);
//        fsu.setName("AF");
//        existingClass.setFsu(fsu1);
//        existingClass.setTrainer_id(Arrays.asList(1,2,3,4).toString());
//        existingClass.setAdmin_id(2);
//        Date date = new Date();
//        existingClass.setStartDate(date);
//        existingClass.setEndDate(date);
//        existingClass.setTrainingProgramId(3);
//        existingClass.setStatus(Status.ACTIVE);
//        existingClass.setModifyBy("a");
//        existingClass.setModifyDate(date);
//        existingClass.setCreateDate(date);
//        existingClass.setCreateBy("a");
//        existingClass.setApproveBy("a");
//        existingClass.setApproveDate(date);
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<String> dateStrings = Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03");
//        try {
//            String jsonArray = objectMapper.writeValueAsString(dateStrings);
//            existingClass.setSchedule(jsonArray);
//        } catch (JsonProcessingException e) {
//        }
//        existingClass.setDurationInDays(1);
//
//        when(classRepository.findById(request.getClass_id())).thenReturn(Optional.of(existingClass));
//
//        // Call the method under test
//
//
//        ClassResponse response = classService.updateClass(request);
//
//        assertEquals(1, response.getClass_id());
//        verify(classRepository, times(1)).save(existingClass);
//    }
//
//    @Test
//    public void testUpdateStatusActive() throws ClassDoesNotExistException, JsonProcessingException {
//        // Create a request with ACTIVE status
//        StatusUpdateRequest request = new StatusUpdateRequest();
//        request.setId(1);
//        request.setStatus(Status.ACTIVE.name());
//
//        // Create a mock existing Class object
//        Class existingClass = new Class();
//        existingClass.setTrainer_id(Arrays.asList(1,2,3,4).toString());
//        existingClass.setStatus(Status.CLOSED); // Set the initial status to CLOSED
//        existingClass.setClassName("Sample Class");
//        existingClass.setClassName("Class A");
//        existingClass.setClassCode("C001");
//        existingClass.setClassName("Sample Class");
//        existingClass.setCreateBy("John Doe");
//        Slot slot = new Slot();
//        slot.setSlotId(1);
//        slot.setClasstime("9:00 - 11:45");
//        existingClass.setClassTime(slot);
//
//        Location location = new Location();
//        location.setId(1);
//        location.setLocation("HCMF1");
//        existingClass.setLocation(location);
//
//        FSU fsu = new FSU();
//        fsu.setId(1);
//        fsu.setName("AF");
//        existingClass.setFsu(fsu);
//        existingClass.setTrainer_id(Arrays.asList("hananh","fed").toString());
//        existingClass.setAdmin_id(2);
//        Date date = new Date();
//        existingClass.setStartDate(date);
//        existingClass.setEndDate(date);
//        existingClass.setTrainingProgramId(3);
//        existingClass.setStatus(Status.ACTIVE);
//        existingClass.setModifyBy("a");
//        existingClass.setModifyDate(date);
//        existingClass.setCreateDate(date);
//        existingClass.setCreateBy("a");
//        existingClass.setApproveBy("a");
//        existingClass.setApproveDate(date);
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<String> dateStrings = Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03");
//        try {
//            String jsonArray = objectMapper.writeValueAsString(dateStrings);
//            existingClass.setSchedule(jsonArray);
//        } catch (JsonProcessingException e) {
//        }
//        existingClass.setDurationInDays(1);
//        // Configure the repository to return the existingClass when findById is called
//        when(classRepository.findById(request.getId())).thenReturn(Optional.of(existingClass));
//
//        // Call the method
//        ClassResponse response = classService.UpdateStatus(request);
//
//        // Verify that repository.save was called
//        verify(classRepository).save(existingClass);
//
//        // Verify that the existingClass status has been updated to ACTIVE
//        assertEquals(Status.ACTIVE, existingClass.getStatus());
//
//        // Verify that the response contains the updated status
//        assertEquals(Status.ACTIVE, response.getStatus());
//    }
//
//    @Test
//    public void testUpdateStatusClosed() throws ClassDoesNotExistException, JsonProcessingException {
//        // Create a request with ACTIVE status
//        StatusUpdateRequest request = new StatusUpdateRequest();
//        request.setId(1);
//        request.setStatus(Status.CLOSED.name());
//
//        // Create a mock existing Class object
//        Class existingClass = new Class();
//        existingClass.setTrainer_id(Arrays.asList(1,2,3,4).toString());
//        existingClass.setStatus(Status.ACTIVE); // Set the initial status to CLOSED
//        existingClass.setClassName("Sample Class");
//        existingClass.setClassName("Class A");
//        existingClass.setClassCode("C001");
//        existingClass.setClassName("Sample Class");
//        existingClass.setCreateBy("John Doe");
//        Slot slot = new Slot();
//        slot.setSlotId(1);
//        slot.setClasstime("9:00 - 11:45");
//        existingClass.setClassTime(slot);
//
//        Location location = new Location();
//        location.setId(1);
//        location.setLocation("HCMF1");
//        existingClass.setLocation(location);
//
//        FSU fsu = new FSU();
//        fsu.setId(1);
//        fsu.setName("AF");
//        existingClass.setFsu(fsu);
//        existingClass.setTrainer_id(Arrays.asList(1,2,3,4).toString());
//        existingClass.setAdmin_id(2);
//        Date date = new Date();
//        existingClass.setStartDate(date);
//        existingClass.setEndDate(date);
//        existingClass.setTrainingProgramId(3);
//        existingClass.setStatus(Status.ACTIVE);
//        existingClass.setModifyBy("a");
//        existingClass.setModifyDate(date);
//        existingClass.setCreateDate(date);
//        existingClass.setCreateBy("a");
//        existingClass.setApproveBy("a");
//        existingClass.setApproveDate(date);
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<String> dateStrings = Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03");
//        try {
//            String jsonArray = objectMapper.writeValueAsString(dateStrings);
//            existingClass.setSchedule(jsonArray);
//        } catch (JsonProcessingException e) {
//        }
//        existingClass.setDurationInDays(1);
//        // Configure the repository to return the existingClass when findById is called
//        when(classRepository.findById(request.getId())).thenReturn(Optional.of(existingClass));
//
//        // Call the method
//        ClassResponse response = classService.UpdateStatus(request);
//
//        // Verify that repository.save was called
//        verify(classRepository).save(existingClass);
//
//        // Verify that the existingClass status has been updated to ACTIVE
//        assertEquals(Status.CLOSED, existingClass.getStatus());
//
//        // Verify that the response contains the updated status
//        assertEquals(Status.CLOSED, response.getStatus());
//    }
//    @Test
//    public void testUpdateStatusPlaning() throws ClassDoesNotExistException, JsonProcessingException {
//        // Create a request with ACTIVE status
//        StatusUpdateRequest request = new StatusUpdateRequest();
//        request.setId(1);
//        request.setStatus(Status.PLANNING.name());
//
//        // Create a mock existing Class object
//        Class existingClass = new Class();
//        existingClass.setTrainer_id(Arrays.asList(1,2,3,4).toString());
//        existingClass.setStatus(Status.ACTIVE); // Set the initial status to CLOSED
//        existingClass.setClassName("Sample Class");
//        existingClass.setClassName("Class A");
//        existingClass.setClassCode("C001");
//        existingClass.setClassName("Sample Class");
//        existingClass.setCreateBy("John Doe");
//        Slot slot = new Slot();
//        slot.setSlotId(1);
//        slot.setClasstime("9:00 - 11:45");
//        existingClass.setClassTime(slot);
//
//        Location location = new Location();
//        location.setId(1);
//        location.setLocation("HCMF1");
//        existingClass.setLocation(location);
//
//        FSU fsu = new FSU();
//        fsu.setId(1);
//        fsu.setName("AF");
//        existingClass.setFsu(fsu);
//        existingClass.setTrainer_id(Arrays.asList(1,2,3,4).toString());
//        existingClass.setAdmin_id(2);
//        Date date = new Date();
//        existingClass.setStartDate(date);
//        existingClass.setEndDate(date);
//        existingClass.setTrainingProgramId(3);
//        existingClass.setStatus(Status.ACTIVE);
//        existingClass.setModifyBy("a");
//        existingClass.setModifyDate(date);
//        existingClass.setCreateDate(date);
//        existingClass.setCreateBy("a");
//        existingClass.setApproveBy("a");
//        existingClass.setApproveDate(date);
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<String> dateStrings = Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03");
//        try {
//            String jsonArray = objectMapper.writeValueAsString(dateStrings);
//            existingClass.setSchedule(jsonArray);
//        } catch (JsonProcessingException e) {
//        }
//        existingClass.setDurationInDays(1);
//        // Configure the repository to return the existingClass when findById is called
//        when(classRepository.findById(request.getId())).thenReturn(Optional.of(existingClass));
//
//        // Call the method
//        ClassResponse response = classService.UpdateStatus(request);
//
//        // Verify that repository.save was called
//        verify(classRepository).save(existingClass);
//
//        // Verify that the existingClass status has been updated to ACTIVE
//        assertEquals(Status.PLANNING, existingClass.getStatus());
//
//        // Verify that the response contains the updated status
//        assertEquals(Status.PLANNING, response.getStatus());
//    }
//    @Test
//    public void testSearchClass() throws ParseException {
//        // Create assumptions
//        PagingClassDto dto = new PagingClassDto();
//        dto.setPageNo(1);
//        dto.setPageSize(10);
//        dto.setSortByColumn("createdDate");
//        dto.setSort(Sort.Direction.ASC);
//
//        String keyword = "Class";
//        String searchType = "className";
//
//        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.ASC, "createdDate"));
//        List<Class> classList = new ArrayList<>();
//        // Add class objects to the classList
//
//        when(classRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(classList));
//
//        // Call the method
//        Page<ClassDto> result = classService.searchClass(keyword, searchType, new ClassDto(), dto);
//
//        // Verify the result
//        verify(classRepository, times(1)).findAll(any(Specification.class), eq(pageable));
//
//        // Assert keyword search functionality
//        ArgumentCaptor<Specification> specCaptor = ArgumentCaptor.forClass(Specification.class);
//        verify(classRepository).findAll(specCaptor.capture(), eq(pageable));
//        Specification<Class> capturedSpec = specCaptor.getValue();
//        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
//        CriteriaQuery criteriaQuery = Mockito.mock(CriteriaQuery.class);
//        Root<Class> root = Mockito.mock(Root.class);
//        Predicate predicate = capturedSpec.toPredicate(root, criteriaQuery, criteriaBuilder);
//
//        // Assert the generated predicate based on the keyword and searchType
//        Predicate expectedPredicate = criteriaBuilder.like(root.get("className"), "%" + keyword + "%");
//        assertEquals(expectedPredicate, predicate);
//    }
//
//    @Test
//    public void testGetClassById() throws JsonProcessingException, SlotDoesNotExistException, ClassNotFoundException {
//        // Create a sample Class object for testing
//        Class classroom = new Class();
//        classroom.setClass_id(1);
//        classroom.setClassName("Sample Class");
//        classroom.setClassName("Class A");
//        classroom.setClassCode("C001");
//        classroom.setClassName("Sample Class");
//        classroom.setCreateBy("John Doe");
//        Slot slot = new Slot();
//        slot.setSlotId(1);
//        slot.setClasstime("9:00 - 11:45");
//        classroom.setClassTime(slot);
//
//        Location location = new Location();
//        location.setId(1);
//        location.setLocation("HCMF1");
//        classroom.setLocation(location);
//
//        FSU fsu = new FSU();
//        fsu.setId(1);
//        fsu.setName("AF");
//        classroom.setFsu(fsu);
//        classroom.setTrainer_id(Arrays.asList(1,2,3,4).toString());
//        classroom.setAdmin_id(2);
//        Date date = new Date();
//        classroom.setStartDate(date);
//        classroom.setEndDate(date);
//        classroom.setTrainingProgramId(3);
//        classroom.setStatus(Status.ACTIVE);
//        classroom.setModifyBy("a");
//        classroom.setModifyDate(date);
//        classroom.setCreateDate(date);
//        classroom.setCreateBy("a");
//        classroom.setApproveBy("a");
//        classroom.setApproveDate(date);
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<String> dateStrings = Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03");
//        try {
//            String jsonArray = objectMapper.writeValueAsString(dateStrings);
//            classroom.setSchedule(jsonArray);
//        } catch (JsonProcessingException e) {
//        }
//        classroom.setDurationInDays(1);
//
//
//        // Mock the repository to return the sample Class object
//        Mockito.when(classRepository.findById(1)).thenReturn(Optional.of(classroom));
//
//        // Call the method to be tested
//        ClassResponse response = classService.getClassById(1);
//
//        // Assert and validate the response
//        assertEquals(1, response.getClass_id());
//    }
//
//    @Test
//    public void testMapToClassDtoList() {
//        // Create a sample Class object for testing
//        Class classObj = new Class();
//        classObj.setClass_id(1);
//        classObj.setClassName("Sample Class");
//        classObj.setClassCode("C001");
//
//
//        Location location = new Location();
//        location.setId(1);
//        location.setLocation("HCMF1");
//        classObj.setLocation(location);
//
//        FSU fsu = new FSU();
//        fsu.setId(1);
//        fsu.setName("AF");
//        classObj.setFsu(fsu);
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<String> dateStrings = Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03");
//        try {
//            String jsonArray = objectMapper.writeValueAsString(dateStrings);
//            classObj.setSchedule(jsonArray);
//        } catch (JsonProcessingException e) {
//        }
//        Slot slot = new Slot();
//        slot.setSlotId(1);
//        slot.setClasstime("9:30 - 11:45");
//        classObj.setClassTime(slot);
//        classObj.setStartDate(new Date());
//        classObj.setEndDate(new Date());
//        classObj.setCreateDate(new Date());
//        classObj.setModifyDate(new Date());
//        classObj.setApproveDate(new Date());
//        classObj.setCreateBy("John Doe");
//        classObj.setApproveBy("Jane Doe");
//        classObj.setModifyBy("Alice");
//        classObj.setAdmin_id(1);
//        classObj.setTrainingProgramId(2);
//        classObj.setTrainer_id(Arrays.asList(1,2,3,4).toString());
//        classObj.setDurationInDays(5);
//
//        // Mock the properties mock to return a date format pattern
//        when(properties.getProperty("date")).thenReturn("yyyy-MM-dd");
//
//        // Mock other dependencies like ObjectMapper if needed
//
//        // Create a list of Class objects
//        List<Class> classList = Arrays.asList(classObj);
//
//        // Call the method to be tested
//        List<ClassDto> classDtoList = classService.mapToClassDtoList(classList);
//
//        // Assert and validate the response
//        assertEquals(1, classDtoList.size());
//        ClassDto classDto = classDtoList.get(0);
//        assertEquals(1, classDto.getId());
//        assertEquals("Sample Class", classDto.getClassName());
//        assertEquals("C001", classDto.getClassCode());
//        // Add more assertions for other properties
//    }
//
//    @Test
//    public void testGetAllClasses() {
//        // Create a sample PagingClassDto for testing
//        PagingClassDto dto = new PagingClassDto();
//        dto.setPageNo(1);
//        dto.setPageSize(10);
//        dto.setKeyword("className");
//        // Create a sample Page of Class objects for testing
//        Page<Class> classPage = new PageImpl<>(Collections.singletonList(createSampleClass()));
//
//        // Mock the repository mock to return the sample Page when findAll is called
//        when(classRepository.findAll(any(Pageable.class))).thenReturn(classPage);
//
//        // Call the method to be tested
//        Page<ClassDto> resultPage = classService.getAllClasses(dto);
//
//        // Assert and validate the response
//        assertEquals(1, resultPage.getContent().size());
//        ClassDto classDto = resultPage.getContent().get(0);
//        assertEquals("Sample Class", classDto.getClassName());
//        // Add more assertions for other properties
//
//        // Verify that the repository's findAll method was called with the expected arguments
//        verify(classRepository).findAll(any(Pageable.class));
//    }
//
//    private Class createSampleClass() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(properties.getProperty("date"));
//        Class classObj = new Class();
//        classObj.setClass_id(1);
//        classObj.setClassName("Sample Class");
//        classObj.setClassCode("C001");
//
//
//        Location location = new Location();
//        location.setId(1);
//        location.setLocation("HCMF1");
//        classObj.setLocation(location);
//
//        FSU fsu = new FSU();
//        fsu.setId(1);
//        fsu.setName("AF");
//        classObj.setFsu(fsu);
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<String> dateStrings = Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03");
//        try {
//            String jsonArray = objectMapper.writeValueAsString(dateStrings);
//            classObj.setSchedule(jsonArray);
//        } catch (JsonProcessingException e) {
//        }
//        Slot slot = new Slot();
//        slot.setSlotId(1);
//        slot.setClasstime("9:30 - 11:45");
//        classObj.setClassTime(slot);
//        classObj.setStartDate(new Date());
//        classObj.setEndDate(new Date());
//        classObj.setCreateDate(new Date());
//        classObj.setModifyDate(new Date());
//        classObj.setApproveDate(new Date());
//        classObj.setCreateBy("John Doe");
//        classObj.setApproveBy("Jane Doe");
//        classObj.setModifyBy("Alice");
//        classObj.setAdmin_id(1);
//        classObj.setTrainingProgramId(2);
//        classObj.setTrainer_id(Arrays.asList(1,2,3,4).toString());
//        classObj.setDurationInDays(5);
//        return classObj;
//    }
//
//    @Test
//    public void testValidateClass_ClassDoesNotExist() {
//        // Arrange
//        String className = "Math101";
//
//        // When repository.existsClassByClassName() is called, return false
//        when(classRepository.existsClassByClassName(className)).thenReturn(false);
//
//
//        try {
//            classService.validateClass(className);
//        } catch (BadRequestException e) {
//            // Assert that an exception is not thrown
//            fail("No exception should be thrown for a non-existing class.");
//        }
//    }
//
//    @Test
//    public void testValidateClass_ClassExists() {
//        // Arrange
//        String className = "Math101";
//        when(classRepository.existsClassByClassName(className)).thenReturn(true);
//
//        // Act and Assert
//        assertThrows(BadRequestException.class, () -> classService.validateClass(className));
//    }
//
//    @Test
//    public void testValidateBlank_AllFieldsBlank() {
//
//        ClassServiceImpl classService = new ClassServiceImpl();
//        ClassCreateRequest request = new ClassCreateRequest();
//        assertThrows(IllegalArgumentException.class, () -> classService.validateBlank(request));
//    }
//
//    @Test
//    public void testValidateBlank_ClassNameBlank() {
//        // Arrange
//        ClassServiceImpl classService = new ClassServiceImpl();
//        ClassCreateRequest request = new ClassCreateRequest();
//        request.setClassCode("MATH123");
//        request.setCreateBy("John Doe");
//        request.setLocation("Room 101");
//        request.setFSU("123456");
//        request.setStartDate("2023/11/01");
//        request.setEndDate("2023/12/01");
//        request.setClassTime("9:00 AM - 11:00 AM");
//        request.setSchedule(Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03"));
//        request.setTrainer_id(Arrays.asList(1,2,3,4));
//        request.setAdmin_id(2);
//        request.setTrainingProgram_id(3);
//        request.setStatus("PLANING"); // Set the default status
//
//        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> classService.validateBlank(request));
//    }
//
//    @Test
//    public void testValidateBlank_ClassCodeBlank() {
//        // Arrange
//        ClassServiceImpl classService = new ClassServiceImpl();
//        ClassCreateRequest request = new ClassCreateRequest();
//        request.setClassName("Math101");
//        request.setCreateBy("John Doe");
//        request.setLocation("Room 101");
//        request.setFSU("123456");
//        request.setStartDate("2023/11/01");
//        request.setEndDate("2023/12/01");
//        request.setClassTime("9:00 AM - 11:00 AM");
//        request.setSchedule(Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03"));
//        request.setTrainer_id(Arrays.asList(1,2,3,4));
//        request.setAdmin_id(2);
//        request.setTrainingProgram_id(3);
//        request.setStatus("PLANING"); // Set the default status
//
//        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> classService.validateBlank(request));
//    }
//    @Test
//    public void testValidateBlank_CreateByBlank() {
//        // Arrange
//        ClassServiceImpl classService = new ClassServiceImpl();
//        ClassCreateRequest request = new ClassCreateRequest();
//        request.setClassName("Math101");
//        request.setClassCode("AS");
//        request.setLocation("Room 101");
//        request.setFSU("123456");
//        request.setStartDate("2023/11/01");
//        request.setEndDate("2023/12/01");
//        request.setClassTime("9:00 AM - 11:00 AM");
//        request.setSchedule(Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03"));
//        request.setTrainer_id(Arrays.asList(1,2,3,4));
//        request.setAdmin_id(2);
//        request.setTrainingProgram_id(3);
//        request.setStatus("PLANING"); // Set the default status
//
//        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> classService.validateBlank(request));
//    }
//
//    @Test
//    public void testValidateBlank_LocationBlank() {
//        // Arrange
//        ClassServiceImpl classService = new ClassServiceImpl();
//        ClassCreateRequest request = new ClassCreateRequest();
//        request.setClassName("Math101");
//        request.setCreateBy("John Doe");
//        request.setClassCode("ADD");
//        request.setFSU("123456");
//        request.setStartDate("2023/11/01");
//        request.setEndDate("2023/12/01");
//        request.setClassTime("9:00 AM - 11:00 AM");
//        request.setSchedule(Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03"));
//        request.setTrainer_id(Arrays.asList(1,2,3,4));
//        request.setAdmin_id(2);
//        request.setTrainingProgram_id(3);
//        request.setStatus("PLANING"); // Set the default status
//
//        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> classService.validateBlank(request));
//    }
//
//    @Test
//    public void testValidateBlank_FSUBlank() {
//        // Arrange
//        ClassServiceImpl classService = new ClassServiceImpl();
//        ClassCreateRequest request = new ClassCreateRequest();
//        request.setClassName("Math101");
//        request.setCreateBy("John Doe");
//        request.setLocation("Room 101");
//        request.setClassCode("123456");
//        request.setStartDate("2023/11/01");
//        request.setEndDate("2023/12/01");
//        request.setClassTime("9:00 AM - 11:00 AM");
//        request.setSchedule(Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03"));
//        request.setTrainer_id(Arrays.asList(1,2,3,4));
//        request.setAdmin_id(2);
//        request.setTrainingProgram_id(3);
//        request.setStatus("PLANING"); // Set the default status
//
//        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> classService.validateBlank(request));
//    }
//
//    @Test
//    public void testValidateBlank_StartDateBlank() {
//        // Arrange
//        ClassServiceImpl classService = new ClassServiceImpl();
//        ClassCreateRequest request = new ClassCreateRequest();
//        request.setClassName("Math101");
//        request.setCreateBy("John Doe");
//        request.setLocation("Room 101");
//        request.setFSU("123456");
//        request.setClassCode("2023/11/01");
//        request.setEndDate("2023/12/01");
//        request.setClassTime("9:00 AM - 11:00 AM");
//        request.setSchedule(Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03"));
//        request.setTrainer_id(Arrays.asList(1,2,3,4));
//        request.setAdmin_id(2);
//        request.setTrainingProgram_id(3);
//        request.setStatus("PLANING"); // Set the default status
//
//        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> classService.validateBlank(request));
//    }
//    @Test
//    public void testValidateBlank_EndDateBlank() {
//        // Arrange
//        ClassServiceImpl classService = new ClassServiceImpl();
//        ClassCreateRequest request = new ClassCreateRequest();
//        request.setClassName("Math101");
//        request.setCreateBy("John Doe");
//        request.setLocation("Room 101");
//        request.setFSU("123456");
//        request.setStartDate("2023/11/01");
//        request.setClassCode("2023/12/01");
//        request.setClassTime("9:00 AM - 11:00 AM");
//        request.setSchedule(Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03"));
//        request.setTrainer_id(Arrays.asList(1,2,3,4));
//        request.setAdmin_id(2);
//        request.setTrainingProgram_id(3);
//        request.setStatus("PLANING"); // Set the default status
//
//        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> classService.validateBlank(request));
//    }
//    @Test
//    public void testValidateBlank_ClassTimeBlank() {
//        // Arrange
//        ClassServiceImpl classService = new ClassServiceImpl();
//        ClassCreateRequest request = new ClassCreateRequest();
//        request.setClassName("Math101");
//        request.setCreateBy("John Doe");
//        request.setLocation("Room 101");
//        request.setFSU("123456");
//        request.setStartDate("2023/11/01");
//        request.setEndDate("2023/12/01");
//        request.setClassCode("UGDUSJ");
//        request.setSchedule(Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03"));
//        request.setTrainer_id(Arrays.asList(1,2,3,4));
//        request.setAdmin_id(2);
//        request.setTrainingProgram_id(3);
//        request.setStatus("PLANING"); // Set the default status
//
//        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> classService.validateBlank(request));
//    }
//    @Test
//    public void testValidateBlank_ScheduleBlank() {
//        // Arrange
//        ClassServiceImpl classService = new ClassServiceImpl();
//        ClassCreateRequest request = new ClassCreateRequest();
//        request.setClassName("Math101");
//        request.setCreateBy("John Doe");
//        request.setLocation("Room 101");
//        request.setFSU("123456");
//        request.setStartDate("2023/11/01");
//        request.setEndDate("2023/12/01");
//        request.setClassTime("9:00 AM - 11:00 AM");
//        request.setClassCode("sdasd");
//        request.setTrainer_id(Arrays.asList(1,2,3,4));
//        request.setAdmin_id(2);
//        request.setTrainingProgram_id(3);
//        request.setStatus("PLANING"); // Set the default status
//
//        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> classService.validateBlank(request));
//    }
//    @Test
//    public void testValidateBlank_TrainerBlank() {
//        // Arrange
//        ClassServiceImpl classService = new ClassServiceImpl();
//        ClassCreateRequest request = new ClassCreateRequest();
//        request.setClassName("Math101");
//        request.setCreateBy("John Doe");
//        request.setLocation("Room 101");
//        request.setFSU("123456");
//        request.setStartDate("2023/11/01");
//        request.setEndDate("2023/12/01");
//        request.setClassTime("9:00 AM - 11:00 AM");
//        request.setSchedule(Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03"));
//        request.setClassCode("HGSJD");
//        request.setAdmin_id(2);
//        request.setTrainingProgram_id(3);
//        request.setStatus("PLANING"); // Set the default status
//
//        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> classService.validateBlank(request));
//    }
//
//    @Test
//    public void testValidateBlank_AdminBlank() {
//        // Arrange
//        ClassServiceImpl classService = new ClassServiceImpl();
//        ClassCreateRequest request = new ClassCreateRequest();
//        request.setClassName("Math101");
//        request.setCreateBy("John Doe");
//        request.setLocation("Room 101");
//        request.setFSU("123456");
//        request.setStartDate("2023/11/01");
//        request.setEndDate("2023/12/01");
//        request.setClassTime("9:00 AM - 11:00 AM");
//        request.setSchedule(Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03"));
//        request.setTrainer_id(Arrays.asList(1,2,3,4));
//        request.setClassCode("HJHKFD");
//        request.setTrainingProgram_id(3);
//        request.setStatus("PLANING"); // Set the default status
//
//        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> classService.validateBlank(request));
//    }
//
//    @Test
//    public void testValidateBlank_TrainingProgramBlank() {
//        // Arrange
//        ClassServiceImpl classService = new ClassServiceImpl();
//        ClassCreateRequest request = new ClassCreateRequest();
//        request.setClassName("Math101");
//        request.setCreateBy("John Doe");
//        request.setLocation("Room 101");
//        request.setFSU("123456");
//        request.setStartDate("2023/11/01");
//        request.setEndDate("2023/12/01");
//        request.setClassTime("9:00 AM - 11:00 AM");
//        request.setSchedule(Arrays.asList("2023/11/01", "2023/11/02", "2023/11/03"));
//        request.setTrainer_id(Arrays.asList(1,2,3,4));
//        request.setAdmin_id(2);
//        request.setClassCode("HKDJN");
//        request.setStatus("PLANING"); // Set the default status
//
//        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> classService.validateBlank(request));
//    }
//}
