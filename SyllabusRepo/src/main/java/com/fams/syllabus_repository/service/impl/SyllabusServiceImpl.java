package com.fams.syllabus_repository.service.impl;

import com.fams.syllabus_repository.converter.*;
import com.fams.syllabus_repository.dto.*;
import com.fams.syllabus_repository.entity.*;
import com.fams.syllabus_repository.enums.OutputStandard;
import com.fams.syllabus_repository.enums.PublicStatus;
import com.fams.syllabus_repository.exceptions.BadRequestException;
import com.fams.syllabus_repository.exceptions.SyllabusNotFoundException;
import com.fams.syllabus_repository.repository.*;
import com.fams.syllabus_repository.request.RequestIdDto;
import com.fams.syllabus_repository.response.ResponseObject;
import com.fams.syllabus_repository.s3.S3Bucket;
import com.fams.syllabus_repository.s3.S3Sevice;
import com.fams.syllabus_repository.service.SyllabusService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SyllabusServiceImpl implements SyllabusService {
    @Autowired
    private final SyllabusRepository repository;
    @Autowired
    private final DaySyllabusRepository daySyllabusRepository;
    @Autowired
    private final TrainingUnitRepository trainingUnitRepository;
    @Autowired
    private final TrainingContentRepository trainingContentRepository;
    @Autowired
    private final TrainingMaterialsRepositoty trainingMaterialsRepositoty;
    @Autowired
    private final OtherSyllabusRepository otherSyllabusRepository;
    @Autowired
    private final AssignmentShemeRepository assignmentShemeRepository;
    @Autowired
    private S3Sevice s3Sevice;
    @Autowired
    private S3Bucket s3Bucket;
    @Autowired
    private Properties properties;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public SyllabusDto createAndUpdate(SyllabusDto syllabusDto) {
        Syllabus syllabus;
        if (syllabusDto.getId() != null) {
            Optional<Syllabus> existingSyllabus = repository.findById(syllabusDto.getId());
            if (existingSyllabus.isPresent()) {
                Syllabus oldSyllabus = existingSyllabus.get();
                Syllabus newSyllabus = SyllabusConverter.toEntity(syllabusDto, oldSyllabus);
                if(oldSyllabus.getPublicStatus().equals(PublicStatus.ACTIVE.getValue()) && newSyllabus.getPublicStatus().equals(PublicStatus.INACTIVE.getValue())){
                    int class_in_use = getSyllabusInClass(oldSyllabus.getId());
                    if(class_in_use > 0){
                        throw new BadRequestException("The Syllabus is in " + class_in_use + " class");
                    }
                }
                if (syllabusDto.getSyllabusName() != null) {
                    String[] words = syllabusDto.getSyllabusName().split("\\s+");
                    StringBuilder generatedCode = new StringBuilder();
                    for (String word : words) {
                        String sanitizedWord = word.replaceAll("[^a-zA-Z]", "");
                        if (!sanitizedWord.isEmpty()) {
                            generatedCode.append(sanitizedWord.charAt(0));
                        }
                    }
                    newSyllabus.setSyllabusCode(generatedCode.toString().toUpperCase());
                }
                syllabus = repository.save(newSyllabus);
            } else {
                syllabus = SyllabusConverter.toEntity(syllabusDto);
                if (syllabusDto.getSyllabusName() != null && !syllabusDto.getSyllabusName().isEmpty()) {
                    String[] words = syllabusDto.getSyllabusName().split("\\s+");
                    StringBuilder generatedCode = new StringBuilder();
                    for (String word : words) {
                        String sanitizedWord = word.replaceAll("[^a-zA-Z]", "");
                        if (!sanitizedWord.isEmpty()) {
                            generatedCode.append(sanitizedWord.charAt(0));
                        }
                    }
                    syllabus.setSyllabusCode(generatedCode.toString().toUpperCase());
                }
            }
        } else {
            syllabus = SyllabusConverter.toEntity(syllabusDto);
            if (syllabusDto.getSyllabusName() != null && !syllabusDto.getSyllabusName().isEmpty()) {
                String[] words = syllabusDto.getSyllabusName().split("\\s+");
                StringBuilder generatedCode = new StringBuilder();
                for (String word : words) {
                    String sanitizedWord = word.replaceAll("[^a-zA-Z]", "");
                    if (!sanitizedWord.isEmpty()) {
                        generatedCode.append(sanitizedWord.charAt(0));
                    }
                }
                syllabus.setSyllabusCode(generatedCode.toString().toUpperCase());
            }
        }
        syllabus = repository.save(syllabus);
        return SyllabusConverter.toDTO(syllabus);
    }





    @Override
    public SyllabusDetailDto getSyllabusById(Long id) {
        SyllabusDetailDto syllabusDetailDto = new SyllabusDetailDto();
        Syllabus syllabus = repository.findById(id).orElseThrow(
                ()-> new SyllabusNotFoundException("Syllabus is not found")
        );
        List<SyllabusDto> syllabusDtos = Optional.of(syllabus).stream()
                .map(SyllabusConverter::toDTO)
                .collect(Collectors.toList());
        syllabusDetailDto.setSyllabusDto(syllabusDtos);

        List<DaySyllabus> daySyllabus = daySyllabusRepository.findBySyllabusId(id);
        List<DaySyllabusDto> daySyllabusDtos = daySyllabus.stream()
                .map(DaySyllabusConverter::toDTOCreate)
                .collect(Collectors.toList());
        syllabusDetailDto.setDaySyllabusDto(daySyllabusDtos);

        List<AssignmentSheme> assignmentSchemes = assignmentShemeRepository.findBySyllabusId(id);
        List<AssignmentShemeDto> assignmentScheme = assignmentSchemes.stream()
                .map(AssignmentShemeConverter::toDTO)
                .collect(Collectors.toList());

        List<OtherSyllabus> otherSyllabuses = otherSyllabusRepository.findBySyllabusId(id);
        List<OtherSyllabusDto> otherSyllabus = otherSyllabuses.stream()
                .map(OtherSyllabusConverter::toDTO)
                .collect(Collectors.toList());
        List<OtherListDto> otherListDtos = new ArrayList<>();
        OtherListDto otherListDto = new OtherListDto();
        otherListDto.setAssignmentShemeDtos(assignmentScheme);
        otherListDto.setOtherSyllabusDtos(otherSyllabus);
        otherListDtos.add(otherListDto);

        syllabusDetailDto.setOtherListDto(otherListDtos);

        return syllabusDetailDto;
    }

    @Override
    public PagingSyllabusDto getAllSyllabusAndSearch(int pageNo, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Specification<Syllabus> spec = searchSyllabus(keyword); // Specification to search
        Page<Syllabus> syllabus = repository.findAll(spec, pageable);

        List<Syllabus> listOfSyllabus = syllabus.getContent();
        List<SyllabusDto> content = listOfSyllabus.stream().map(p -> SyllabusConverter.toDTO(p)).collect(Collectors.toList());

//        Set<String> uniqueOutputStandards = new HashSet<>();
//        for (Syllabus syllabusOutputStandard : listOfSyllabus) {
//            uniqueOutputStandards.addAll(syllabusOutputStandard.getOutputStandard());
//        }

        PagingSyllabusDto syllabusPageingDto = new PagingSyllabusDto();
        if (content.isEmpty()) {
            syllabusPageingDto.setMessage("Not found");
        } else {
            syllabusPageingDto.setContent(content);
        }
        syllabusPageingDto.setPageNo(syllabus.getNumber());
        syllabusPageingDto.setPageSize(syllabus.getSize());
        syllabusPageingDto.setTotalElements(syllabus.getTotalElements());
        syllabusPageingDto.setTotalPages(syllabus.getTotalPages());
        syllabusPageingDto.setLast(syllabus.isLast());
        syllabusPageingDto.setSearch(keyword);
//        syllabusPageingDto.setSort(sort.toString());
//        syllabusPageingDto.setFilter(filterRequest);
        return syllabusPageingDto;
    }


    @Override
    public SyllabusDto duplicateSyllabus(Long id) {
        try {
            // Get the initial syllabus information from the database
            Syllabus originalSyllabus = repository.findById(id).orElse(null);

            if (originalSyllabus == null) {
                throw new SyllabusNotFoundException("Original syllabus not found");
            }

            SyllabusDto syllabusDto = SyllabusConverter.toDTO(originalSyllabus);
            Syllabus duplicateSyllabus = SyllabusConverter.toEntity(syllabusDto);

            String originalName = originalSyllabus.getSyllabusName();
            String newName = "Copy of " + originalName;

            String finalName = newName;
            int counter = 1;
            while (repository.existsBySyllabusName(finalName)) {
                finalName = newName +" version"+ " " + counter;
                counter++;
            }

            duplicateSyllabus.setSyllabusName(finalName);
            duplicateSyllabus.setPublicStatus(PublicStatus.DRAFT.getValue());
            // Save the copied version of syllabus to the database
            duplicateSyllabus = repository.save(duplicateSyllabus);

            // Get all the DaySyllabuses related to the original syllabus record
            List<DaySyllabus> originalDaySyllabuses = originalSyllabus.getDaSyllabus();

            // Browse through all DaySyllabus and copy them
            for (DaySyllabus originalDaySyllabus : originalDaySyllabuses) {
                DaySyllabusDto daySyllabusDto = DaySyllabusConverter.toDTO(originalDaySyllabus);
                DaySyllabus duplicateDaySyllabus = DaySyllabusConverter.toEntity(daySyllabusDto);
                // Assign the replicated instance of DaySyllabus to the replicated instance of syllabus
                duplicateDaySyllabus.setSyllabus(duplicateSyllabus);
                // Save the copied version of DaySyllabus to the database
                duplicateDaySyllabus = daySyllabusRepository.save(duplicateDaySyllabus);
                // Get all TrainingUnits related to the original DaySyllabus
                List<TrainingUnit> originalTrainingUnits = originalDaySyllabus.getTrainingUnit();

                // Browse all TrainingUnit and copy them
                for (TrainingUnit originalTrainingUnit : originalTrainingUnits) {
                    TrainingUnitDto trainingUnitDto = TrainingUnitConverter.toDTO(originalTrainingUnit);
                    TrainingUnit duplicateTrainingUnit = TrainingUnitConverter.toEntity(trainingUnitDto);
                    duplicateTrainingUnit.setDaySyllabus(duplicateDaySyllabus);

                    // Save the copied version of TrainingUnit to the database
                    duplicateTrainingUnit = trainingUnitRepository.save(duplicateTrainingUnit);
                    // Get all TrainingContents related to the original TrainingUnit
                    List<TrainingContent> originalTrainingContents = originalTrainingUnit.getTrainingContents();

                    // Browse through all TrainingContent and copy them
                    for (TrainingContent originalTrainingContent : originalTrainingContents) {
                        TrainingContentDto trainingContentDto = TrainingContentConverter.toDto(originalTrainingContent);
                        TrainingContent duplicateTrainingContent = TrainingContentConverter.toEntity(trainingContentDto);
                        List<OutputStandard> duplicateOutputStandard = new ArrayList<>();
                        for (OutputStandard outputStandard : originalTrainingContent.getOutputStandard()) {
                            // Tạo bản sao mới cho từng mục trong @ElementCollection
                            duplicateOutputStandard.add(outputStandard);
                        }
                        duplicateTrainingContent.setOutputStandard(duplicateOutputStandard);
                        //duplicateTrainingContent.setOutputStandard(originalTrainingContent.getOutputStandard());
                        // Assign the copied version of TrainingContent to the copied version of TrainingUnit
                        duplicateTrainingContent.setTrainingUnit(duplicateTrainingUnit);
                        // Save the copied version of TrainingContent to the database
                        duplicateTrainingContent = trainingContentRepository.save(duplicateTrainingContent);

                        List<TrainingMaterials> originalTrainingMaterials = originalTrainingContent.getTrainingMaterials();
                        for (TrainingMaterials originalTrainingMaterial : originalTrainingMaterials) {
                            TrainingMaterialsDto trainingMaterialsDto = TrainingMaterialsConverter.toDTO(originalTrainingMaterial);
                            TrainingMaterials duplicateTrainingMaterials = TrainingMaterialsConverter.toEntity(trainingMaterialsDto);
                            duplicateTrainingMaterials.setTrainingContent(duplicateTrainingContent);
                            duplicateTrainingMaterials = trainingMaterialsRepositoty.save(duplicateTrainingMaterials);
                        }
                    }
                }
            }
            List<OtherSyllabus> originalOtherSyllabus = otherSyllabusRepository.findBySyllabusId(id);
            for (OtherSyllabus otherSyllabus : originalOtherSyllabus) {
                OtherSyllabusDto otherSyllabusDto = OtherSyllabusConverter.toDTO(otherSyllabus);
                OtherSyllabus duplicateOtherSyllabus = OtherSyllabusConverter.toEntity(otherSyllabusDto);
                duplicateOtherSyllabus.setSyllabus(duplicateSyllabus);
                otherSyllabusRepository.save(duplicateOtherSyllabus);
            }
            List<AssignmentSheme> originalAssignmentSheme = assignmentShemeRepository.findBySyllabusId(id);
            for (AssignmentSheme AssignmentSheme : originalAssignmentSheme) {
                AssignmentShemeDto assignmentShemeDto = AssignmentShemeConverter.toDTO(AssignmentSheme);
                AssignmentSheme duplicateAssignmentSheme = AssignmentShemeConverter.toEntity(assignmentShemeDto);
                duplicateAssignmentSheme.setSyllabus(duplicateSyllabus);
                assignmentShemeRepository.save(duplicateAssignmentSheme);
            }
            return SyllabusConverter.toDTO(duplicateSyllabus);
        } catch (Exception ex) {
            throw new SyllabusNotFoundException("Failed to duplicate syllabus");
        }
    }

    @Override
    public void importSyllabusFromCSV(MultipartFile file, String createdBy, String modifiedBy, String choice) {
        if(!isCsvFile(file.getOriginalFilename())){
            throw new BadRequestException("The file should be .csv file");
        }
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVReader csvReader = new CSVReader(reader);
            String[] nextLine;
            csvReader.readNext();

            while ((nextLine = csvReader.readNext()) != null) {
                Syllabus syllabus = new Syllabus();
                // Điền thông tin Syllabus từ dòng CSV
                syllabus.setSyllabusName(nextLine[0]);
                String[] words = nextLine[0].split("\\s+");
                StringBuilder generatedCode = new StringBuilder();
                for (String word : words) {
                    String sanitizedWord = word.replaceAll("[^a-zA-Z]", "");
                    if (!sanitizedWord.isEmpty()) {
                        generatedCode.append(sanitizedWord.charAt(0));
                    }
                }
                syllabus.setSyllabusCode(generatedCode.toString().toUpperCase());
                syllabus.setVersion(nextLine[1]);
                syllabus.setAttendeeNumber(Integer.parseInt(nextLine[2]));
                syllabus.setDuration(Integer.parseInt(nextLine[3]));
                syllabus.setTechnicalRequirement(nextLine[4]);
                syllabus.setCourseObjectives(nextLine[5]);
                syllabus.setLevel(nextLine[6]);
                syllabus.setPublicStatus(PublicStatus.DRAFT.getValue());
                syllabus.setCreatedDate(LocalDate.now());
                syllabus.setModifyDate(LocalDate.now());
                syllabus.setCreatedBy(createdBy);
                syllabus.setModifyBy(modifiedBy);

                if (choice.equals("Replace")) {
                    List<Syllabus> existingSyllabuses = repository.findBySyllabusNameAndVersionAndTechnicalRequirementAndCourseObjectives(
                            syllabus.getSyllabusName(),
                            syllabus.getVersion(),
                            syllabus.getTechnicalRequirement(),
                            syllabus.getCourseObjectives()
                    );

                    if (!existingSyllabuses.isEmpty()) {
                        continue;
                    }
                }
                // save Syllabus to database
                repository.save(syllabus);
            }
        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isCsvFile(String fileName) {
        String fileExtension = StringUtils.getFilenameExtension(fileName);
        return "csv".equalsIgnoreCase(fileExtension);
    }

    @Override
    public Syllabus getSyllabusEntityById(Long id) {
        Optional<Syllabus> syllabusOptional = repository.findById(id);
        return syllabusOptional.orElse(null);
    }

    @Override
    public void deleteSyllabusById(Long id) throws SyllabusNotFoundException {
        Optional<Syllabus> syllabusOptional = repository.findById(id);
        if (syllabusOptional.isPresent()) {
            List<DaySyllabus> existingDay = daySyllabusRepository.findBySyllabusId(syllabusOptional.get().getId());
            for (DaySyllabus d: existingDay
                 ) {
                TrainingUnit trainingUnit = trainingUnitRepository.findTrainingUnitByDaySyllabus(d);
                if(trainingUnit != null){
                    TrainingContent trainingContent = trainingContentRepository.findTrainingContentByTrainingUnit(trainingUnit);
                    if(trainingContent != null){
                        TrainingMaterials materials = trainingMaterialsRepositoty.findTrainingMaterialsByTrainingContent(trainingContent);
                        if(materials != null){
                            s3Sevice.deleteObject(s3Bucket.getCustomer(), materials.getS3Location());
                        }

                    }
                }
                trainingUnitRepository.deleteTrainingUnitByDaySyllabus(d);
            }
            repository.delete(syllabusOptional.get());
        } else {
            throw new SyllabusNotFoundException("Syllabus Not Found");
        }
    }
    @Transactional
    @Override
    public void deleteSyllabus(Long syllabusId) {
        try {
            otherSyllabusRepository.findBySyllabusId(syllabusId);
            assignmentShemeRepository.findBySyllabusId(syllabusId);

            Syllabus syllabus = repository.findById(syllabusId)
                    .orElseThrow(() -> new SyllabusNotFoundException("Syllabus not found"));
            if(syllabus.getPublicStatus().equals(PublicStatus.ACTIVE.name())){
                throw new BadRequestException("The syllabus is in use");
            }
            List<DaySyllabus> daySyllabuses = syllabus.getDaSyllabus();
            for (DaySyllabus daySyllabus : daySyllabuses) {
                List<TrainingUnit> trainingUnits = daySyllabus.getTrainingUnit();
                for (TrainingUnit trainingUnit : trainingUnits) {
                    List<TrainingContent> trainingContents = trainingUnit.getTrainingContents();
                    for (TrainingContent trainingContent : trainingContents) {
                        List<TrainingMaterials> trainingMaterials = trainingContent.getTrainingMaterials();
                        for (TrainingMaterials m: trainingMaterials
                        ) {
                            s3Sevice.deleteObject(s3Bucket.getCustomer(), m.getS3Location());
                        }
                        trainingMaterialsRepositoty.deleteAll(trainingMaterials);
                    }
                    trainingContentRepository.deleteAll(trainingContents);
                }
                trainingUnitRepository.deleteAll(trainingUnits);
            }
            daySyllabusRepository.deleteAll(daySyllabuses);

            repository.delete(syllabus);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int getSyllabusInClass(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestIdDto getIdRequest = new RequestIdDto();
        getIdRequest.setId(id);
        String url = properties.getProperty("class-url");
        String endpoint = url + "/get_syllabus_in_use_forService";

        HttpEntity<RequestIdDto> requestEntity = new HttpEntity<>(getIdRequest, headers);

        ResponseEntity<Integer> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Integer>() {}
        );
        return responseEntity.getBody();
    }

    @Override
    public List<SyllabusDto> getListSyllabusFOrService(List<RequestIdDto> request) {
        List<SyllabusDto> list = new ArrayList<>();
        for (RequestIdDto id: request
        ) {
            Syllabus optional = repository.findById(id.getId()).orElseThrow(
                    ()-> new SyllabusNotFoundException("Syllabus is not found")
            );
            list.add(
                    SyllabusConverter.toDTO(optional)
            );
        }
        return list;
    }

    @Override
    public List<SyllabusDto> getAllSyllabus() {
        List<SyllabusDto> result = new ArrayList<>();
        List<Syllabus> syllabusEntity = repository.findAll();
        for (Syllabus item : syllabusEntity
        ) {
            SyllabusDto dto = SyllabusConverter.toDTO(item);
            result.add(dto);
        }
        return result;
    }


    public Specification<Syllabus> searchSyllabus(String keyword) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                Predicate keywordPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(root.get("syllabusName"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.get("syllabusCode"), "%" + keyword + "%")
//                        criteriaBuilder.like(root.get("publicStatus"), "%" + keyword + "%")
//                        criteriaBuilder.like(root.get("createdBy"), "%" + keyword + "%")
                        //criteriaBuilder.like(root.get("level"), "%" + keyword + "%")
                );
                predicates.add(keywordPredicate);
            }
//            if (filterRequest != null) {
//                if (filterRequest.getStatus() != null && !filterRequest.getStatus().isEmpty()) {
//                    List<Predicate> statusPredicates = filterRequest.getStatus().stream()
//                            .map(status -> criteriaBuilder.equal(root.get("publicStatus"), status))
//                            .collect(Collectors.toList());
//                    predicates.add(criteriaBuilder.or(statusPredicates.toArray(new Predicate[0])));
//                }
//
////                if (filterRequest.getOutputStandard() != null && !filterRequest.getOutputStandard().isEmpty()) {
////                    List<Predicate> outputStandardPredicates = filterRequest.getOutputStandard().stream()
////                            .map(standard -> criteriaBuilder.equal(root.get("outputStandard"), standard))
////                            .collect(Collectors.toList());
////                    predicates.add(criteriaBuilder.or(outputStandardPredicates.toArray(new Predicate[0])));
////                }
//            }
//            if (sort != null) {
//                query.orderBy(sort.stream()
//                        .map(order -> {
//                            Expression<?> expression = root.get(order.getProperty());
//                            return order.isAscending() ? criteriaBuilder.asc(expression) : criteriaBuilder.desc(expression);
//                        })
//                        .toArray(Order[]::new)
//                );
//            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

