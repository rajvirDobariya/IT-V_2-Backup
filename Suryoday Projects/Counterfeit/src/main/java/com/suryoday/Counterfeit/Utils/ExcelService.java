package com.suryoday.Counterfeit.Utils;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.suryoday.Counterfeit.Mapper.CounterfeitDailyMapper;
import com.suryoday.Counterfeit.Mapper.CounterfeitMonthlyMapper;
import com.suryoday.Counterfeit.dao.CounterfeitDailyDAO;
import com.suryoday.Counterfeit.dao.CounterfeitMonthlyDAO;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class ExcelService {

	public static String createExcel(JSONObject request) {
		JSONObject data = request.getJSONObject("Data");
		JSONArray counterfeits = data.getJSONArray("counterfeits");
		String dateType = data.getString("dateType");
		String excelBase64 = "";
		if (dateType.equals("DAILY")) {
			excelBase64 = getDaily(counterfeits);
		} else if (dateType.equals("MONTHLY")) {
			excelBase64 = getMonthly(counterfeits);
		}

		return excelBase64;
	}

	private static String getMonthly(JSONArray counterfeits) {
	    try {
	        // Create Workbook and Sheet
	        Workbook workbook = new XSSFWorkbook();
	        Sheet sheet = workbook.createSheet("Monthly Counterfeits");

	        // Add Header Row
	        Row headerRow = sheet.createRow(0);
	        String[] headers = { "ID", "Is Detect", "Branch Code", "Branch Name", "Status", "Month", "Created Date",
	                "RO Remarks", "BM Remarks" };

	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	            cell.setCellStyle(getHeaderCellStyle(workbook));
	        }

	        // Populate Rows
	        for (int i = 0; i < counterfeits.length(); i++) {
	            JSONObject counterfeit = counterfeits.getJSONObject(i);
	            Row row = sheet.createRow(i + 1);

	            row.createCell(0).setCellValue(counterfeit.optLong("id"));
	            row.createCell(1).setCellValue(counterfeit.optString("isDetect"));
	            row.createCell(2).setCellValue(counterfeit.optString("branchCode"));
	            row.createCell(3).setCellValue(counterfeit.optString("branchName"));
	            row.createCell(4).setCellValue(counterfeit.optString("status"));
	            row.createCell(5).setCellValue(counterfeit.optString("month"));
	            row.createCell(6).setCellValue(counterfeit.optString("createdDate"));
	            row.createCell(7).setCellValue(counterfeit.optString("roRemarks"));
	            row.createCell(8).setCellValue(counterfeit.optString("bmRemarks"));
	        }

	        // Adjust column widths
	        for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }

	        // Write to ByteArrayOutputStream
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        workbook.write(outputStream);
	        workbook.close();

	        // Convert to Base64
	        return Base64.getEncoder().encodeToString(outputStream.toByteArray());

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	public static String getDaily(JSONArray counterfeits) {
		try {
			// Create Workbook and Sheet
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Counterfeits");

			// Add Header Row
			Row headerRow = sheet.createRow(0);
			String[] headers = { "ID", "Daily/Monthly", "Is Detect", "Month", "Branch Code", "Branch Name", "Status",
					"RO ID", "RO Role", "BM ID", "BM Role", "Created Date", "Created By", "Impounded Notes",
					"Register Updated", "Acknowledgment Receipt", "Updated Date", "Updated By", "RO Remarks",
					"BM Remarks" };

			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(getHeaderCellStyle(workbook));
			}

			// Populate Rows
			for (int i = 0; i < counterfeits.length(); i++) {
				JSONObject counterfeit = counterfeits.getJSONObject(i);
				Row row = sheet.createRow(i + 1);

				row.createCell(0).setCellValue(counterfeit.optLong("id"));
				row.createCell(1).setCellValue(counterfeit.optString("dailyMonthly"));
				row.createCell(2).setCellValue(counterfeit.optString("isDetect"));
				row.createCell(3).setCellValue(counterfeit.optString("month"));
				row.createCell(4).setCellValue(counterfeit.optString("branchCode"));
				row.createCell(5).setCellValue(counterfeit.optString("branchName"));
				row.createCell(6).setCellValue(counterfeit.optString("status"));
				row.createCell(7).setCellValue(counterfeit.optString("roId"));
				row.createCell(8).setCellValue(counterfeit.optString("roRole"));
				row.createCell(9).setCellValue(counterfeit.optString("bmId"));
				row.createCell(10).setCellValue(counterfeit.optString("bmRole"));
				row.createCell(11).setCellValue(counterfeit.optString("createdDate"));
				row.createCell(12).setCellValue(counterfeit.optString("createdBy"));
				row.createCell(13).setCellValue(counterfeit.optString("theNotesHaveBeenImpounded"));
				row.createCell(14).setCellValue(counterfeit.optString("theRegisterUpdatedWithDetails"));
				row.createCell(15).setCellValue(counterfeit.optString("anAcknowledgmentReceiptPrepared"));
				row.createCell(16).setCellValue(counterfeit.optString("updateDate"));
				row.createCell(17).setCellValue(counterfeit.optString("updateBy"));
				row.createCell(18).setCellValue(counterfeit.optString("roRemarks"));
				row.createCell(19).setCellValue(counterfeit.optString("bmRemarks"));
			}

			// Adjust column widths
			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			// Write to ByteArrayOutputStream
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			workbook.close();

			// Convert to Base64
			return Base64.getEncoder().encodeToString(outputStream.toByteArray());

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private static CellStyle getHeaderCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		style.setFont(font);
		return style;
	}

}
