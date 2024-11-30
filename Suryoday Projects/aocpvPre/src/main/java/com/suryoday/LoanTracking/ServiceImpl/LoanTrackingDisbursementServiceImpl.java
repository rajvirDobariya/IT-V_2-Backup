package com.suryoday.LoanTracking.ServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.LoanTracking.Pojo.LoanTrackingDisbursement;
import com.suryoday.LoanTracking.Repository.LoanTrackingDisbursementRepository;
import com.suryoday.LoanTracking.Service.LoanTrackingDisbursementService;
import com.suryoday.aocpv.exceptionhandling.DuplicateEntryException;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.connector.pojo.User;
import com.suryoday.connector.repository.UserRepository;


@Component
public class LoanTrackingDisbursementServiceImpl implements LoanTrackingDisbursementService {
	private static Logger logger = LoggerFactory.getLogger(LoanTrackingDisbursementServiceImpl.class);
	@Autowired
	LoanTrackingDisbursementRepository disbursementrepo;
	@Autowired
	UserRepository userrepo;

	@Override
	public String createApplicationNo() {
		String applicationNo = (LocalDate.now().toString().replace("-", "") + "0001").substring(2, 12);

		Optional<String> fetchLastApplicationNo = disbursementrepo.fetchLastApplicationNo();

		if (fetchLastApplicationNo.isPresent()) {
			logger.debug("If ApplicationNo is Present");

			String application_No = fetchLastApplicationNo.get();
			logger.debug(application_No);
			String dateInDB = application_No.substring(0, 6);
			String currentDate = LocalDate.now().toString().replace("-", "").substring(2, 8);
			if (currentDate.equals(dateInDB)) {
				logger.debug("If current Date  is equal to db date");
				Long applicationno = Long.parseLong(application_No);
				applicationno++;
				applicationNo = applicationno.toString();
				logger.debug(applicationNo + "after increment");
			}

//			Long application = Long.parseLong(application_No);
//			application++;
//			applicationNo = application.toString();
		}
		return applicationNo;
	}

	@Override
	public void save(LoanTrackingDisbursement disbursement, String panNo,String productType) {
		Optional<LoanTrackingDisbursement> byPanCard = disbursementrepo.getByPanCard(panNo,productType);
		if (byPanCard.isPresent()) {
			throw new DuplicateEntryException("Application ALready Exists");
		}
		disbursementrepo.save(disbursement);

	}

	@Override
	public List<LoanTrackingDisbursement> fetchByApplicationId(String applicationId) {
		long applicationNo = Long.parseLong(applicationId);
		List<LoanTrackingDisbursement> list = disbursementrepo.fetchByApplicationId(applicationNo);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No record found");
		}
		return list;
	}

	@Override
	public List<LoanTrackingDisbursement> fetchByDate(LocalDateTime startdate, LocalDateTime enddate,
			String createdBy) {

		List<LoanTrackingDisbursement> list = disbursementrepo.fetchByDate(startdate, enddate, createdBy);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public List<User> fetchByCredit(String assignrole) {
		String userpermissionid = "";
//		if (assignrole.equalsIgnoreCase("CREDIT")) {
//			userpermissionid = "17";
//		} else if (assignrole.equalsIgnoreCase("RO")) {
//			userpermissionid = "16";
//		} else if (assignrole.equalsIgnoreCase("CREDIT_OPS")) {
//			userpermissionid = "20";
//		} else if (assignrole.equalsIgnoreCase("OPS")) {
//			userpermissionid = "13";
//		}
		if (assignrole.equalsIgnoreCase("CREDIT")) {
			userpermissionid = "5";
		} else if (assignrole.equalsIgnoreCase("RO")) {
			userpermissionid = "9";
		} else if (assignrole.equalsIgnoreCase("CREDIT_OPS")) {
			userpermissionid = "14";
		} else if (assignrole.equalsIgnoreCase("OPS")) {
			userpermissionid = "13";
		}
		List<User> list = userrepo.fetchByCredit(userpermissionid);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No record found");
		}
		return list;
	}

	@Override
	public List<LoanTrackingDisbursement> fetchByUserRoleAndUserId(String userRole, String userId,
			LocalDateTime startdate, LocalDateTime enddate) {
		String userRole1 = "";
		if (userRole.equalsIgnoreCase("CREDIT")) {
			userRole1 = "CREDIT_DISCREPANCY";
			List<LoanTrackingDisbursement> list2 = disbursementrepo.fetchByUserRoleAndUserId(userRole, userRole1,
					userId, startdate, enddate);
			if (list2.isEmpty()) {
				throw new NoSuchElementException("No record found");
			}
			return list2;
		} else if (userRole.equalsIgnoreCase("CREDIT_OPS")) {
			userRole1 = "CREDITOPS_DISCREPANCY";
			List<LoanTrackingDisbursement> list2 = disbursementrepo.fetchByUserRoleAndUserId(userRole, userRole1,
					userId, startdate, enddate);
			if (list2.isEmpty()) {
				throw new NoSuchElementException("No record found");
			}
			return list2;
		} else if (userRole.equalsIgnoreCase("RO")) {
			userRole1 = "SALES_DISCREPANCY";
			List<LoanTrackingDisbursement> list2 = disbursementrepo.fetchByUserRoleAndUserId(userRole, userRole1,
					userId, startdate, enddate);
			if (list2.isEmpty()) {
				throw new NoSuchElementException("No record found");
			}
			return list2;
		} else {
			throw new NoSuchElementException("No record found");
		}
	}

	@Override
	public void save(LoanTrackingDisbursement disbursement) {
		disbursementrepo.save(disbursement);

	}

	@Override
	public List<LoanTrackingDisbursement> fetchByDateAll(LocalDateTime startdate, LocalDateTime enddate) {
		List<LoanTrackingDisbursement> list = disbursementrepo.fetchByDateAll(startdate, enddate);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public List<LoanTrackingDisbursement> fetchMyTasks(String userRole, String userId, LocalDateTime startdate,
			LocalDateTime enddate) {
		String userRole1 = "";
		String userRole2 = "";
		if (userRole.equalsIgnoreCase("CREDIT")) {
			userRole1 = "CREDIT_DISCREPANCY";
			userRole2="PARTIAL_DISCREPANCY";
			List<LoanTrackingDisbursement> list2 = disbursementrepo.fetchMyTasks(userRole, userRole1,userRole2, userId, startdate,
					enddate);
			if (list2.isEmpty()) {
				throw new NoSuchElementException("No record found");
			}
			return list2;
		} else if (userRole.equalsIgnoreCase("CREDIT_OPS")) {
			userRole1 = "CREDITOPS_DISCREPANCY";
			List<LoanTrackingDisbursement> list2 = disbursementrepo.fetchMyTasks(userRole, userRole1,userRole2, userId, startdate,
					enddate);
			if (list2.isEmpty()) {
				throw new NoSuchElementException("No record found");
			}
			return list2;
		} else if (userRole.equalsIgnoreCase("RO")) {
			userRole1 = "SALES_DISCREPANCY";
			userRole2="SALESANDCREDIT_DISCREPANCY";
			List<LoanTrackingDisbursement> list2 = disbursementrepo.fetchMyTasks(userRole, userRole1,userRole2, userId, startdate,
					enddate);
			if (list2.isEmpty()) {
				throw new NoSuchElementException("No record found");
			}
			return list2;
		} else if (userRole.equalsIgnoreCase("OPS")) {
			userRole1 = "OPS_DISCREPANCY";
			List<LoanTrackingDisbursement> list2 = disbursementrepo.fetchMyTasks(userRole, userRole1,userRole2, userId, startdate,
					enddate);
			if (list2.isEmpty()) {
				throw new NoSuchElementException("No record found");
			}
			return list2;
		} else {
			throw new NoSuchElementException("No record found");
		}

	}

	@Override
	public String getUserName(String userId) {
		String userName = userrepo.getUserName(userId);
		return userName;
	}

	@Override
	public List<String> getAllStates() {
		List<String> list = disbursementrepo.getAllStates();
		return list;
	}

	@Override
	public int countPendingRecords2(String currentStage, String product) {
		String currentStage2 = "";
		String currentStage3 = "";
		int count = 0;
		if (currentStage.equalsIgnoreCase("RO")) {
			currentStage2 = "SALES_DISCREPANCY";
			currentStage3 = "SALES_ADMIN";
			if (product == null) {
				count = disbursementrepo.countPendingRecordsByProductNull(currentStage, currentStage2, currentStage3);
				return count;
			}
			count = disbursementrepo.countPendingRecordsByProduct(currentStage, currentStage2, currentStage3, product);
			return count;
		} else if (currentStage.equalsIgnoreCase("CREDIT")) {
			currentStage2 = "CREDIT_DISCREPANCY";
			currentStage3 = "CREDIT_ADMIN";
			if (product == null) {
				count = disbursementrepo.countPendingRecordsByProductNull(currentStage, currentStage2, currentStage3);
				return count;
			}
			count = disbursementrepo.countPendingRecordsByProduct(currentStage, currentStage2, currentStage3, product);
			return count;
		} else if (currentStage.equalsIgnoreCase("CREDIT_OPS")) {
			currentStage2 = "CREDITOPS_DISCREPANCY";
			currentStage3 = "CREDITOPS_ADMIN";
			if (product == null) {
				count = disbursementrepo.countPendingRecordsByProductNull(currentStage, currentStage2, currentStage3);
				return count;
			}
			count = disbursementrepo.countPendingRecordsByProduct(currentStage, currentStage2, currentStage3, product);
			return count;
		} else if (currentStage.equalsIgnoreCase("OPS")) {
			currentStage2 = "OPS_DISCREPANCY";
			currentStage3 = "OPS_ADMIN";
			if (product == null) {
				count = disbursementrepo.countPendingRecordsByProductNull(currentStage, currentStage2, currentStage3);
				return count;
			}
			count = disbursementrepo.countPendingRecordsByProduct(currentStage, currentStage2, currentStage3, product);
			return count;
		}
		else if (currentStage.equalsIgnoreCase("END")) {
			if (product == null) {
				count = disbursementrepo.countPendingRecordsByProductNull(currentStage, currentStage2, currentStage3);
				return count;
			}
			count = disbursementrepo.countPendingRecordsByProduct(currentStage, currentStage2, currentStage3, product);
			return count;
		}
		return count;
	}

	@Override
	public List<LoanTrackingDisbursement> fetchByDateAndUserRole(LocalDateTime startdate, LocalDateTime enddate,
			String userRole) {
		String userRole2 = "";
		String userRole3 ="";
		if (userRole.equalsIgnoreCase("RO")) {
			userRole2 = "SALES_DISCREPANCY";
			userRole3 = "SALESANDCREDIT_DISCREPANCY";
			List<LoanTrackingDisbursement> list = disbursementrepo.fetchByDateAndUserRole(startdate, enddate, userRole,
					userRole2,userRole3);
			if (list.isEmpty()) {
				throw new NoSuchElementException("No Record Found");
			}
			return list;
		} else if (userRole.equalsIgnoreCase("CREDIT")) {
			userRole2 = "CREDIT_DISCREPANCY";
			userRole3="PARTIAL_DISCREPANCY";
			List<LoanTrackingDisbursement> list = disbursementrepo.fetchByDateAndUserRole(startdate, enddate, userRole,
					userRole2,userRole3);
			if (list.isEmpty()) {
				throw new NoSuchElementException("No Record Found");
			}
			return list;
		} else if (userRole.equalsIgnoreCase("CREDIT_OPS")) {
			userRole2 = "CREDITOPS_DISCREPANCY";
			List<LoanTrackingDisbursement> list = disbursementrepo.fetchByDateAndUserRole(startdate, enddate, userRole,
					userRole2,userRole3);
			if (list.isEmpty()) {
				throw new NoSuchElementException("No Record Found");
			}
			return list;
		} else {
			List<LoanTrackingDisbursement> list = disbursementrepo.fetchByDateAndUserRole(startdate, enddate, userRole,
					userRole2,userRole3);
			if (list.isEmpty()) {
				throw new NoSuchElementException("No Record Found");
			}
			return list;
		}
	}

	@Override
	public LoanTrackingDisbursement getByApplicationId(long applicationNo) {
		Optional<LoanTrackingDisbursement> optional = disbursementrepo.getByApplicationId(applicationNo);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public void saveData(LoanTrackingDisbursement loanTracking) {
		disbursementrepo.save(loanTracking);

	}

	@Override
	public List<String> getAllProducts() {
		List<String> list = disbursementrepo.getAllProducts();
		return list;
	}

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
			// headerFont.setColor(IndexedColors.BLACK.index);
			headerStyle.setFont(headerFont);
			headerStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
			// headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			// Create a cell and put a value in it.

			String[] coloumnHeading = { "ApplicationId", "ProductType","CreatedBy", "StartDate", "EndDate", "CurrentStage",
					"Remarks", "Name", "PanNo", "IsVerify", "LoanAmount","BranchId", "AssignTo","AssignRole"};
			for (int i = 0; i < coloumnHeading.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(coloumnHeading[i]);
				cell.setCellStyle(headerStyle);
			}

			for (int i = 0; i < json.length(); i++) {
				JSONObject obj = json.getJSONObject(i);
				System.out.println(obj.toString());
				long applicationId = obj.getLong("applicationId");
				String productType = obj.getString("productType");
				String createdBy = obj.getString("createdBy");
				String startDate = obj.getString("startDate");
				String endDate="";
				if(obj.has("endDate"));
				{
				endDate = obj.getString("endDate");
				}
				String currentStage = obj.getString("currentStage");
				String remarks = obj.getString("remarks");
				String name=obj.getString("name");
				String panNo = obj.getString("panNo");
				String isVerify = obj.getString("isVerify");
				String loanAmount = obj.getString("loanAmount");
				String branchId = obj.getString("branchId");
				String assignTo = obj.getString("assignTo");
				String assignRole = obj.getString("assignRole");
				row1 = sheet.createRow((short) i + 1);
				row1.createCell(0).setCellValue(applicationId);
				row1.createCell(1).setCellValue(productType);
				row1.createCell(2).setCellValue(createdBy);
				row1.createCell(3).setCellValue(startDate);
				row1.createCell(4).setCellValue(endDate);
				row1.createCell(5).setCellValue(currentStage);
				row1.createCell(6).setCellValue(remarks);
				row1.createCell(7).setCellValue(name);
				row1.createCell(8).setCellValue(panNo);
				row1.createCell(9).setCellValue(isVerify);
				row1.createCell(10).setCellValue(loanAmount);
				row1.createCell(11).setCellValue(branchId);
				row1.createCell(12).setCellValue(assignTo);
				row1.createCell(13).setCellValue(assignRole);

			}


//FileOutputStream fileOut = new FileOutputStream("/home/appadmin@suryodaybank.local/tomcat/tomcat/webapps/ExcelFiles/branchdigi/workbook.xlsx");
			// FileOutputStream fileOut = new
//			 FileOutputStream fileOut=new FileOutputStream("D:\\Excel\\loantrackingdata.xlsx");
			 FileOutputStream fileOut=new FileOutputStream("/opt/tomcat-suryodaysarathi/apache-tomcat-9.0.75/reports/loantrackingdata.xlsx");
//			FileOutputStream fileOut = new FileOutputStream("/opt/tomcat-suryodaysarathi/apache-tomcat-9.0.76/reports/loantrackingdata.xlsx");

			wb.write(fileOut);
			fileOut.close();
			 base64 = exceltobase64("/opt/tomcat-suryodaysarathi/apache-tomcat-9.0.75/reports/loantrackingdata.xlsx");
//			base64 = exceltobase64("/opt/tomcat-suryodaysarathi/apache-tomcat-9.0.76/reports/loantrackingdata.xlsx");
//			base64 = exceltobase64("D:\\Excel\\loantrackingdata.xlsx");
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
	
	@Override
	public List<LoanTrackingDisbursement> findTopTenByDate(LocalDateTime startdate, LocalDateTime enddate) {
		List<LoanTrackingDisbursement> list = disbursementrepo.findTopTenByDate(startdate, enddate);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public List<LoanTrackingDisbursement> searchByNameOrApplication(String name, long applicationId) {
		List<LoanTrackingDisbursement> list = disbursementrepo.searchByNameOrApplication(name,applicationId);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

}
