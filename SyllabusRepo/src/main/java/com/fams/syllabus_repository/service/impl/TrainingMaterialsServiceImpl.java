package com.fams.syllabus_repository.service.impl;

import com.fams.syllabus_repository.converter.TrainingMaterialsConverter;
import com.fams.syllabus_repository.dto.TrainingMaterialsDto;
import com.fams.syllabus_repository.entity.TrainingContent;
import com.fams.syllabus_repository.entity.TrainingMaterials;
import com.fams.syllabus_repository.exceptions.MaterialNotFoundException;
import com.fams.syllabus_repository.exceptions.ResourceNotFoundException;
import com.fams.syllabus_repository.repository.TrainingContentRepository;
import com.fams.syllabus_repository.repository.TrainingMaterialsRepositoty;
import com.fams.syllabus_repository.s3.S3Bucket;
import com.fams.syllabus_repository.s3.S3Sevice;
import com.fams.syllabus_repository.service.TrainingMaterialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingMaterialsServiceImpl implements TrainingMaterialsService {
    @Autowired
    private final TrainingMaterialsRepositoty trainingMaterialsRepositoty;
    @Autowired
    private final TrainingContentRepository trainingContentRepository;

    @Autowired
    private S3Sevice s3Sevice;

    @Autowired
    private S3Bucket s3Bucket;
    private static final long MAX_FILE_SIZE = 25 * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS =
            new HashSet<>(Set.of("jpg", "jpeg", "png", "pdf", "ppt", "video",
                    "xls", "csv", "pptx", "txt", "docx"));
    @Override
    public List<TrainingMaterialsDto> getAllResource() {
        List<TrainingMaterialsDto> result = new ArrayList<>();
        List<TrainingMaterials> trainingMaterials = trainingMaterialsRepositoty.findAll();
        for (TrainingMaterials item : trainingMaterials
        ) {
            TrainingMaterialsDto dto = TrainingMaterialsConverter.toDTO(item);
            result.add(dto);
        }
        return result;
    }

//    @Override
//    public boolean uploadTrainingMaterial(Long trainingContentId, String description, MultipartFile file) {
//        try {
//            if (file.getSize() > MAX_FILE_SIZE) {
//                return false;
//            }
//
//            String originalFilename = file.getOriginalFilename();
//            String fileExtension = StringUtils.getFilenameExtension(originalFilename).toLowerCase();
//
//            if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
//                return false;
//            }
//
//            byte[] data = file.getBytes();
//
//            // Lấy thông tin về TrainingContent từ trainingContentId
//            TrainingContent trainingContent = trainingContentRepository.findById(trainingContentId)
//                    .orElseThrow(() -> new TrainingContentNotFoundException("TrainingContent not found"));
//
//            TrainingMaterials resource = TrainingMaterials.builder()
//                    .description(trainingContent.getName()) // Có thể sử dụng thông tin từ TrainingContent hoặc truyền từ client
//                    .filename(originalFilename)
//                    .data(data)
//                    .createdDate(LocalDate.now())
//                    .trainingContent(trainingContent) // Gán TrainingContent cho TrainingMaterials
//                    .build();
//
//            trainingMaterialsRepositoty.save(resource);
//
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    @Override
    public byte[] downloadTrainingMaterial(Long materialsId) {
        Optional<TrainingMaterials> resourceOptional = trainingMaterialsRepositoty.findById(materialsId);
        if (resourceOptional.isPresent()) {
            TrainingMaterials resource = resourceOptional.get();
            return resource.getData();
        }
        return null;
    }

    @Override
    public boolean deleteMaterials(int materialsId) {
        return false;
    }

//    public String uploadTrainingMaterialToS3(Long trainingMaterialId, MultipartFile file) {
//
//        //store image
//        TrainingMaterials trainingMaterial = trainingMaterialsRepositoty.findById(trainingMaterialId).get();
//        String key = "trainingMaterials/" + trainingMaterial.getId() + "/" + UUID.randomUUID();
//        try {
//            s3Sevice.putObject(
//                    s3Bucket.getCustomer(),
//                    key,
//                    file.getBytes()
//            );
//
//            trainingMaterial.setS3Location(key);
//            trainingMaterialsRepositoty.save(trainingMaterial);
//
//            return key;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


//    @Override
//    public String uploadTrainingMaterialToS3(Long trainingContentId, Long trainingMaterialId, MultipartFile file, String createdBy, String modifyBy) {
//        Optional<TrainingContent> trainingContentOptional = trainingContentRepository.findById(trainingContentId);
//
//        if (trainingContentOptional.isPresent()) {
//            TrainingContent trainingContent = trainingContentOptional.get();
//
//            TrainingMaterials trainingMaterial;
//            if (trainingMaterialId != null && !trainingMaterialId.equals(0L)) {
//                // Update existing TrainingMaterials
//                trainingMaterial = trainingMaterialsRepositoty.findById(trainingMaterialId)
//                        .orElseThrow(() -> new RuntimeException("TrainingMaterials with ID not found: " + trainingMaterialId));
//            } else {
//                // Create a new TrainingMaterials
//                trainingMaterial = new TrainingMaterials();
//            }
//
//            String key = "trainingMaterials/" + trainingContentId + "/" + UUID.randomUUID();
//            try {
//                s3Sevice.putObject(s3Bucket.getCustomer(), key, file.getBytes());
//                trainingMaterial.setS3Location(key);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            trainingMaterial.setTrainingContent(trainingContent);
//            trainingMaterial.setTitle(file.getOriginalFilename());
//            trainingMaterial.setCreatedDate(LocalDate.now());
//            trainingMaterial.setCreatedBy(createdBy);
//            trainingMaterial.setModifyDate(LocalDate.now());
//            trainingMaterial.setModifyBy(modifyBy);
//
//            trainingMaterialsRepositoty.save(trainingMaterial);
//            return key;
//        } else {
//            throw new RuntimeException("TrainingContent with ID not found: " + trainingContentId);
//        }
//    }
public String uploadTrainingMaterialToS3(Long trainingContentId, MultipartFile file, String createdBy, String modifyBy) {
    Optional<TrainingContent> trainingContentOptional = trainingContentRepository.findById(trainingContentId);
//String createdBy = null;
//String modifyBy = null;
    if (trainingContentOptional.isPresent()) {
        TrainingContent trainingContent = trainingContentOptional.get();

        TrainingMaterials trainingMaterial = new TrainingMaterials();

        String key = "trainingMaterials/" + trainingContentId + "/" + UUID.randomUUID();
        try {
            s3Sevice.putObject(s3Bucket.getCustomer(), key, file.getBytes());

            trainingMaterial.setS3Location(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        trainingMaterial.setTrainingContent(trainingContent);
        trainingMaterial.setTitle(file.getOriginalFilename());
        trainingMaterial.setCreatedDate(LocalDate.now());
        trainingMaterial.setCreatedBy(createdBy);
        trainingMaterial.setModifyDate(LocalDate.now());
        trainingMaterial.setModifyBy(modifyBy);
        trainingMaterialsRepositoty.save(trainingMaterial);

        return key;
    } else {
        throw new RuntimeException("TrainingContent with ID not found: " + trainingContentId);
    }
}


    public byte[] getTrainingMaterialFromS3(Long trainingMaterialId){
        TrainingMaterials trainingMaterial = trainingMaterialsRepositoty.findById(trainingMaterialId).get();
        return s3Sevice.getObject(
                s3Bucket.getCustomer(),
                trainingMaterial.getS3Location()
        );
    }

//    public InputStream downloadTrainingMaterialFromS33(Long trainingMaterialId) {
//        TrainingMaterials trainingMaterial = trainingMaterialsRepositoty.findById(trainingMaterialId)
//                .orElseThrow(() -> new RuntimeException("TrainingMaterials with ID not found: " + trainingMaterialId));
//
//        String s3Location = trainingMaterial.getS3Location(); // Lấy vị trí trên S3
//
//        // Sử dụng AmazonS3 client để tải tệp từ S3
//        byte[] s3Object = s3Sevice.getObject(s3Bucket.getCustomer(), s3Location);
//        S3ObjectInputStream inputStream = s3Object.getObjectContent();
//
//        return inputStream; // Trả về luồng đầu vào để người dùng có thể đọc tệp
//    }


    public TrainingMaterialsDto getTrainingMaterialDataById(Long id) throws ResourceNotFoundException {
        TrainingMaterials trainingMaterial = trainingMaterialsRepositoty.findById(id).get();

        if(trainingMaterial != null){
            TrainingMaterialsDto trainingMaterialResponse = TrainingMaterialsDto.builder()
                    .s3Location(trainingMaterial.getS3Location())
                    .trainingMaterialData(getTrainingMaterialFromS3(trainingMaterial.getId()))
                    .build();

            return trainingMaterialResponse;
        }
        else{
            throw new ResourceNotFoundException("Training material is not found");
        }
    }
    public TrainingMaterialsDto updateTrainingMaterials(Long contentId, String newTitle) {
        Optional<TrainingMaterials> optionalTrainingMaterials = trainingMaterialsRepositoty.findById(contentId);
        if (optionalTrainingMaterials.isPresent()) {
            TrainingMaterials trainingMaterials = optionalTrainingMaterials.get();
            trainingMaterials.setTitle(newTitle);
            TrainingMaterials updatedTrainingMaterials = trainingMaterialsRepositoty.save(trainingMaterials);

            return TrainingMaterialsConverter.toDTO(updatedTrainingMaterials);
        } else {
            return null;
        }
    }
    public void deleteMaterial(Long materialId) {
        TrainingMaterials material = trainingMaterialsRepositoty.findById(materialId)
                .orElseThrow(() -> new MaterialNotFoundException("Material not found for ID: " + materialId));
        trainingMaterialsRepositoty.delete(material);
    }

    @Override
    public List<TrainingMaterialsDto> getMaterialByContentId(Long id) {
        List<TrainingMaterials> trainingMaterials = trainingMaterialsRepositoty.findByTrainingContentId(id);
        return trainingMaterials.stream().map(day -> {
            TrainingMaterialsDto trainingMaterialsDto = TrainingMaterialsConverter.toDTO(day);
            return trainingMaterialsDto;
        }).collect(Collectors.toList());
    }
}
