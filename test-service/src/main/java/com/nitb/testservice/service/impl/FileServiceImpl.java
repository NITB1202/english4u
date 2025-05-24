package com.nitb.testservice.service.impl;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.testservice.entity.Part;
import com.nitb.testservice.entity.Test;
import com.nitb.testservice.grpc.CreateQuestionRequest;
import com.nitb.testservice.grpc.CreateTestRequest;
import com.nitb.testservice.grpc.UploadTestTemplateRequest;
import com.nitb.testservice.service.FileService;
import com.nitb.testservice.service.PartService;
import com.nitb.testservice.service.QuestionService;
import com.nitb.testservice.service.TestService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final TestService testService;
    private final PartService partService;
    private final QuestionService questionService;

    @Override
    public byte[] generateTestTemplate() {
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Style for header
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Sheet 1: Test
        XSSFSheet sheet1 = workbook.createSheet("Test");
        Row row1 = sheet1.createRow(0);
        createStyledCell(row1, 0, "Name", headerStyle);
        createStyledCell(row1, 1, "Topic", headerStyle);
        createStyledCell(row1, 2, "Minutes", headerStyle);

        // Sheet 2: Parts
        XSSFSheet sheet2 = workbook.createSheet("Parts");
        Row row2 = sheet2.createRow(0);
        createStyledCell(row2, 0, "Content", headerStyle);

        // Sheet 3: Questions
        XSSFSheet sheet3 = workbook.createSheet("Questions");
        Row row3 = sheet3.createRow(0);
        String[] headers = {
                "Part order", "Content",
                "Answer A", "Answer B", "Answer C", "Answer D",
                "Correct answer", "Explanation"
        };
        for (int i = 0; i < headers.length; i++) {
            createStyledCell(row3, i, headers[i], headerStyle);
        }

        // Auto-size column
        for (XSSFSheet sheet : new XSSFSheet[]{sheet1, sheet2, sheet3}) {
            if (sheet.getRow(0) != null) {
                for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                    sheet.autoSizeColumn(i);
                }
            }
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new BusinessException("Failed to export Excel template: " + e.getMessage());
        }
    }

    @Override
    public void uploadTestTemplate(UploadTestTemplateRequest request) {
        InputStream is = new ByteArrayInputStream(request.getFileContent().toByteArray());

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);

            //Read Test sheet
            XSSFSheet testSheet = workbook.getSheet("Test");
            if (testSheet == null) {
                throw new BusinessException("Missing 'Test' sheet");
            }

            //Row 0 is header so the data start from row 1
            Row testRow = testSheet.getRow(1);
            if (testRow == null) {
                throw new BusinessException("No data in 'Test' sheet");
            }

            //Create test
            String name = getCellStringSafe(testRow, 0);
            String topic = getCellStringSafe(testRow, 1);
            int minutes = getCellIntSafe(testRow, 2);

            CreateTestRequest testRequest = CreateTestRequest.newBuilder()
                    .setUserId(request.getUserId())
                    .setName(name)
                    .setTopic(topic)
                    .setMinutes(minutes)
                    .build();

            Test test = testService.createTest(testRequest);

            //Read Tasks sheet
            XSSFSheet partsSheet = workbook.getSheet("Parts");
            if (partsSheet == null) {
                throw new BusinessException("Missing 'Parts' sheet");
            }

            int partPosition = 1;
            List<UUID> partIds = new ArrayList<>();

            for (int i = 1; i <= partsSheet.getLastRowNum(); i++) {
                Row row = partsSheet.getRow(i);
                if (row == null) continue;

                String content = getCellStringSafe(row, 0);
                Part part = partService.createPart(test.getId(), partPosition, content);
                partPosition++;

                partIds.add(part.getId());
            }

            //Update part count
            testService.updatePartCount(test.getId(), partIds.size());

            //Read Questions sheet
            XSSFSheet questionsSheet = workbook.getSheet("Questions");
            if (questionsSheet == null) {
                throw new BusinessException("Missing 'Questions' sheet");
            }

            //Initialize question creation request lists based on part count.
            List<List<CreateQuestionRequest>> questions = new ArrayList<>();
            for (int i = 0; i < partIds.size(); i++) {
                questions.add(new ArrayList<>());
            }

            //Add requests
            for (int i = 1; i <= questionsSheet.getLastRowNum(); i++) {
                Row row = questionsSheet.getRow(i);
                if (row == null) continue;

                int partOrder = getCellIntSafe(row, 0);
                if (partOrder < 1 || partOrder > partIds.size()) {
                    throw new BusinessException("Invalid partOrder: " + partOrder);
                }

                List<CreateQuestionRequest> questionsInPart = questions.get(partOrder - 1);

                String content = getCellStringSafe(row, 1);

                String answerA = getCellStringSafe(row, 2);
                String answerB = getCellStringSafe(row, 3);
                String answerC = getCellStringSafe(row, 4);
                String answerD = getCellStringSafe(row, 5);

                String correctAnswer = getCellStringSafe(row, 6);
                String explanation = getCellStringSafe(row, 7);


                List<String> answers = List.of(answerA, answerB, answerC, answerD);

                CreateQuestionRequest questionRequest = CreateQuestionRequest.newBuilder()
                        .setContent(content)
                        .setAnswers(String.join(",", answers))
                        .setCorrectAnswer(correctAnswer)
                        .setExplanation(explanation)
                        .build();

                questionsInPart.add(questionRequest);
            }

            for(int i = 0; i < partIds.size(); i++) {
                //Create questions
                questionService.createQuestions(partIds.get(i), questions.get(i));
                //Update question count
                partService.updateQuestionCount(partIds.get(i), questions.get(i).size());
            }

            workbook.close();

        } catch (IOException e) {
            throw new BusinessException("Failed to process Excel template: " + e.getMessage());
        }
    }

    private void createStyledCell(Row row, int colIndex, String value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private String getCellStringSafe(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf(cell.getNumericCellValue());
        if (cell.getCellType() == CellType.BOOLEAN) return String.valueOf(cell.getBooleanCellValue());
        return "";
    }

    private int getCellIntSafe(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return 0;
        if (cell.getCellType() == CellType.NUMERIC) return (int) cell.getNumericCellValue();
        if (cell.getCellType() == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
