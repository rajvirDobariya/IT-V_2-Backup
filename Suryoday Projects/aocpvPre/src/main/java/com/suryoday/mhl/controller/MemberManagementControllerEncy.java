package com.suryoday.mhl.controller;

import java.time.LocalDateTime;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.mhl.pojo.CibilEqifaxDetails;
import com.suryoday.mhl.pojo.MemberManagement;
import com.suryoday.mhl.service.CollateralDetailsService;
import com.suryoday.mhl.service.MHLAddressDetailService;
import com.suryoday.mhl.service.MHLCibilReportService;
import com.suryoday.mhl.service.MHLImageService;
import com.suryoday.mhl.service.MemberManagementService;

@RestController
@RequestMapping("/mhl")
public class MemberManagementControllerEncy {
	
	@Autowired
	MemberManagementService memberManagementService;

	@Autowired
	MHLImageService imageService;
	
	@Autowired
	MHLAddressDetailService addressDetailService;
	
	@Autowired
	CollateralDetailsService collateralDetailsService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	MHLCibilReportService mHLCibilReportService;
	
	Logger logger = LoggerFactory.getLogger(MemberManagementControllerEncy.class);
	
	@RequestMapping(value="/personalDetails/saveDataEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> savePersonalDetailsData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		//System.out.println(jsonRequest);
	boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	  if (sessionId == true ) {
		 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
	//logger.debug("start request" + jsonRequest.toString());

	String key = X_Session_ID;

	String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
		JSONObject jsonObject=new JSONObject(decryptContainerString);
		logger.debug("request"+decryptContainerString);
		//System.out.println("JSON STRING --->"+jsonObject);
		
		if(decryptContainerString.isEmpty()) {
			logger.debug("request is empty"+decryptContainerString);
			throw new EmptyInputException("Input cannot be empty");
		}
		
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String applicationRole = jsonObject.getJSONObject("Data").getString("applicationRole");
		String memberId = jsonObject.getJSONObject("Data").getString("memberId");
		String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
		String kycDetails = jsonObject.getJSONObject("Data").getString("kycDetails");
		String kycNumber = jsonObject.getJSONObject("Data").getString("kycNumber");
		String addressProof = jsonObject.getJSONObject("Data").getString("addressProof");
		String otherProof = jsonObject.getJSONObject("Data").getString("otherProof");
		String title = jsonObject.getJSONObject("Data").getString("title");
		String firstName = jsonObject.getJSONObject("Data").getString("firstName");
		String middleName = jsonObject.getJSONObject("Data").getString("middleName");
		String lastName = jsonObject.getJSONObject("Data").getString("lastName");
		String dateOfBirth = jsonObject.getJSONObject("Data").getString("dateOfBirth");
		JSONObject object = jsonObject.getJSONObject("Data");
		int age_cust =0;
		if(object.has("age")) {
			String age = jsonObject.getJSONObject("Data").getString("age");	
			 age_cust = Integer.parseInt(age);
		}
		String gender = jsonObject.getJSONObject("Data").getString("gender");
		String fatherName = jsonObject.getJSONObject("Data").getString("fatherName");
		String panNo = jsonObject.getJSONObject("Data").getString("pancardNo");
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		String isBankCustomer = jsonObject.getJSONObject("Data").getString("isBankCustomer");
		String customerId = jsonObject.getJSONObject("Data").getString("customerId");
		String emailId = jsonObject.getJSONObject("Data").getString("emailId");
		String customerCategory = jsonObject.getJSONObject("Data").getString("customerCategory");
		String occupation = jsonObject.getJSONObject("Data").getString("occupation");
		String educationQualification = jsonObject.getJSONObject("Data").getString("educationQualification");
		String noDependency = jsonObject.getJSONObject("Data").getString("noDependency");
		String noFamilyMember = jsonObject.getJSONObject("Data").getString("noFamilyMember");
		String caste = jsonObject.getJSONObject("Data").getString("caste");
		String religion = jsonObject.getJSONObject("Data").getString("religion");
		String motherName = jsonObject.getJSONObject("Data").getString("motherName");
		String marriedStatus = jsonObject.getJSONObject("Data").getString("marriedStatus");
		String spouseName = jsonObject.getJSONObject("Data").getString("spouseName");
		JSONArray jsonArray = jsonObject.getJSONObject("Data").getJSONArray("Address");
		
		MemberManagement member = new MemberManagement();
		
		member.setApplicationNo(applicationNo);
		member.setApplicationRole(applicationRole);
		long memberIdInLong= Long.parseLong(memberId);
		member.setMemberId(memberIdInLong);
		member.setMobileNo(mobileNo);
		member.setKycDetails(kycDetails);
		member.setKycNo(kycNumber);
		member.setAddressProof(addressProof);
		member.setOtherProof(otherProof);
		member.setTitle(title);
		member.setFirstName(firstName);
		member.setMiddleName(middleName);
		member.setLastName(lastName);
		member.setDateOfBirth(dateOfBirth);
		member.setAge(age_cust);
		member.setGender(gender);
		member.setFatherName(fatherName);
		member.setBranchId(branchId);
		member.setIsBankCustomer(isBankCustomer);
		member.setCustomerId(customerId);
		member.setEmailId(emailId);
		member.setCustomerCategory(customerCategory);
		member.setOccupation(occupation);
		member.setEducationQualification(educationQualification);
		member.setNoDependency(noDependency);
		member.setNoFamilyMember(noFamilyMember);
		member.setCaste(caste);
		member.setReligion(religion);
		member.setMotherName(motherName);
		member.setMarriedStatus(marriedStatus);
		member.setSpouseName(spouseName);
		member.setPanNo(panNo);
		LocalDateTime now = LocalDateTime.now();
		member.setCreationDate(now);
		member.setUpdatedate(now);
		//logger.debug("Db call start"+member);
		String saveMember = memberManagementService.savePersonalData(member);
		System.out.println(saveMember+"---------------------------");
		//logger.debug("Db call end"+saveMember);
		addressDetailService.saveAddress(jsonArray,applicationNo,applicationRole,memberId);
		org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
		response.put("message", saveMember);
		response.put("status", HttpStatus.OK.toString());
		logger.debug("final response "+response.toString());
		
		String	data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
		data2.put("value", encryptString2);
		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
		data3.put("Data", data2);
		System.out.println(data3);
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
	
	@RequestMapping(value="/saveImageEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveImage(@RequestParam("file") MultipartFile files,@RequestParam("Data") String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		//System.out.println(jsonRequest);
	boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	  if (sessionId == true ) {
		 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
	//logger.debug("start request" + jsonRequest.toString());

	String key = X_Session_ID;

	String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
		JSONObject jsonObject=new JSONObject(decryptContainerString);
		logger.debug("request"+decryptContainerString);
		//System.out.println("JSON STRING --->"+jsonObject);
		
		if(decryptContainerString.isEmpty()) {
			logger.debug("request is empty"+decryptContainerString);
			throw new EmptyInputException("Input cannot be empty");
		}
				logger.debug("image save to db start");
				String saveImage =imageService.saveImage(files,jsonObject);
				logger.debug("image save to db sucessfully");
				org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
				response.put("message", saveImage);
				response.put("status", HttpStatus.OK.toString());
				logger.debug("final response"+response.toString());
				String	data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				System.out.println(data3);
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
	
	
	// Fetch Member
	
		@RequestMapping(value="/fetchByApplicationNoAndMemberEncy", method = RequestMethod.POST)
		public ResponseEntity<Object> fetchByApplicationNoAndMember(@RequestBody String jsonRequest ,
				@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
				 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
				 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
				 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
				 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
				 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
					HttpServletRequest request,
				 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		  if (sessionId == true ) {
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				System.out.println(encryptString);
		//logger.debug("start request" + jsonRequest.toString());

		String key = X_Session_ID;

		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
			logger.debug("request"+decryptContainerString);
			System.out.println("JSON STRING --->"+jsonObject);
			if(decryptContainerString.isEmpty()) {
				logger.debug("request is empty"+decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
			
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String member = jsonObject.getJSONObject("Data").getString("member");
			MemberManagement memberM = memberManagementService.findMemberByApplicationNo(applicationNo,member);
			//logger.debug("Details"+memberM);
			String bureauReportResponse=null;
			CibilEqifaxDetails cibilEqifaxDetails =mHLCibilReportService.fetchByapplicationNoAndMemberId(applicationNo,memberM.getMemberId());
			if(cibilEqifaxDetails!= null) {
				 bureauReportResponse = cibilEqifaxDetails.getBureauReportResponse();
			}
			System.out.println("Details ----->"+memberM);
			org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
			JSONObject j=new JSONObject(memberM);
			response.put("status", HttpStatus.OK.toString());
			response.put("Data", j);
			response.put("BureauReportResponse", bureauReportResponse);
			logger.debug("final response"+response.toString());
			String	data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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
		
		@RequestMapping(value="/collateralDetails/saveDataEncy", method = RequestMethod.POST)
		public ResponseEntity<Object> saveCollateralDetailsData(@RequestBody String jsonRequest,
				@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
				 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
				 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
				 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
				 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
				 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
					HttpServletRequest request,
				 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		  if (sessionId == true ) {
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				System.out.println(encryptString);
		//logger.debug("start request" + jsonRequest.toString());

		String key = X_Session_ID;

		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
			logger.debug("request"+decryptContainerString);
			System.out.println("JSON STRING --->"+jsonObject);
			
			if(decryptContainerString.isEmpty()) {
				logger.debug("request is empty"+decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
			logger.debug("db Call start");
			String save=collateralDetailsService.saveCollateralDetails(jsonObject);
			logger.debug("db Call end Collateral Details Save");
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			JSONArray jsonArray = jsonObject.getJSONObject("Data").getJSONArray("Address");
			logger.debug("db Call start");
			addressDetailService.savePropertyAddress(jsonArray,applicationNo);
			logger.debug("db Call end Property Address Save");
			
			org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
			response.put("message", save);
			response.put("status", HttpStatus.OK.toString());
			logger.debug("final response"+response.toString());
			String	data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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
		
		@RequestMapping(value="/saveMultipleImagesEncy", method = RequestMethod.POST)
		public ResponseEntity<Object> saveMultipleImages(@RequestParam("file") MultipartFile files[],@RequestParam("Data") String jsonRequest,
				@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
				 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
				 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
				 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
				 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
				 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
					HttpServletRequest request,
				 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		//	System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		  if (sessionId == true ) {
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				System.out.println(encryptString);
		//logger.debug("start request" + jsonRequest.toString());

		String key = X_Session_ID;

		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
			logger.debug("request"+decryptContainerString);
			System.out.println("JSON STRING --->"+jsonObject);
			
			if(decryptContainerString.isEmpty()) {
				logger.debug("request is empty"+decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
			
			
			logger.debug("image save to db start");
			String saveImage =imageService.saveMultipleImages(files,jsonObject);
			logger.debug("image save to db sucessfully");
			org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
			response.put("message", saveImage);
			response.put("status", HttpStatus.OK.toString());
			logger.debug("final response"+response.toString());
			String	data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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
		@RequestMapping(value="/deleteMemberEncy", method = RequestMethod.POST)
		public ResponseEntity<Object> deleteMember(@RequestBody String jsonRequest,
				@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
				 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
				 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
				 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
				 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
				 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
					HttpServletRequest request,
				 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		  if (sessionId == true ) {
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				System.out.println(encryptString);
		//logger.debug("start request" + jsonRequest.toString());

		String key = X_Session_ID;

		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
			logger.debug("request"+decryptContainerString);
			System.out.println("JSON STRING --->"+jsonObject);
			
			if(decryptContainerString.isEmpty()) {
				logger.debug("request is empty"+decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
			
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String member = jsonObject.getJSONObject("Data").getString("member");
			logger.debug("db Call start");
			String message=memberManagementService.deleteByAppNoAndMember(applicationNo,member);
			logger.debug("db Call end member is deleted");
			
			org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
			response.put("message", "member deleted");
			response.put("status", HttpStatus.OK.toString());
			logger.debug("final response"+response.toString());
			String	data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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
		@RequestMapping(value="/fetchByApplicationNoEncy", method = RequestMethod.POST)
		public ResponseEntity<Object> fetchByApplicationNo(@RequestBody String jsonRequest ,
				@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
				 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
				 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
				 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
				 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
				 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
					HttpServletRequest request,
				 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		  if (sessionId == true ) {
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				System.out.println(encryptString);
		//logger.debug("start request" + jsonRequest.toString());

		String key = X_Session_ID;

		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
			logger.debug("request"+decryptContainerString);
			//System.out.println("JSON STRING --->"+jsonObject);
			if(decryptContainerString.isEmpty()) {
				logger.debug("request is empty"+decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
			
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			List<MemberManagement> memberM = memberManagementService.findMemberByApplicationNo(applicationNo);
			logger.debug("Details"+memberM);
			System.out.println("Details ----->"+memberM);
			org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
			response.put("status", HttpStatus.OK.toString());
			JSONArray j= new JSONArray(memberM);
			int count =memberM.size();
			response.put("Data", j);
			logger.debug("final response size"+count);
			String	data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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
		
		@RequestMapping(value="/createMemberEncy", method = RequestMethod.POST)
		public ResponseEntity<Object> createMember(@RequestBody String jsonRequest ,
				@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
				 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
				 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
				 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
				 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
				 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
					HttpServletRequest request,
				 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		  if (sessionId == true ) {
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				System.out.println(encryptString);
		//logger.debug("start request" + jsonRequest.toString());

		String key = X_Session_ID;

		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
			//logger.debug("request"+jsonRequest);
			//System.out.println("JSON STRING --->"+jsonObject);
			if(decryptContainerString.isEmpty()) {
				logger.debug("request is empty"+decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
			JSONObject object = jsonObject.getJSONObject("Data");
			String applicationNo = object.getString("applicationNo");
			String applicationRole = object.getString("applicationRole");
			String title = object.getString("title");
			String firstName = object.getString("firstName");
			String middleName = object.getString("middleName");
			String lastName = object.getString("lastName");
			String mobileNo = object.getString("mobileNo");
			String panNo = object.getString("pancardNo");
			String dateOfBirth = object.getString("dateOfBirth");
			String emailId = object.getString("emailId");
			MemberManagement member = new MemberManagement();
			
			member.setApplicationNo(applicationNo);
			member.setApplicationRole(applicationRole);
			member.setMobileNo(mobileNo);
			member.setTitle(title);
			member.setFirstName(firstName);
			member.setMiddleName(middleName);
			member.setLastName(lastName);
			member.setDateOfBirth(dateOfBirth);	
			member.setEmailId(emailId);		
			member.setPanNo(panNo);
			LocalDateTime now = LocalDateTime.now();
			member.setCreationDate(now);
			member.setUpdatedate(now);
			//logger.debug("Db call start"+member);
			String saveMember = memberManagementService.savePersonalData(member);
			System.out.println(saveMember+"---------------------------");
			logger.debug("Db call end"+saveMember);
			org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
			response.put("message", saveMember);
			response.put("status", HttpStatus.OK.toString());
			logger.debug("final response "+response.toString());
			String	data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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
}
