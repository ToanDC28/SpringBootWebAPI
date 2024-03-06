//package com.fams.syllabus_repository.Service.Impl;
//
//import com.fams.syllabus_repository.dto.TrainingContentDto;
//import com.fams.syllabus_repository.entity.TrainingContent;
//import com.fams.syllabus_repository.entity.TrainingUnit;
//import com.fams.syllabus_repository.exceptions.TrainingUnitNotFoundException;
//import com.fams.syllabus_repository.repository.TrainingContentRepository;
//import com.fams.syllabus_repository.repository.TrainingUnitRepository;
//import com.fams.syllabus_repository.service.impl.TrainingContentServiceImpl;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//public class TrainingContentServiceImplTest {
//    @InjectMocks
//    private TrainingContentServiceImpl trainingContentService;
//
//    @Mock
//    private TrainingContentRepository trainingContentRepository;
//    @Mock
//    private TrainingUnitRepository trainingUnitRepository;
//
//
//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }
//
////    @BeforeEach
////    public void setUp() {
////        MockitoAnnotations.openMocks(this);
////        trainingContentService = new TrainingContentServiceImpl(trainingContentRepository, trainingUnitRepository);
////    }
//
//    @Test
//    public void testGetAllTraniningContent() {
//        // Tạo dữ liệu mẫu
//        TrainingContent trainingContent1 = new TrainingContent();
//        TrainingContent trainingContent2 = new TrainingContent();
//        List<TrainingContent> trainingContents = new ArrayList<>();
//        trainingContents.add(trainingContent1);
//        trainingContents.add(trainingContent2);
//
//        Mockito.when(trainingContentRepository.findAll()).thenReturn(trainingContents);
//
//        List<TrainingContentDto> result = trainingContentService.getAllTraniningContent();
//
//        assertEquals(2, result.size());
//    }
//
//    @Test
//    public void testCreateContent() {
//        // Tạo dữ liệu mẫu
//        TrainingContentDto trainingContentDto = new TrainingContentDto();
//        Long trainingUnitId = 1L;
//        TrainingUnit trainingUnitEntity = new TrainingUnit();
//
//        // Mock phương thức findById của trainingUnitRepository
//        when(trainingUnitRepository.findById(trainingUnitId)).thenReturn(Optional.of(trainingUnitEntity));
//
//        // Mock phương thức save của trainingContentRepository
//        when(trainingContentRepository.save(Mockito.any(TrainingContent.class))).thenAnswer(invocation -> {
//            TrainingContent content = invocation.getArgument(0);
//            content.setId(1L); // Mock id của đối tượng đã lưu
//            return content;
//        });
//
//        // Gọi phương thức cần kiểm tra
//        TrainingContentDto result = trainingContentService.createContent(trainingUnitId, trainingContentDto);
//
//        // Kiểm tra kết quả
//        assertEquals(Long.class, result.getId().getClass());
//        // Có thể thêm các kiểm tra khác tùy theo logic của bạn
//    }
//
//    @Test
//    public void testCreateContent_WithNonExistingTrainingUnit() {
//        // Mock data
//        Long trainingUnitId = 1L;
//        TrainingContentDto trainingContentDto = new TrainingContentDto();
//        trainingContentDto.setName("New Content");
//
//        when(trainingUnitRepository.findById(trainingUnitId)).thenReturn(Optional.empty());
//
//        // Call the method and verify the exception
//        assertThrows(TrainingUnitNotFoundException.class,
//                () -> trainingContentService.createContent(trainingUnitId, trainingContentDto));
//
//        // Verify that repository methods were called
//        verify(trainingUnitRepository, times(1)).findById(trainingUnitId);
//        verify(trainingContentRepository, times(0)).save(any(TrainingContent.class));
//    }
//}
