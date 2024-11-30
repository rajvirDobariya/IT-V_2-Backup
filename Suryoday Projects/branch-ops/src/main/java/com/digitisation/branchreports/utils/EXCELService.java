package com.digitisation.branchreports.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.digitisation.branchreports.model.BranchMaster;
import com.digitisation.branchreports.model.BranchUserMakerModel;

public class EXCELService {

	public static String generateEXEL(JSONObject result) {
		JSONObject data = result.optJSONObject("Data");
		JSONArray reports = data.optJSONArray("reports");
		byte[] excelBytes = null;
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Reports");

			// Create header row
			Row headerRow = sheet.createRow(0);
			String[] headers = { "ID", "Branch Code", "Report Name", "Frequency", "Report Date", "Submission Date",
					"RO ID", "Creation Date", " Status", "RO Remarks", "Action" };
			for (int i = 0; i < headers.length; i++) {
				headerRow.createCell(i).setCellValue(headers[i]);
			}

			// Create data rows
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			for (int i = 0; i < reports.length(); i++) {
				JSONObject report = reports.getJSONObject(i);
				Row row = sheet.createRow(i + 1);
				int j = 0;
				row.createCell(j).setCellValue(report.optString("id"));
				j++;
				row.createCell(j).setCellValue(report.optString("branchcode"));
				j++;
				row.createCell(j).setCellValue(report.optString("branchname"));
				j++;
				row.createCell(j).setCellValue(report.optString("reportfrequency"));
				j++;
				if (report.has("reportDate")) {
					LocalDate reportDate = LocalDate.parse(report.optString("reportDate"));
					row.createCell(j).setCellValue(reportDate.format(dateFormatter));
				}
				j++;
				if (report.has("submissiondate")) {
					LocalDate submissionDate = LocalDate.parse(report.optString("submissiondate"));
					row.createCell(j).setCellValue(submissionDate.format(dateFormatter));
				}
				j++;
				row.createCell(j).setCellValue(report.optString("roId"));
				j++;
				if (report.has("createdDate")) {
					LocalDateTime createdDate = DateUtils.getDateStringIntoDateTime(report.optString("createdDate"));
					row.createCell(j).setCellValue(createdDate.format(dateTimeFormatter));
				}
				j++;
				row.createCell(j).setCellValue(report.optString("status"));
				j++;
				row.createCell(j).setCellValue(report.optString("roRemarks"));
				j++;
				row.createCell(j).setCellValue(report.optString("action"));
			}

			// Write workbook to ByteArrayOutputStream
			workbook.write(out);

			// Convert ByteArrayOutputStream to byte array
			excelBytes = out.toByteArray();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// Convert byte array to Base64 encoded string
		return Base64.getEncoder().encodeToString(excelBytes);
	}

	public static String generateEXEL(List<BranchUserMakerModel> reports) {
		try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			// Create a sheet
			Sheet sheet = workbook.createSheet("Branch User Maker Report");

			// Create the header row
			Row headerRow = sheet.createRow(0);
			String[] columns = { "ID", "Report ID", "Created By", "Updated By", "Created Date", "Updated Date",
					"Branch Name", "Branch Code", "Status", "Action", "RO Document", "Auditor Document", "Report Date",
					"Submission Date", "RO Remarks", "BM Remarks", "HO Remarks", "Auditor Remarks" };

			// Write headers to the first row
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
			}

			// Write data rows
			int rowIdx = 1;
			for (BranchUserMakerModel report : reports) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(report.getId());
				row.createCell(1).setCellValue(report.getReportId());
				row.createCell(2).setCellValue(report.getCreatedby());
				row.createCell(3).setCellValue(report.getUpdatedby());
				row.createCell(4)
						.setCellValue(report.getCreatedDate() != null ? report.getCreatedDate().toString() : "");
				row.createCell(5)
						.setCellValue(report.getUpdatedDate() != null ? report.getUpdatedDate().toString() : "");
				row.createCell(6).setCellValue(report.getBranchname());
				row.createCell(7).setCellValue(report.getBranchcode());
				row.createCell(8).setCellValue(report.getStatus());
				row.createCell(9).setCellValue(report.getAction());
				row.createCell(10)
						.setCellValue(report.getRoDocument() != null ? report.getRoDocument().toString() : "");
				row.createCell(11).setCellValue(
						report.getAuditorDocument() != null ? report.getAuditorDocument().toString() : "");
				row.createCell(12)
						.setCellValue(report.getReportdate() != null ? report.getReportdate().toString() : "");
				row.createCell(13)
						.setCellValue(report.getSubmissiondate() != null ? report.getSubmissiondate().toString() : "");
				row.createCell(14).setCellValue(report.getRoRemarks());
				row.createCell(15).setCellValue(report.getBmRemarks());
				row.createCell(16).setCellValue(report.getHoRemarks());
				row.createCell(17).setCellValue(report.getAuditorRemarks());
			}

			// Resize columns to fit the content
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			// Write the output to a ByteArrayOutputStream
			workbook.write(out);

			// Return the Excel file content as a Base64 string
			return java.util.Base64.getEncoder().encodeToString(out.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<BranchMaster> getBranchesFromExcel() {
		List<BranchMaster> branchList = new ArrayList<>();

		try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("Book2.xlsx");
				Workbook workbook = new XSSFWorkbook(is)) {

			Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

			// Skip header row and iterate over rows
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					BranchMaster branch = new BranchMaster();

					// Handle Branch ID
					Cell branchIdCell = row.getCell(0);
					if (branchIdCell != null && branchIdCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						branch.setBranchid((long) branchIdCell.getNumericCellValue());
					} else {
						branch.setBranchid(null); // Handle null or empty case
					}

					// Handle Branch Name
					Cell branchNameCell = row.getCell(1);
					if (branchNameCell != null && branchNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
						branch.setBranchname(branchNameCell.getStringCellValue());
					} else {
						branch.setBranchname(""); // Handle null or empty case
					}

					// Handle State
					Cell stateCell = row.getCell(6);
					if (stateCell != null && stateCell.getCellType() == Cell.CELL_TYPE_STRING) {
						branch.setState(stateCell.getStringCellValue());
					} else {
						branch.setState(""); // Handle null or empty case
					}

					// Handle City
					Cell cityCell = row.getCell(11);
					if (cityCell != null && cityCell.getCellType() == Cell.CELL_TYPE_STRING) {
						branch.setCity(cityCell.getStringCellValue());
					} else {
						branch.setCity(""); // Handle null or empty case
					}

					// Handle Address
					Cell addressCell = row.getCell(29);
					if (addressCell != null && addressCell.getCellType() == Cell.CELL_TYPE_STRING) {
						branch.setAddress(addressCell.getStringCellValue());
					} else {
						branch.setAddress(""); // Handle null or empty case
					}

					branchList.add(branch);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return branchList;
	}

	public static byte[] generateTodayBranchExcelBytes(List<Object[]> todayBranchesList) {
        byte[] excelBytes = null;

        // Create a new workbook (XLSX format)
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Create a sheet in the workbook
            Sheet sheet = workbook.createSheet("Branches");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Branch Code");
            headerRow.createCell(1).setCellValue("Branch Name");

            // Populate rows with data
            int rowNum = 1;
            for (Object[] rowData : todayBranchesList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue((String) rowData[0]);  // Branch code
                row.createCell(1).setCellValue((String) rowData[1]);  // Branch name
            }

            // Adjust column width
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            // Write workbook to ByteArrayOutputStream
            workbook.write(outputStream);
            excelBytes = outputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return excelBytes;
    }
}
