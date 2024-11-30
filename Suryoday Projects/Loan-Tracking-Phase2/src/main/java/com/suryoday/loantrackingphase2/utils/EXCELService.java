package com.suryoday.loantrackingphase2.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

public class EXCELService {

	public static String generateNOIUpdateExcelBase64(JSONObject jsonObject) {
		// Create workbook and sheet
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Activities");

		// Create header row
		Row headerRow = sheet.createRow(0);
		String[] headers = { "Final Status", "Mortgage Type", "Selfie Photo", "Index2", "NOI Pending With",
				"MOE Challan", "NOI Initiated Date", "NOI Receive Date", "Created Date", "KYC", "Created By",
				"Sanction Letter", "Token Number", "Disbursement Date", "ID", "Loan ID", "Remarks",
				"NOI Comformation From Vendor" };

		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
		}

		// Extract data from JSON
		JSONArray activities = jsonObject.getJSONObject("Data").getJSONArray("activities");

		// Fill rows with data
		for (int i = 0; i < activities.length(); i++) {
			JSONObject activity = activities.getJSONObject(i);
			Row row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(activity.getString("finalStatus"));
			row.createCell(1).setCellValue(activity.getString("mortgageType"));
			row.createCell(2).setCellValue(activity.getBoolean("selfiePhoto"));
			row.createCell(3).setCellValue(activity.getBoolean("index2"));
			row.createCell(4).setCellValue(activity.getString("noiPendingWith"));
			row.createCell(5).setCellValue(activity.getBoolean("moeChallan"));
			row.createCell(6).setCellValue(activity.getString("noiInitiatedDate"));
			row.createCell(7).setCellValue(activity.getString("noiReceiveDate"));
			row.createCell(8).setCellValue(activity.getString("createdDate"));
			row.createCell(9).setCellValue(activity.getBoolean("KYC"));
			row.createCell(10).setCellValue(activity.getInt("createdBy"));
			row.createCell(11).setCellValue(activity.getBoolean("sanctionLetter"));
			row.createCell(12).setCellValue(activity.getString("tokenNumber"));
			row.createCell(13).setCellValue(activity.getString("disbursementDate"));
			row.createCell(14).setCellValue(activity.getInt("id"));
			row.createCell(15).setCellValue(activity.getString("loanId"));
			row.createCell(16).setCellValue(activity.getString("remarks"));
			row.createCell(16).setCellValue(activity.getBoolean("noiComformationFromVendor"));
		}

		// Convert workbook to byte array and encode it as Base64
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			workbook.write(bos);
			workbook.close();
			byte[] excelBytes = bos.toByteArray();
			return Base64.getEncoder().encodeToString(excelBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String generateHealthCheckExcelBase64(JSONObject jsonObject) {
		// Create workbook and sheet
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("HealthCheckActivities");

		// Create header row
		Row headerRow = sheet.createRow(0);
		String[] headers = { "ID", "Status", "PAN No", "Customer ID", "Customer Name", "File Receive Date",
				"BDM Employee ID", "SM Employee ID", "ASM Employee ID", "BDM Employee Name", "SM Employee Name",
				"ASM Employee Name", "Customer Sourcing Branch Name", "Product ID", "Product Data", "Queries",
				"Remarks", "Loan Amount", "Application ID", "First Time Right", "Billed or Dispatched" };

		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
		}

		// Extract data from JSON
		JSONArray healthCheckActivities = jsonObject.getJSONObject("Data").getJSONArray("activities");

		// Fill rows with data
		for (int i = 0; i < healthCheckActivities.length(); i++) {
			JSONObject activity = healthCheckActivities.getJSONObject(i);
			Row row = sheet.createRow(i + 1);

			row.createCell(0).setCellValue(activity.getLong("id"));
			row.createCell(1).setCellValue(activity.getString("status"));
			row.createCell(2).setCellValue(activity.getString("panNo"));
			row.createCell(3).setCellValue(activity.getString("custId"));
			row.createCell(4).setCellValue(activity.getString("custName"));
			row.createCell(5).setCellValue(activity.getString("fileReceiveDate"));
			row.createCell(6).setCellValue(activity.getLong("BDMEmpId"));
			row.createCell(7).setCellValue(activity.getLong("SMEmpId"));
			row.createCell(8).setCellValue(activity.getLong("ASMEmpId"));
			row.createCell(9).setCellValue(activity.getString("BDMEmpName"));
			row.createCell(10).setCellValue(activity.getString("SMEmpName"));
			row.createCell(11).setCellValue(activity.getString("ASMEmpName"));
			row.createCell(12).setCellValue(activity.getString("custSourcingBranchName"));
			row.createCell(13).setCellValue(activity.getLong("productCode"));
			row.createCell(14).setCellValue("productData");

			// Handle queries (assumed as a JSON array of strings)
			JSONArray queries = activity.optJSONArray("queries");
			if (queries != null) {
				row.createCell(15).setCellValue(queries.join(", "));
			} else {
				row.createCell(15).setCellValue("");
			}

			row.createCell(16).setCellValue(activity.optString("remarks", ""));
			row.createCell(17).setCellValue(activity.optString("loanAmount", ""));
			row.createCell(18).setCellValue(activity.optString("applicationId", ""));
			row.createCell(19).setCellValue(activity.optString("firstTimeRight", ""));
			row.createCell(20).setCellValue(activity.optString("billedOrDispatched", ""));
		}

		// Convert workbook to byte array and encode it as Base64
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			workbook.write(bos);
			workbook.close();
			byte[] excelBytes = bos.toByteArray();
			return Base64.getEncoder().encodeToString(excelBytes);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
