package com.nitb.testservice.service.impl;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.testservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

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

    private void createStyledCell(Row row, int colIndex, String value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
}
