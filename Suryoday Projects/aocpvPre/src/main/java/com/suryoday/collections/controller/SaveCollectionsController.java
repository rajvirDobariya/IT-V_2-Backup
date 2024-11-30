package com.suryoday.collections.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.collections.pojo.ReceiptImage;
import com.suryoday.collections.pojo.ReceiptPojo;
import com.suryoday.collections.pojo.UpdateTrailBean;
import com.suryoday.collections.pojo.UserlogUpdate;
import com.suryoday.collections.service.ReceiptImageService;
import com.suryoday.collections.service.ReceiptService;
import com.suryoday.collections.service.UpdateTrailService;
import com.suryoday.collections.service.UserLogUpadteService;

@RestController
@RequestMapping("/collections")
public class SaveCollectionsController {
	
	@Autowired
	ReceiptService receiptService;
	
	@Autowired
	ReceiptImageService imageService;
	
	@Autowired
	UpdateTrailService updateTrailService;
	
	@Autowired
	UserLogUpadteService userLogUpadteService;

	Logger logger = LoggerFactory.getLogger(SaveCollectionsController.class);
	
	@RequestMapping(value="/saveReceipt", method = RequestMethod.POST)
	public ResponseEntity<Object> saveReceiptData(@RequestParam("file") MultipartFile files, @RequestParam("Data")String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("request"+jsonRequest);
		System.out.println("JSON STRING "+jsonObject);
		
			if(jsonRequest.isEmpty()) {
				logger.debug("request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
			String totalCount = null;
			String upi = null;
			String remarks = null;
			String customerID = jsonObject.getJSONObject("Data").getString("customerID");
			String accountNo = jsonObject.getJSONObject("Data").getString("accountNo");
			String custName = jsonObject.getJSONObject("Data").getString("custName");
			String ptpAmount = jsonObject.getJSONObject("Data").getString("ptpAmount");
			String emiAmount = jsonObject.getJSONObject("Data").getString("emiAmount");
			String emiOverdue = jsonObject.getJSONObject("Data").getString("emiOverdue");
			String charges = jsonObject.getJSONObject("Data").getString("charges");
			String totalAmount = jsonObject.getJSONObject("Data").getString("totalAmount");
			String modeOfPayment = jsonObject.getJSONObject("Data").getString("modeOfPayment");
			String receiptNo = jsonObject.getJSONObject("Data").getString("receiptNo");
			if(modeOfPayment.equals("Cash")) {
				 totalCount = jsonObject.getJSONObject("Data").getString("totalCount");	
			}
			else if(modeOfPayment.equals("UPI")) {
				 upi = jsonObject.getJSONObject("Data").getString("upiID");
				 remarks = jsonObject.getJSONObject("Data").getString("remarks");
			}
			String panNO = jsonObject.getJSONObject("Data").getString("panNO");
			String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
			String branchId = jsonObject.getJSONObject("Data").getString("branchId");
			String paymanetStatus = jsonObject.getJSONObject("Data").getString("paymanetStatus");
			
			ReceiptPojo pojo = new ReceiptPojo();
			pojo.setCustomerID(customerID);
			pojo.setAccountNo(accountNo);
			pojo.setCustName(custName);
			pojo.setPtpAmount(ptpAmount);
			pojo.setEmiAmount(emiAmount);
			pojo.setEmiOverdue(emiOverdue);
			pojo.setCharges(charges);
			pojo.setTotalAmount(totalAmount);
			pojo.setModeOfPayment(modeOfPayment);			
			pojo.setTotalCount(totalCount);
			pojo.setUpiID(upi);
			pojo.setRemarks(remarks);
			pojo.setReceiptNo(receiptNo);
			pojo.setPanNO(panNO);
			pojo.setMobileNo(mobileNo);			
			LocalDate now = LocalDate.now();
			pojo.setCurrentDate(now);
			pojo.setBranchId(branchId);
			pojo.setPaymanetStatus(paymanetStatus);
			
			String res = receiptService.saveReciept(pojo);
			
			String customerImage = jsonObject.getJSONObject("Data").getString("customerImage");
			String latitude = jsonObject.getJSONObject("Data").getString("latitude");
			String longitude = jsonObject.getJSONObject("Data").getString("longitude");
			String address = jsonObject.getJSONObject("Data").getString("address");
			String timestamp = jsonObject.getJSONObject("Data").getString("timestamp");
		
			ReceiptImage images = new ReceiptImage();
			images.setCustomerImage(customerImage);
			images.setLatitude(latitude);
			images.setLongitude(longitude);
			images.setAddress(address);
			images.setTimestamp(timestamp);
			images.setCustomerID(customerID);
			images.setReceiptNo(receiptNo);
			
			String image = imageService.saveImageReceipt(images,files);
			System.out.println(image);
			org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
			response.put("message", res);
			
			return new ResponseEntity<Object>(response,HttpStatus.OK);
			
		}
	@RequestMapping(value="/saveUpdateTrail", method = RequestMethod.POST)
	public ResponseEntity<Object> saveUpdateTrail(@RequestBody String jsonRequest ,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		
		JSONObject jsonObject=new JSONObject(jsonRequest);
		logger.debug("request"+jsonRequest);
		System.out.println("JSON STRING "+jsonObject);
		
			if(jsonRequest.isEmpty()) {
				logger.debug("request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
			
			String customerID = jsonObject.getJSONObject("Data").getString("customerID");
			String accountNo = jsonObject.getJSONObject("Data").getString("AccountNo");
			String custName = jsonObject.getJSONObject("Data").getString("CustomerName");
			String customerMate = jsonObject.getJSONObject("Data").getString("CustomerMate");
			String dispositionCode = jsonObject.getJSONObject("Data").getString("DispositionCode");
			String shortDescp = jsonObject.getJSONObject("Data").getString("ShortDescription");
			String ptpnextactionDate = jsonObject.getJSONObject("Data").getString("PTPNextActionDate");
			String nextaction = jsonObject.getJSONObject("Data").getString("NextAction");
			String ptpAmount = jsonObject.getJSONObject("Data").getString("ptpAmount");
			String emiAmount = jsonObject.getJSONObject("Data").getString("emiAmount");			
			String assignTo = jsonObject.getJSONObject("Data").getString("AssignTo");
			String reasonforAssigning = jsonObject.getJSONObject("Data").getString("AssigningReason");
			String newcontactNo = jsonObject.getJSONObject("Data").getString("ContactNumber");
			String reasonforNonpayment = jsonObject.getJSONObject("Data").getString("ReasonForNonpayment");
			String remarks = jsonObject.getJSONObject("Data").getString("Remarks");	
			String branchId = jsonObject.getJSONObject("Data").getString("branchId");
			String updatedDate = jsonObject.getJSONObject("Data").getString("UpdatedDate");
			String dueDate = jsonObject.getJSONObject("Data").getString("DueDate");
			
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			 LocalDate updated = LocalDate.parse(updatedDate, formatter);
			 DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			 LocalDate due = LocalDate.parse(dueDate, formatter2);
			
			UpdateTrailBean pojo = new UpdateTrailBean();
			pojo.setCustomerId(customerID);
			pojo.setAccountNo(accountNo);
			pojo.setCustomerName(custName);
			pojo.setCustomerMate(customerMate);
			pojo.setDispositionCode(dispositionCode);
			pojo.setShortDescp(shortDescp);
			pojo.setPtpnextactionDate(ptpnextactionDate);
			pojo.setNextaction(nextaction);
			pojo.setPtpAmount(ptpAmount);
			pojo.setEmiAmount(emiAmount);
			pojo.setAssignTo(assignTo);
			pojo.setReasonforAssigning(reasonforAssigning);
			pojo.setNewcontactNo(newcontactNo);
			pojo.setReasonforNonpayment(reasonforNonpayment);
			pojo.setRemarks(remarks);
			pojo.setBranchId(branchId);
			pojo.setUpdatedDate(updated);
			pojo.setDueDate(due);			
			LocalDate now = LocalDate.now();
			pojo.setCreatedDate(now);
				
			String message = updateTrailService.updateTrailSave(pojo);
			
			UserlogUpdate userLogUpadte = new UserlogUpdate();
			userLogUpadte.setLoanNo(accountNo);
			userLogUpadte.setUserId(X_User_ID);
			
			String message2 = userLogUpadteService.userLogUpdatedSave(userLogUpadte);
			
			org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
			response.put("message", message);
			response.put("message", message2);
			
			return new ResponseEntity<Object>(response,HttpStatus.OK);
			
		}

}
