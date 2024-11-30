package com.suryoday.travelvisit.utils;

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

public class Excel {

	public static String visitToExcelBase64(JSONObject jsonObject) {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			// Create a sheet
			Sheet sheet = workbook.createSheet("Visit To");

			// Get the "activities" array from the JSON object
			JSONObject data = jsonObject.getJSONObject("Data");
			JSONArray activities = data.getJSONArray("activities");

			// Create header row
			Row headerRow = sheet.createRow(0);
			String[] headers = { "ID", "Name", "Product", "Address", "CIF No", "Center Report", "Map URL Link",
					"Alternative Phone", "Phone Number", "Created Date", "Created By", "Remarks" };
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}

			// Populate data rows
			for (int i = 0; i < activities.length(); i++) {
				Row row = sheet.createRow(i + 1);
				JSONObject activity = activities.getJSONObject(i);

				row.createCell(0).setCellValue(activity.getInt("id"));
				row.createCell(1).setCellValue(activity.getString("name"));
				row.createCell(2).setCellValue(activity.getString("product"));
				row.createCell(3).setCellValue(activity.getString("address"));
				row.createCell(4).setCellValue(activity.getString("cifNo"));
				row.createCell(5).setCellValue(activity.getString("centerReport"));
				row.createCell(6).setCellValue(activity.getString("mapUrlLink"));
				row.createCell(7).setCellValue(activity.getString("alternativePhoneNumber"));
				row.createCell(8).setCellValue(activity.getString("phoneNumber"));
				row.createCell(9).setCellValue(activity.getString("createdDate"));
				row.createCell(10).setCellValue(activity.getInt("createdBy"));
				row.createCell(11).setCellValue(activity.getString("remarks"));
			}

			// Convert workbook to Base64
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			byte[] bytes = outputStream.toByteArray();
			return Base64.getEncoder().encodeToString(bytes);

		} catch (Exception e) {
//			throw new 
			return null;
		}
	}

	public static String lucExcelBase64(JSONObject result) {
		String base64Excel = "";
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Luc");

			// Create the header row
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("ID");
			headerRow.createCell(1).setCellValue("Name");
			headerRow.createCell(2).setCellValue("CIF No");
			headerRow.createCell(3).setCellValue("Address");
			headerRow.createCell(4).setCellValue("Phone Number");
			headerRow.createCell(5).setCellValue("Alternative Phone Number");
			headerRow.createCell(6).setCellValue("Remarks");
			headerRow.createCell(7).setCellValue("Business");
			headerRow.createCell(8).setCellValue("Map URL Link");
			headerRow.createCell(9).setCellValue("Created By");
			headerRow.createCell(10).setCellValue("Created Date");

			// Extract activities array
			JSONArray activities = result.getJSONObject("Data").getJSONArray("activities");

			// Loop through the activities and populate the sheet
			for (int i = 0; i < activities.length(); i++) {
				JSONObject activity = activities.getJSONObject(i);
				Row row = sheet.createRow(i + 1); // Row starts at index 1 (after the header)

				row.createCell(0).setCellValue(activity.getInt("id"));
				row.createCell(1).setCellValue(activity.getString("name"));
				row.createCell(2).setCellValue(activity.getString("cifNo"));
				row.createCell(3).setCellValue(activity.getString("address"));
				row.createCell(4).setCellValue(activity.getString("phoneNumber"));
				row.createCell(5).setCellValue(activity.getString("alternativePhoneNumber"));
				row.createCell(6).setCellValue(activity.getString("remarks"));
				row.createCell(7).setCellValue(activity.getString("business"));
				row.createCell(8).setCellValue(activity.getString("mapUrlLink"));
				row.createCell(9).setCellValue(activity.getInt("createdBy"));
				row.createCell(10).setCellValue(activity.getString("createdDate"));
			}

			// Auto-size all the columns
			for (int i = 0; i < 11; i++) {
				sheet.autoSizeColumn(i);
			}

			// Write the Excel file to a byte array and encode it in Base64
			try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
				workbook.write(outputStream);
				byte[] excelBytes = outputStream.toByteArray();
				base64Excel = Base64.getEncoder().encodeToString(excelBytes);
			}

		} catch (IOException e) {

		}
		return base64Excel;
	}

	public static String odCustomerVisitExcelBase64(JSONObject result) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Od Customer Visits");

		// Create header row
		Row headerRow = sheet.createRow(0);
		String[] headers = { "ID", "Name", "Address", "Phone Number", "Alternative Phone Number", "Business", "CIF No",
				"Reason", "Remarks", "PTP Date","Created Date" };
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
		}

		// Populate data
		JSONArray activities = result.getJSONObject("Data").getJSONArray("activities");
		for (int i = 0; i < activities.length(); i++) {
			JSONObject activity = activities.getJSONObject(i);
			Row row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(activity.optLong("id"));
			row.createCell(1).setCellValue(activity.optString("name"));
			row.createCell(2).setCellValue(activity.optString("address"));
			row.createCell(3).setCellValue(activity.optString("phoneNumber"));
			row.createCell(4).setCellValue(activity.optString("alternativePhoneNumber"));
			row.createCell(5).setCellValue(activity.optString("business"));
			row.createCell(6).setCellValue(activity.optString("cifNo"));
			row.createCell(7).setCellValue(activity.optString("reason"));
			row.createCell(8).setCellValue(activity.optString("remarks"));
			row.createCell(8).setCellValue(activity.optString("ptpDate"));
			row.createCell(9).setCellValue(activity.optString("createdDate"));
		}

		// Convert to Base64
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			workbook.write(byteArrayOutputStream);
			byte[] excelBytes = byteArrayOutputStream.toByteArray();
			return Base64.getEncoder().encodeToString(excelBytes);
		} catch (IOException e) {
			e.printStackTrace();
			return null; // Handle the exception as needed
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				  throw new RuntimeException("Error writing to Excel", e);
			}
		}
	}

	public static String branchHygieneExcelBase64(JSONObject result) {
	    String base64Excel = "";
	    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
	        Sheet sheet = workbook.createSheet("BranchHygiene");

	        // Create the header row
	        Row headerRow = sheet.createRow(0);
	        headerRow.createCell(0).setCellValue("ID");
	        headerRow.createCell(1).setCellValue("Branch ID");
	        headerRow.createCell(2).setCellValue("Branch Name");
	        headerRow.createCell(3).setCellValue("Receipt Book Check");
	        headerRow.createCell(4).setCellValue("Dress Code Check");
	        headerRow.createCell(5).setCellValue("Keys Check");
	        headerRow.createCell(6).setCellValue("Cash Verification");
	        headerRow.createCell(7).setCellValue("Disto Application Downloaded By All");
	        headerRow.createCell(8).setCellValue("Map URL Link");
	        headerRow.createCell(9).setCellValue("Created By");
	        headerRow.createCell(10).setCellValue("Created Date");
	        headerRow.createCell(10).setCellValue("Remarks");

	        // Extract activities array
	        JSONArray activities = result.getJSONObject("Data").getJSONArray("activities");

	        // Loop through the activities and populate the sheet
	        for (int i = 0; i < activities.length(); i++) {
	            JSONObject activity = activities.getJSONObject(i);
	            Row row = sheet.createRow(i + 1); // Row starts at index 1 (after the header)

	            row.createCell(0).setCellValue(activity.getLong("id"));
	            row.createCell(1).setCellValue(activity.getString("branchId"));
	            row.createCell(2).setCellValue(activity.getString("branchName"));
	            row.createCell(3).setCellValue(activity.getString("receiptBookCheck"));
	            row.createCell(4).setCellValue(activity.getString("dressCodeCheck"));
	            row.createCell(5).setCellValue(activity.getString("keysCheck"));
	            row.createCell(6).setCellValue(activity.getString("cashVerification"));
	            row.createCell(7).setCellValue(activity.getString("distoApplicationDownloadedByAll"));
	            row.createCell(8).setCellValue(activity.getString("mapUrlLink"));
	            row.createCell(9).setCellValue(activity.getLong("createdBy"));
	            row.createCell(10).setCellValue(activity.getString("createdDate"));
	            row.createCell(10).setCellValue(activity.getString("remarks"));
	        }

	        // Auto-size all the columns
	        for (int i = 0; i < 11; i++) {
	            sheet.autoSizeColumn(i);
	        }

	        // Write the Excel file to a byte array and encode it in Base64
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            workbook.write(outputStream);
	            byte[] excelBytes = outputStream.toByteArray();
	            base64Excel = Base64.getEncoder().encodeToString(excelBytes);
	        }

	    } catch (IOException e) {
			  throw new RuntimeException("Error writing to Excel", e);
	    }
	    return base64Excel;
	}

}
