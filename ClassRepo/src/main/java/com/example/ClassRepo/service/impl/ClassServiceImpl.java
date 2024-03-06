package com.example.ClassRepo.service.impl;

import com.example.ClassRepo.dto.ClassDto;
import com.example.ClassRepo.dto.PagingClassDto;
import com.example.ClassRepo.entity.*;
import com.example.ClassRepo.entity.Class;
import com.example.ClassRepo.enums.Status;
import com.example.ClassRepo.exceptions.BadRequestException;
import com.example.ClassRepo.exceptions.ClassDoesNotExistException;
import com.example.ClassRepo.exceptions.ResourceNotFoundException;
import com.example.ClassRepo.exceptions.SlotDoesNotExistException;
import com.example.ClassRepo.reponses.*;
import com.example.ClassRepo.repository.ClassRepository;
import com.example.ClassRepo.repository.TrainingForClassRepository;
import com.example.ClassRepo.request.*;
import com.example.ClassRepo.service.ClassService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    private ClassRepository repository;
    @Autowired
    private SlotServiceImpl slotService;
    @Autowired
    private LocationServiceImpl locationService;
    @Autowired
    private FSUServiceImpl fsuService;
    @Autowired
    private Properties properties;
    @Autowired
    private TrainingForClassRepository trainingForClassRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void createClass(ClassCreateRequest request) throws ParseException, SlotDoesNotExistException, JsonProcessingException, ResourceNotFoundException {
        if(request.getTrainingForClassRequest().getTrainingProgramID() == null){
            throw new BadRequestException("Training can not be empty");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(properties.getProperty("date"));
        validateClass(request.getClassName());
        Date currentDate = new Date();
        Date createAt = new Date();
        try {
            String formattedDate = sdf.format(currentDate);
            createAt = sdf.parse(formattedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Location location = locationService.getLocationsByLocation(request.getLocation());
        FSU fsu = fsuService.getFSUsByFsu(request.getFSU());
        if (fsu.getId() == null) {
            throw new ResourceNotFoundException("FSU does not exist");
        }
        Date startDate = sdf.parse(request.getStartDate());
        Date endDate = sdf.parse(request.getEndDate());
        validateDate(startDate, endDate);
        Slot slot = slotService.getSlotByClassTime(request.getClassTime());

        if (slot == null) {
            throw new SlotDoesNotExistException();
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request.getSchedule());

        var classroom = Class.builder()
                .className(request.getClassName())
                .classCode(request.getClassCode())
                .createBy(request.getCreateBy())
                .createDate(createAt)
                .classTime(slot)
                .admin_id(request.getAdmin_id())
                .startDate(startDate)
                .endDate(endDate)
                .durationInDays(request.getSchedule().size())
                .schedule(json)
                .status(Status.PLANNING)
                .location(location)
                .fsu(fsu)
                .build();
        Class savedClass = repository.save(classroom);

        for (User_SyllabusRequest us: request.getTrainingForClassRequest().getUserSyllabus()
             ) {
            trainingForClassRepository.save(
                    TrainingForClass.builder()
                            .userId(us.getUser_id())
                            .myClass(savedClass)
                            .syllabusId(us.getSyllabus_id())
                            .trainingProgramId(request.getTrainingForClassRequest().getTrainingProgramID())
                            .build()
            );
        }
    }

    @Override
    public Page<ClassDto> searchClass(String keyword, String searchType, ClassDto classDto, PagingClassDto searchRequest) {
        Specification<Class> spec = Specification.where(null);
        Pageable pageable = PageRequest.of(searchRequest.getPageNo(), searchRequest.getPageSize(), Sort.by(searchRequest.getSort().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC, searchRequest.getSortByColumn()));

        if (keyword != null && !keyword.isEmpty()) {
            if ("className".equalsIgnoreCase(searchType)) {
                spec = spec.and((root, query, builder) -> builder.like(root.get("className"), "%" + keyword + "%"));
            }
            if ("classCode".equalsIgnoreCase(searchType) && classDto != null && classDto.getClassCode() != null) {
                spec = spec.and((root, query, builder) -> builder.equal(root.get("classCode"), classDto.getClassCode()));
            }
        }
        Page<Class> searchResults = repository.findAll(spec, pageable);
        List<ClassDto> classDtoList = mapToClassDtoList(searchResults.getContent());

        return new PageImpl<>(classDtoList, pageable, searchResults.getTotalElements());
    }


    public void validateClass(String className) {
        if (repository.existsClassByClassName(className)) {
            throw new BadRequestException("Class " + className + "is existed");
        }
    }


    public void validateDate(Date startDate, Date endDate) {
        if (endDate.before(startDate)) {
            throw new BadRequestException("Time Exception!!!");
        }
    }

    @Override
    public ClassResponse getClassById(GetIdRequest request) throws JsonProcessingException, ClassNotFoundException, ResourceNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        Optional<Class> optionalClass = repository.findById(request.getId());
        if (optionalClass.isPresent()) {
            Class classroom = optionalClass.get();
            return ClassResponse.builder()
                    .class_id(classroom.getClass_id())
                    .className(classroom.getClassName())
                    .classCode(classroom.getClassCode())
                    .approveBy(classroom.getApproveBy())
                    .approveDate(classroom.getApproveDate())
                    .createDate(classroom.getCreateDate())
                    .endDate(classroom.getEndDate())
                    .modifyBy(classroom.getModifyBy())
                    .modifyDate(classroom.getModifyDate())
                    .location(classroom.getLocation().getLocation())
                    .admin_id(getAdmin(classroom.getAdmin_id()))
                    .schedule(mapper.readValue(classroom.getSchedule(), List.class))
                    .classTime(classroom.getClassTime().getClasstime())
                    .status(classroom.getStatus())
                    .startDate(classroom.getStartDate())
                    .durationInDays(classroom.getDurationInDays())
                    .fsu(classroom.getFsu().getName())
                    .createBy(classroom.getCreateBy())
                    .trainingForClass(getTrainingDetailForClass(classroom))
                    .build();
        } else {
            throw new ClassNotFoundException("Class with ID " + request.getId() + " not found");
        }
    }

    @Override
    public Page<ClassDto> getAllClasses(PagingClassDto dto) {
        Pageable pageable = PageRequest.of(
                dto.getPageNo(),
                dto.getPageSize(),
                Sort.by(dto.getSort().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC,
                        dto.getSortByColumn()));
        Page<Class> classList = repository.findAll(pageable);
        List<ClassDto> classDtoList = mapToClassDtoList(classList.getContent());
        return new PageImpl<>(classDtoList, pageable, classList.getTotalElements());
    }


    public List<ClassDto> mapToClassDtoList(List<Class> classes) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(properties.getProperty("date"));
        ObjectMapper mapper = new ObjectMapper();

        return classes.stream().map(classObj -> {
            ClassDto classDto = new ClassDto();

            classDto.setId(classObj.getClass_id());
            classDto.setClassName(classObj.getClassName());
            classDto.setClassCode(classObj.getClassCode());
            if (classObj.getStartDate() != null && classObj.getEndDate() != null) {
                classDto.setEndDate(dateFormat.format(classObj.getEndDate()));
                classDto.setStartDate(dateFormat.format(classObj.getStartDate()));

            } else {
                classDto.setEndDate("N/A");
                classDto.setStartDate("N/A");
            }

            if (classObj.getCreateDate() != null) {
                classDto.setCreateDate(dateFormat.format(classObj.getCreateDate()));
            } else {
                classDto.setCreateDate("N/A");
            }

            if (classObj.getModifyDate() != null) {
                classDto.setModifyDate(dateFormat.format(classObj.getModifyDate()));
            } else {
                classDto.setModifyDate("N/A");
            }
            if (classObj.getApproveDate() != null) {
                classDto.setApproveDate(dateFormat.format(classObj.getApproveDate()));
            } else {
                classDto.setApproveDate("N/A");
            }
            classDto.setCreateBy(classObj.getCreateBy());
            classDto.setApproveBy(classObj.getApproveBy());
            classDto.setModifyBy(classObj.getModifyBy());
            classDto.setDurationInDays(classObj.getDurationInDays());
            classDto.setClassTime(classObj.getClassTime().getClasstime());
            classDto.setStatus(classObj.getStatus());
            classDto.setFsu(classObj.getFsu().getName());
            classDto.setAdmin_id(classObj.getAdmin_id().toString());
//            classDto.setTrainingProgramId(classObj.getTrainingProgramId().toString());
            classDto.setLocation(classObj.getLocation().getLocation());
            try {
                classDto.setSchedule(mapper.readValue(classObj.getSchedule(), List.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            // Set other properties here
            // Ví dụ: classDto.setTrainer_id(classObj.getTrainer_id());

            return classDto;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateClass(ClassUpdateRequest classUpdateRequest) throws ClassDoesNotExistException, ParseException, JsonProcessingException {
        Optional<Class> optionalClass = repository.findById(classUpdateRequest.getClass_id());

        if (optionalClass.isPresent()) {
            Class existedClass = optionalClass.get();
            Slot slot = slotService.getSlotByClassTime(classUpdateRequest.getClassTime());
            Location location = locationService.getLocationsByLocation(classUpdateRequest.getLocation());
            FSU fsu = fsuService.getFSUsByFsu(classUpdateRequest.getFsu());
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(classUpdateRequest.getSchedule());
            SimpleDateFormat sdf = new SimpleDateFormat(properties.getProperty("date"));
            validateDate(sdf.parse(classUpdateRequest.getStartDate()), sdf.parse(classUpdateRequest.getEndDate()));

            if (classUpdateRequest.getClassName() != null) {
                existedClass.setClassName(classUpdateRequest.getClassName());
            }
            if (classUpdateRequest.getClassCode() != null) {
                existedClass.setClassCode(classUpdateRequest.getClassCode());
            }
            if (classUpdateRequest.getModifyBy() != null) {
                existedClass.setModifyBy(classUpdateRequest.getModifyBy());
            }

            if (classUpdateRequest.getClassTime() != null) {
                existedClass.setClassTime(slot);
            }
            if (classUpdateRequest.getLocation() != null) {
                //find in database
                existedClass.setLocation(location);
            }
            if (classUpdateRequest.getFsu() != null) {
                //find in database
                existedClass.setFsu(fsu);
            }
            if (classUpdateRequest.getAdmin_id() != null) {
                existedClass.setAdmin_id(classUpdateRequest.getAdmin_id());
            }

            if (classUpdateRequest.getStartDate() != null) {
                existedClass.setStartDate(sdf.parse(classUpdateRequest.getStartDate()));

            }
            if (classUpdateRequest.getEndDate() != null) {
                existedClass.setEndDate(sdf.parse(classUpdateRequest.getEndDate()));
            }

            if (classUpdateRequest.getSchedule() != null) {
                existedClass.setSchedule(json);
                existedClass.setDurationInDays(classUpdateRequest.getSchedule().size());
            }
            Date currentDate = new Date();
            Date modifiDate = null;
            try {
                String formattedDate = sdf.format(currentDate);
                modifiDate = sdf.parse(formattedDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            existedClass.setModifyDate(modifiDate);
            existedClass.setStatus(Status.PLANNING);
            repository.save(existedClass);
            if (classUpdateRequest.getTrainingForClassList().getTrainingProgramID() != null) {
                List<TrainingForClass> list = trainingForClassRepository.findAllByMyClass(existedClass);
                trainingForClassRepository.deleteAll(list);
                for (User_SyllabusRequest us : classUpdateRequest.getTrainingForClassList().getUserSyllabus()
                ) {
                    trainingForClassRepository.save(
                            TrainingForClass.builder()
                                    .userId(us.getUser_id())
                                    .myClass(existedClass)
                                    .syllabusId(us.getSyllabus_id())
                                    .trainingProgramId(classUpdateRequest.getTrainingForClassList().getTrainingProgramID())
                                    .build()
                    );
                }
            }

        } else {
            throw new ClassDoesNotExistException();
        }
    }


    @Override
    public void UpdateStatus(StatusUpdateRequest request) throws ClassDoesNotExistException, JsonProcessingException {
        Class existingClass = repository.findById(request.getId()).orElseThrow(
                ()-> new ResourceNotFoundException("Class does not exist")
        );
        SimpleDateFormat sdf = new SimpleDateFormat(properties.getProperty("date"));
        Date currentDate = new Date();
        Date modifiDate = null;
        try {
            String formattedDate = sdf.format(currentDate);
            modifiDate = sdf.parse(formattedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (request.getStatus().equalsIgnoreCase(Status.ACTIVE.name())) {
            existingClass.setStatus(Status.ACTIVE);
            existingClass.setApproveBy(request.getBy());
            existingClass.setApproveDate(modifiDate);
        } else if (request.getStatus().equalsIgnoreCase(Status.CLOSED.name())) {
            existingClass.setStatus(Status.CLOSED);
            existingClass.setModifyBy(request.getBy());
            existingClass.setModifyDate(modifiDate);
        } else if (request.getStatus().equalsIgnoreCase(Status.PLANNING.name())) {
            existingClass.setStatus(Status.PLANNING);
            existingClass.setModifyBy(request.getBy());
            existingClass.setModifyDate(modifiDate);
        }else{
            throw new BadRequestException("Status not found");
        }

        repository.save(existingClass);
    }


    public void validateBlank(ClassCreateRequest request) {
        if (request.getClassName() == null || request.getClassName().isEmpty()) {
            throw new IllegalArgumentException("Class Name cannot be blank");
        }
        if (request.getClassCode() == null || request.getClassCode().isEmpty()) {
            throw new IllegalArgumentException("Class Code cannot be blank");
        }
        if (request.getCreateBy() == null || request.getCreateBy().isEmpty()) {
            throw new IllegalArgumentException("Create By cannot be blank");
        }
        if (request.getLocation() == null || request.getLocation().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be blank");
        }
        if (request.getFSU() == null || request.getFSU().isEmpty()) {
            throw new IllegalArgumentException("FSU cannot be blank");
        }
        if (request.getStartDate() == null || request.getStartDate().isEmpty()) {
            throw new IllegalArgumentException("Start Date cannot be blank");
        }
        if (request.getEndDate() == null || request.getEndDate().isEmpty()) {
            throw new IllegalArgumentException("End Date cannot be blank");
        }
        if (request.getClassTime() == null || request.getClassTime().isEmpty()) {
            throw new IllegalArgumentException("Class Time cannot be blank");
        }
        if (request.getSchedule() == null || request.getSchedule().isEmpty()) {
            throw new IllegalArgumentException("Schedule cannot be blank");
        }
        if (request.getAdmin_id() == null) {
            throw new IllegalArgumentException("Admin ID cannot be blank");
        }
    }

    public TrainingForClassResponse getTrainingDetailForClass(Class request) throws ResourceNotFoundException {
        List<TrainingForClass> listTraining = trainingForClassRepository.findAllByMyClass(request);
        if (listTraining.isEmpty()) {
            return null;
        }
        int training_id = 0;
        List<GetIdRequest> listUser = new ArrayList<>();
        List<SyllabusIDRequest> listSyllabus = new ArrayList<>();

        for (int i = 0; i < listTraining.size(); i++){
            GetIdRequest getIdRequest = new GetIdRequest();
            SyllabusIDRequest syllabusIDRequest = new SyllabusIDRequest();
            if (training_id == 0) {
                training_id = listTraining.get(i).getTrainingProgramId();
            }
            getIdRequest.setId(listTraining.get(i).getUserId());
            listUser.add(i, getIdRequest);
            syllabusIDRequest.setId(listTraining.get(i).getSyllabusId());
            listSyllabus.add(i, syllabusIDRequest);
        }
        TrainingProgram trainingProgram = getTrainingProgram(training_id);
        TrainingForClassResponse t = new TrainingForClassResponse();
        t.setTrainingProgram(trainingProgram);
        List<UserDto> user = getUser(listUser);
        List<SyllabusDto> syllabus = getSyllabus(listSyllabus);
        List<User_Syllabus> user_syllabusList = new ArrayList<>();
        if (syllabus.isEmpty()) {
            throw new ResourceNotFoundException("List syllabus not Found");
        }
        for (int i = 0; i < user.size(); i++){
            user_syllabusList.add(
                    User_Syllabus.builder()
                            .user(user.get(i))
                            .syllabus(syllabus.get(i))
                            .build()
            );
        }
        t.setUserSyllabus(user_syllabusList);
        return t;
    }

    private TrainingProgram getTrainingProgram(int trainingId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        GetIdRequest getIdRequest = new GetIdRequest();
        getIdRequest.setId(trainingId);
        String url = properties.getProperty("training");
        String endpoint = url + "/get_training_for_service";

        HttpEntity<GetIdRequest> requestEntity = new HttpEntity<>(getIdRequest, headers);

        ResponseEntity<TrainingProgram> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<TrainingProgram>() {
                }
        );
        return responseEntity.getBody();
    }

    private List<UserDto> getUser(List<GetIdRequest> requests) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = properties.getProperty("user-url");
        String endpoint = url + "/get_profile_for_service";

        HttpEntity<List<GetIdRequest>> requestEntity = new HttpEntity<>(requests, headers);

        ResponseEntity<List<UserDto>> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<UserDto>>() {
                }
        );

        List<UserDto> userList = responseEntity.getBody();

        return userList;
    }
    private UserDto getAdmin(Integer id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = properties.getProperty("user-url");
        String endpoint = url + "/get_profile_admin/" + id;

        HttpEntity<Integer> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<UserDto>() {
                }
        );

        return responseEntity.getBody();
    }

    private List<SyllabusDto> getSyllabus(List<SyllabusIDRequest> requests) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = properties.getProperty("syllabus-service-url");
        String endpoint = url + "/get_listSyllabus_for_service";

        HttpEntity<List<SyllabusIDRequest>> requestEntity = new HttpEntity<>(requests, headers);

        ResponseEntity<List<SyllabusDto>> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<SyllabusDto>>() {
                }
        );

        List<SyllabusDto> list = responseEntity.getBody();

        return list;
    }

    @Override
    public void deleteClass(Integer class_id) throws ClassDoesNotExistException {
        Optional<Class> optionalClass = repository.findById(class_id);
        if (optionalClass.isPresent()) {
            Class myClass = optionalClass.get();
            if (myClass.getStatus() == Status.ACTIVE) {
                throw new BadRequestException("The class " + myClass.getClassName() + " is in use.");
            }
            List<TrainingForClass> list = trainingForClassRepository.findAllByMyClass(myClass);
            if (!list.isEmpty()) {
                trainingForClassRepository.deleteAll(list);
            }
            repository.deleteById(class_id);
        } else {
            throw new ClassDoesNotExistException();
        }
    }

    public Integer getSyllabusInClass(SyllabusIDRequest request) {
        List<TrainingForClass> trainingForClass = trainingForClassRepository.findAllBySyllabusId(request.getId());
        if (trainingForClass.isEmpty()) {
            return 0;
        }
        return trainingForClass.size();
    }

    public Integer getTrainingInUse(GetIdRequest request) {
        List<TrainingForClass> trainingForClass = trainingForClassRepository.findAllByTrainingProgramId(request.getId());
        if (trainingForClass.isEmpty()) {
            return 0;
        }
        return trainingForClass.size();
    }

    public Integer getUserInUse(GetIdRequest request) {
        int result = 0;
        List<Class> list = repository.findAll();
        for (Class clas: list
             ) {
            if(clas.getAdmin_id().equals(request.getId())){
                result += 1;
            }
        }
        List<TrainingForClass> trainingForClass = trainingForClassRepository.findAllByUserId(request.getId());
        result += trainingForClass.size();
        return result;
    }
}

