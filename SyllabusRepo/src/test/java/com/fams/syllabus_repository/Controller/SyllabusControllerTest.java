//package com.fams.syllabus_repository.Controller;
//
//import com.fams.syllabus_repository.controller.SyllabusController;
//import com.fams.syllabus_repository.dto.*;
//import com.fams.syllabus_repository.entity.Syllabus;
//import com.fams.syllabus_repository.entity.TrainingMaterials;
//import com.fams.syllabus_repository.exceptions.MaterialNotFoundException;
//import com.fams.syllabus_repository.exceptions.SyllabusNotFoundException;
//import com.fams.syllabus_repository.repository.TrainingMaterialsRepositoty;
//import com.fams.syllabus_repository.request.RequestIdDto;
//import com.fams.syllabus_repository.response.ResponseObject;
//import com.fams.syllabus_repository.service.*;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.function.Executable;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.exceptions.base.MockitoException;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.multipart.MultipartFile;
//import org.webjars.NotFoundException;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//public class SyllabusControllerTest {
//
//    private MockMvc mockMvc;
//    @InjectMocks
//    private SyllabusController syllabusController;
//
//    @Mock
//    private SyllabusService syllabusService;
//
//    @Mock
//    private TrainingUnitService trainingUnitService;
//
//    @Mock
//    private TrainingContentService trainingContentService;
//
//    @Mock
//    private OtherSyllabusService otherSyllabusService;
//
//    @Mock
//    private DaySyllabusService daySyllabusService;
//
//    @Mock
//    private TrainingMaterialsRepositoty trainingMaterialsRepository;
//
//    @Mock
//    private TrainingMaterialsService trainingMaterialsService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(syllabusController).build();
//    }
//
//    private static String asJsonString(final Object obj) {
//        try {
//            final ObjectMapper objectMapper = new ObjectMapper();
//            return objectMapper.writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Test
//    public void testGetAllSyllabus() throws Exception {
//        String selectedDateStr = "2023-10-18";
//        String selectedEndDateStr = "2023-10-20";
//        int pageNo = 0;
//        int pageSize = 10;
//        String keyword = "test";
//        String sort = "propertyName:asc,anotherProperty:desc";
//        PagingSyllabusDto expectedResponse = new PagingSyllabusDto();
//        when(syllabusService.getAllSyllabusAndSearch(
//                pageNo, pageSize, keyword
//        )).thenReturn(expectedResponse);
//
//        try {
//            mockMvc.perform(post("/api/syllabus/listAll") // Change get to post
//                    .param("pageNo", String.valueOf(pageNo))
//                    .param("pageSize", String.valueOf(pageSize))
//                    .param("search", keyword)
//                    .param("sort", sort)
//                    .param("selectedDate", selectedDateStr)
//                    .param("selectedEndDate", selectedEndDateStr)
//                    .contentType(MediaType.APPLICATION_JSON));
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail("An exception occurred during the test.");
//        }
//    }
//
//
//    @Test
//    public void testSyllabusDetailSuccess() throws SyllabusNotFoundException {
//        // Arrange
//        Long id = 1L;
//        SyllabusDetailDto expectedDto = new SyllabusDetailDto(/* Initialize with test data */);
//        RequestIdDto request = new RequestIdDto(id);
//
//        when(syllabusService.getSyllabusById(id)).thenReturn(expectedDto);
//
//        // Act
//        ResponseObject<Object> response = syllabusController.syllabusDetail(request);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
//        assertEquals("Syllabus successfully", response.getMessage());
//        assertEquals(expectedDto, response.getData());
//    }
//
//    @Test
//    public void testSyllabusDetailSyllabusNotFoundException() throws SyllabusNotFoundException {
//        // Arrange
//        Long id = 2L;
//        RequestIdDto request = new RequestIdDto(id);
//
//        when(syllabusService.getSyllabusById(id)).thenThrow(new SyllabusNotFoundException("Syllabus not found"));
//
//        // Act
//        ResponseObject<Object> response = syllabusController.syllabusDetail(request);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
//        assertEquals("Error syllabus", response.getMessage());
//    }
//
//    @Test
//    public void testUpdateGeneral() {
//        // Tạo một đối tượng SyllabusDto mẫu để sử dụng trong kiểm thử
//        SyllabusDto requestDto = new SyllabusDto();
//        requestDto.setId(1L);
//        requestDto.setSyllabusName("Updated Name");
//
//        SyllabusDto expectedResponse = new SyllabusDto();
//        expectedResponse.setId(1L);
//        expectedResponse.setSyllabusName("Updated Name");
//
//        // Mô phỏng behavior cho syllabusService.createAndUpdate
//        when(syllabusService.createAndUpdate(requestDto)).thenReturn(expectedResponse);
//
//        // Gọi phương thức cần được kiểm thử
//        ResponseEntity<SyllabusDto> responseEntity = syllabusController.updateGeneral(requestDto);
//
//        // Kiểm tra xem phản hồi có đúng không
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(expectedResponse, responseEntity.getBody());
//    }
//
//    @Test
//    public void testDuplicateSyllabus() {
//        Long id = 1L;
//        RequestIdDto requestIdDto = new RequestIdDto();
//        requestIdDto.setId(id);
//
//        SyllabusDto duplicatedSyllabus = new SyllabusDto();
//        duplicatedSyllabus.setId(id);
//
//        when(syllabusService.duplicateSyllabus(id)).thenReturn(duplicatedSyllabus);
//
//        ResponseEntity<SyllabusDto> response = syllabusController.duplicateSyllabus(requestIdDto);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(id, response.getBody().getId());
//    }
//    @Test
//    public void testDuplicateSyllabus_NotFound() {
//        Long id = 1L; // Đây là một giá trị id giả định, bạn có thể thay đổi nó tùy theo trường hợp kiểm tra
//
//        // Tạo một đối tượng RequestIdDto với giá trị id
//        RequestIdDto requestIdDto = new RequestIdDto();
//        requestIdDto.setId(id);
//
//        // Mô phỏng việc gọi syllabusService.duplicateSyllabus và trả về giá trị null
//        Mockito.when(syllabusService.duplicateSyllabus(id)).thenReturn(null);
//
//        // Gọi phương thức cần được kiểm thử
//        ResponseEntity<SyllabusDto> responseEntity = syllabusController.duplicateSyllabus(requestIdDto);
//
//        // Kiểm tra xem phản hồi có đúng không
//        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//    }
//
//
////    @Test
////    public void testImportDataCSV_Success() throws Exception{
////        MultipartFile file = createMockMultipartFile("test.csv", "text/csv", "CSV data");
////        Mockito.doNothing().when(syllabusService).importDataFromCsv(file);
////        ResponseEntity<String> response = syllabusController.importDataCSV(file);
////        Mockito.verify(syllabusService, Mockito.times(1)).importDataFromCsv(file);
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////        assertEquals("Imported successfully", response.getBody());
////    }
//
////    @Test
////    public void testImportDataCSV_Failure() {
////        MultipartFile file = createMockMultipartFile("test.csv", "text/csv", "CSV data");
////        Mockito.doThrow(new MockitoException("Import failed")).when(syllabusService).importDataFromCsv(file);
////        ResponseEntity<String> response = syllabusController.importDataCSV(file);
////        Mockito.verify(syllabusService, Mockito.times(1)).importDataFromCsv(file);
////        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
////        assertEquals("Failed to import: Import failed", response.getBody());
////    }
//
//    @Test
//    public void testDeleteSyllabus_Success() {
//        RequestIdDto requestIdDto = new RequestIdDto();
//        requestIdDto.setId(1L);
//        syllabusController.deleteSyllabus(requestIdDto);
//        Mockito.verify(syllabusService, Mockito.times(1)).deleteSyllabusById(1L);
//    }
//
//    @Test
//    public void testDeleteSyllabus_Exception() {
//        RequestIdDto requestIdDto = new RequestIdDto();
//        requestIdDto.setId(99L);
//        Mockito.doThrow(new IllegalArgumentException()).when(syllabusService).deleteSyllabusById(99L);
//        syllabusController.deleteSyllabus(requestIdDto);
//        Mockito.verify(syllabusService, Mockito.times(1)).deleteSyllabusById(99L);
//    }
//
////    @Test
////    public void testDeleteMaterial_Success() {
////        RequestIdDto requestIdDto = new RequestIdDto();
////        requestIdDto.setId(1L);
////
////        doNothing().when(trainingMaterialsService).deleteMaterial(1L);
////
////        ResponseEntity<String> response = trainingMaterialsService.deleteMaterial(requestIdDto);
////
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////        assertEquals("Material with ID 1 has been deleted", response.getBody());
////    }
//
////    @Test
////    public void testDeleteMaterial_Fail() {
////        RequestIdDto requestIdDto = new RequestIdDto();
////        requestIdDto.setId(1L);
////
////        // Giả lập behavior để ném một ngoại lệ hoặc trả về false khi gọi trainingMaterialsService.deleteMaterial
////        doThrow(new RuntimeException("Delete failed")).when(trainingMaterialsService).deleteMaterial(1L);
////
////        ResponseEntity<String> response = syllabusController.deleteMaterial(requestIdDto);
////
////        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
////        assertEquals("An error occurred while deleting the material", response.getBody());
////    }
//
//
//
////    @Test
////    public void testDeleteMaterial_InternalError() {
////        RequestIdDto requestIdDto = new RequestIdDto();
////        requestIdDto.setId(1L);
////
////        doThrow(new RuntimeException("Internal error")).when(trainingMaterialsService).deleteMaterial(1L);
////
////        ResponseEntity<String> response = syllabusController.deleteMaterial(requestIdDto);
////
////        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
////        // Add assertion for error message here if necessary
////    }
//
////    @Test
////    public void testDownloadResource_Success() {
////        Long resourceId = 1L;
////
////        // Giả lập dữ liệu tải xuống
////        byte[] fileData = "Sample file content".getBytes();
////
////        // Giả lập dữ liệu TrainingMaterials
////        TrainingMaterials resource = new TrainingMaterials();
////        resource.setS3Location("sample-file.txt");
////
////        when(trainingMaterialsService.downloadTrainingMaterial(resourceId)).thenReturn(fileData);
////        when(trainingMaterialsRepository.findById(resourceId)).thenReturn(Optional.of(resource));
////
////        ResponseEntity<byte[]> response = syllabusController.downloadResource(resourceId);
////
////        // Kiểm tra mã trạng thái
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////
////        // Kiểm tra headers
////        HttpHeaders headers = response.getHeaders();
////        assertTrue(headers.containsKey(HttpHeaders.CONTENT_DISPOSITION));
////        assertEquals("attachment; filename=sample-file.txt", headers.getFirst(HttpHeaders.CONTENT_DISPOSITION));
////
////        // Kiểm tra kiểu nội dung
////        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
////
////        // Kiểm tra dữ liệu
////        assertArrayEquals(fileData, response.getBody());
////    }
//
////    @Test
////    public void testDownloadResource_NotFound() {
////        Long resourceId = 1L;
////
////        // Giả lập dữ liệu tải xuống trả về null
////        when(trainingMaterialsService.downloadTrainingMaterial(resourceId)).thenReturn(null);
////
////        ResponseEntity<byte[]> response = syllabusController.downloadResource(resourceId);
////
////        // Kiểm tra mã trạng thái 404 Not Found
////        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
////    }
//
//    private MultipartFile createMockMultipartFile(String originalFilename, String contentType, String content) {
//        return new MockMultipartFile(originalFilename, originalFilename, contentType, content.getBytes());
//    }
//
////    @Test
////    public void testDownloadResourceNotFound() {
////        // Mô phỏng resourceId
////        Long resourceId = 1L;
////
////        // Mô phỏng behavior cho trainingMaterialsService.downloadTrainingMaterial trả về null
////        Mockito.when(trainingMaterialsService.downloadTrainingMaterial(resourceId)).thenReturn(null);
////
////        // Mô phỏng behavior cho trainingMaterialsRepository.findById trả về null
////        Mockito.when(trainingMaterialsRepository.findById(resourceId)).thenReturn(java.util.Optional.empty());
////
////        // Gọi phương thức cần được kiểm thử
////        ResponseEntity<byte[]> response = syllabusController.downloadResource(resourceId);
////
////        // Kiểm tra xem phản hồi có đúng không
////        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
////    }
//
//
////    @Test
////    public void testGetDaySyllabusBySyllabusId() throws Exception {
////        Long syllabusId = 1L;
////        List<DaySyllabusListDto> expectedDaySyllabusList = new ArrayList<>();
////        when(daySyllabusService.getDaySyllabusBySyllabusId(syllabusId)).thenReturn(expectedDaySyllabusList);
////        mockMvc.perform(MockMvcRequestBuilders.get("/api/syllabus/day-syllabus/{syllabusId}", syllabusId))
////                .andExpect(MockMvcResultMatchers.status().isOk());
////    }
//
//
////    @Test
////    public void testCreateDaySyllabus() {
////        // Mô phỏng syllabusId và daySyllabusDto
////        Long syllabusId = 1L;
////        DaySyllabusDto daySyllabusDto = new DaySyllabusDto();
////        daySyllabusDto.setDay("Day 1");
////
////        // Mô phỏng behavior cho daySyllabusService.createDaySyllabus
////        DaySyllabusDto createdDaySyllabus = new DaySyllabusDto();
////        createdDaySyllabus.setId(1L);
////        createdDaySyllabus.setDay("Day 1");
////
////        Mockito.when(daySyllabusService.createDaySyllabus(eq(syllabusId), any(DaySyllabusDto.class))).thenReturn(createdDaySyllabus);
////
////        // Gọi phương thức cần được kiểm thử
////        ResponseEntity<DaySyllabusDto> response = syllabusController.createDaySyllabus(syllabusId, daySyllabusDto);
////
////        // Kiểm tra xem phản hồi có đúng không
////        assertEquals(HttpStatus.CREATED, response.getStatusCode());
////        //assertEquals(createdDaySyllabus, response.getBody());
////    }
////    @Test
////    public void testYourControllerMethod_Success() {
////        // Mô phỏng daySyllabusService.deleteDaySyllabus thành công
////        Long syllabusId = 1L;
////        Long dayId = 2L;
////
////        Mockito.doNothing().when(daySyllabusService).deleteDaySyllabus(syllabusId, dayId);
////
////        // Gọi phương thức cần được kiểm thử
////        String result = syllabusController.yourControllerMethod(syllabusId, dayId);
////
////        // Kiểm tra xem kết quả có phải là "success_view" không
////        assertEquals("success_view", result);
////    }
////
////    @Test
////    public void testYourControllerMethod_Failure() {
////        // Mô phỏng daySyllabusService.deleteDaySyllabus bị lỗi
////        Long syllabusId = 1L;
////        Long dayId = 2L;
////
////        Mockito.doThrow(new RuntimeException("Delete error")).when(daySyllabusService).deleteDaySyllabus(syllabusId, dayId);
////
////        // Gọi phương thức cần được kiểm thử
////        String result = syllabusController.yourControllerMethod(syllabusId, dayId);
////
////        // Kiểm tra xem kết quả có phải là "error_view" không
////        assertEquals("error_view", result);
////    }
////    @Test
////    public void testImportCSV_Exception() {
////        // Mô phỏng ngoại lệ khi gọi syllabusService.importSyllabusFromCSV
////        MultipartFile file = null; // Đây chỉ là ví dụ, bạn có thể sử dụng MultipartFile thật
////
////        Mockito.doThrow(new RuntimeException("Import error")).when(syllabusService).importSyllabusFromCSV(file);
////
////        // Gọi phương thức cần được kiểm thử
////        ResponseEntity<String> responseEntity = syllabusController.importCSV(file);
////
////        // Kiểm tra xem phản hồi có đúng không
////        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
////        assertEquals("Failed to import: Import error", responseEntity.getBody());
////    }
//
//
//    @Test
//    public void testHandleException() {
//        // Tạo một exception giả định để sử dụng trong phương thức handleException
//        Exception simulatedException = new Exception("Simulated exception message");
//
//        // Thực hiện phương thức handleException và kiểm tra ngoại lệ được ném
//        Executable executable = () -> syllabusController.handleException(simulatedException);
//        assertThrows(RuntimeException.class, executable);
//
//        // Kiểm tra xem phản hồi có đúng không
//        try {
//            syllabusController.handleException(simulatedException);
//        } catch (RuntimeException e) {
//            assertEquals("An error occurred: Simulated exception message", e.getMessage());
//        }
//    }
////    @Test
////    public void testGetAllSyllabusWithException() {
////        // Tạo giá trị đầu vào giả định
////        int pageNo = 0;
////        int pageSize = 10;
////        String keyword = "searchKeyword";
////        String sort = "propertyName:asc";
////        String selectedDateStr = "2023-10-13";
////        String selectedEndDateStr = "2023-10-14";
////
////        // Giả lập một exception để kiểm tra trường hợp catch (Exception)
////        Exception simulatedException = new Exception("Simulated exception message");
////
////        // Giả lập sự kiện khi gọi syllabusService.getAllSyllabusAndSearch sẽ ném ra exception
////        Mockito.when(syllabusService.getAllSyllabusAndSearch(pageNo, pageSize, keyword, Sort.unsorted(), null, null))
////                .thenThrow(new SyllabusNotFoundException("Simulated exception message"));
////
////        // Thực hiện phương thức và kiểm tra kết quả
////        PagingSyllabusDto result = syllabusController.getAllSyllabus(pageNo, pageSize, keyword, sort, selectedDateStr, selectedEndDateStr);
////        // Kiểm tra kết quả nếu có một exception
////        assertEquals(null, result);
////        SyllabusController syllabusController = Mockito.mock(SyllabusController.class);
////
////        // Kiểm tra xem exception có được log không
////       // Mockito.verify(syllabusController, Mockito.times(1)).handleException(Mockito.any(Exception.class));
////    }
////    @Test
////    public void testUploadMaterialsResource_Success() {
////        // Tạo đối tượng MultipartFile giả mạo
////        MultipartFile file = createMockMultipartFile("test.txt", "text/plain", "Sample file content");
////
////        // Mock trainingMaterialsService.uploadTrainingMaterial để trả về true khi gọi
////        when(trainingMaterialsService.uploadTrainingMaterial("Sample Description", file)).thenReturn(true);
////
////        // Gọi phương thức cần kiểm tra
////        String response = syllabusController.uploadMaterialsResource("Sample Description", file);
////
////        // Xác minh kết quả
////        assertEquals("Resource uploaded successfully!", response);
////    }
////
////    @Test
////    public void testUploadMaterialsResource_Failure() {
////        // Tạo đối tượng MultipartFile giả mạo
////        MultipartFile file = createMockMultipartFile("test.txt", "text/plain", "Sample file content");
////
////        // Mock trainingMaterialsService.uploadTrainingMaterial để trả về false khi gọi
////        when(trainingMaterialsService.uploadTrainingMaterial("Sample Description", file)).thenReturn(false);
////
////        // Gọi phương thức cần kiểm tra
////        String response = syllabusController.uploadMaterialsResource("Sample Description", file);
////
////        // Xác minh kết quả
////        assertEquals("Failed to upload resource.", response);
////    }
//}
