//package com.team2.trainingprogramrepo.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.team2.trainingprogramrepo.TrainingProgramRepoApplication;
//import com.team2.trainingprogramrepo.dto.*;
//import com.team2.trainingprogramrepo.entity.TrainingProgram;
//import com.team2.trainingprogramrepo.exception.BadRequestException;
//import com.team2.trainingprogramrepo.exception.DuplicateRecordException;
//import com.team2.trainingprogramrepo.request.GetIDRequest;
//import com.team2.trainingprogramrepo.response.Training_SyllabusResponse;
//import com.team2.trainingprogramrepo.service.TrainingProgramService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.web.client.HttpServerErrorException.InternalServerError;
//
//@SpringBootTest(classes = TrainingProgramRepoApplication.class)
//@AutoConfigureMockMvc
//class TrainingProgramControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private TrainingProgramService trainingProgramService;
//
//    @InjectMocks
//    private TrainingProgramController trainingProgramController;
//
//    @Test
//    public void getAllTrainingProgramsPaginationListGivenValidRequestReturnsListSuccessfully() throws Exception {
//        PageRequestDto dto = new PageRequestDto();
//        dto.setPageNo(0);
//        dto.setPageSize(10);
//
//        Page<TrainingProgramRequest> page = new PageImpl<>(List.of());
//
//        when(trainingProgramService.getAllTrainingPrograms(dto)).thenReturn(page);
//
//        // Make a request to the /training-programs endpoint
//        MockHttpServletResponse response = mockMvc.perform(
//                        post("/api/trainingProgram/list")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(dto))
//                )
//                .andReturn().getResponse();
//
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
//    }
//
//    @Test
//    public void getAllTrainingProgramsPaginationListGivenNullRequestReturnsNull() {
//        Page<TrainingProgramRequest> page = trainingProgramController.getAllTrainingProgramsPaginationList(null);
//
//        assertNull(page);
//    }
//
//    @Test
//    public void getTrainingSyllabusByProgramIdGivenValidRequestReturnsSuccess() throws Exception {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(1);
//
//        Training_SyllabusResponse trainingSyllabusResponse = Training_SyllabusResponse.builder()
//                .program(null)
//                .syllabusList(null)
//                .build();
//
//        when(trainingProgramService.findTrainingSyllabusByProgramId(getIDRequest.getId())).thenReturn(trainingSyllabusResponse);
//
//        MockHttpServletResponse response = mockMvc.perform(
//                        post("/api/trainingProgram/syllabus")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(getIDRequest))
//                )
//                .andReturn().getResponse();
//
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
//    }
//
//    @Test
//    public void getClassesByTrainingProgramIdWithValidTrainingProgramIdReturnsSuccess() throws Exception {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(1);
//
//        List<ClassDto> sampleClassList = List.of(
//                ClassDto.builder().className("haha").build()
//        );
//
//        when(trainingProgramService.getClassesByTrainingProgramId(getIDRequest.getId())).thenReturn(sampleClassList);
//
//        mockMvc.perform(
//                        post("/api/trainingProgram/classes")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(getIDRequest))
//                )
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void getClassesByTrainingProgramIdWithNullTrainingProgramIdReturnsNotFound() throws Exception {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(null);
//
//
//        when(trainingProgramService.getClassesByTrainingProgramId(getIDRequest.getId())).thenReturn(null);
//
//        mockMvc.perform(
//                        post("/api/trainingProgram/classes")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(getIDRequest))
//                )
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void searchTrainingProgramsGivenValidRequestReturnsSuccess() throws Exception {
//        SearchRequestWrapper requestWrapper = new SearchRequestWrapper();
//        PageRequestDto searchRequest = new PageRequestDto();
//        searchRequest.setKeyword("Test Keyword");
//        searchRequest.setSearchType("name");
//        requestWrapper.setSearchRequest(searchRequest);
//
//        TrainingProgramRequest trainingProgramRequest = new TrainingProgramRequest();
//        trainingProgramRequest.setStatus("ACTIVE");
//        requestWrapper.setTrainingProgramRequest(trainingProgramRequest);
//
//        Page<TrainingProgramRequest> searchResults = new PageImpl<>(List.of(
//                trainingProgramRequest
//        ));
//
//        String requestWrapperJson = objectMapper.writeValueAsString(requestWrapper);
//
//        when(trainingProgramService.searchTrainingProgram(
//                requestWrapper.getSearchRequest().getKeyword(),
//                requestWrapper.getSearchRequest().getSearchType(),
//                requestWrapper.getSearchRequest()))
//                .thenReturn(searchResults);
//
//        mockMvc.perform(
//                        post("/api/trainingProgram/search")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(requestWrapperJson)
//                )
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void createTrainingProgramGivenIdExistsReturnsOk() throws Exception {
//        TrainingProgramCreateRequest trainingProgram = TrainingProgramCreateRequest.builder()
//                .status("Drafting")
//                .trainingProgramCode("a003")
//                .build();
//        String trainingProgramJson = objectMapper.writeValueAsString(trainingProgram);
//
//        when(trainingProgramService.createTrainingProgram(trainingProgram)).thenReturn(1);
//
//        mockMvc.perform(
//                        post("/api/trainingProgram/create")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(trainingProgramJson)
//
//                )
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    public void testCreateTrainingProgramGivenTrainingProgramIsNullReturnsNotFound() {
//        ResponseEntity<ResponseObject> responseEntity = trainingProgramController.createTrainingProgram(null);
//
//        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//        assertEquals(1, responseEntity.getBody().getCode());
//        assertEquals("Training program empty or null", responseEntity.getBody().getMessage());
//        assertEquals(null, responseEntity.getBody().getData());
//    }
//
//    @Test
//    void createTrainingProgramFailureReturnsBadRequest() throws Exception {
//        TrainingProgramCreateRequest trainingProgram = TrainingProgramCreateRequest.builder()
//                .status("Drafting")
//                .trainingProgramCode("a003")
//                .build();
//        String trainingProgramJson = objectMapper.writeValueAsString(trainingProgram);
//
//        when(trainingProgramService.createTrainingProgram(trainingProgram)).thenReturn(0);
//
//        mockMvc.perform(
//                        post("/api/trainingProgram/create")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(trainingProgramJson)
//                )
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void createTrainingProgramWithExceptionThrowsBadRequestException() throws Exception {
//        TrainingProgramCreateRequest trainingProgram = new TrainingProgramCreateRequest();
//
//        when(trainingProgramService.createTrainingProgram(trainingProgram)).thenThrow(BadRequestException.class);
//
//        MockHttpServletResponse response = mockMvc.perform(
//                        post("/api/trainingProgram/create")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(trainingProgram))
//                )
//                .andReturn().getResponse();
//
//        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
//    }
//
//    @Test
//    void duplicateTrainingProgramWithValidRequestReturnsOk() throws Exception {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(1);
//
//        TrainingProgram trainingProgram = TrainingProgram.builder()
//                .trainingProgramId(1)
//                .build();
//
//        when(trainingProgramService.duplicateTrainingProgram(getIDRequest.getId())).thenReturn(trainingProgram);
//
//        mockMvc.perform(
//                        post("/api/trainingProgram/duplicate")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(getIDRequest))
//                )
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void duplicateTrainingProgramWhenIdIsNullReturnsNotFound() throws Exception {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(null);
//
//        when(trainingProgramService.duplicateTrainingProgram(getIDRequest.getId())).thenReturn(null);
//
//        mockMvc.perform(
//                        post("/api/trainingProgram/duplicate")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(getIDRequest))
//                )
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void duplicateTrainingProgramWhenExceptionIsThrownThrowsBadRequestException() throws Exception {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(1);
//
//        when(trainingProgramService.duplicateTrainingProgram(getIDRequest.getId())).thenThrow(BadRequestException.class);
//
//        MockHttpServletResponse response = mockMvc.perform(
//                        post("/api/trainingProgram/duplicate")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(getIDRequest))
//                )
//                .andReturn().getResponse();
//
//        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
//    }
//
//    @Test
//    void duplicateTrainingProgramWhenDuplicateFailsReturnsBadRequest() throws Exception {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(2);
//
//        when(trainingProgramService.duplicateTrainingProgram(getIDRequest.getId())).thenReturn(null);
//
//        mockMvc.perform(
//                        post("/api/trainingProgram/duplicate")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(getIDRequest))
//                )
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void getTrainingProgramsWithStatusActiveWithValidRequestReturnsOk() throws Exception {
//        PageRequestDto dto = new PageRequestDto();
//        dto.setKeyword("Java");
//
//        List<TrainingProgramRequest> trainingProgramRequests = List.of(new TrainingProgramRequest());
//
//        when(trainingProgramService.getTrainingProgramsWithStatusActive(dto.getKeyword())).thenReturn(trainingProgramRequests);
//
//        MockHttpServletResponse response = mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/trainingProgram/active")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(dto))
//                )
//                .andReturn().getResponse();
//
//        assertEquals(HttpStatus.OK.value(), response.getStatus());
//        assertEquals(objectMapper.writeValueAsString(trainingProgramRequests), response.getContentAsString());
//    }
//
//    @Test
//    void deactivateTrainingProgramWithValidRequestReturnsOk() throws Exception {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(1);
//
//        when(trainingProgramService.deactivateTrainingProgram(getIDRequest.getId())).thenReturn(1);
//
//        mockMvc.perform(
//                        post("/api/trainingProgram/deactivate")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(getIDRequest))
//                )
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void deactivateTrainingProgramWithNullIdReturnsNotFound() {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(null);
//        ResponseEntity<ResponseObject> responseEntity = trainingProgramController.deactivateTrainingProgram(getIDRequest);
//
//        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//        assertEquals(1, responseEntity.getBody().getCode());
//        assertEquals(null, responseEntity.getBody().getData());
//    }
//
//    @Test
//    public void deactivateTrainingProgramWithTrainingProgramNotFoundReturnsBadRequest() throws Exception {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(1);
//
//        when(trainingProgramService.deactivateTrainingProgram(getIDRequest.getId())).thenReturn(0);
//
//        mockMvc.perform(
//                        post("/api/trainingProgram/deactivate")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(getIDRequest))
//                )
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void deactivateTrainingProgramWithThrownExceptionThrowsBadRequestException() throws Exception {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(1);
//
//        when(trainingProgramService.deactivateTrainingProgram(getIDRequest.getId())).thenThrow(BadRequestException.class);
//
//        MockHttpServletResponse response = mockMvc.perform(
//                        post("/api/trainingProgram/deactivate")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(getIDRequest))
//                )
//                .andReturn().getResponse();
//
//        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
//    }
//
//    @Test
//    void activateTrainingProgramWithValidRequestReturnsOk() throws Exception {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(1);
//
//        when(trainingProgramService.activateTrainingProgram(getIDRequest.getId())).thenReturn(1);
//
//        mockMvc.perform(
//                        post("/api/trainingProgram/activate")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(getIDRequest))
//                )
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void activateTrainingProgramWithNullIdReturnsNotFound() {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(null);
//        ResponseEntity<ResponseObject> responseEntity = trainingProgramController.activateTrainingProgram(getIDRequest);
//
//        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//        assertEquals(1, responseEntity.getBody().getCode());
//        assertEquals(null, responseEntity.getBody().getData());
//    }
//
//    @Test
//    public void activateTrainingProgramWithTrainingProgramNotFoundReturnsBadRequest() throws Exception {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(1);
//
//        when(trainingProgramService.activateTrainingProgram(getIDRequest.getId())).thenReturn(0);
//
//        mockMvc.perform(
//                        post("/api/trainingProgram/activate")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(getIDRequest))
//                )
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void activateTrainingProgramWithThrownExceptionThrowsBadRequestException() throws Exception {
//        GetIDRequest getIDRequest = new GetIDRequest();
//        getIDRequest.setId(1);
//
//        when(trainingProgramService.activateTrainingProgram(getIDRequest.getId())).thenThrow(BadRequestException.class);
//
//        MockHttpServletResponse response = mockMvc.perform(
//                        post("/api/trainingProgram/activate")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(getIDRequest))
//                )
//                .andReturn().getResponse();
//
//        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
//    }
//
//    @Test
//    public void updateTrainingProgramWithValidRequestReturnsOk() throws Exception {
//        TrainingProgramCreateRequest trainingProgramCreateRequest = TrainingProgramCreateRequest.builder()
//                .id(1)
//                .name("hello")
//                .build();
//
//        TrainingProgram trainingProgram = TrainingProgram.builder()
//                .trainingProgramId(1)
//                .name("hello")
//                .build();
//
//        mockMvc.perform(
//                        post("/api/trainingProgram/update")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(trainingProgramCreateRequest))
//                )
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void updateTrainingProgramGivenTrainingProgramNullReturnsNotFound() throws Exception {
//        TrainingProgramUpdateRequest trainingProgram = null;
//
//        ResponseEntity<ResponseObject> responseEntity = trainingProgramController.updateTrainingProgram(trainingProgram);
//
//        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//        assertEquals(1, responseEntity.getBody().getCode());
//        assertEquals(null, responseEntity.getBody().getData());
//    }
//
//    @Test
//    public void updateTrainingProgramWhenExceptionIsThrownThrowsBadRequestException() throws Exception {
//        TrainingProgramUpdateRequest trainingProgram = TrainingProgramUpdateRequest.builder()
//                .id(1)
//                .name("hello")
//                .build();
//
//        when(trainingProgramService.updateTrainingProgram(trainingProgram)).thenThrow(BadRequestException.class);
//
//        MockHttpServletResponse response = mockMvc.perform(
//                        post("/api/trainingProgram/update")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(trainingProgram))
//                )
//                .andReturn().getResponse();
//
//        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
//    }
//
//    @Test
//    public void importFileGivenValidFileReturnsSuccess() throws Exception {
//        // Given
//        MockMultipartFile file = new MockMultipartFile("file", "training_programs.csv", MediaType.TEXT_PLAIN_VALUE, "name1,name2,name3".getBytes());
//
//        // When
//        when(trainingProgramService.importFile(file)).thenReturn(1);
//
//        // Then
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/trainingProgram/import")
//                        .file(file))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Uploaded the file successfully: training_programs.csv"));
//    }
//
//    @Test
//    public void importFileGivenInvalidFileReturnsBadRequest() throws Exception {
//        // Given
//        MockMultipartFile file = new MockMultipartFile("file", "invalid_file.txt", MediaType.TEXT_PLAIN_VALUE, "invalid data".getBytes());
//
//        // When
//        when(trainingProgramService.importFile(file)).thenReturn(0);
//
//        // Then
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/trainingProgram/import")
//                        .file(file))
//                .andExpect(status().isBadRequest());
//    }
//
////    @Test
////    public void importFileGivenDuplicateRecordsReturnsConflict() throws Exception {
////        // Given
////        MockMultipartFile file = new MockMultipartFile("file", "training_programs.csv", MediaType.TEXT_PLAIN_VALUE, "name1,name2,name3".getBytes());
////
////        // When
////        when(trainingProgramService.importFile(file)).thenThrow(new DuplicateRecordException("Duplicate records found"));
////
////        // Then
////        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/trainingProgram/import")
////                        .file(file))
////                .andExpect(status().isConflict());
////    }
//
//    @Test
//    public void importFileGivenFileImportFailsReturnsBadRequest() throws Exception {
//        // Given
//        MockMultipartFile file = new MockMultipartFile("file", "training_programs.csv", MediaType.TEXT_PLAIN_VALUE, "name1,name2,name3".getBytes());
//
//        // When
//        when(trainingProgramService.importFile(file)).thenThrow(InternalServerError.class);
//
//        // Then
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/trainingProgram/import")
//                        .file(file))
//                .andExpect(status().isBadRequest());
//    }
//}