package com.suryoday.twowheeler.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.FinalSaction;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.aocpv.service.LoanInputService;
import com.suryoday.connector.rd.util.Crypt;
import com.suryoday.connector.service.UserService;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.pojo.TwowheelerDetailsResponse;
import com.suryoday.twowheeler.pojo.TwowheelerLoanCreation;
import com.suryoday.twowheeler.pojo.TwowheelerResponse;
import com.suryoday.twowheeler.service.DisbursementApiService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;
import com.suryoday.twowheeler.service.TwowheelerImageService;
import com.suryoday.twowheeler.service.TwowheelerLoanCreationService;
import com.suryoday.twowheeler.service.TwowheelerUserLogService;

@RestController
@RequestMapping("/twowheelerDetails/customer/web")
public class TwowheelerDetailsControllerEncyWeb {

	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;

	@Autowired
	TwowheelerFamilyMemberService familyMemberService;

	@Autowired
	LoanInputService loanInputService;

	@Autowired
	TwowheelerImageService imageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DisbursementApiService disbursementservice;
	
	@Autowired
	TwowheelerLoanCreationService twowheelerLoanCreationService;
	
	@Autowired 
	TwowheelerUserLogService twowheelerUserLogService;
	
	Logger logger = LoggerFactory.getLogger(TwowheelerDetailsControllerEncyWeb.class);
	
	@RequestMapping(value = "/fetchByDateEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByDate(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String key = X_Session_ID;
		String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String startDate = jsonObject.getJSONObject("Data").getString("startDate");
		String endDate = jsonObject.getJSONObject("Data").getString("endDate");
		String status = jsonObject.getJSONObject("Data").getString("status");
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");

		if (startDate == null || startDate.isEmpty() && endDate == null || endDate.isEmpty()) {
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("status", HttpStatus.BAD_REQUEST.toString());
			response.put("message", "enter a proper Dates");
			logger.debug("final response" + response.toString());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
//			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
		LocalDateTime enddate = LocalDateTime.parse(endDate, formatter);

		List<TwowheelerResponse> fetchByApplicationNo =null;
		
		if(branchId.isEmpty() && status.isEmpty()) {
			 fetchByApplicationNo = twowheelerDetailsService.fetchByDate(startdate, enddate);
		}
		else if(branchId.isEmpty()) {
			fetchByApplicationNo = twowheelerDetailsService.fetchByDateWithStatus(startdate, enddate,status);
		}
		else if(status.isEmpty()) {
			fetchByApplicationNo = twowheelerDetailsService.fetchByDateWithBranchId(startdate, enddate,branchId);
		}
		else {
			 fetchByApplicationNo = twowheelerDetailsService.fetchByDate(startdate, enddate, status,branchId);
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		JSONArray j = new JSONArray(fetchByApplicationNo);
		response.put("status", HttpStatus.OK.toString());
		response.put("Data", j);
		logger.debug(response.toString());
		logger.debug("PD end");
		String	data = response.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);

		    } 
		    else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 
	}
	
	@RequestMapping(value = "/fetchByApplicationNoEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByApplicationNo(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String key = X_Session_ID;
		String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");

		TwowheelerDetailsResponse fetchByApplicationNo = twowheelerDetailsService.getByApplicationNo(applicationNo,"web");
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		JSONObject j = new JSONObject(fetchByApplicationNo);
		response.put("status", HttpStatus.OK.toString());
		response.put("Data", j);
		logger.debug(response.toString());
		logger.debug("PD end");
		String	data = response.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
		    else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 
	}
	
	@RequestMapping(value = "/fetchByApplicationNoOrCustomerIdEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByApplicationNoOrCustomerId(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String key = X_Session_ID;
		String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			
			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			String customerId = jsonObject.getJSONObject("Data").getString("customerId");
			List<TwowheelerResponse> twowheelerResponse =null;
			if(applicationNo.equals("")) {
				twowheelerResponse=twowheelerDetailsService.fetchbyCustomerId(customerId);
			}
			else {
				twowheelerResponse=twowheelerDetailsService.fetchbyApplicationNo(applicationNo);
			}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		JSONArray j = new JSONArray(twowheelerResponse);
		response.put("status", HttpStatus.OK.toString());
		response.put("Data", j);
		logger.debug(response.toString());
		logger.debug("PD end");
		String	data = response.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
		    else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 
	}
	
	@RequestMapping(value = "/getreviewDataEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getreviewData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String key = X_Session_ID;
		String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		org.json.simple.JSONObject fetchByApplicationNo = twowheelerDetailsService.getreviewData(applicationNo,"web");
		
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		JSONObject j = new JSONObject(fetchByApplicationNo);
		response.put("status", HttpStatus.OK.toString());
		response.put("Data", j);
		logger.debug(response.toString());
		logger.debug("PD end");
		String	data = response.toString();
 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		    } 
		    else {
		        org.json.JSONObject data2 = new org.json.JSONObject();
		        data2.put("value", "SessionId is expired or Invalid sessionId");
		        org.json.JSONObject data3 = new org.json.JSONObject();
		        data3.put("Error", data2);
		        logger.debug("SessionId is expired or Invalid sessionId");
		        return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		    } 
	}
	
	@RequestMapping(value = "/saveImageEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveImageEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			//userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
//			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
			  String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			  String member="SELF";
			  if(jsonObject.getJSONObject("Data").has("member")) {
				   member = jsonObject.getJSONObject("Data").getString("member");
			  }
			  String base64Image = jsonObject.getJSONObject("Data").getString("base64Image");
			  byte[] image = Base64.getDecoder().decode(base64Image);
			
			if (!document.isEmpty()) {
				imageService.saveImageWeb(image,document,applicationNo,member);
			}


			org.json.simple.JSONObject response = new org.json.simple.JSONObject();

			response.put("status", HttpStatus.OK.toString());
			response.put("Data", "Image Saved");
			logger.debug(response.toString());
			logger.debug("PD end");
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
//			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/fetchMemberNameEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchMemberName(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, 
			 HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception{
		logger.debug("fetchMemberName Start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			//userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		 TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		 org.json.simple.JSONArray   selfCompulsory= new org.json.simple.JSONArray();
		 selfCompulsory.add("aadharFront");
		 selfCompulsory.add("aadharBack");	
		 selfCompulsory.add("Pan");
		
		 org.json.simple.JSONArray   selfOptional= new org.json.simple.JSONArray();
		 selfOptional.add("VoterIdFront");
		 selfOptional.add("VoterIdBack");
		 selfOptional.add("DrivingLicense");
		 selfOptional.add("Passport");
		 selfOptional.add("RationCard");
	 
		 org.json.simple.JSONArray   otherCompulsory= new org.json.simple.JSONArray();
		 otherCompulsory.add("EbBill");

		 org.json.simple.JSONArray   otherOptional= new org.json.simple.JSONArray();
		 otherOptional.add("ITRCopy");
		 otherOptional.add("Form16");
		 otherOptional.add("RentedAgreement");	
		 otherOptional.add("roadsideImage");
		 otherOptional.add("EducationProof");	
		 otherOptional.add("EmployementProof");
		 otherOptional.add("RC");
		 otherOptional.add("PNG");	
		 otherOptional.add("LPG");
		 otherOptional.add("NotarizedAgreement");
		 otherOptional.add("ManualFI");	
		 otherOptional.add("BloodRelationProof");
		 otherOptional.add("ManualFI");
		 otherOptional.add("DisbursementMemo");
		 otherOptional.add("RepaymentPDC1");
		 otherOptional.add("RepaymentPDC2");
		 otherOptional.add("SecurityPDC1");
		 otherOptional.add("SecurityPDC2");
		 
		 if(twowheelerDetails.getStatus().equalsIgnoreCase("PSD")) {
			 otherCompulsory.add("Invoice");
			 otherCompulsory.add("AssetInsurance");
			 otherCompulsory.add("RTO");
			 otherCompulsory.add("DeliveryOrder");
			 otherCompulsory.add("MarginMoney");
			 otherCompulsory.add("CreditLifeInsurance");
			
		 }else {
			 otherOptional.add("Invoice");
			 otherOptional.add("AssetInsurance");
			 otherOptional.add("RTO");
			 otherOptional.add("DeliveryOrder");
			 otherOptional.add("MarginMoney");
			 otherOptional.add("CreditLifeInsurance");
			
		 }
		 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
		 response.put("SELF_COMPULSORY", selfCompulsory);
		 response.put("SELF_OPTIONAL", selfOptional);
		 response.put("LOAN_DOCUMENTS_COMPULSORY", otherCompulsory);
		 response.put("LOAN_DOCUMENTS_OPTIONAL", otherOptional);
		 	
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
			
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	@RequestMapping(value="/fetchMemberImageEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchMemberImage(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, 
			 HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception{
		logger.debug("fetchMemberImage Start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			//userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		 String member = jsonObject.getJSONObject("Data").getString("member");
		 String documentType = jsonObject.getJSONObject("Data").getString("documentType");
		 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
		 if(documentType.equalsIgnoreCase("pancardResponse")) {
			 TwoWheelerFamilyMember memberFamilyMember = familyMemberService.getByApplicationNoAndMember(applicationNo, "APPLICANT");
			 if(memberFamilyMember.getPanCardResponse() != null) {
				 org.json.JSONObject pan=new org.json.JSONObject(memberFamilyMember.getPanCardResponse());
				 	response.put("Data", pan.toString());
				 	String data = response.toString();
					String encryptString2 = Crypt.encrypt(data, X_encode_ID);
					org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
					data2.put("value", encryptString2);
					org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3, HttpStatus.OK);
			 }
			 else {
				 throw new NoSuchElementException("pancardResponse not present");
			 }
			 	
		 }
		 List<Image> list=imageService.getByAppNoAndMember(applicationNo,member,documentType);
		 JSONArray j = new JSONArray(list);
		 	
				response.put("List", j);
				logger.debug("final response"+list.size());
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	@RequestMapping(value="/updateStatusEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> updateStatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, 
			 HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception{
		logger.debug("updateStatus Start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			//userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		 String status = jsonObject.getJSONObject("Data").getString("status");
		 JSONArray remark = jsonObject.getJSONObject("Data").getJSONArray("remark");
		
		 TwowheelerDetailesTable twowheelerDetailesTable = twowheelerDetailsService.getByApplication(applicationNo);
		 if(status.equalsIgnoreCase("APPROVED")) {
			 String checkList = twowheelerDetailesTable.getCheckList();
			 if(twowheelerDetailesTable.getCheckList() != null) {
					org.json.JSONArray checkListWeb =new org.json.JSONArray(checkList);
		  			for(int n=0;n<checkListWeb.length();n++) {
		  				JSONObject jsonObject2 = checkListWeb.getJSONObject(n);
		  				if(jsonObject2.getString("result").equalsIgnoreCase("IMAGE_DISTORTED")) {
		  					throw new NoSuchElementException("PLEASE UPLOAD ALL DOCUMENT PROPERLY");	
		  				}
		  			}
				}else {
					throw new NoSuchElementException("PLEASE UPLOAD ALL DOCUMENT PROPERLY");
				}
			 TwowheelerLoanCreation twowheelerLoanCreation= twowheelerLoanCreationService.fetchByapplicationNo(applicationNo); 
			 twowheelerLoanCreation.setApplicationNo(applicationNo);
			 twowheelerLoanCreation.setCreatedTimestamp(LocalDateTime.now());
			 twowheelerLoanCreation.setUpdatedTimestamp(LocalDateTime.now());
			 twowheelerLoanCreation.setStatus("INITIATED");
			 twowheelerLoanCreation.setListType(twowheelerDetailesTable.getListType());
			 twowheelerLoanCreation.setDisbursalAmount(twowheelerDetailesTable.getAmount());
			 twowheelerLoanCreation.setCustomerId(twowheelerDetailesTable.getCustomerId());
			 twowheelerLoanCreationService.save(twowheelerLoanCreation);
		 }
		 twowheelerDetailesTable.setStatus(status);
		 twowheelerDetailesTable.setRemark(remark.toString());
		 String saveData = twowheelerDetailsService.saveData(twowheelerDetailesTable);
		 				   twowheelerUserLogService.saveUserLog(applicationNo,status,X_User_ID);
		 				   
		 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
				response.put("message", saveData);
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/getLoanDetailsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getLoanDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, 
			 HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception{
		logger.debug("getLoanDetailsEncy Start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			//userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		
		 TwowheelerLoanCreation twowheelerLoanCreation =twowheelerLoanCreationService.getbyApplicationNo(applicationNo);
		 JSONObject j = new JSONObject(twowheelerLoanCreation);
		 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
				response.put("Data", j);
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/getDocumentTypesEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getDocumentTypes(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, 
			 HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception{
		logger.debug("getDocumentTypesEncy Start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			//userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		
		 Set<String> list =imageService.getDocumentTypesWeb(applicationNo);
//		 list.add("Invoice");
//		 list.add("AssetInsurance");
//		 list.add("RTO");
//		 list.add("SecurityPDC1");
//		 list.add("SecurityPDC2");
//		 list.add("DeliveryOrder");
//		 list.add("MarginMoney");
//		 list.add("CreditLifeInsurance");
//		 list.add("EbBill");
		 JSONArray j = new JSONArray(list);
		 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
				response.put("Data", j);
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/saveChecklistEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveChecklist(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, 
			 HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception{
		logger.debug("saveChecklist Start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			//userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		 JSONArray checkList = jsonObject.getJSONObject("Data").getJSONArray("checkList");
		 TwowheelerDetailesTable twowheelerDetailesTable = twowheelerDetailsService.getByApplication(applicationNo);
		 twowheelerDetailesTable.setCheckList(checkList.toString());
		 String saveData = twowheelerDetailsService.saveData(twowheelerDetailesTable);
		 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
				response.put("message", saveData);
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/getChecklistEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getChecklist(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, 
			 HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception{
		logger.debug("getChecklist Start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			//userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");

		 TwowheelerDetailesTable twowheelerDetailesTable = twowheelerDetailsService.getByApplication(applicationNo);
		 String checkList = twowheelerDetailesTable.getCheckList();
		 List<FinalSaction> checklist=new ArrayList<>();
			if(checkList != null) {
				org.json.JSONArray checkListWeb =new org.json.JSONArray(checkList);
	  			for(int n=0;n<checkListWeb.length();n++) {
	  				JSONObject jsonObject2 = checkListWeb.getJSONObject(n);
	  				String checklistname = jsonObject2.getString("checklistname");
	  				String result = jsonObject2.getString("result");
	  				String Remarks = jsonObject2.getString("remarks");
	  				FinalSaction finalSaction=new FinalSaction(checklistname,result,Remarks);
	  				checklist.add(finalSaction);
	  			}
			}
			JSONArray j = new JSONArray(checklist);
		 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
				response.put("Data", j);
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	@RequestMapping(value = "/fetchLoanDetailsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchLoanDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Branch-ID", required = true) String X_Branch_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, 
			 HttpServletRequest request) throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Request-ID", X_Request_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Branch-ID", X_Branch_ID);
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			//userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		TwowheelerLoanCreation twloanCreation = twowheelerLoanCreationService.getbyApplicationNo(applicationNo);
			if(!twloanCreation.isCRM()) {
				JSONObject crmModification = disbursementservice.crmModification(applicationNo,Header);
				if(crmModification!=null) {
					String crmResp = crmModification.getString("data");
					JSONObject crmJsonResp=new JSONObject(crmResp);
					if(crmJsonResp.has("Data")) {
						twloanCreation.setCRM(true);
						twloanCreation.setCrmModificationResponse(crmJsonResp.toString());
						twloanCreation.setCrmModificationRequest(crmModification.getJSONObject("request").toString());
						twowheelerLoanCreationService.save(twloanCreation);
					}
					else if (crmJsonResp.has("Error"))
					{
//						h = HttpStatus.BAD_REQUEST;
//						String data = crmJsonResp.toString();
//						String encryptString2 = Crypt.encrypt(data, X_encode_ID);
//						org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
//						data2.put("value", encryptString2);
//						org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
//						data3.put("Data", data2);
//						return new ResponseEntity<Object>(data3, h);
						twloanCreation.setErrorResponse(crmJsonResp.toString());
						twloanCreation.setCrmModificationResponse(crmJsonResp.toString());
						twloanCreation.setCrmModificationRequest(crmModification.getJSONObject("request").toString());
						twowheelerLoanCreationService.save(twloanCreation);
						JSONObject j=new JSONObject(twloanCreation);
						org.json.simple.JSONObject response = new org.json.simple.JSONObject();
						response.put("Data",j);
						String data = response.toString();
						String encryptString2 = Crypt.encrypt(data, X_encode_ID);
						org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
						data2.put("value", encryptString2);
						org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
						data3.put("Data", data2);
						return new ResponseEntity<Object>(data3, HttpStatus.OK);	
					}
				}else {
					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			}
			if(!twloanCreation.isLoanCreation()) {
				JSONObject loanCreation = disbursementservice.loanCreation(applicationNo, Header);
				if(loanCreation!=null) {
					String loanCreationResp = loanCreation.getString("data");
					logger.debug(loanCreationResp);
					JSONObject loanCreationJsonResp=new JSONObject(loanCreationResp);
					if(loanCreationJsonResp.has("Data")) {
						String loanAccountNo = loanCreationJsonResp.getJSONObject("Data").getString("ResponseValue");
						if(loanAccountNo.length()!=0) {
							twloanCreation.setLoanAccoutNumber(loanAccountNo);
							twloanCreation.setLoanCreation(true);
							twloanCreation.setLoanCreationRequest(loanCreation.getJSONObject("request").toString());
							twloanCreation.setLoanCreationResponse(loanCreationJsonResp.toString());
							twloanCreation.setLoanCreationDate(LocalDate.now());
							twowheelerLoanCreationService.save(twloanCreation);
							}
						
					}else if (loanCreationJsonResp.has("Error")) {
//						h = HttpStatus.BAD_REQUEST;
//						String data = loanCreationJsonResp.toString();
//						String encryptString2 = Crypt.encrypt(data, X_encode_ID);
//						org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
//						data2.put("value", encryptString2);
//						org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
//						data3.put("Data", data2);
//						return new ResponseEntity<Object>(data3, h);
						twloanCreation.setErrorResponse(loanCreationJsonResp.toString());
						twloanCreation.setLoanCreationRequest(loanCreation.getJSONObject("request").toString());
						twloanCreation.setLoanCreationResponse(loanCreationJsonResp.toString());
						twowheelerLoanCreationService.save(twloanCreation);
						JSONObject j=new JSONObject(twloanCreation);
						org.json.simple.JSONObject response = new org.json.simple.JSONObject();
						response.put("Data",j);
						String data = response.toString();
						String encryptString2 = Crypt.encrypt(data, X_encode_ID);
						org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
						data2.put("value", encryptString2);
						org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
						data3.put("Data", data2);
						return new ResponseEntity<Object>(data3, HttpStatus.OK);
					}
				}else {
					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			}
			
			if(!twloanCreation.isCollateralDetails()) {
				JSONObject collateralDetails = disbursementservice.collateralDetails(applicationNo, Header);
				if (collateralDetails != null) {
					String collateralDetailsResp = collateralDetails.getString("data");
					logger.debug(collateralDetailsResp);
					JSONObject collateralDetailsJsonResp = new JSONObject(collateralDetailsResp);
					if (collateralDetailsJsonResp.has("Data")) {
						h = HttpStatus.OK;
						String collateralNo = collateralDetailsJsonResp.getJSONObject("Data").getString("CollateralNumber");
						twloanCreation.setCollateralNo(collateralNo);
						twloanCreation.setCollateralDetails(true);
						twloanCreation.setCollateralDetailsRequest(collateralDetails.getJSONObject("request").toString());
						twloanCreation.setCollateralDetailsResponse(collateralDetailsJsonResp.toString());
						twowheelerLoanCreationService.save(twloanCreation);
					} else if (collateralDetailsJsonResp.has("Error")) {
//						h = HttpStatus.BAD_REQUEST;
//						String data = collateralDetailsJsonResp.toString();
//						String encryptString2 = Crypt.encrypt(data, X_encode_ID);
//						org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
//						data2.put("value", encryptString2);
//						org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
//						data3.put("Data", data2);
//						return new ResponseEntity<Object>(data3, h);
						twloanCreation.setErrorResponse(collateralDetailsJsonResp.toString());
						twloanCreation.setCollateralDetailsRequest(collateralDetails.getJSONObject("request").toString());
						twloanCreation.setCollateralDetailsResponse(collateralDetailsJsonResp.toString());
						twowheelerLoanCreationService.save(twloanCreation);
						JSONObject j=new JSONObject(twloanCreation);
						org.json.simple.JSONObject response = new org.json.simple.JSONObject();
						response.put("Data",j);
						String data = response.toString();
						String encryptString2 = Crypt.encrypt(data, X_encode_ID);
						org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
						data2.put("value", encryptString2);
						org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
						data3.put("Data", data2);
						return new ResponseEntity<Object>(data3, HttpStatus.OK);
					}
				}else {

					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			}	
			
		
			if(!twloanCreation.isDisbusrment()) {
				JSONObject disbursement = disbursementservice.disbursement(applicationNo, Header);
				if (disbursement != null) {
					String Data2 = disbursement.getString("data");
					logger.debug("data2");
					logger.debug(Data2);
					JSONObject Data1 = new JSONObject(Data2);
					if (Data1.has("Data")) {
						String disbTransId = Data1.getJSONObject("Data").getString("ResponseValue");
						System.out.println(disbTransId);
						twloanCreation.setDisbursedTransId(disbTransId);
						twloanCreation.setDisbusrment(true);
						twloanCreation.setDisbursementRequest(disbursement.getJSONObject("request").toString());
						twloanCreation.setDisbursementResponse(Data1.toString());
						twowheelerLoanCreationService.save(twloanCreation);
						h = HttpStatus.OK;
					} else if (Data1.has("Error")) {
//						h = HttpStatus.BAD_REQUEST;
//						String data = Data1.toString();
//						String encryptString2 = Crypt.encrypt(data, X_encode_ID);
//						org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
//						data2.put("value", encryptString2);
//						org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
//						data3.put("Data", data2);
//						return new ResponseEntity<Object>(data3, h);
						twloanCreation.setErrorResponse(Data1.toString());
						twloanCreation.setDisbursementResponse(Data1.toString());
						twloanCreation.setDisbursementRequest(disbursement.getJSONObject("request").toString());
						twowheelerLoanCreationService.save(twloanCreation);
						JSONObject j=new JSONObject(twloanCreation);
						org.json.simple.JSONObject response = new org.json.simple.JSONObject();
						response.put("Data",j);
						String data = response.toString();
						String encryptString2 = Crypt.encrypt(data, X_encode_ID);
						org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
						data2.put("value", encryptString2);
						org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
						data3.put("Data", data2);
						return new ResponseEntity<Object>(data3, HttpStatus.OK);
					}

				} else {

					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			}
			JSONObject j=new JSONObject(twloanCreation);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data",j);
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}

	}
	
	@RequestMapping(value="/saveTvrFormEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveTvrForm(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, 
			 HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception{
		logger.debug("saveTvrForm Start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		 String tvrForm = jsonObject.getJSONObject("Data").getJSONObject("tvrForm").toString();
		 
		 TwowheelerDetailesTable twowheelerDetailesTable = twowheelerDetailsService.getByApplication(applicationNo);
		 twowheelerDetailesTable.setTvrForm(tvrForm);
		 String saveData = twowheelerDetailsService.saveData(twowheelerDetailesTable);
		 
		 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
				response.put("message",saveData );
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
				
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
	
}
