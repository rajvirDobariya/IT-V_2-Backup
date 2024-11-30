package com.suryoday.connector.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.pojo.LoanDetails;
import com.suryoday.aocpv.pojo.ResponseAocpCustomerData;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.LoanInputService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.connector.serviceImpl.CustomerDetailsServiceImpl;

@RestController
@RequestMapping(value = "/connector/v1")
public class CustomerDetailsEncyController {

	@Autowired
	CustomerDetailsServiceImpl customerDetailsServiceImpl;

	@Autowired
	AocpCustomerDataService aocpCustomerDataService;

	@Autowired
	LoanInputService loanInputService;

	@Autowired
	UserService userService;

	private static Logger logger = LoggerFactory.getLogger(CustomerDetailsEncyController.class);

	@RequestMapping(value = "/fetchByCustomerIdEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchByCustomerId(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);
		//logger.debug("POST Request: " + bm);

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			//logger.debug("start request" + bm.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			String data = "";

			if (X_Request_ID.equals("AOCPV")) {
				JSONObject jsonObject = new JSONObject(decryptContainerString);
				//logger.debug("JSON Object Sent to Request : " + jsonObject.toString());

				JSONObject customerDetails = customerDetailsServiceImpl.fetchbycustomerid(jsonObject, Header);
				//logger.debug("Response from the API: " + customerDetails);
				HttpStatus h = HttpStatus.BAD_GATEWAY;
				if (customerDetails != null) {
					// String Data2= sendsms.getString("data");
					// System.out.println("data2");90978
					// JSONObject Data1= new JSONObject(Data2);

					// System.out.println(Data1);
					JSONObject Data2 = null;
					if (customerDetails.has("data")) {

						h = HttpStatus.OK;
						String Data1 = customerDetails.getString("data");
						Data2 = new JSONObject(Data1);
						//logger.debug("JSON Object from Response : " + Data1);

					} else if (customerDetails.has("Error")) {
						h = HttpStatus.BAD_REQUEST;

					}
					//logger.debug("Main Response from API :" + Data2.toString());
					data = Data2.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
					logger.debug("response : " + data3.toString());
					return new ResponseEntity<Object>(data3.toString(), h);

				} else {
					logger.debug("GATEWAY_TIMEOUT");
					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			} else {
				logger.debug("INVALID REQUEST");
				return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);

			}
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}

	}

	@RequestMapping(value = "/downloadEncy/customers.xlsx", method = RequestMethod.POST, produces = "application/json")
	public void downloadCsv(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req,
			HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=customers.xlsx");

		ByteArrayInputStream stream = null;

		JSONObject jsonObject = new JSONObject(bm);
		String customerId = jsonObject.getJSONObject("Data").getString("customerId");
		if (customerId == null || customerId.isEmpty()) {
			throw new EmptyInputException("input field is empty");
		}
		long customerIdInLong = Long.parseLong(customerId);
		ResponseAocpCustomerData aocpCustomerData = aocpCustomerDataService.getByCustomerId(customerIdInLong);
//		LoanDetails loanDetails = loanInputService.getByReferenceNo(customerId);
		LoanDetails loanDetails =null;
//        ResponseEntity<Object> fetchByCustomerId = fetchByCustomerId(bm, X_CORRELATION_ID, X_From_ID, X_To_ID, X_Transaction_ID, X_User_ID, X_Request_ID, req);
//       
//        Object body = fetchByCustomerId.getBody();
//        System.out.println(body);
//        String string = body.toString();

		//System.out.println(aocpCustomerData);
		//System.out.println(loanDetails);
		// JSONObject jsonObject = new JSONObject(string);
		// customerId =
		// jsonObject.getJSONObject("CustomerDetails").getLong("customerId");

		// String name = jsonObject.getJSONObject("CustomerDetails").getString("name");
		String name = aocpCustomerData.getName();
		long mfi_VINTAGE_SSFB = loanDetails.getMFI_VINTAGE_SSFB();
		double retail_IMPUTED_EMI_WO_CCOD_CURRENT = loanDetails.getRETAIL_IMPUTED_EMI_WO_CCOD_CURRENT();
		long retail_BUREAU_VINTAGE = loanDetails.getRETAIL_BUREAU_VINTAGE();
		double max_CURRENT_EMI = loanDetails.getMAX_CURRENT_EMI();
		double sumOutstandingBalanceOwn = loanDetails.getSumOutstandingBalanceOwn();
		double sumInstallmentAmountopen = loanDetails.getSumInstallmentAmountopen();
		long mfi_VINTAGE = loanDetails.getMFI_VINTAGE();
		long num_SECURED_ACCTS = loanDetails.getNUM_SECURED_ACCTS();
		long num_UNSECURED_ACCTS = loanDetails.getNUM_UNSECURED_ACCTS();
		long num_SECURED_LIVE_ACCTS = loanDetails.getNUM_SECURED_LIVE_ACCTS();
		long num_HL_LAP_LIVE = loanDetails.getNUM_HL_LAP_LIVE();
		long num_UNSECURED_LIVE_ACCTS = loanDetails.getNUM_UNSECURED_LIVE_ACCTS();
		long num_PL_LIVE = loanDetails.getNUM_PL_LIVE();
		long num_BL_LIVE = loanDetails.getNUM_BL_LIVE();
		long numOpenAccount = loanDetails.getNumOpenAccount();
		long num_SECURED_CLOSED_ACCTS = loanDetails.getNUM_SECURED_CLOSED_ACCTS();
		long num_HL_LAP_CLOSED = loanDetails.getNUM_HL_LAP_CLOSED();
		long num_UNSECURED_CLOSED_ACCTS = loanDetails.getNUM_UNSECURED_CLOSED_ACCTS();
		long num_PL_CLOSED = loanDetails.getNUM_PL_CLOSED();
		long num_BL_CLOSED = loanDetails.getNUM_BL_CLOSED();
		String latest_ACCOUNTSTATUS_HL_LAP = loanDetails.getLATEST_ACCOUNTSTATUS_HL_LAP();
		String latest_ACCOUNTSTATUS_PL = loanDetails.getLATEST_ACCOUNTSTATUS_PL();
		String latest_ACCOUNTSTATUS_BL = loanDetails.getLATEST_ACCOUNTSTATUS_BL();
		String latest_ACCOUNTSTATUS_MFI = loanDetails.getLATEST_ACCOUNTSTATUS_MFI();
		double total_DISB_HL_LAP = loanDetails.getTOTAL_DISB_HL_LAP();
		double total_DISB_PL = loanDetails.getTOTAL_DISB_PL();
		double total_DISB_BL = loanDetails.getTOTAL_DISB_BL();
		double secured_POS = loanDetails.getSECURED_POS();
		double hl_POS = loanDetails.getHL_POS();
		double lap_POS = loanDetails.getLAP_POS();
		double unsecured_POS = loanDetails.getUNSECURED_POS();
		double pl_POS = loanDetails.getPL_POS();
		double bl_POS = loanDetails.getBL_POS();
		double sumOutstandingBalance = loanDetails.getSumOutstandingBalance();
		int bureau_VINTAGE_HL_LAP = loanDetails.getBUREAU_VINTAGE_HL_LAP();
		int bureau_VINTAGE_PL = loanDetails.getBUREAU_VINTAGE_PL();
		int bureau_VINTAGE_BL = loanDetails.getBUREAU_VINTAGE_BL();
		Date latest_CLOSEDATE_SECURED = loanDetails.getLATEST_CLOSEDATE_SECURED();
		Date latest_CLOSEDATE_HL_LAP = loanDetails.getLATEST_CLOSEDATE_HL_LAP();
		Date latest_CLOSEDATE_UNSECURED = loanDetails.getLATEST_CLOSEDATE_UNSECURED();
		Date latest_CLOSEDATE_PL = loanDetails.getLATEST_CLOSEDATE_PL();
		Date latest_CLOSEDATE_BL = loanDetails.getLATEST_CLOSEDATE_BL();
		Date latestCloseDate = loanDetails.getLatestCloseDate();
		double max_EMI_SECURED = loanDetails.getMAX_EMI_SECURED();
		double max_EMI_HL_LAP = loanDetails.getMAX_EMI_HL_LAP();
		double max_EMI_UNSECURED = loanDetails.getMAX_EMI_UNSECURED();
		double max_EMI_PL = loanDetails.getMAX_EMI_PL();
		double max_EMI_BL = loanDetails.getMAX_EMI_BL();
		int max_LOAN_TENURE_SECURED = loanDetails.getMAX_LOAN_TENURE_SECURED();
		int max_LOAN_TENURE_HL_LAP = loanDetails.getMAX_LOAN_TENURE_HL_LAP();
		int max_LOAN_TENURE_UNSECURED = loanDetails.getMAX_LOAN_TENURE_UNSECURED();
		int max_LOAN_TENURE_PL = loanDetails.getMAX_LOAN_TENURE_PL();
		int max_LOAN_TENURE_BL = loanDetails.getMAX_LOAN_TENURE_BL();
		double max_LOAN_AMOUNT_SECURED = loanDetails.getMAX_LOAN_AMOUNT_SECURED();
		double max_LOAN_AMOUNT_HL_LAP = loanDetails.getMAX_LOAN_AMOUNT_HL_LAP();
		double max_LOAN_AMOUNT_UNSECURED = loanDetails.getMAX_LOAN_AMOUNT_UNSECURED();
		double max_LOAN_AMOUNT_PL = loanDetails.getMAX_LOAN_AMOUNT_PL();
		double max_LOAN_AMOUNT_BL = loanDetails.getMAX_LOAN_AMOUNT_BL();
		double max_LOAN_AMOUNT_MFI = loanDetails.getMAX_LOAN_AMOUNT_MFI();

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Customers");

			CellStyle headerCellStyle = workbook.createCellStyle();
			Font defaultFont1 = workbook.createFont();
			defaultFont1.setBold(true);
			headerCellStyle.setFont(defaultFont1);

			// Creating header
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(4);
			cell.setCellValue("CUSTOMER PROFILE - 1 pager");
			CellStyle style = workbook.createCellStyle();
			Font defaultFont = workbook.createFont();
			defaultFont.setBold(true);
			defaultFont.setUnderline(Font.U_DOUBLE);
			style.setFont(defaultFont);
			style.setAlignment(HorizontalAlignment.CENTER);
			cell.setCellStyle(style);

			Row row0 = sheet.createRow(2);
			cell = row0.createCell(0);
			cell.setCellValue("Customer ID");
			cell.setCellStyle(headerCellStyle);

			cell = row0.createCell(1);
			cell.setCellValue(customerId);

			cell = row0.createCell(4);
			cell.setCellValue("Customer Rating ");
			cell.setCellStyle(headerCellStyle);

			cell = row0.createCell(5);
			cell.setCellValue("NOT AVAILABLE FROM BUREAU");

			Row row1 = sheet.createRow(3);
			cell = row1.createCell(0);
			cell.setCellValue("Name");
			cell.setCellStyle(headerCellStyle);

			cell = row1.createCell(1);
			cell.setCellValue(name);

			cell = row1.createCell(4);
			cell.setCellValue("SSFB Disbursed");
			cell.setCellStyle(headerCellStyle);

			cell = row1.createCell(5);
			cell.setCellValue("DisbursedAmt");

			Row row2 = sheet.createRow(5);
			cell = row2.createCell(0);
			cell.setCellValue("SSFB Vintage");
			cell.setCellStyle(headerCellStyle);
			cell = row2.createCell(1);
			cell.setCellValue(mfi_VINTAGE_SSFB);

			cell = row2.createCell(4);
			cell.setCellValue("Current MFI EMI");
			cell.setCellStyle(headerCellStyle);

			cell = row2.createCell(5);
			cell.setCellValue(sumInstallmentAmountopen);

			Row row3 = sheet.createRow(6);
			cell = row3.createCell(0);
			cell.setCellValue("SSFB DPD bucket");
			cell.setCellStyle(headerCellStyle);

			cell = row3.createCell(1);
			cell.setCellValue("SSFB DPD bucket");
			cell.setCellStyle(headerCellStyle);

			cell = row3.createCell(4);
			cell.setCellValue("MFI vintage");
			cell.setCellStyle(headerCellStyle);

			cell = row3.createCell(5);
			cell.setCellValue(mfi_VINTAGE);

			Row row4 = sheet.createRow(7);
			cell = row4.createCell(0);
			cell.setCellValue("Current Retail EMI");
			cell.setCellStyle(headerCellStyle);

			cell = row4.createCell(1);
			cell.setCellValue(retail_IMPUTED_EMI_WO_CCOD_CURRENT);

			Row row5 = sheet.createRow(8);
			cell = row5.createCell(0);
			cell.setCellValue("Retail vintage");
			cell.setCellStyle(headerCellStyle);

			cell = row5.createCell(1);
			cell.setCellValue(retail_BUREAU_VINTAGE);

			Row row6 = sheet.createRow(9);
			cell = row6.createCell(0);
			cell.setCellValue("Max EMI Amount");
			cell.setCellStyle(headerCellStyle);
			cell = row6.createCell(1);
			cell.setCellValue(max_CURRENT_EMI);

			Row row7 = sheet.createRow(13);
			cell = row7.createCell(0);
			cell.setCellValue("Secured");
			cell.setCellStyle(headerCellStyle);

			cell = row7.createCell(1);
			cell.setCellValue(num_SECURED_ACCTS);

			cell = row7.createCell(2);
			cell.setCellValue(num_SECURED_LIVE_ACCTS);

			cell = row7.createCell(3);
			cell.setCellValue(num_SECURED_CLOSED_ACCTS);

			cell = row7.createCell(4);
			cell.setCellValue("LATEST ACCOUNTSTATUS SECURED");

			cell = row7.createCell(5);
			cell.setCellValue("TOTAL DISB SECURED");

			cell = row7.createCell(6);
			cell.setCellValue(secured_POS);

			cell = row7.createCell(7);
			cell.setCellValue("BUREAU VINTAGE SECURED");

			cell = row7.createCell(8);
			cell.setCellValue(latest_CLOSEDATE_SECURED);

			cell = row7.createCell(9);
			cell.setCellValue(max_EMI_SECURED);

			cell = row7.createCell(10);
			cell.setCellValue(max_LOAN_TENURE_SECURED);

			cell = row7.createCell(11);
			cell.setCellValue(max_LOAN_AMOUNT_SECURED);

			Row row8 = sheet.createRow(14);
			cell = row8.createCell(0);
			cell.setCellValue("HL/LAP");
			cell.setCellStyle(headerCellStyle);

			cell = row8.createCell(1);
			cell.setCellValue("NUM_HL_LAP_ACCTS");

			cell = row8.createCell(2);
			cell.setCellValue(num_HL_LAP_LIVE);

			cell = row8.createCell(3);
			cell.setCellValue(num_HL_LAP_CLOSED);

			cell = row8.createCell(4);
			cell.setCellValue(latest_ACCOUNTSTATUS_HL_LAP);

			cell = row8.createCell(5);
			cell.setCellValue(total_DISB_HL_LAP);

			cell = row8.createCell(6);
			cell.setCellValue(hl_POS + lap_POS);

			cell = row8.createCell(7);
			cell.setCellValue(bureau_VINTAGE_HL_LAP);

			cell = row8.createCell(8);
			cell.setCellValue(latest_CLOSEDATE_HL_LAP);

			cell = row8.createCell(9);
			cell.setCellValue(max_EMI_HL_LAP);

			cell = row8.createCell(10);
			cell.setCellValue(max_LOAN_TENURE_HL_LAP);

			cell = row8.createCell(11);
			cell.setCellValue(max_LOAN_AMOUNT_HL_LAP);

			Row row9 = sheet.createRow(15);
			cell = row9.createCell(0);
			cell.setCellValue("Unsecured");
			cell.setCellStyle(headerCellStyle);

			cell = row9.createCell(1);
			cell.setCellValue(num_UNSECURED_ACCTS);

			cell = row9.createCell(2);
			cell.setCellValue(num_UNSECURED_LIVE_ACCTS);

			cell = row9.createCell(3);
			cell.setCellValue(num_UNSECURED_CLOSED_ACCTS);

			cell = row9.createCell(4);
			cell.setCellValue("LATEST ACCOUNSTATUS UNSECURED");

			cell = row9.createCell(5);
			cell.setCellValue("TOTAL DISB UNSECURED");

			cell = row9.createCell(6);
			cell.setCellValue(unsecured_POS);

			cell = row9.createCell(7);
			cell.setCellValue("BUREAU VINTAGE UNSECURED");

			cell = row9.createCell(8);
			cell.setCellValue(latest_CLOSEDATE_UNSECURED);

			cell = row9.createCell(9);
			cell.setCellValue(max_EMI_UNSECURED);

			cell = row9.createCell(10);
			cell.setCellValue(max_LOAN_TENURE_UNSECURED);

			cell = row9.createCell(11);
			cell.setCellValue(max_LOAN_AMOUNT_UNSECURED);

			Row row10 = sheet.createRow(16);
			cell = row10.createCell(0);
			cell.setCellValue("PL");
			cell.setCellStyle(headerCellStyle);

			cell = row10.createCell(1);
			cell.setCellValue("NUM_PL_ACCTS");

			cell = row10.createCell(2);
			cell.setCellValue(num_PL_LIVE);

			cell = row10.createCell(3);
			cell.setCellValue(num_PL_CLOSED);

			cell = row10.createCell(4);
			cell.setCellValue(latest_ACCOUNTSTATUS_PL);

			cell = row10.createCell(5);
			cell.setCellValue(total_DISB_PL);

			cell = row10.createCell(6);
			cell.setCellValue(pl_POS);

			cell = row10.createCell(7);
			cell.setCellValue(bureau_VINTAGE_PL);

			cell = row10.createCell(8);
			cell.setCellValue(latest_CLOSEDATE_PL);

			cell = row10.createCell(9);
			cell.setCellValue(max_EMI_PL);

			cell = row10.createCell(10);
			cell.setCellValue(max_LOAN_TENURE_PL);

			cell = row10.createCell(11);
			cell.setCellValue(max_LOAN_AMOUNT_PL);

			Row row11 = sheet.createRow(17);
			cell = row11.createCell(0);
			cell.setCellValue("BL");
			cell.setCellStyle(headerCellStyle);

			cell = row11.createCell(1);
			cell.setCellValue("NUM_BL_ACCTS");

			cell = row11.createCell(2);
			cell.setCellValue(num_BL_LIVE);

			cell = row11.createCell(3);
			cell.setCellValue(num_BL_CLOSED);

			cell = row11.createCell(4);
			cell.setCellValue(latest_ACCOUNTSTATUS_BL);

			cell = row11.createCell(5);
			cell.setCellValue(total_DISB_BL);

			cell = row11.createCell(6);
			cell.setCellValue(bl_POS);

			cell = row11.createCell(7);
			cell.setCellValue(bureau_VINTAGE_BL);

			cell = row11.createCell(8);
			cell.setCellValue(latest_CLOSEDATE_BL);

			cell = row11.createCell(9);
			cell.setCellValue(max_EMI_BL);

			cell = row11.createCell(10);
			cell.setCellValue(max_LOAN_TENURE_BL);

			cell = row11.createCell(11);
			cell.setCellValue(max_LOAN_AMOUNT_BL);

			Row row12 = sheet.createRow(18);
			cell = row12.createCell(0);
			cell.setCellValue("MFI");
			cell.setCellStyle(headerCellStyle);

			cell = row12.createCell(1);
			cell.setCellValue("TOTAL NUM MFI ACCTS");

			cell = row12.createCell(2);
			cell.setCellValue(numOpenAccount);

			cell = row12.createCell(3);
			cell.setCellValue("NUM CLOSED ACCTS");

			cell = row12.createCell(4);
			cell.setCellValue(latest_ACCOUNTSTATUS_MFI);

			cell = row12.createCell(5);
			cell.setCellValue("TOTAL MFI DISBURSEMENT");

			cell = row12.createCell(6);
			cell.setCellValue(sumOutstandingBalance);

			cell = row12.createCell(7);
			cell.setCellValue(mfi_VINTAGE);

			cell = row12.createCell(8);
			cell.setCellValue(latestCloseDate);

			cell = row12.createCell(9);
			cell.setCellValue("MAX MFI EMI");

			cell = row12.createCell(10);
			cell.setCellValue("NOT AVAILABLE FROM BUREAU");

			cell = row12.createCell(11);
			cell.setCellValue(max_LOAN_AMOUNT_MFI);

			Row row13 = sheet.createRow(19);
			cell = row13.createCell(0);
			cell.setCellValue("Total Current Balance (To be reoved)");
			cell.setCellStyle(headerCellStyle);

			Row row15 = sheet.createRow(4);
			cell = row15.createCell(4);
			cell.setCellValue("SSFB POS");
			cell.setCellStyle(headerCellStyle);

			cell = row15.createCell(5);
			cell.setCellValue(sumOutstandingBalanceOwn);

			Row row14 = sheet.createRow(12);
			cell = row14.createCell(1);
			cell.setCellValue("No. of Loans");
			cell.setCellStyle(headerCellStyle);

			cell = row14.createCell(2);
			cell.setCellValue("Live account #");
			cell.setCellStyle(headerCellStyle);

			cell = row14.createCell(3);
			cell.setCellValue("Closed accounts #");
			cell.setCellStyle(headerCellStyle);

			cell = row14.createCell(4);
			cell.setCellValue("Latest Account Status");
			cell.setCellStyle(headerCellStyle);

			cell = row14.createCell(4);
			cell.setCellValue("Total Disbursement");
			cell.setCellStyle(headerCellStyle);

			cell = row14.createCell(5);
			cell.setCellValue("Opening Balance (POS)");
			cell.setCellStyle(headerCellStyle);

			cell = row14.createCell(6);
			cell.setCellValue("Bureau Vinatge");
			cell.setCellStyle(headerCellStyle);

			cell = row14.createCell(7);
			cell.setCellValue("Last Closed Date");
			cell.setCellStyle(headerCellStyle);

			cell = row14.createCell(8);
			cell.setCellValue("Max EMI served");
			cell.setCellStyle(headerCellStyle);

			cell = row14.createCell(9);
			cell.setCellValue("Max Loan Tenure");
			cell.setCellStyle(headerCellStyle);

			cell = row14.createCell(10);
			cell.setCellValue("Max. Loan amount");
			cell.setCellStyle(headerCellStyle);

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			sheet.autoSizeColumn(10);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			stream = new ByteArrayInputStream(outputStream.toByteArray());

		} catch (IOException e) {

			e.printStackTrace();

		}

		IOUtils.copy(stream, response.getOutputStream());
	}

}
