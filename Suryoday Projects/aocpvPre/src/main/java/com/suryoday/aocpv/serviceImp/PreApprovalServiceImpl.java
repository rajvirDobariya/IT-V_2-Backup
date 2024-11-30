package com.suryoday.aocpv.serviceImp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.service.PreApprovalService;
import com.suryoday.connector.serviceImpl.GenerateProperty;

@Service
public class PreApprovalServiceImpl implements PreApprovalService {

	@Override
	public JSONObject writeExcel(JSONArray json) {

		String base64 = null;
		JSONObject returnjson = new JSONObject();
		try {
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("new sheet");
			Row row = sheet.createRow(0);

			Row row1 = null;
			CellStyle headerStyle = wb.createCellStyle();

			Font headerFont = wb.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 12);
			//headerFont.setColor(IndexedColors.BLACK.index);
			headerStyle.setFont(headerFont);
			headerStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
			//headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			// Create a cell and put a value in it.

			String[] coloumnHeading = { "mobilePhone", "referenceNo", "customerID", "memberName", "branchId", "EligibleAmount",
					"status", "createDate", "updatedDate" , "city","state","area"};
			for (int i = 0; i < coloumnHeading.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(coloumnHeading[i]);
				cell.setCellStyle(headerStyle);
			}

			for (int i = 0; i < json.length(); i++) {
				JSONObject obj = json.getJSONObject(i);
				long mobilePhone = obj.getLong("mobilePhone");
				String referenceNo = obj.getString("referenceNo");
				long customerID = obj.getLong("customerID");
				String memberName = obj.getString("memberName");
				long branchId = obj.getLong("branchId");
				double amount = obj.getDouble("amount");
				String status = obj.getString("status");
				String createDate = obj.getString("createDate");
				String updatedDate = obj.getString("updatedDate");
				String city = obj.getString("city");
				String state = obj.getString("state");
				String area = obj.getString("area");

				row1 = sheet.createRow((short) i + 1);
				row1.createCell(0).setCellValue(mobilePhone);
				row1.createCell(1).setCellValue(referenceNo);
				row1.createCell(2).setCellValue(customerID);
				row1.createCell(3).setCellValue(memberName);
				row1.createCell(4).setCellValue(branchId);
				row1.createCell(5).setCellValue(amount);
				row1.createCell(6).setCellValue(status);
				row1.createCell(5).setCellValue(createDate);
				row1.createCell(6).setCellValue(updatedDate);
				row1.createCell(6).setCellValue(city);
				row1.createCell(6).setCellValue(state);
				row1.createCell(6).setCellValue(area);
				
			}

			GenerateProperty x = GenerateProperty.getInstance();

//FileOutputStream fileOut = new FileOutputStream("/home/appadmin@suryodaybank.local/tomcat/tomcat/webapps/ExcelFiles/branchdigi/workbook.xlsx");
			// FileOutputStream fileOut = new
			// FileOutputStream("C:\\Users\\Neosoft\\AppData/workbook.xlsx");
			FileOutputStream fileOut = new FileOutputStream(
					"/opt/tomcat-suryodaysarathi/apache-tomcat-9.0.37/reports/workbook.xlsx");

			wb.write(fileOut);
			fileOut.close();
			// base64 = exceltobase64("C:\\Users\\Neosoft\\AppData/workbook.xlsx");
			base64 = exceltobase64("/opt/tomcat-suryodaysarathi/apache-tomcat-9.0.37/reports/workbook.xlsx");
			returnjson.put("Base64", base64);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnjson;

	}

	public String exceltobase64(String filepath) {
		String base64 = null;
		try {
			File file = new File(filepath);
			FileInputStream fis = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fis.read(bytes);
			base64 = Base64.getEncoder().encodeToString(bytes);

		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		return base64;
	}

}
