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
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.service.RetrieveReportService;
import com.suryoday.connector.serviceImpl.GenerateProperty;

@Service
public class RetrieveReportServiceImpl implements RetrieveReportService {

	Logger logger = LoggerFactory.getLogger(RetrieveReportServiceImpl.class);
	
	@Override
	public JSONObject writeExcel(JSONArray json,String userId) {

		String base64 = null;
		JSONObject returnjson = new JSONObject();
		try {
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("new sheet");
			Row row = sheet.createRow(1);

			Row headerRow = sheet.createRow(0); 
			String headerText = "APPLICANT DETAILS"; 
			String headerText2 = "NOMINEE DETAILS"; 
			CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 0, 47);
			CellRangeAddress mergedRegion1 = new CellRangeAddress(0, 0, 48, 63);
			sheet.addMergedRegion(mergedRegion); 
			sheet.addMergedRegion(mergedRegion1); 
			Cell mergedCell = headerRow.createCell(0);
			Cell mergedCell1 = headerRow.createCell(48);
			mergedCell.setCellValue(headerText);
			mergedCell1.setCellValue(headerText2);
			
			CellStyle headerStyle1 = wb.createCellStyle();
			headerStyle1.setAlignment(HorizontalAlignment.CENTER);
			mergedCell.setCellStyle(headerStyle1); 
			mergedCell1.setCellStyle(headerStyle1); 
			Row row1 = null;
			CellStyle headerStyle = wb.createCellStyle();

			Font headerFont = wb.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 12);
			//headerFont.setColor(IndexedColors.BLACK.index);
			headerStyle.setFont(headerFont);
			headerStyle1.setFont(headerFont);
			headerStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
			//headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			// Create a cell and put a value in it.

			String[] coloumnHeading = { "applicationNo", "customerID", "memberName", "dateOfBirth", "address", "state",
					"postal", "branchId", "amount", "status", "createDate", "updateDate", "mobilePhone", "interestRate","originationFees",
					"paymentAmount","term","totalAmountFinanced","amountRequested","interestAmount",
					"buisness", "createdBy", "existingLoanPurpose", "mobileNoVerify", "monthlyBalance", "other",
					"remarks", "rent", "residenceStability", "roofType", "total", "totalMonthlyEmi",
					"totalMonthlyIncome", "transportation", "utilityBill", "vintage","centerId","schemeId","purposeCategoryId",
					"loanPurposeId","nomineeName","savingAccount","sanctionAmount","sanctionTENURE","finalSanctionAmount" ,"updatedBy",
					"purposeid","subCategoryPurposeId","title","firstName","lastName","dob","age","RelationshipWithApplicant","mobile",
					"mobileVerify","aadharCard","aadharNoVerify","voterId","voterIdVerify","panCard","pancardNoVerify","occupation",
					"earning","monthlyIncome","primarySourceOfIncome"};
			for (int i = 0; i < coloumnHeading.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(coloumnHeading[i]);
				cell.setCellStyle(headerStyle);
			}

			for (int i = 0; i < json.length(); i++) {
				JSONObject obj = json.getJSONObject(i);

				long applicationNo = obj.getLong("applicationNo");
				long customerID = obj.getLong("customerID");
				String memberName = obj.getString("memberName");
				String dateOfBirth = obj.getString("dateOfBirth");
				String address = obj.getString("address");
				String state = obj.getString("state");
				String postal = obj.getString("postal");
				String centerId = obj.getString("centerId");
				String schemeId = obj.getString("schemeId");
				String purposeCategoryId = obj.getString("purposeCategoryId");
				String loanPurposeId = obj.getString("loanPurposeId");
				String nomineeName = obj.getString("nomineeName");
				String savingAccount = obj.getString("savingAccount");
				String branchId = obj.getString("branchId");
				String amount = obj.getString("amount");
				String status = obj.getString("status");
				String createDate = obj.getString("createDate");
				String updateDate = obj.getString("updateDate");
				long mobilePhone = obj.getLong("mobilePhone");
				String sanctionTENURE = obj.getString("sanctionTENURE");
				String sanctionAmount = obj.getString("sanctionAmount");
				String updatedBy = obj.getString("updatedBy");
				String purposeid = obj.getString("purposeid");
				String subCategoryPurposeId = obj.getString("subCategoryPurposeId");
				String finalSanctionAmount =obj.getString("finalSanctionAmount");
				String amountRequested =obj.getString("amountRequested");
				String interestRate =obj.getString("interestRate");
				String originationFees =obj.getString("originationFees");
				String paymentAmount =obj.getString("paymentAmount");
				String term =obj.getString("term");
				String totalAmountFinanced =obj.getString("totalAmountFinanced");
				String interestAmount =obj.getString("interestAmount");
				
//				Object object=null;
//				if(obj.has("responseEmi")) {
//					 object = obj.get("responseEmi");
//				}
//				String responseEMI = null;
//				String interestRate ="NA";
//				String originationFees ="NA";
//				String paymentAmount ="NA";
//				String term ="NA";
//				String totalAmountFinanced ="NA";
//				String amountRequested ="NA";
//				String interestAmount ="NA";
//				
//				//if(!obj.get("responseEmi").equals(null)){
//				if(object !=null){
//					interestRate = obj.getJSONObject("responseEmi").getString("INTERESTRATE");
//					originationFees = obj.getJSONObject("responseEmi").getString("ORIGINATIONFEES");
//					paymentAmount = obj.getJSONObject("responseEmi").getString("PAYMENTAMOUNT");
//					term = obj.getJSONObject("responseEmi").getString("TERM");
//					totalAmountFinanced = obj.getJSONObject("responseEmi").getString("TOTALAMOUNTFINANCED");
//					amountRequested = obj.getJSONObject("responseEmi").getString("AMOUNTREQUESTED");
//					interestAmount = obj.getJSONObject("responseEmi").getString("INTERESTAMOUNT");	
//				}
				String buisness = null;
				if(!obj.get("buisness").equals(null)) {
					 buisness = obj.getString("buisness");
				}else {
					buisness = null;
				}
				
				String createdBy = obj.getString("createdBy");
				Object object2 = obj.get("existingLoanPurpose");
				String existingLoanPurpose = null;
				if(!object2.equals(null)) {
					existingLoanPurpose = obj.getString("existingLoanPurpose");
				}else {
					existingLoanPurpose = null;
				}
			 
				String mobileNoVerify = obj.getString("mobileNoVerify");
				double monthlyBalance = obj.getDouble("monthlyBalance");
				double other = obj.getDouble("other");
				 String remarks = obj.get("remarks").toString();
				double rent = obj.getDouble("rent");
				String residenceStability = null;
				if(!obj.get("residenceStability").equals(null)) {
				residenceStability = obj.getString("residenceStability");
				}else {
					residenceStability = null;
				}
				String roofType = null;
				if(!obj.get("roofType").equals(null)) {
				 roofType = obj.getString("roofType");
				}else {
					roofType=null;
				}
				double total = obj.getDouble("total");
				double totalMonthlyEmi = obj.getDouble("totalMonthlyEmi");
				double totalMonthlyIncome = obj.getDouble("totalMonthlyIncome");
				double transportation = obj.getDouble("transportation");
				String utilityBill = null;
				if(!obj.get("utilityBill").equals(null)) {
				 utilityBill = obj.getString("utilityBill");
				}else {
					utilityBill = null;
				}
				String vintage = null;
				if(!obj.get("vintage").equals(null)) {
				 vintage = obj.getString("vintage");
				}else {
					vintage = null;
				}
				String title =obj.getString("title");
				String firstName =obj.getString("firstName");
				String lastName =obj.getString("lastName");
				String dob =obj.getString("dob");
				int age =obj.getInt("age");
				String member =obj.getString("member");
				long mobile =obj.getLong("mobile");
				String mobileVerify =obj.getString("mobileVerify");
				String aadharCard =obj.getString("aadharCard");
				String aadharNoVerify =obj.getString("aadharNoVerify");
				String voterId =obj.getString("voterId");
				String voterIdVerify =obj.getString("voterIdVerify");
				String panCard =obj.getString("panCard");
				String pancardNoVerify =obj.getString("pancardNoVerify");
				String occupation =obj.getString("occupation");
				String earning =obj.getString("earning");
				double monthlyIncome =obj.getDouble("monthlyIncome");
				String primarySourceOfIncome =obj.getString("primarySourceOfIncome");
				
				row1 = sheet.createRow((short) i + 2);
				row1.createCell(0).setCellValue(applicationNo);
				row1.createCell(1).setCellValue(customerID);
				row1.createCell(2).setCellValue(memberName);
				row1.createCell(3).setCellValue(dateOfBirth);
				row1.createCell(4).setCellValue(address);
				row1.createCell(5).setCellValue(state);
				row1.createCell(6).setCellValue(postal);
				row1.createCell(7).setCellValue(branchId);
				row1.createCell(8).setCellValue(amount);
				row1.createCell(9).setCellValue(status);
				row1.createCell(10).setCellValue(createDate);
				row1.createCell(11).setCellValue(updateDate);
				row1.createCell(12).setCellValue(mobilePhone);
				row1.createCell(13).setCellValue(interestRate);
				row1.createCell(14).setCellValue(originationFees);
				row1.createCell(15).setCellValue(paymentAmount);
				row1.createCell(16).setCellValue(term);
				row1.createCell(17).setCellValue(totalAmountFinanced);
				row1.createCell(18).setCellValue(amountRequested);
				row1.createCell(19).setCellValue(interestAmount);
				row1.createCell(20).setCellValue(buisness);
				row1.createCell(21).setCellValue(createdBy);
				row1.createCell(22).setCellValue(existingLoanPurpose);
				row1.createCell(23).setCellValue(mobileNoVerify);
				row1.createCell(24).setCellValue(monthlyBalance);
				row1.createCell(25).setCellValue(other);
				row1.createCell(26).setCellValue(remarks);
				row1.createCell(27).setCellValue(rent);
				row1.createCell(28).setCellValue(residenceStability);
				row1.createCell(29).setCellValue(roofType);
				row1.createCell(30).setCellValue(total);
				row1.createCell(31).setCellValue(totalMonthlyEmi);
				row1.createCell(32).setCellValue(totalMonthlyIncome);
				row1.createCell(33).setCellValue(transportation);
				row1.createCell(34).setCellValue(utilityBill);
				row1.createCell(35).setCellValue(vintage);
				row1.createCell(36).setCellValue(centerId);
				row1.createCell(37).setCellValue(schemeId);
				row1.createCell(38).setCellValue(purposeCategoryId);
				row1.createCell(39).setCellValue(loanPurposeId);
				row1.createCell(40).setCellValue(nomineeName);
				row1.createCell(41).setCellValue(savingAccount);
				row1.createCell(42).setCellValue(sanctionAmount);
				row1.createCell(43).setCellValue(sanctionTENURE);
				row1.createCell(44).setCellValue(finalSanctionAmount);
				row1.createCell(45).setCellValue(updatedBy);
				row1.createCell(46).setCellValue(purposeid);
				row1.createCell(47).setCellValue(subCategoryPurposeId);
				row1.createCell(48).setCellValue(title);
				row1.createCell(49).setCellValue(firstName);
				row1.createCell(50).setCellValue(lastName);
				row1.createCell(51).setCellValue(dob);
				row1.createCell(52).setCellValue(age);
				row1.createCell(53).setCellValue(member);
				row1.createCell(54).setCellValue(mobile);
				row1.createCell(55).setCellValue(mobileVerify);
				row1.createCell(56).setCellValue(aadharCard);
				row1.createCell(57).setCellValue(aadharNoVerify);
				row1.createCell(58).setCellValue(voterId);
				row1.createCell(59).setCellValue(voterIdVerify);
				row1.createCell(60).setCellValue(panCard);
				row1.createCell(61).setCellValue(pancardNoVerify);
				row1.createCell(62).setCellValue(occupation);
				row1.createCell(63).setCellValue(earning);
				row1.createCell(64).setCellValue(monthlyIncome);
				row1.createCell(65).setCellValue(primarySourceOfIncome);
			}

			GenerateProperty x = GenerateProperty.getInstance();
			x.getappprop();

			FileOutputStream fileOut = new FileOutputStream(x.reports+"workbook"+userId+".xlsx");

			wb.write(fileOut);
			fileOut.close();

			base64 = exceltobase64(x.reports+"workbook"+userId+".xlsx");
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
