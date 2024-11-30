package com.suryoday.dsaOnboard.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.connector.service.UserService;
import com.suryoday.dsaOnboard.pojo.DsaOnboardDetails;
import com.suryoday.dsaOnboard.service.ConnectorUserLoginService;
import com.suryoday.dsaOnboard.service.DsaOnBoardDetailsService;
import com.suryoday.dsaOnboard.service.DsaUserLogService;

@Component
@RestController
@RequestMapping(value="/dsaOnBoard")
public class ConnectorUserLoginController {

	@Autowired
	ConnectorUserLoginService userLoginService;

	@Autowired
	DsaOnBoardDetailsService dsaOnBoardDetailsService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DsaUserLogService dsaUserLogService;
	
	@RequestMapping(value = "/SignUpUser", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> SignUpUser(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject jsonObject = new org.json.JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String typeOfRelationship = jsonObject.getJSONObject("Data").getString("typeOfRelationship");
		String productType = jsonObject.getJSONObject("Data").getString("productType");
		String noOfPartner = jsonObject.getJSONObject("Data").getString("noOfPartner");
		String password = jsonObject.getJSONObject("Data").getString("Password");
		String emailId = jsonObject.getJSONObject("Data").getString("EmailID");
		String companyName = jsonObject.getJSONObject("Data").getString("CompanyName");
		String entity = jsonObject.getJSONObject("Data").getString("Entity");
		String leadId = jsonObject.getJSONObject("Data").getString("leadId");
		String leadName = jsonObject.getJSONObject("Data").getString("leadName");
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		String constitutionType = jsonObject.getJSONObject("Data").getString("constitutionType");
		
		String checkEmailId = userLoginService.checkEmailId(emailId);
//		String checkMobileNo = userLoginService.checkMobileNo(mobileNo);
		String checkStrongness = CheckStrongness(password);
		if (!checkStrongness.equals("strong")) {
			throw new EmptyInputException("please  enter Strong  password");
		}
		String encryptPassword = encryptPassword(password);
		DsaOnboardDetails dsaOnboardDetails = dsaOnBoardDetailsService.getByApplicationno(applicationNo);
		
		dsaOnboardDetails.setEmailId(emailId);
		dsaOnboardDetails.setCompanyName(companyName);
		dsaOnboardDetails.setPassword(encryptPassword);
		dsaOnboardDetails.setEntity(entity);
		dsaOnboardDetails.setTypeOfRelationship(typeOfRelationship);
		dsaOnboardDetails.setProductType(productType);
		dsaOnboardDetails.setNoOfPartner(noOfPartner);
		dsaOnboardDetails.setLeadId(leadId);
		dsaOnboardDetails.setBranchId(branchId);
		dsaOnboardDetails.setLeadName(leadName);
		dsaOnboardDetails.setConstitutionType(constitutionType);
		dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
		dsaOnboardDetails.setStatus("Progress");
		dsaOnboardDetails.setFlowStatus("DSAREG");
		String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
		
		dsaUserLogService.saveUserLog(Long.parseLong(applicationNo), "REGISTRACTION", X_User_ID,null);
		
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("message", saveData);

		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	public static String CheckStrongness(String input) {
		// Checking lower alphabet in string
		int n = input.length();
		boolean hasLower = false, hasUpper = false, hasDigit = false, specialChar = false;
		Set<Character> set = new HashSet<Character>(
				Arrays.asList('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+'));
		for (char i : input.toCharArray()) {
			if (Character.isLowerCase(i))
				hasLower = true;
			if (Character.isUpperCase(i))
				hasUpper = true;
			if (Character.isDigit(i))
				hasDigit = true;
			if (set.contains(i))
				specialChar = true;
		}

		if (hasDigit && hasLower && hasUpper && specialChar && (n >= 8))
			return "strong";

		else
			return "weak";
	}

	public static String encryptPassword(String password) {
		String encryptedpassword = null;
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(password.getBytes());

			byte[] bytes = m.digest();

			StringBuilder s = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			encryptedpassword = s.toString();
		} catch (NoSuchAlgorithmException e) {
			e.getMessage();
		}
		return encryptedpassword;

	}
	
	
	@RequestMapping(value = "/SignInUser", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> SignInUser(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject jsonObject = new org.json.JSONObject(jsonRequest);
		
		String emailId = jsonObject.getJSONObject("Data").getString("EmailID");
		String password = jsonObject.getJSONObject("Data").getString("Password");
		String encryptPassword = encryptPassword(password);
	//	LoginResponse dsaOnboardDetails=dsaOnBoardDetailsService.validateUser(emailId,encryptPassword);
		
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", "");
	

		return new ResponseEntity<Object>(response, HttpStatus.OK);
	
	}
	
//	@RequestMapping(value = "/validateLeadId", method = RequestMethod.POST, produces = "application/json")
//	public ResponseEntity<Object> validateLeadId(@RequestBody String jsonRequest,
//			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
//			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
//			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
//			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
//			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
//			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
//
//		org.json.JSONObject jsonObject = new org.json.JSONObject(jsonRequest);
//		
//		String leadId = jsonObject.getJSONObject("Data").getString("leadId");
//		User fetchById = userService.fetchById(leadId);
//		LeadResponse leadResponse=new LeadResponse(X_User_ID, fetchById.getUserName(), fetchById.getBranchId(), fetchById.getIsActive(), fetchById.getDesignation());
//		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
//		response.put("Data", leadResponse);
//
//		return new ResponseEntity<Object>(response, HttpStatus.OK);
//	
//	}
}
