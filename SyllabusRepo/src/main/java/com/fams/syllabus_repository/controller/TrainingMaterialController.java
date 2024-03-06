package com.fams.syllabus_repository.controller;

import com.fams.syllabus_repository.dto.TrainingMaterialsDto;
import com.fams.syllabus_repository.entity.TrainingMaterials;
import com.fams.syllabus_repository.exceptions.MaterialNotFoundException;
import com.fams.syllabus_repository.exceptions.ResourceNotFoundException;
import com.fams.syllabus_repository.exceptions.SyllabusNotFoundException;
import com.fams.syllabus_repository.repository.TrainingMaterialsRepositoty;
import com.fams.syllabus_repository.request.RequestIdDto;
import com.fams.syllabus_repository.response.ResponseObject;
import com.fams.syllabus_repository.response.ResponseObjectDto;
import com.fams.syllabus_repository.service.impl.TrainingMaterialsServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/training-material")
public class TrainingMaterialController {
    private static final Logger logger = LogManager.getLogger(SyllabusController.class);
    @Autowired
    TrainingMaterialsServiceImpl trainingMaterialsService;

    @Autowired
    TrainingMaterialsRepositoty trainingMaterialsRepositoty;

    @PostMapping("/material-detail")
    public ResponseObject<Object> materialDetail(@RequestBody RequestIdDto request) {
        //Long id = request.getId();
        try {
            Long id = request.getId();
            List<TrainingMaterialsDto> material = trainingMaterialsService.getMaterialByContentId(id);
            return ResponseObject.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Material successfully")
                    .data(material)
                    .build();
        } catch (SyllabusNotFoundException e) {
            logger.error("Error in Material", e);
            return ResponseObject.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error syllabus")
                    .build();
        }
    }

    @PostMapping("/upload-file")
    public ResponseEntity<String> uploadTrainingMaterialToS3(
            @RequestParam("trainingContentId") Long trainingContentId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("createdBy") String createdBy,
            @RequestParam("modifyBy") String modifyBy
    ) {
        try {
            String s3Key = trainingMaterialsService.uploadTrainingMaterialToS3(trainingContentId, file, createdBy, modifyBy);
            // Nếu mọi thứ diễn ra bình thường, trả về mã trạng thái 200 OK
            return ResponseEntity.ok("Training material uploaded with S3 key: " + s3Key);
        } catch (Exception e) {
            // Bắt lỗi và trả về mã trạng thái 500 INTERNAL SERVER ERROR
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/get-training-material/{id}")
    public ResponseEntity<ResponseObjectDto> getTrainingMaterialFromS3(@PathVariable Long id) {
        try {
            TrainingMaterialsDto trainingMaterialResponse = trainingMaterialsService.getTrainingMaterialDataById(id);
            return ResponseEntity.ok(
                    new ResponseObjectDto(0, "success", trainingMaterialResponse)
            );
        } catch (ResourceNotFoundException e) {
            logger.error("Error in getTrainingMaterialFromS3", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObjectDto(1, "Resource not found", null)
            );
        }
    }


//    @PutMapping("/updateTitleByContentId/{contentId}")
//    public ResponseEntity<ResponseObject<TrainingMaterialsDto>> updateTrainingMaterialsTitleByContentId(
//            @PathVariable Long contentId,
//            @RequestBody TrainingMaterialsDto trainingMaterialsDto
//    ) {
//        TrainingMaterialsDto updatedTrainingMaterials = trainingMaterialsService.updateTrainingMaterials(contentId, trainingMaterialsDto.getTitle());
//
//        if (updatedTrainingMaterials != null) {
//            ResponseObject<TrainingMaterialsDto> response = ResponseObject.<TrainingMaterialsDto>builder()
//                    .statusCode(HttpStatus.OK.value())
//                    .message("Title in TrainingMaterials updated successfully")
//                    .data(updatedTrainingMaterials)
//                    .build();
//            return ResponseEntity.ok(response);
//        } else {
//            ResponseObject<TrainingMaterialsDto> response = ResponseObject.<TrainingMaterialsDto>builder()
//                    .statusCode(HttpStatus.NOT_FOUND.value())
//                    .message("TrainingMaterials not found for the given contentId")
//                    .build();
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//    }

    @GetMapping("/download-materials/{resourceId}")
    public ResponseEntity<byte[]> downloadFromS3(@PathVariable Long resourceId) {
        byte[] fileData = trainingMaterialsService.getTrainingMaterialFromS3(resourceId);
        if (fileData != null) {
            TrainingMaterials resource = trainingMaterialsRepositoty.findById(resourceId).orElse(null);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getTitle());

            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("delete-material")
    public ResponseEntity<String> deleteMaterial(@RequestBody RequestIdDto request) {
        try {
            Long id = request.getId();
            trainingMaterialsService.deleteMaterial(id);
            return ResponseEntity.ok("Material with ID " + id + " has been deleted");
        } catch (MaterialNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the material");
        }
    }

}
