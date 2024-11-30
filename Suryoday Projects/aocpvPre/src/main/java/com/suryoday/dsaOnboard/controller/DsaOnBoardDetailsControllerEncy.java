package com.suryoday.dsaOnboard.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.dsaOnboard.pojo.BankDetailsResponse;
import com.suryoday.dsaOnboard.pojo.BusinessReference;
import com.suryoday.dsaOnboard.pojo.CreditFeedbackResponse;
import com.suryoday.dsaOnboard.pojo.DeviationDetailsResponse;
import com.suryoday.dsaOnboard.pojo.DsaOnboardDetails;
import com.suryoday.dsaOnboard.pojo.DsaOnboardMember;
import com.suryoday.dsaOnboard.pojo.DsaOnboardMemberResponse;
import com.suryoday.dsaOnboard.pojo.DsaOnboardPayoutDetails;
import com.suryoday.dsaOnboard.pojo.DsaOnboardResponse;
import com.suryoday.dsaOnboard.pojo.EmpanellementCriteriaResponse;
import com.suryoday.dsaOnboard.pojo.LoginResponse;
import com.suryoday.dsaOnboard.pojo.ReferenceCheckResponse;
import com.suryoday.dsaOnboard.pojo.RelationToSsfbStaffResponse;
import com.suryoday.dsaOnboard.pojo.SalesManagerFeedbackResponse;
import com.suryoday.dsaOnboard.service.DmsUploadService;
import com.suryoday.dsaOnboard.service.DsaCodeCreationService;
import com.suryoday.dsaOnboard.service.DsaImageService;
import com.suryoday.dsaOnboard.service.DsaOnBoardDetailsService;
import com.suryoday.dsaOnboard.service.DsaOnBoardSendSmsService;
import com.suryoday.dsaOnboard.service.DsaOnboardMemberService;
import com.suryoday.dsaOnboard.service.DsaPayoutDetailsService;
import com.suryoday.dsaOnboard.service.DsaUserLogService;
import com.suryoday.twowheeler.pojo.DocCheckListResponse;
import com.suryoday.twowheeler.service.TwowheelerNameMatchService;

@RestController
@RequestMapping("/dsaOnBoard")
public class DsaOnBoardDetailsControllerEncy {

	@Autowired
	DsaOnBoardDetailsService dsaOnBoardDetailsService;
	
	@Autowired
	DsaImageService imageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DsaOnboardMemberService dsaOnboardMemberService;
	
	@Autowired
	DsaUserLogService dsaUserLogService;
	
	@Autowired
	DsaPayoutDetailsService dsaPayoutDetailsService;
	
	@Autowired
	DsaOnBoardSendSmsService dsaOnBoardSendSmsService;
	
	@Autowired
	TwowheelerNameMatchService nameMatchService;
	
	@Autowired
	DsaCodeCreationService dsaCodeCreationService;
	
	@Autowired 
	DmsUploadService dmsUploadService;
	
	Logger logger = LoggerFactory.getLogger(DsaOnBoardDetailsControllerEncy.class);
			
	@RequestMapping(value = "/saveRegistrationDetailsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveRegistrationDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		
		DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationNo(applicationNo);
		
		String typeOfRelationship = jsonObject.getJSONObject("Data").getString("typeOfRelationship");
		String productType = jsonObject.getJSONObject("Data").getString("productType");
		String noOfPartner = jsonObject.getJSONObject("Data").getString("noOfPartner");
		String emailId = jsonObject.getJSONObject("Data").getString("EmailID");
		String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
		String companyName = jsonObject.getJSONObject("Data").getString("CompanyName");
		String entity = jsonObject.getJSONObject("Data").getString("Entity");
		String leadId = jsonObject.getJSONObject("Data").getString("leadId");
		String leadName = jsonObject.getJSONObject("Data").getString("leadName");
		String branchId = jsonObject.getJSONObject("Data").getString("branchId");
		String constitutionType = jsonObject.getJSONObject("Data").getString("constitutionType");
		
		dsaOnboardDetails.setEmailId(emailId);
		dsaOnboardDetails.setCompanyName(companyName);
		dsaOnboardDetails.setEntity(entity);
		dsaOnboardDetails.setTypeOfRelationship(typeOfRelationship);
		dsaOnboardDetails.setProductType(productType);
		dsaOnboardDetails.setNoOfPartner(noOfPartner);
		if(dsaOnboardDetails.getLeadId() == null) {
			dsaOnboardDetails.setLeadId(leadId);
			dsaOnboardDetails.setBranchId(branchId);
			dsaOnboardDetails.setLeadName(leadName);
		}
		dsaOnboardDetails.setConstitutionType(constitutionType);
		dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
		dsaOnboardDetails.setStatus("Progress");
		dsaOnboardDetails.setFlowStatus("DSAREG");
		
		if(dsaOnboardDetails.getEntity() == null){
			DsaOnboardMember dsaOnboardMember=new DsaOnboardMember();
			dsaOnboardMember.setMobile(mobileNo);
			dsaOnboardMember.setMobileNoVerify(true);
			dsaOnboardMember.setApplicationNo(Long.parseLong(applicationNo));
			dsaOnboardMember.setMember(RandomStringUtils.randomAlphanumeric(10));
			dsaOnboardMember.setIsPrimaryMember("YES");
			String saveData = dsaOnboardMemberService.saveData(dsaOnboardMember);
		}
		
		String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
		
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveData);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/saveBasicDetailsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveBasicDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String member = jsonObject.getJSONObject("Data").getString("member");
		
		DsaOnboardMember dsaOnboardMember=dsaOnboardMemberService.getByApplicationNoAndMember(applicationNo,member);
		
		String panCardNo = jsonObject.getJSONObject("Data").getString("panCardNo");
		String name = jsonObject.getJSONObject("Data").getString("name");
		String emailId = jsonObject.getJSONObject("Data").getString("emailId");
		String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
//		boolean mobileNoVerify = jsonObject.getJSONObject("Data").getBoolean("mobileNoVerify");
		String gender = jsonObject.getJSONObject("Data").getString("gender");
		String gstNo = jsonObject.getJSONObject("Data").getString("gstNo");
		if(jsonObject.getJSONObject("Data").has("secondaryGst")) {
			String secondaryGst = jsonObject.getJSONObject("Data").getJSONArray("secondaryGst").toString();	
			dsaOnboardMember.setSecondaryGst(secondaryGst);
			}
		String presentOccupation = jsonObject.getJSONObject("Data").getString("presentOccupation");
		String alternateMobileNo = jsonObject.getJSONObject("Data").getString("alternateMobileNo");
//		boolean alternateMobileNoVerify = jsonObject.getJSONObject("Data").getBoolean("alternateMobileNoVerify");
		String aadharNo = jsonObject.getJSONObject("Data").getString("aadharNo");
		String mobLinkwithAadhar = jsonObject.getJSONObject("Data").getString("mobLinkwithAadhar");
		String dateOfBirth = jsonObject.getJSONObject("Data").getString("dateOfBirth");
		String isMsmeRegister = jsonObject.getJSONObject("Data").getString("isMsmeRegister");
		String fatherName = jsonObject.getJSONObject("Data").getString("fatherName");
		String companyName = jsonObject.getJSONObject("Data").getString("companyName");
		
		dsaOnboardMember.setApplicationNo(Long.parseLong(applicationNo));
//		dsaOnboardMember.setPanCardNo(panCardNo);
		dsaOnboardMember.setName(name);
		dsaOnboardMember.setGender(gender);
		dsaOnboardMember.setPresentOccupation(presentOccupation);
		dsaOnboardMember.setAlternateMobileNo(alternateMobileNo);
//		dsaOnboardMember.setAlternateMobileNoVerify(alternateMobileNoVerify);
		dsaOnboardMember.setAadharNo(aadharNo);
		dsaOnboardMember.setEmailId(emailId);
		dsaOnboardMember.setMobLinkwithAadhar(mobLinkwithAadhar);
		dsaOnboardMember.setDateOfBirth(dateOfBirth);
		dsaOnboardMember.setMember(member);
		dsaOnboardMember.setFatherName(fatherName);
		dsaOnboardMember.setMobile(mobileNo);
		dsaOnboardMember.setIsMsmeRegister(isMsmeRegister);
		dsaOnboardMember.setGstNo(gstNo);
		dsaOnboardMember.setFaceMatch("");
		dsaOnboardMember.setFaceMatchPercent("0");
		dsaOnboardMember.setCompanyName(companyName);
		
		if(dsaOnboardMember.getNameOnPanCard()!=null && dsaOnboardMember.getNameOnAadhar()!=null && dsaOnboardMember.getNameMatch()==null) {
			
			JSONObject nameMatch = nameMatchService.nameMatch(dsaOnboardMember.getNameOnPanCard(),dsaOnboardMember.getNameOnAadhar());
			if (nameMatch != null) {
				String nameMatchString = nameMatch.getString("data");
				JSONObject nameMatchJson = new JSONObject(nameMatchString);
				if (nameMatchJson.has("Data")) {
					String nameMAtchScore = nameMatchJson.getJSONObject("Data").getJSONObject("Result").getString("Score");
					double nameInDouble = Double.parseDouble(nameMAtchScore);
					nameInDouble=nameInDouble*100;
					dsaOnboardMember.setNameMatch("YES");
					dsaOnboardMember.setNameMatchPercent(Double.toString(nameInDouble));
			
				} 
			}	
		
		}
		if(dsaOnboardMember.getIsPrimaryMember() ==null) {
			dsaOnboardMember.setIsPrimaryMember("NO");
		}
		String saveData = dsaOnboardMemberService.saveData(dsaOnboardMember);
		dsaOnBoardDetailsService.updateFlowStatus(Long.parseLong(applicationNo),"DSAPD");
		
		dsaUserLogService.saveUserLog(Long.parseLong(applicationNo), "PROGRESS", X_User_ID,"PROGRESS");
		
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveData);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/saveAddressDetailsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveAddressDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String member = jsonObject.getJSONObject("Data").getString("member");
		String addressForCommunication = jsonObject.getJSONObject("Data").getString("addressForCommunication");
		
		DsaOnboardMember dsaOnboardMember=dsaOnboardMemberService.getByApplicationnoAndMember(applicationNo,member);
		
		String address = jsonObject.getJSONObject("Data").getJSONArray("Address").toString();
		dsaOnboardMember.setAddress(address);
		dsaOnboardMember.setAddressForCommunication(addressForCommunication);
		
		String saveData = dsaOnboardMemberService.saveData(dsaOnboardMember);
		dsaOnBoardDetailsService.updateFlowStatus(Long.parseLong(applicationNo),"DSADD");
		
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveData);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/saveBankDetailsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveBankDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		
		DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
		
		String ifscCode = jsonObject.getJSONObject("Data").getString("ifscCode");
		String bankAccountNo = jsonObject.getJSONObject("Data").getString("bankAccountNo");
		String bankName = jsonObject.getJSONObject("Data").getString("bankName");
		String branchName = jsonObject.getJSONObject("Data").getString("branchName");
		String accountholderName = jsonObject.getJSONObject("Data").getString("accountholderName");
		
		
		dsaOnboardDetails.setIfscCode(ifscCode);
		dsaOnboardDetails.setBankAccountNo(bankAccountNo);
		dsaOnboardDetails.setBankName(bankName);
		dsaOnboardDetails.setBranchName(branchName);
		dsaOnboardDetails.setAccountholderName(accountholderName);
		dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
		dsaOnboardDetails.setFlowStatus("DSABD");
		
		String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveData);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/saveBusinessReferenceEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveBusinessReference(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String businessReference = jsonObject.getJSONObject("Data").getJSONArray("businessReference").toString();
		DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
		dsaOnboardDetails.setBusinessReference(businessReference);
		dsaOnboardDetails.setFlowStatus("DSABR");
		dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
		String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveData);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/getByApplicationNoEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getByApplicationNo(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		
		DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
		
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		JSONObject j = new JSONObject(dsaOnboardDetails);
		response.put("status", HttpStatus.OK.toString());
		response.put("Data", j);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/saveImageEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveImage(@RequestParam("file") MultipartFile[] files,
			@RequestParam("Data") String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String member = jsonObject.getJSONObject("Data").getString("member");
		org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
		
		if (!document.isEmpty()) {
			imageService.saveImage(files, applicationNo, document,member);
		}

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", "Image Saved");
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/getReviewDataEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getReviewData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		
		org.json.simple.JSONObject fetchByApplicationNo=dsaOnBoardDetailsService.getReviewData(applicationNo,"mob");
		
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		
		response.put("status", HttpStatus.OK.toString());
		response.put("Data", fetchByApplicationNo);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/saveRelationToStaffDetailsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveRelationToStaffDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String relationToSsfbStaff = jsonObject.getJSONObject("Data").getJSONArray("relationToSsfbStaff").toString();
			DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
			dsaOnboardDetails.setRelationToSsfbStaff(relationToSsfbStaff);
			dsaOnboardDetails.setFlowStatus("DSARTS");
			dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
			String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
			
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveData);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/saveOtherDetailsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveOtherDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String noOfYearsInBusiness = jsonObject.getJSONObject("Data").getString("noOfYearsInBusiness");
		String noOfYearsInFinancialBusiness = jsonObject.getJSONObject("Data").getString("noOfYearsInFinancialBusiness");
		String empanelledFiType = jsonObject.getJSONObject("Data").getString("empanelledFiType");
		String empanelledFiName = jsonObject.getJSONObject("Data").getString("empanelledFiName");
		String cfrReport = jsonObject.getJSONObject("Data").getJSONArray("cfrReport").toString();
		String amlReport = jsonObject.getJSONObject("Data").getJSONObject("amlReport").toString();
		String ageOfProprietor = jsonObject.getJSONObject("Data").getString("ageOfProprietor");
//		String otherServices = jsonObject.getJSONObject("Data").getString("otherServices");
		
		DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
		
		dsaOnboardDetails.setFlowStatus("DSAOD");
		dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
		dsaOnboardDetails.setNoOfYearsInBusiness(noOfYearsInBusiness);
		dsaOnboardDetails.setNoOfYearsInFinancialBusiness(noOfYearsInFinancialBusiness);
		dsaOnboardDetails.setEmpanelledFiType(empanelledFiType);
		dsaOnboardDetails.setEmpanelledFiName(empanelledFiName);
		dsaOnboardDetails.setCfrReport(cfrReport);
		dsaOnboardDetails.setAgeOfProprietor(ageOfProprietor);
		dsaOnboardDetails.setAmlReport(amlReport);
//		dsaOnboardDetails.setOtherServices(otherServices);
		
		String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveData);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/saveSalesManagerFeedbackEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveSalesManagerFeedback(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			JSONObject salesManagerFeedback = jsonObject.getJSONObject("Data").getJSONObject("salesManagerFeedback");
			DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
			dsaOnboardDetails.setSalesManagerFeedback(salesManagerFeedback.toString());
			dsaOnboardDetails.setFlowStatus("DSASMF");
			dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
			dsaUserLogService.saveUserLog(Long.parseLong(applicationNo), "SALESMANAGER", X_User_ID,salesManagerFeedback.getString("salesManagerRemarks"));
			String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveData);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/fetchByDateEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByDate(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
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

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
		LocalDateTime enddate = LocalDateTime.parse(endDate, formatter);

	
		List<DsaOnboardResponse> fetchByApplicationNo = dsaOnBoardDetailsService.fetchByDate(startdate, enddate, status,branchId);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		JSONArray j= new JSONArray(fetchByApplicationNo);
		response.put("status", HttpStatus.OK.toString());
		response.put("Data", j);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/getByApplicationNoAndFlowStatusEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getByApplicationNoAndFlowStatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String flowStatus = jsonObject.getJSONObject("Data").getString("flowStatus");
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		if(flowStatus.equals("DSAPD")) {
			List<DsaOnboardMemberResponse> list=dsaOnboardMemberService.getByApplicationNo(Long.parseLong(applicationNo));
			JSONArray j = new JSONArray(list);
			response.put("Data", j);
		}else if(flowStatus.equals("DSARTS")) {
			DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
			if(dsaOnboardDetails.getRelationToSsfbStaff() != null) {
				String relationToSsfbStaff = dsaOnboardDetails.getRelationToSsfbStaff();
				org.json.JSONArray businessReferenceInArray =new org.json.JSONArray(relationToSsfbStaff);
				 ObjectMapper objectMapper = new ObjectMapper();
				 try {
				 List<RelationToSsfbStaffResponse> reference = objectMapper.readValue(relationToSsfbStaff, new TypeReference<List<RelationToSsfbStaffResponse>>() {});
				 JSONArray j = new JSONArray(reference);
				 response.put("Data", j);
				 } catch (Exception e) {
			            e.printStackTrace();
			        } 
			}
		}else if(flowStatus.equals("DSABR")) {
			DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
			if(dsaOnboardDetails.getBusinessReference() != null) {
				String businessReference = dsaOnboardDetails.getBusinessReference();
				org.json.JSONArray businessReferenceInArray =new org.json.JSONArray(businessReference);
				 ObjectMapper objectMapper = new ObjectMapper();
				 try {
				 List<BusinessReference> reference = objectMapper.readValue(businessReference, new TypeReference<List<BusinessReference>>() {});
				 JSONArray j = new JSONArray(reference);
				 response.put("Data", j);
				 } catch (Exception e) {
			            e.printStackTrace();
			        } 
			}
		}else if(flowStatus.equals("DSAOD")) {
			EmpanellementCriteriaResponse dsaOnboardDetails=dsaOnBoardDetailsService.getEmpanellementCriteria(applicationNo);
			JSONObject j = new JSONObject(dsaOnboardDetails);
			response.put("Data", j);
		}else if(flowStatus.equals("DSAREFC")) {
			DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
			if(dsaOnboardDetails.getReferenceCheck() != null) {
				String referenceCheck = dsaOnboardDetails.getReferenceCheck();
				org.json.JSONArray businessReferenceInArray =new org.json.JSONArray(referenceCheck);
				 ObjectMapper objectMapper = new ObjectMapper();
				 try {
				 List<ReferenceCheckResponse> reference = objectMapper.readValue(referenceCheck, new TypeReference<List<ReferenceCheckResponse>>() {});
				 JSONArray j = new JSONArray(reference);
				 response.put("Data", reference);
				 } catch (Exception e) {
			            e.printStackTrace();
			        } 
			}
		}else if(flowStatus.equals("DSAREG")) {
			LoginResponse registrationDetails = dsaOnBoardDetailsService.getRegistrationDetails(applicationNo);
			JSONObject j = new JSONObject(registrationDetails);
			response.put("Data", j);
		}else if(flowStatus.equals("DSABD")) {
			BankDetailsResponse bankDetailsResponse = dsaOnBoardDetailsService.getBankDetails(applicationNo);
			JSONObject j = new JSONObject(bankDetailsResponse);
			response.put("Data", j);
		}else if(flowStatus.equals("DSACFB")) {
			DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
			if(dsaOnboardDetails.getCreditFeedback() != null) {
				String creditFeedback = dsaOnboardDetails.getCreditFeedback();
				org.json.JSONArray businessReferenceInArray =new org.json.JSONArray(creditFeedback);
				 ObjectMapper objectMapper = new ObjectMapper();
				 try {
				 List<CreditFeedbackResponse> reference = objectMapper.readValue(creditFeedback, new TypeReference<List<CreditFeedbackResponse>>() {});
				 JSONArray j = new JSONArray(reference);
				 response.put("Data", j);
				 } catch (Exception e) {
			            e.printStackTrace();
			        } 
			}
		}else if(flowStatus.equals("DSADEV")) {
			List<DeviationDetailsResponse> list=dsaOnboardMemberService.getDeviationDetails(applicationNo);
			 JSONArray j = new JSONArray(list);
			 response.put("Data", j);
		}else if(flowStatus.equals("DSAPYOD")) {
			DsaOnboardPayoutDetails dsaOnboardPayoutDetails=dsaPayoutDetailsService.getByApplicationNo(applicationNo);
			JSONArray j = new JSONArray(dsaOnboardPayoutDetails);
			 response.put("Data", j);
		}else if(flowStatus.equals("DSASMF")) {
			DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
			if(dsaOnboardDetails.getSalesManagerFeedback() != null) {
				String salesManagerFeedback = dsaOnboardDetails.getSalesManagerFeedback();
				
				 ObjectMapper objectMapper = new ObjectMapper();
				 try {
				 SalesManagerFeedbackResponse reference = objectMapper.readValue(salesManagerFeedback, new TypeReference<SalesManagerFeedbackResponse>() {});
				 JSONObject j = new JSONObject(reference);
				 response.put("Data", j);
				 response.put("productType", dsaOnboardDetails.getProductType());
				 } catch (Exception e) {
			            e.printStackTrace();
			        } 
			}
		}else if(flowStatus.equals("DSANSMF")) {
			DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
//			JSONObject j = new JSONObject(registrationDetails);
			response.put("nationalSalesManagerRemarks", dsaOnboardDetails.getNationalSalesManager());
		}
		else if(flowStatus.equals("DSALD")) {
			DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
			response.put("sendLeegalityVerify", dsaOnboardDetails.getSendLeegalityVerify());
			response.put("checkLeegalityVerify", dsaOnboardDetails.getCheckLeegalityVerify());
		}
		response.put("status", HttpStatus.OK.toString());
//		response.put("Data", dsaOnboardDetails);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/changeStatusEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> changeStatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String status = jsonObject.getJSONObject("Data").getString("status");
			DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
			dsaOnboardDetails.setStatus(status);
			dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
//			if(status.equals("CREDITOPS") && dsaOnboardDetails.getDsaCode()==null) {
			
			if(status.equals("CREDITOPS") && applicationNo != null) {

				JSONObject codeCreation=dsaCodeCreationService.codeCreation(dsaOnboardDetails);
				dsaOnboardDetails.setDsaCodeCreationResponse(codeCreation.toString());
				if (codeCreation != null) {
					String dsaCode=codeCreation.getString("dsaCode");
					dsaOnboardDetails.setDsaCode(dsaCode);
					JSONObject dmsUpload = dmsUploadService.dmsUpload(applicationNo,dsaCode);
					logger.debug("API Response"+dmsUpload.toString());
					if (dmsUpload != null) {
						JSONArray jsonArray = dmsUpload.getJSONArray("Response");

						for (int n = 0; n < jsonArray.length(); n++) {
							JSONObject jsonResponse = jsonArray.getJSONObject(n);

							if (jsonResponse.has("Errors")) {
								dsaOnboardDetails.setDmsUploadVerify("NO");
								break;
							} else {
								dsaOnboardDetails.setDmsUploadVerify("YES");
							}
						}	
					}
				}
				JSONObject addDsaId=dsaCodeCreationService.addDsaId(dsaOnboardDetails);
				dsaOnboardDetails.setFinacalDsaMasterResponse(addDsaId.toString());
			}
			String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveData);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/savePayoutDetailsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> savePayoutDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		
		DsaOnboardPayoutDetails dsaOnboardPayoutDetails=dsaPayoutDetailsService.getByApplicationNo(applicationNo);
		
		String agencyType = jsonObject.getJSONObject("Data").getString("agencyType");
		String productType = jsonObject.getJSONObject("Data").getString("productType");
		String payoutFrequency = jsonObject.getJSONObject("Data").getString("payoutFrequency");
		String year = jsonObject.getJSONObject("Data").getString("year");
		String schemeCode = jsonObject.getJSONObject("Data").getString("schemeCode");
		String assignedToBranchCode = jsonObject.getJSONObject("Data").getString("assignedToBranchCode");
		String remarks = jsonObject.getJSONObject("Data").getString("remarks");
		
		dsaOnboardPayoutDetails.setAgencyType(agencyType);
		dsaOnboardPayoutDetails.setProductType(productType);
		dsaOnboardPayoutDetails.setPayoutFrequency(payoutFrequency);
		dsaOnboardPayoutDetails.setYear(year);
		dsaOnboardPayoutDetails.setSchemeCode(schemeCode);
		dsaOnboardPayoutDetails.setAssignedToBranchCode(assignedToBranchCode);
		dsaOnboardPayoutDetails.setRemarks(remarks);
		dsaOnboardPayoutDetails.setApplicationNo(Long.parseLong(applicationNo));
		
		DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
		dsaOnboardDetails.setFlowStatus("DSAPYOD");
		dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
		dsaOnboardDetails.setPayoutDetailsVerify("YES");
		String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
		dsaUserLogService.saveUserLog(Long.parseLong(applicationNo), "BH", X_User_ID,remarks);
		String save = dsaPayoutDetailsService.saveData(dsaOnboardPayoutDetails);
		
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveData);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/sendLinkEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> sendLink(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject Header= new JSONObject();
			 Header.put("X-Correlation-ID",X_CORRELATION_ID );
			 Header.put("X-From-ID",X_From_ID );
			 Header.put("X-To-ID",X_To_ID );
			 Header.put("X-Transaction-ID",X_Transaction_ID );
			 Header.put("X-User-ID",X_User_ID );
			 Header.put("X-Request-ID",X_Request_ID );
		String linkSendBy = jsonObject.getJSONObject("Data").getString("linkSendBy");

		String applicationNo = dsaOnBoardDetailsService.createlead(jsonObject);
		jsonObject.put("applicationNo", applicationNo);
		if(linkSendBy.equalsIgnoreCase("SMS")) {
			JSONObject sendOtp=dsaOnBoardSendSmsService.sendLink(jsonObject,Header);
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("applicationNo", applicationNo);
		
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value="/fetchDocumentNameEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchDocumentName(@RequestBody String jsonRequest,
			 @RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		 
		 DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
		 
		 
		 org.json.simple.JSONArray   documentOptional= new org.json.simple.JSONArray();
		 documentOptional.add("connector Agreement");
		 documentOptional.add("vendor EnrolmentSheet");	
		 documentOptional.add("salesManager VisitReport");
		 documentOptional.add("bank Statement");
		 documentOptional.add("Gstin Letter");	
		 documentOptional.add("Udyam Aadhaar");
		 
		 org.json.simple.JSONArray   documentCompulsory= new org.json.simple.JSONArray();
		 documentCompulsory.add("customerPhoto");
		 if(dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL") &&  dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PROPRIETARY")) {
			 documentCompulsory.add("addressProof(Copy Of ElectricityBill)");
			 documentCompulsory.add("addressProof(property TaxBill)");
			 documentCompulsory.add("addressProof(rental Agreement)");
			 documentCompulsory.add("addressProof(udyog Aadhar)");
			 documentCompulsory.add("addressProof(shopAct)");
			 documentCompulsory.add("addressProof(index)");
			 documentCompulsory.add("proprietorship DeclarationForm");
		 }
		 else if(dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL") &&  dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PARTNERSHIP")) {
			 documentOptional.add("partnershipPan");
			 documentCompulsory.add("addressProof(Copy Of ElectricityBill)");
			 documentCompulsory.add("addressProof(property TaxBill)");
			 documentCompulsory.add("addressProof(rental Agreement)");
			 documentCompulsory.add("addressProof(udyog Aadhar)");
			 documentCompulsory.add("partnership Deed");
			 documentCompulsory.add("partners Authority Letter With Pan"); 
		 }	
		 else if(dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL") &&  dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PRIVATE LTD")) {
			 documentOptional.add("companyPan");
			 documentOptional.add("board Resolution");	
			 documentCompulsory.add("COI");
			 documentCompulsory.add("addressProof(Copy Of ElectricityBill)");
			 documentCompulsory.add("addressProof(property TaxBill)");
			 documentCompulsory.add("addressProof(rental Agreement)");
			 documentCompulsory.add("addressProof(udyog Aadhar)");
			 documentCompulsory.add("MOA AOA");
			 documentCompulsory.add("list Of Directors");
		 }	
		 
		 org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
		 response.put("documentOptional", documentOptional);
		 response.put("documentCompulsory", documentCompulsory);
		 
		logger.debug("final response"+response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value="/fetchDocumentEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchDocument(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID){
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		 String documentType = jsonObject.getJSONObject("Data").getString("documentType");
		 
		 List<Image> list=imageService.fetchByDocumentType(applicationNo,documentType);
		 	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
		 	JSONArray j = new JSONArray(list);
				response.put("List", j);
				logger.debug("final response"+list.size());
				String data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/documentVerifyEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> documentVerify(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
	 
		List<String> documentNotPresent=new ArrayList<>();
		List<String> list =imageService.getDocumentTypes(applicationNo);
		DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
		List<String> documentType =new ArrayList<>();
		 if(dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL") &&  dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PROPRIETARY")) {
			 documentType = new ArrayList<>(Arrays.asList("proprietorship DeclarationForm"));
		 }
		 else if(dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL") &&  dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PARTNERSHIP")) {
			 documentType = new ArrayList<>(Arrays.asList("partnership Deed","partners Authority Letter With Pan")); 
		 }
		 else if(dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL") &&  dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PRIVATE LTD")) {
			 documentType = new ArrayList<>(Arrays.asList("COI","MOA AOA","list Of Directors")); 
		 }
		 for(String documentInList:documentType) {
			 int count=1;
			 	for(String documentInDb:list) {
			 		if(documentInList.equalsIgnoreCase(documentInDb)) {
			 			count++;
			 			break;
			 		}
			 	}
			 	if(count==1) {
			 		documentNotPresent.add(documentInList);
			 	}
		 }
		 org.json.simple.JSONObject response = new org.json.simple.JSONObject();

				response.put("List", documentNotPresent);

				String data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/documentCheckListEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> documentCheckList(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
	 
		Set<DocCheckListResponse> documentResponse=new HashSet<>();
		 List<String> list =imageService.getDocumentTypes(applicationNo);
			DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
		 List<String> documentType =new ArrayList<>();
		 if(dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL") &&  dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PROPRIETARY")) {
			 documentType = new ArrayList<>(Arrays.asList("proprietorship DeclarationForm","customerPhoto"));
		 }
		 else if(dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL") &&  dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PARTNERSHIP")) {
			 documentType = new ArrayList<>(Arrays.asList("partnership Deed","partners Authority Letter With Pan","customerPhoto")); 
		 }
		 else if(dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL") &&  dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PRIVATE LTD")) {
			 documentType = new ArrayList<>(Arrays.asList("COI","MOA AOA","list Of Directors","customerPhoto")); 
		 }
		 else if(dsaOnboardDetails.getEntity().equalsIgnoreCase("INDIVIDUAL")) {
			 documentType = new ArrayList<>(Arrays.asList("customerPhoto")); 
		 }
		 for(String documentInList:documentType) {
			 int count=0;
			 	for(String documentInDb:list) {
			 		if(documentInList.equalsIgnoreCase(documentInDb)) {
			 			count++;
			 		}
			 	}
			 	if(count==0) {
			 		DocCheckListResponse checkListResponse=new DocCheckListResponse(documentInList,Integer.toString(count), "NO");
			 		documentResponse.add(checkListResponse);
			 	}else {
			 		DocCheckListResponse checkListResponse=new DocCheckListResponse(documentInList,Integer.toString(count), "YES");
			 		documentResponse.add(checkListResponse);
			 	}
			 	
		 }
		 List<String> filteredList=new ArrayList<>();
		 if(dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL") &&  dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PROPRIETARY")) {
			  filteredList = list.stream()
		                .filter(element -> !new ArrayList<>(Arrays.asList("proprietorship DeclarationForm")).contains(element))
		                .collect(Collectors.toList()); 
		 }else if(dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL") &&  dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PARTNERSHIP")) {
			 filteredList = list.stream()
		                .filter(element -> !new ArrayList<>(Arrays.asList("partnership Deed","partners Authority Letter With Pan")).contains(element))
		                .collect(Collectors.toList()); 
		 }else if(dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL") &&  dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PRIVATE LTD")) {
			 filteredList = list.stream()
		                .filter(element -> !new ArrayList<>(Arrays.asList("COI","MOA AOA","list Of Directors")).contains(element))
		                .collect(Collectors.toList()); 
		 }else {
			 list.stream()
             .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
             .forEach((element, count) ->documentResponse.add(new DocCheckListResponse(element,Long.toString(count), "YES")));
		 }
	    filteredList.stream()
	                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
	                .forEach((element, count) ->documentResponse.add(new DocCheckListResponse(element,Long.toString(count), "YES")));
	    JSONArray j = new JSONArray(documentResponse);
		 org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("List", j);
				String data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/saveNationalSalesManagerEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveISMFeedback(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String nationalSalesManagerRemarks = jsonObject.getJSONObject("Data").getString("nationalSalesManagerRemarks");
		
		DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
		dsaOnboardDetails.setNationalSalesManager(nationalSalesManagerRemarks);
		dsaOnboardDetails.setFlowStatus("DSANSMF");
		dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
		dsaUserLogService.saveUserLog(Long.parseLong(applicationNo), "NATIONALSALESMANAGER", X_User_ID,nationalSalesManagerRemarks);
		dsaOnboardDetails.setNsmRemarkVerify("YES");
		String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveData);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/customSearchEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> customSearch(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);

			String customSearch = jsonObject.getJSONObject("Data").getString("customSearch");
			String branchId = jsonObject.getJSONObject("Data").getString("branchId");

			if (customSearch == null || customSearch.isEmpty()) {
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("status", HttpStatus.BAD_REQUEST.toString());
				response.put("message", "enter a proper Dates");
				logger.debug("final response" + response.toString());
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
			List<DsaOnboardResponse> fetchByApplicationNo = dsaOnBoardDetailsService.customSearch(customSearch,branchId);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(fetchByApplicationNo);
			response.put("status", HttpStatus.OK.toString());
			response.put("Data", j);
			logger.debug(response.toString());
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
	
	@RequestMapping(value = "/ReSendLinkEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> reSendLink(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, 
			 HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
		
		JSONObject Header= new JSONObject();
		 Header.put("X-Correlation-ID",X_CORRELATION_ID );
		 Header.put("X-From-ID",X_From_ID );
		 Header.put("X-To-ID",X_To_ID );
		 Header.put("X-Transaction-ID",X_Transaction_ID );
		 Header.put("X-User-ID",X_User_ID );
		 Header.put("X-Request-ID",X_Request_ID );
		 
		String linkSendBy = jsonObject.getJSONObject("Data").getString("linkSendBy");
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		
		DsaOnboardDetails dsaOnboardDetails=dsaOnBoardDetailsService.getByApplicationno(applicationNo);
		
		JSONObject jsonObjectNew = new JSONObject(jsonRequest);
		JSONObject data1 = new JSONObject(jsonRequest);
		data1.put("CompanyName", dsaOnboardDetails.getCompanyName());
		
		jsonObjectNew.put("applicationNo", dsaOnboardDetails.getRegiCode());
		data1.put("mobileNo", dsaOnboardDetails.getMobileNo());
		
		jsonObjectNew.put("Data", data1);
		
		if(linkSendBy.equalsIgnoreCase("SMS")) {
			JSONObject sendOtp=dsaOnBoardSendSmsService.sendLink(jsonObjectNew,Header);
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("applicationNo", applicationNo);
		
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
