package com.team2.trainingprogramrepo.service;

import com.team2.trainingprogramrepo.dto.ClassDto;
import com.team2.trainingprogramrepo.request.TrainingProgramCreateRequest;
import com.team2.trainingprogramrepo.request.TrainingProgramUpdateRequest;
import com.team2.trainingprogramrepo.entity.TrainingProgram;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

public interface ITrainingProgramService {

    List<TrainingProgram> getAllTrainingPrograms();

//    List<TrainingProgram> getAllTrainingPrograms(String fieldName, boolean isAscending);

    List<ClassDto> getClassesByTrainingProgramId(Integer trainingProgramId);

//    int createTrainingProgram(TrainingProgram trainingProgram);

    int createTrainingProgram(TrainingProgramCreateRequest trainingProgramCreateRequest);

    TrainingProgram duplicateTrainingProgram(Integer trainingProgramId);

    int deactivateTrainingProgram(Integer trainingProgramId);

    int activateTrainingProgram(Integer trainingProgramId);

    TrainingProgram updateTrainingProgram(TrainingProgramUpdateRequest trainingProgramUpdateRequest);

    List<TrainingProgram> importTrainingProgramFromFile(InputStream is) throws IOException, ParseException;

    int importFile(MultipartFile file);
}
