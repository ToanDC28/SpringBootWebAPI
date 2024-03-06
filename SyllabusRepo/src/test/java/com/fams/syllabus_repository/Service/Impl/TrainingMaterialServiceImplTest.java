//package com.fams.syllabus_repository.Service.Impl;
//
//import com.fams.syllabus_repository.converter.TrainingMaterialsConverter;
//import com.fams.syllabus_repository.dto.TrainingMaterialsDto;
//import com.fams.syllabus_repository.entity.TrainingContent;
//import com.fams.syllabus_repository.entity.TrainingMaterials;
//import com.fams.syllabus_repository.repository.TrainingContentRepository;
//import com.fams.syllabus_repository.repository.TrainingMaterialsRepositoty;
//import com.fams.syllabus_repository.service.TrainingMaterialsService;
//import com.fams.syllabus_repository.service.impl.TrainingMaterialsServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//import static org.testng.Assert.assertFalse;
//import static org.testng.Assert.assertTrue;
//
//public class TrainingMaterialServiceImplTest {
//    @Mock
//    private TrainingMaterialsRepositoty trainingMaterialsRepositoty;
//    @Mock
//    private TrainingContentRepository trainingContentRepository;
//
//    @InjectMocks
//    private TrainingMaterialsServiceImpl trainingMaterialsService;
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
////    @Test
////    public void testGetAllResource() {
////        // Tạo danh sách mẫu cho TrainingMaterials
////        List<TrainingMaterials> trainingMaterialsList = new ArrayList<>();
////        TrainingMaterials material1 = new TrainingMaterials();
////        material1.setId(1L);
////        material1.setDescription("Material 1");
////        material1.setFilename("material1.pdf");
////        material1.setData(new byte[0]); // Dữ liệu file (byte array), có thể cung cấp thêm dữ liệu thực tế
////        material1.setCreatedDate(LocalDate.now());
////
////        TrainingMaterials material2 = new TrainingMaterials();
////        material2.setId(2L);
////        material2.setDescription("Material 2");
////        material2.setFilename("material2.pdf");
////        material2.setData(new byte[0]);
////        material2.setCreatedDate(LocalDate.now());
////
////        trainingMaterialsList.add(material1);
////        trainingMaterialsList.add(material2);
////
////        // Thiết lập giả lập cho repository
////        when(trainingMaterialsRepositoty.findAll()).thenReturn(trainingMaterialsList);
////
////        // Gọi hàm cần kiểm tra
////        List<TrainingMaterialsDto> result = trainingMaterialsService.getAllResource();
////
////        // Kiểm tra kết quả
////        assertEquals(2, result.size());
////
////        // Có thể kiểm tra cụ thể các giá trị của TrainingMaterialsDto tại đây
////        assertEquals(1L, result.get(0).getId());
////        assertEquals("Material 1", result.get(0).getDescription());
////        assertEquals("material1.pdf", result.get(0).getFilename());
////
////        assertEquals(2L, result.get(1).getId());
////        assertEquals("Material 2", result.get(1).getDescription());
////        assertEquals("material2.pdf", result.get(1).getFilename());
////    }
////    @Test
////    public void testUploadTrainingMaterial_Success() throws IOException {
////        // Mock input data
////        Long trainingContentId = 1L;
////        String description = "Sample description";
////        String originalFilename = "sample.pdf";
////        byte[] fileData = "Sample file data".getBytes();
////
////        // Mock TrainingContent
////        TrainingContent trainingContent = new TrainingContent();
////        trainingContent.setId(trainingContentId);
////        trainingContent.setName(description);
////
////        // Mock MultipartFile
////        MockMultipartFile mockFile = new MockMultipartFile("file", originalFilename, "application/pdf", fileData);
////
////        // Mock the behavior of trainingContentRepository
////        when(trainingContentRepository.findById(trainingContentId)).thenReturn(Optional.of(trainingContent));
////
////        // Mock the behavior of trainingMaterialsRepositoty.save()
////        when(trainingMaterialsRepositoty.save(Mockito.any(TrainingMaterials.class))).thenAnswer(invocation -> {
////            TrainingMaterials savedResource = invocation.getArgument(0);
////            savedResource.setId(1L);  // Assuming an ID is generated upon save
////            return savedResource;
////        });
////
////        // Test the uploadTrainingMaterial method
////        boolean result = trainingMaterialsService.uploadTrainingMaterial(trainingContentId, description, mockFile);
////
////        // Assert the result
////        assertTrue(result);
////    }
//    private static final long MAX_FILE_SIZE = 25 * 1024 * 1024;
////    @Test
////    public void testUploadTrainingMaterial_ExceedMaxFileSize() throws IOException {
////        // Mock input data
////        Long trainingContentId = 1L;
////        String description = "Sample description";
////        String originalFilename = "sample.pdf";
////        byte[] fileData = new byte[(int) (( 25 * 1024 * 1024) + 1)];  // Sử dụng MAX_FILE_SIZE từ TrainingMaterialsServiceImpl
////
////        // Mock TrainingContent
////        TrainingContent trainingContent = new TrainingContent();
////        trainingContent.setId(trainingContentId);
////        trainingContent.setName(description);
////
////        // Mock MultipartFile
////        MockMultipartFile mockFile = new MockMultipartFile("file", originalFilename, "application/pdf", fileData);
////
////        // Mock the behavior of trainingContentRepository
////        when(trainingContentRepository.findById(trainingContentId)).thenReturn(Optional.of(trainingContent));
////
////        // Test the uploadTrainingMaterial method
////        boolean result = trainingMaterialsService.uploadTrainingMaterial(trainingContentId, description, mockFile);
////
////        // Assert the result
////        assertFalse(result);
////    }
//
//    @Test
//    public void testUploadTrainingMaterial_InvalidFileExtension() throws IOException {
//        // Mock input data
//        Long trainingContentId = 1L;
//        String description = "Sample description";
//        String originalFilename = "sample.txt";  // Using an invalid file extension
//        byte[] fileData = "Sample file data".getBytes();
//
//        // Mock TrainingContent
//        TrainingContent trainingContent = new TrainingContent();
//        trainingContent.setId(trainingContentId);
//        trainingContent.setName(description);
//
//        // Mock MultipartFile
//        MockMultipartFile mockFile = new MockMultipartFile("file", originalFilename, "text/plain", fileData);
//
//        // Mock the behavior of trainingContentRepository
//        when(trainingContentRepository.findById(trainingContentId)).thenReturn(Optional.of(trainingContent));
//
//        // Test the uploadTrainingMaterial method
//       // boolean result = trainingMaterialsService.uploadTrainingMaterial(trainingContentId, description, mockFile);
//
//        // Assert the result with a message
//        //assertThat("Upload should fail due to an invalid file extension.", result, is(false));
//    }
//    @Test
//    public void testDownloadTrainingMaterial_ExistingMaterial() {
//        // Mock materialsId và dữ liệu tài liệu đào tạo (chúng ta giả định rằng có tài liệu đào tạo với materialsId = 1)
//        Long materialsId = 1L;
//        byte[] expectedData = "Sample file data".getBytes();
//
//        // Mock behavior của repository
//        when(trainingMaterialsRepositoty.findById(materialsId))
//                .thenReturn(Optional.of(new TrainingMaterials()));
//
//        // Gọi phương thức cần kiểm tra
//        byte[] result = trainingMaterialsService.downloadTrainingMaterial(materialsId);
//
//        // Kiểm tra kết quả và so sánh dữ liệu trả về với dữ liệu mong đợi
//       // assertArrayEquals(expectedData, result);
//    }
//
//    @Test
//    public void testDownloadTrainingMaterial_NonExistentMaterial() {
//        // Mock materialsId và không tìm thấy tài liệu đào tạo (chúng ta giả định rằng không có tài liệu đào tạo với materialsId = 2)
//        Long materialsId = 2L;
//
//        // Mock behavior của repository
//        when(trainingMaterialsRepositoty.findById(materialsId)).thenReturn(Optional.empty());
//
//        // Gọi phương thức cần kiểm tra
//        byte[] result = trainingMaterialsService.downloadTrainingMaterial(materialsId);
//
//        // Kiểm tra kết quả và xác nhận rằng kết quả là null
//        assertNull(result);
//    }
//
//}
