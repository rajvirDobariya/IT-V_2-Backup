package com.digitisation.branchreports.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExportService {

	public static String exportReportsToExcelBase64(List<Object[]> reportsCount) {
		// Create a workbook and sheet
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Branch Reports");

		// Create the header row
		Row headerRow = sheet.createRow(0);
		String[] headers = { "Branch Code", "Branch Name", "Report Date", "Report Frequency", "Reports",
				"Days Since Report" };
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
		}

		// Date formatter for reportDate
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate today = LocalDate.now(); // Get today's date

		// Fill the data rows
		int rowCount = 1;
		for (Object[] report : reportsCount) {
			Row row = sheet.createRow(rowCount++);

			// Extract values from Object[]
			String branchCode = (String) report[0];
			String branchName = (String) report[1];
			LocalDate reportDate = (LocalDate) report[2]; // Assuming this is a LocalDate
			String reportFrequency = (String) report[3];
			Long reports = (Long) report[4];

			// Calculate days since reportDate
			int daysSinceReport = Period.between(reportDate, today).getDays();

			// Fill the cells
			row.createCell(0).setCellValue(branchCode);
			row.createCell(1).setCellValue(branchName);
			row.createCell(2).setCellValue(reportDate.format(dateFormatter));
			row.createCell(3).setCellValue(reportFrequency);
			row.createCell(4).setCellValue(reports);
			row.createCell(5).setCellValue(daysSinceReport); // New column for days since report
		}

		// Auto-size columns
		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write the Excel file to a ByteArrayOutputStream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			throw new RuntimeException("Failed to write Excel file", e);
		}

		// Convert the byte array to Base64
		byte[] excelBytes = outputStream.toByteArray();
		return Base64.getEncoder().encodeToString(excelBytes);
	}
}