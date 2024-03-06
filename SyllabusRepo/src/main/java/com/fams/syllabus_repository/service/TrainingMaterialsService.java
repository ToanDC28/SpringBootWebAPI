package com.fams.syllabus_repository.service;

import com.fams.syllabus_repository.dto.DaySyllabusListDto;
import com.fams.syllabus_repository.dto.TrainingMaterialsDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface TrainingMaterialsService {
    List<TrainingMaterialsDto> getAllResource();
    //boolean uploadTrainingMaterial(Long trainingContentId,String description, MultipartFile file);
    byte[] downloadTrainingMaterial(Long materialsId);
    boolean deleteMaterials(int materialsId);
    String uploadTrainingMaterialToS3(Long trainingContentId, MultipartFile file, String createdBy, String modifyBy);
    TrainingMaterialsDto updateTrainingMaterials(Long contentId, String newTitle);
    void deleteMaterial(Long materialId);
    List<TrainingMaterialsDto> getMaterialByContentId(Long id);
    //InputStream downloadTrainingMaterialFromS33(Long trainingMaterialId);
}
