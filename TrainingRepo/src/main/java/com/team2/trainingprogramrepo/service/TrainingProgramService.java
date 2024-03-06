package com.team2.trainingprogramrepo.service;

import com.team2.trainingprogramrepo.config.SqlDateConverter;
import com.team2.trainingprogramrepo.dto.*;
import com.team2.trainingprogramrepo.entity.TrainingProgram;
import com.team2.trainingprogramrepo.entity.TrainingSyllabus;
import com.team2.trainingprogramrepo.exception.BadRequestException;
import com.team2.trainingprogramrepo.exception.DateTimeException;
import com.team2.trainingprogramrepo.exception.ResourceNotFoundException;
import com.team2.trainingprogramrepo.exception.StatusNotFoundException;
import com.team2.trainingprogramrepo.repository.TrainingProgramRepository;
import com.team2.trainingprogramrepo.repository.TrainingSyllabusRepository;
import com.team2.trainingprogramrepo.request.*;
import com.team2.trainingprogramrepo.response.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrainingProgramService implements ITrainingProgramService {

    private static final Logger LOGGER = LogManager.getLogger(TrainingProgramService.class);

    @Autowired
    private TrainingProgramRepository trainingProgramRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Properties properties;

    @Autowired
    private TrainingSyllabusRepository trainingSyllabusRepository;


    @Override
    public List<TrainingProgram> getAllTrainingPrograms() {
        return trainingProgramRepository.findAll();
    }


    public Training_SyllabusResponse findTrainingSyllabusByProgramId(Integer programId) {
        List<TrainingSyllabus> list = trainingProgramRepository.findTrainingSyllabusByProgramId(programId);
        List<OrderResponse> listOrder = new ArrayList<>();
        for (TrainingSyllabus ts : list
        ) {
            listOrder.add(OrderResponse.builder()
                    .syllabusId(ts.getSyllabusId())
                    .order(ts.getOrderNumber())
                    .build());
        }

        return Training_SyllabusResponse.builder()
                .program(list.get(0).getTrainingProgram())
                .syllabusList(listOrder)
                .build();
    }

    @Override
    public List<ClassDto> getClassesByTrainingProgramId(Integer trainingProgramId) {
//        ResponseEntity<List> responseEntity = restTemplate.getForEntity("http://class-api/class-management/api/class/list",
//                List.class);
        if (trainingProgramId == null) return null;

        ClassPageRequestDto classPageRequestDto = new ClassPageRequestDto();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ResponseObject> response = restTemplate
                .postForEntity(properties.getProperty("class-service-url") + "/admin/list", classPageRequestDto, ResponseObject.class);

        ResponseObject responseObject = response.getBody();
        Map<String, ClassDto> classMap = (Map<String, ClassDto>) responseObject.getData();
        List<ClassDto> classDtoList = new ArrayList<>(classMap.values());
        LOGGER.info("Get classes successfully");

        return classDtoList;
    }

    public List<SyllabusDto> getSyllabusesByTrainingProgramId(Integer trainingProgramId) {
        if (trainingProgramId == null) return null;

        TrainingProgram trainingProgram = trainingProgramRepository.findById(trainingProgramId).orElse(null);

        if (trainingProgram == null) throw new IllegalStateException("No training program found");

        PagingSyllabusDto pagingSyllabusDto = new PagingSyllabusDto();
        pagingSyllabusDto.setPageNo(0);
        pagingSyllabusDto.setPageSize(10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        PagingSyllabusDto pagingSyllabusResponse = restTemplate
                .postForObject(properties.getProperty("syllabus-service-url") + "/listAll", pagingSyllabusDto, PagingSyllabusDto.class);

        List<SyllabusDto> syllabusList = pagingSyllabusResponse.getContent();

        List<Long> syllabusIds = trainingProgramRepository.findTrainingSyllabusByProgramId(trainingProgramId).stream()
                .map(trainingSyllabus -> trainingSyllabus.getSyllabusId())
                .collect(Collectors.toList());

        return syllabusList.stream()
                .filter(syllabusDto -> syllabusIds.contains(syllabusDto.getId()))
                .collect(Collectors.toList());

    }

    @Override
    public int createTrainingProgram(TrainingProgramCreateRequest trainingProgramCreateRequest) {
        if (trainingProgramCreateRequest == null) return 0;
        String name = trainingProgramCreateRequest.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or empty.");
        }


        if (trainingProgramCreateRequest.getStatus() == null || trainingProgramCreateRequest.getStatus().isBlank())
            trainingProgramCreateRequest.setStatus("PLANNING");

        if (!validateStatus(trainingProgramCreateRequest.getStatus())) return 0;

        Date currentDate = new Date(System.currentTimeMillis());
        trainingProgramCreateRequest.setCreatedDate(currentDate);
        trainingProgramCreateRequest.setModifiedDate(currentDate);
        trainingProgramCreateRequest.setModifiedBy(trainingProgramCreateRequest.getCreatedBy());

        // Generate trainingProgramCode based on name
        String trainingProgramCode = generateTrainingProgramCode(name);
        trainingProgramCreateRequest.setTrainingProgramCode(trainingProgramCode);

        try {
            TrainingProgram trainingProgram = mapToEntity(trainingProgramCreateRequest);
            trainingProgramRepository.save(trainingProgram);

            trainingProgramCreateRequest.getSyllabusList()
                    .forEach(syllabusRequest -> {
                        TrainingSyllabus trainingSyllabus = TrainingSyllabus.builder()
                                .orderNumber(syllabusRequest.getOrder())
                                .syllabusId(syllabusRequest.getSyllabusId())
                                .trainingProgram(trainingProgram)
                                .build();

                        trainingSyllabusRepository.save(trainingSyllabus);
                    });

            LOGGER.info("Create class successfully");

            return 1;
        } catch (Exception e) {
            LOGGER.error("Training program creation failed");

            throw new IllegalStateException("Training program save failed");
        }
    }


    private String generateTrainingProgramCode(String name) {
        StringBuilder codeBuilder = new StringBuilder();

        boolean hasUppercaseOrSpecialChar = false;

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c) || isSpecialCharacter(c)) {
                codeBuilder.append(c);
                hasUppercaseOrSpecialChar = true;
            }
        }

        if (!hasUppercaseOrSpecialChar) {
            char firstChar = Character.toUpperCase(name.charAt(0));
            codeBuilder.append(firstChar);
        }

        long timestamp = System.currentTimeMillis();
        codeBuilder.append("_").append(timestamp);

        if (!isTrainingProgramCodeExists(codeBuilder.toString())) {
            return codeBuilder.toString();
        } else {
            throw new IllegalArgumentException("The generated code is not unique. Please try again.");
        }
    }

    private boolean isSpecialCharacter(char c) {
        String specialCharacters = "!@#$%^&*().";
        return specialCharacters.indexOf(c) != -1;
    }

    private boolean isTrainingProgramCodeExists(String trainingProgramCode) {

        List<TrainingProgram> trainingProgramList = trainingProgramRepository.findAll();
        for (TrainingProgram trainingProgram : trainingProgramList) {
            if (trainingProgram.getTrainingProgramCode().equals(trainingProgramCode)) {
                return true;
            }
        }

        return false;
    }


    private TrainingProgram mapToEntity(TrainingProgramCreateRequest trainingProgramCreateRequest) {
        TrainingProgram trainingProgram = TrainingProgram.builder()
                .trainingProgramCode(trainingProgramCreateRequest.getTrainingProgramCode())
                .name(trainingProgramCreateRequest.getName())
                .createdDate(trainingProgramCreateRequest.getCreatedDate())
                .modifiedBy(trainingProgramCreateRequest.getModifiedBy())
                .modifiedDate(trainingProgramCreateRequest.getModifiedDate())
                .createdBy(trainingProgramCreateRequest.getCreatedBy())
                .topicCode(trainingProgramCreateRequest.getTopicCode())
                .status(trainingProgramCreateRequest.getStatus())
                .build();

        return trainingProgram;
    }

    @Override
    public TrainingProgram duplicateTrainingProgram(Integer trainingProgramId) {
        if (trainingProgramId == null) return null;

        TrainingProgram existedTrainingProgram = trainingProgramRepository.findById(trainingProgramId)
                .orElseThrow(() -> new ResourceNotFoundException("Training program with id " + trainingProgramId + " not found"));

        TrainingProgram duplicatedTrainingProgram = new TrainingProgram();
        duplicatedTrainingProgram.setTrainingProgramCode(existedTrainingProgram.getTrainingProgramCode());
        duplicatedTrainingProgram.setName(existedTrainingProgram.getName() + " (duplicate)");
        duplicatedTrainingProgram.setCreatedDate(existedTrainingProgram.getCreatedDate());
        duplicatedTrainingProgram.setModifiedDate(existedTrainingProgram.getModifiedDate());
        duplicatedTrainingProgram.setModifiedBy(existedTrainingProgram.getModifiedBy());
        duplicatedTrainingProgram.setTopicCode(existedTrainingProgram.getTopicCode());
        duplicatedTrainingProgram.setStatus(existedTrainingProgram.getStatus());

        trainingProgramRepository.save(duplicatedTrainingProgram);

        LOGGER.info("Duplicate successfully");

        return duplicatedTrainingProgram;
    }

    public void deleteTrainingProgram(Integer trainingProgramId) {
        TrainingProgram trainingProgram = trainingProgramRepository.findById(trainingProgramId).orElseThrow(
                () -> new ResourceNotFoundException("The Training program is not existed")
        );

        if (!trainingProgram.getStatus().equals("INACTIVE")) {
            throw new BadRequestException("The class is in use");
        }
        trainingSyllabusRepository.deleteByTrainingProgram(trainingProgram);

        trainingProgramRepository.deleteById(trainingProgramId);

    }

    @Override
    public int deactivateTrainingProgram(Integer trainingProgramId) {

        try {
            TrainingProgram trainingProgram = trainingProgramRepository.findById(trainingProgramId).get();
            int class_in_use = getTrainingInClass(trainingProgramId);
            if (class_in_use > 0) {
                throw new BadRequestException("The TrainingProgram is in " + class_in_use + " class");
            }
            trainingProgram.setStatus("INACTIVE");

            trainingProgramRepository.save(trainingProgram);

            LOGGER.info("deactivate successfully");

            return 1;
        } catch (Exception e) {
            LOGGER.error("Deactivate failed");

            throw new IllegalStateException("Training program deactivated failed");
        }
    }

    private int getTrainingInClass(Integer trainingProgramId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        GetIDRequest getIdRequest = new GetIDRequest();
        getIdRequest.setId(trainingProgramId);
        String url = properties.getProperty("class-service-url");
        String endpoint = url + "/get_training_in_use_forService";

        HttpEntity<GetIDRequest> requestEntity = new HttpEntity<>(getIdRequest, headers);

        ResponseEntity<Integer> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Integer>() {
                }
        );
        return responseEntity.getBody();
    }

    @Override
    public int activateTrainingProgram(Integer trainingProgramId) {
        if (trainingProgramId == null) return 0;

        try {
            TrainingProgram trainingProgram = trainingProgramRepository.findById(trainingProgramId).get();
            trainingProgram.setStatus("ACTIVE");

            trainingProgramRepository.save(trainingProgram);

            LOGGER.info("Activate successfully");

            return 1;
        } catch (Exception e) {
            LOGGER.error("Activate failed");
            throw new IllegalStateException("Training program activated failed");
        }
    }


    @Override
    public TrainingProgram updateTrainingProgram(TrainingProgramUpdateRequest trainingProgramUpdateRequest) {
        TrainingProgram updatedTrainingProgram = trainingProgramRepository.findById(trainingProgramUpdateRequest.getId())
                .orElseThrow(() -> new IllegalStateException("Training program not found"));

        if (!validateStatus(trainingProgramUpdateRequest.getStatus()))
            throw new IllegalArgumentException("Training program status is not valid");

        updatedTrainingProgram.setName(trainingProgramUpdateRequest.getName());
        updatedTrainingProgram.setModifiedDate(new Date(System.currentTimeMillis()));
        updatedTrainingProgram.setModifiedBy(trainingProgramUpdateRequest.getModifiedBy());
        updatedTrainingProgram.setTopicCode(trainingProgramUpdateRequest.getTopicCode());
        updatedTrainingProgram.setStatus(trainingProgramUpdateRequest.getStatus());
        trainingProgramRepository.save(updatedTrainingProgram);
        trainingSyllabusRepository.deleteByTrainingProgram(updatedTrainingProgram);

        trainingProgramUpdateRequest.getSyllabusList().stream()
                .forEach(syllabusRequest -> {
                    TrainingSyllabus trainingSyllabus = TrainingSyllabus.builder()
                            .orderNumber(syllabusRequest.getOrder())
                            .syllabusId(syllabusRequest.getSyllabusId())
                            .trainingProgram(updatedTrainingProgram)
                            .build();

                    trainingSyllabusRepository.save(trainingSyllabus);
                });

        LOGGER.info("Update successfully");

        return updatedTrainingProgram;
    }

    @Override
    public List<TrainingProgram> importTrainingProgramFromFile(InputStream is) throws IOException, ParseException {
        try (
                BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader,
                        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())
        ) {

            List<TrainingProgram> trainingProgramList = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            SqlDateConverter sqlDateConverter = new SqlDateConverter();

            int count = 0;

            for (CSVRecord csvRecord : csvRecords) {
                ++count;

                String name = csvRecord.get("name");
                Date createdDate = sqlDateConverter.convert(csvRecord.get("createdDate"));
                Date modifiedDate = sqlDateConverter.convert(csvRecord.get("modifiedDate"));
                String createdBy = csvRecord.get("createdBy");
                String modifiedBy = csvRecord.get("modifiedBy");
                String topicCode = csvRecord.get("topicCode");
                String status = csvRecord.get("status");

                if (name == null || name.isBlank()) {
                    throw new IllegalArgumentException("Name must not be null or empty.");
                }

                if (modifiedDate.before(createdDate)) {
                    int lineNumber = count + 1;
                    throw new DateTimeException("Modified Date cannot be before Created Date at line: " + lineNumber);
                }

                if (!validateStatus(status))
                    throw new StatusNotFoundException("Status is not valid");


                Integer id = getLastTrainingProgramId() + count;

                TrainingProgram trainingProgram = TrainingProgram.builder()
                        .trainingProgramId(id)
                        .trainingProgramCode(generateTrainingProgramCode(name))
                        .name(name)
                        .createdDate(createdDate)
                        .modifiedDate(modifiedDate)
                        .createdBy(createdBy)
                        .modifiedBy(modifiedBy)
                        .topicCode(topicCode)
                        .status(status)
                        .build();

                trainingProgramList.add(trainingProgram);

            }

            LOGGER.info("Import file successfully");

            return trainingProgramList;
        }

    }

    @Override
    public int importFile(MultipartFile file) {

        if (!file.getContentType().equals("text/csv")) return 0;

        try {
            List<TrainingProgram> trainingProgram = importTrainingProgramFromFile(file.getInputStream());
            trainingProgramRepository.saveAll(trainingProgram);

            LOGGER.info("Import file successfully");

            return 1;
        } catch (IOException e) {
            LOGGER.error("Import Fail");

            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        } catch (ParseException e) {
            LOGGER.error("Import Fail");

            throw new RuntimeException(e);
        }
    }

    public Integer getLastTrainingProgramId() {
        TrainingProgram trainingProgram = trainingProgramRepository.findTopByOrderByTrainingProgramIdDesc();

        if (trainingProgram == null) return 0;

        Integer maxTrainingProgramId = trainingProgram.getTrainingProgramId();

        return maxTrainingProgramId;
    }

    public List<TrainingProgramRequest> getTrainingProgramsWithStatusActive(String keyword) {
        Specification<TrainingProgram> spec = Specification.where((root, query, builder) -> builder.equal(root.get("status"), "ACTIVE"));

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, builder) -> builder.like(root.get("name"), "%" + keyword + "%"));
        }

        List<TrainingProgram> trainingPrograms = trainingProgramRepository.findAll(spec);
        return mapToTrainingProgramDtotoList(trainingPrograms);
    }

    //    public Page<TrainingProgramRequest> searchTrainingProgram(String keyword, String searchType, PageRequestDto dto) {
//        Specification<TrainingProgram> spec = Specification.where(null);
//        Pageable pageable = PageRequest.of(dto.getPageNo(), dto.getPageSize(), Sort.by(dto.getSort().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC, dto.getSortByColumn()));
//
//
//        if (keyword != null && !keyword.isEmpty()) {
//            if ("name".equalsIgnoreCase(searchType)) {
//                spec = spec.and((root, query, builder) -> builder.like(root.get("name"), "%" + keyword + "%"));
//            }
//        }
//
//        Page<TrainingProgram> searchResults = trainingProgramRepository.findAll(spec, pageable);
//        List<TrainingProgramRequest> trainingProgramDtoList = mapToTrainingProgramDtotoList(searchResults.getContent());
//
//        return new PageImpl<>(trainingProgramDtoList);
//    }
    public Page<TrainingProgramRequest> searchTrainingProgram(String keyword, String searchType, PageRequestDto dto) throws ParseException {
        Specification<TrainingProgram> spec = Specification.where(null);
        Pageable pageable = PageRequest.of(
                dto.getPageNo(),
                dto.getPageSize(),
                Sort.by(dto.getSort().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC,
                        dto.getSortByColumn())
        );

        if (keyword != null && !keyword.isEmpty()) {
            if ("name".equalsIgnoreCase(searchType)) {
                spec = spec.and((root, query, builder) -> builder.like(root.get("name"), "%" + keyword + "%"));
            }
        }

        Page<TrainingProgram> searchResults = trainingProgramRepository.findAll(spec, pageable);
        List<TrainingProgramRequest> trainingProgramDtoList = mapToTrainingProgramDtotoList(searchResults.getContent());

        return new PageImpl<>(trainingProgramDtoList, pageable, searchResults.getTotalElements());
    }

    public Page<TrainingProgramRequest> getAllTrainingPrograms(PageRequestDto dto) {
        Pageable pageable = PageRequest.of(
                dto.getPageNo(),
                dto.getPageSize(),
                Sort.by(dto.getSort().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC,
                        dto.getSortByColumn())
        );
        Page<TrainingProgram> trainingProgramList = trainingProgramRepository.findAll(pageable);
        List<TrainingProgramRequest> trainingProgramDtoList = mapToTrainingProgramDtotoList(trainingProgramList.getContent());

        return new PageImpl<>(trainingProgramDtoList, pageable, trainingProgramList.getTotalElements());
    }

    public UserDto getUserDetailsByUserId(Integer userId) {

        return null;
    }

    public SyllabusDetailDto getSyllabusDetailsBySyllabusId(Integer syllabusId) {
        if (syllabusId == null) return null;

        GetIDRequest getIDRequest = new GetIDRequest();
        getIDRequest.setId(syllabusId);

        SyllabusDetailDto syllabusDetailDto = restTemplate.postForObject(properties.getProperty("syllabus-service-url") + "/syllabusDetail",
                getIDRequest, SyllabusDetailDto.class);

        return syllabusDetailDto;
    }

    public ClassResponse getClassDetailsByClassId(Integer classId) {
        if (classId == null) return null;

        GetIDRequest getIDRequest = new GetIDRequest();
        getIDRequest.setId(classId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ClassResponse> response = restTemplate.postForEntity(
                properties.getProperty("class-service-url") + "/class-detail",
                getIDRequest, ClassResponse.class);

        ClassResponse classDto = response.getBody();

        return classDto;
    }

    public TrainingProgramRequest getTrainingDetailsByTrainingProgramId(Integer trainingProgramId) {
        return null;
    }

    private List<TrainingProgramRequest> mapToTrainingProgramDtotoList(List<TrainingProgram> trainingPrograms) {
        if (trainingPrograms == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        if (trainingPrograms.isEmpty()) {
            return Collections.emptyList();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(properties.getProperty("date-format"));
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return trainingPrograms.stream().map(trainingProgram -> {
            TrainingProgramRequest trainingProgramRequest = new TrainingProgramRequest();
            trainingProgramRequest.setTrainingProgramId(trainingProgram.getTrainingProgramId());
            trainingProgramRequest.setTrainingProgramCode(trainingProgram.getTrainingProgramCode());
            trainingProgramRequest.setName(trainingProgram.getName());
            trainingProgramRequest.setStatus(trainingProgram.getStatus());
            trainingProgramRequest.setCreatedBy(trainingProgram.getCreatedBy());
            trainingProgramRequest.setModifiedBy(trainingProgram.getModifiedBy());
            trainingProgramRequest.setTopicCode(trainingProgram.getTopicCode());

            if (trainingProgram.getCreatedDate() != null) {
                trainingProgramRequest.setCreatedDate(dateFormat.format(trainingProgram.getCreatedDate()));
            } else {
                trainingProgramRequest.setCreatedDate("N/A");
            }

            if (trainingProgram.getModifiedDate() != null) {
                trainingProgramRequest.setModifiedDate(dateFormat.format(trainingProgram.getModifiedDate()));
            } else {
                trainingProgramRequest.setModifiedDate("N/A");
            }

            return trainingProgramRequest;
        }).collect(Collectors.toList());
    }

    private boolean validateStatus(String status) {
        List<String> validStatus = List.of("ACTIVE", "INACTIVE", "PLANNING");

        return validStatus.contains(status);
    }

    public TrainingProgram getTrainingDetailForClass(GetIDRequest request) {
        return trainingProgramRepository.findById(request.getId()).orElse(null);

    }
}
