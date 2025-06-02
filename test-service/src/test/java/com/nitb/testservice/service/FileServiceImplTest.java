package com.nitb.testservice.service;

import com.google.protobuf.ByteString;
import com.nitb.testservice.entity.Part;
import com.nitb.testservice.grpc.CreateTestRequest;
import com.nitb.testservice.grpc.UploadTestTemplateRequest;
import com.nitb.testservice.service.impl.FileServiceImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private TestService testService;

    @Mock
    private PartService partService;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private FileServiceImpl fileService;

    @Test
    void generateTestTemplate_shouldReturnNonEmptyExcelFile() {
        // When
        byte[] result = fileService.generateTestTemplate();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.length).isGreaterThan(0);

        // Verify Excel file is valid
        assertDoesNotThrow(() -> {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(result);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // Sheet count
            assertThat(workbook.getNumberOfSheets()).isEqualTo(3);
            assertThat(workbook.getSheetAt(0).getSheetName()).isEqualTo("Test");
            assertThat(workbook.getSheetAt(1).getSheetName()).isEqualTo("Parts");
            assertThat(workbook.getSheetAt(2).getSheetName()).isEqualTo("Questions");

            // Header checks
            assertThat(workbook.getSheet("Test").getRow(0).getCell(0).getStringCellValue()).isEqualTo("NAME");
            assertThat(workbook.getSheet("Parts").getRow(0).getCell(0).getStringCellValue()).isEqualTo("CONTENT");
            assertThat(workbook.getSheet("Questions").getRow(0).getCell(0).getStringCellValue()).isEqualTo("PART ORDER");

            workbook.close();
        });
    }

    @Test
    void uploadTestTemplate_shouldReturnTest_whenValidTemplate() throws IOException {
        // Arrange
        UUID testId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID part1Id = UUID.randomUUID();
        UUID part2Id = UUID.randomUUID();

        com.nitb.testservice.entity.Test test = new com.nitb.testservice.entity.Test();
        test.setId(testId);

        Part part1 = new Part();
        part1.setId(part1Id);
        Part part2 = new Part();
        part2.setId(part2Id);

        XSSFWorkbook workbook = new XSSFWorkbook();

        // Sheet: Test
        XSSFSheet testSheet = workbook.createSheet("Test");
        Row header1 = testSheet.createRow(0);
        header1.createCell(0).setCellValue("NAME");
        header1.createCell(1).setCellValue("TOPIC");
        header1.createCell(2).setCellValue("MINUTES");

        Row data1 = testSheet.createRow(1);
        data1.createCell(0).setCellValue("Sample Test");
        data1.createCell(1).setCellValue("Grammar");
        data1.createCell(2).setCellValue(45);

        // Sheet: Parts
        XSSFSheet partsSheet = workbook.createSheet("Parts");
        Row header2 = partsSheet.createRow(0);
        header2.createCell(0).setCellValue("CONTENT");

        Row partRow1 = partsSheet.createRow(1);
        partRow1.createCell(0).setCellValue("Part 1 Content");
        Row partRow2 = partsSheet.createRow(2);
        partRow2.createCell(0).setCellValue("Part 2 Content");

        // Sheet: Questions
        XSSFSheet questionsSheet = workbook.createSheet("Questions");
        Row header3 = questionsSheet.createRow(0);
        String[] headers = {"PART ORDER", "CONTENT", "ANSWER A", "ANSWER B", "ANSWER C", "ANSWER D", "CORRECT ANSWER", "EXPLANATION"};
        for (int i = 0; i < headers.length; i++) {
            header3.createCell(i).setCellValue(headers[i]);
        }

        Row questionRow = questionsSheet.createRow(1);
        questionRow.createCell(0).setCellValue(1); // only for part 1
        questionRow.createCell(1).setCellValue("What is Java?");
        questionRow.createCell(2).setCellValue("Language");
        questionRow.createCell(3).setCellValue("Fruit");
        questionRow.createCell(4).setCellValue("Car");
        questionRow.createCell(5).setCellValue("Planet");
        questionRow.createCell(6).setCellValue("A");
        questionRow.createCell(7).setCellValue("It's a programming language.");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        UploadTestTemplateRequest request = UploadTestTemplateRequest.newBuilder()
                .setUserId(userId.toString())
                .setFileContent(ByteString.copyFrom(out.toByteArray()))
                .build();

        given(testService.createTest(any(CreateTestRequest.class))).willReturn(test);
        given(partService.createPart(eq(testId), eq(1), anyString())).willReturn(part1);
        given(partService.createPart(eq(testId), eq(2), anyString())).willReturn(part2);

        // Act
        com.nitb.testservice.entity.Test result = fileService.uploadTestTemplate(request);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());

        then(testService).should().updatePartCount(eq(testId), eq(2));
        then(questionService).should().createQuestions(eq(part1Id), anyList());
        then(partService).should().updateQuestionCount(eq(part1Id), eq(1));
    }
}
