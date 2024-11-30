package com.suryoday.dsaOnboard.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.connector.rd.util.Crypt;
import com.suryoday.connector.service.UserService;
import com.suryoday.dsaOnboard.pojo.DsaOnboardCPV;
import com.suryoday.dsaOnboard.pojo.DsaOnboardDetails;
import com.suryoday.dsaOnboard.pojo.DsaOnboardMember;
import com.suryoday.dsaOnboard.pojo.DsaOnboardResponse;
import com.suryoday.dsaOnboard.service.DmsUploadService;
import com.suryoday.dsaOnboard.service.DsaCodeCreationService;
import com.suryoday.dsaOnboard.service.DsaImageService;
import com.suryoday.dsaOnboard.service.DsaOnBoardDetailsService;
import com.suryoday.dsaOnboard.service.DsaOnboardCPVService;
import com.suryoday.dsaOnboard.service.DsaOnboardMemberService;
import com.suryoday.dsaOnboard.service.DsaUserLogService;

@RestController
@RequestMapping("/dsaOnBoard/web")
public class DsaOnBoardDetailsControllerWebEncy {

	@Autowired
	DsaOnBoardDetailsService dsaOnBoardDetailsService;

	@Autowired
	DsaImageService imageService;

	@Autowired
	DsaOnboardMemberService dsaOnboardMemberService;

	@Autowired
	DsaUserLogService dsaUserLogService;

	@Autowired
	UserService userService;

	@Autowired
	DsaOnboardCPVService dsaOnboardCPVService;

	@Autowired
	DsaCodeCreationService dsaCodeCreationService;

	@Autowired
	DmsUploadService dmsUploadService;

	Logger logger = LoggerFactory.getLogger(DsaOnBoardDetailsControllerWebEncy.class);

	@Value("${REGKEY}")
	private String regKey;

	@RequestMapping(value = "/fetchByDateEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByDate(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
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
			List<DsaOnboardResponse> fetchByApplicationNo = null;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
			LocalDateTime enddate = LocalDateTime.parse(endDate, formatter);

			if (status.equals("") && branchId.equals("")) {
				fetchByApplicationNo = dsaOnBoardDetailsService.fetchByDate(startdate, enddate);
			} else if (status.equals("")) {
				fetchByApplicationNo = dsaOnBoardDetailsService.fetchByDateAndBranch(startdate, enddate, branchId);
			} else if (branchId.equals("")) {
				fetchByApplicationNo = dsaOnBoardDetailsService.fetchByDateAndStatus(startdate, enddate, status);
			} else {
				fetchByApplicationNo = dsaOnBoardDetailsService.fetchByDate(startdate, enddate, status, branchId);
			}

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(fetchByApplicationNo);
			response.put("status", HttpStatus.OK.toString());
			response.put("Data", j);
			logger.debug(response.toString());
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

	@RequestMapping(value = "/saveReferenceCheckEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveReferenceCheck(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String referenceCheck = jsonObject.getJSONObject("Data").getJSONArray("referenceCheck").toString();
			DsaOnboardDetails dsaOnboardDetails = dsaOnBoardDetailsService.getByApplicationno(applicationNo);
			dsaOnboardDetails.setReferenceCheck(referenceCheck);
			dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
			String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();

			response.put("status", HttpStatus.OK.toString());
			response.put("message", saveData);
			logger.debug(response.toString());
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

	@RequestMapping(value = "/getByApplicationNoEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getByApplicationNo(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
			String emailId = jsonObject.getJSONObject("Data").getString("emailId");
			List<DsaOnboardResponse> dsaOnboardDetails = dsaOnBoardDetailsService.getByApplication(applicationNo,
					mobileNo, emailId);

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(dsaOnboardDetails);
			response.put("status", HttpStatus.OK.toString());
			response.put("Data", j);
			logger.debug(response.toString());
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

	@RequestMapping(value = "/saveCreditFeedBackEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveCreditFeedBack(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String status = jsonObject.getJSONObject("Data").getString("status");
			if (status.equals("DSANSM") || status.equals("REFERBACK") || status.equals("REJECTED")) {
				JSONArray creditFeedback = jsonObject.getJSONObject("Data").getJSONArray("creditFeedback");
				String remarks = creditFeedback.getJSONObject(creditFeedback.length() - 1).getString("feedback");
				DsaOnboardDetails dsaOnboardDetails = dsaOnBoardDetailsService.getByApplicationno(applicationNo);
				dsaOnboardDetails.setStatus(status);
				dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
				dsaOnboardDetails.setCreditFeedback(creditFeedback.toString());

				for (int i = 0; i < creditFeedback.length(); i++) {
					String feedback = creditFeedback.getJSONObject(i).getString("feedback");
					if (feedback.equals("")) {
						throw new NoSuchElementException("Remark cannot be empty");
					}
					checkSpeciaChracters(feedback);
				}
				String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
				dsaUserLogService.saveUserLog(Long.parseLong(applicationNo), "CREDIT", X_User_ID, remarks);
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("status", HttpStatus.OK.toString());
				response.put("message", saveData);
				logger.debug(response.toString());
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
			} else {
				throw new NoSuchElementException("Please Enter Proper Status");
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

	private void checkSpeciaChracters(String input) {
		String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 .-:,";
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			// Check if the character is not in the allowed set
			if (allowedChars.indexOf(ch) == -1) {
				throw new NoSuchElementException("Special character not allowed " + ch);
			}
		}
	}

	@RequestMapping(value = "/getReviewDataEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getReviewData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");

			org.json.simple.JSONObject fetchByApplicationNo = dsaOnBoardDetailsService.getReviewData(applicationNo,
					"web");

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();

			response.put("status", HttpStatus.OK.toString());
			response.put("Data", fetchByApplicationNo);
			logger.debug(response.toString());
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

	@RequestMapping(value = "/saveCreditOpsFeedBackEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveCreditOpsFeedBack(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
//		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		boolean sessionId = true;
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String status = jsonObject.getJSONObject("Data").getString("status");
			if (status.equals("APPROVED") || status.equals("REFERBACK") || status.equals("REJECTED")) {
				JSONArray creditOpsFeedback = jsonObject.getJSONObject("Data").getJSONArray("creditOpsFeedback");
				String remarks = creditOpsFeedback.getJSONObject(creditOpsFeedback.length() - 1).getString("feedback");

				DsaOnboardDetails dsaOnboardDetails = dsaOnBoardDetailsService.getByApplicationno(applicationNo);
				dsaOnboardDetails.setStatus(status);
				dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
				dsaOnboardDetails.setCreditOpsFeedback(creditOpsFeedback.toString());
				for (int i = 0; i < creditOpsFeedback.length(); i++) {
					String feedback = creditOpsFeedback.getJSONObject(i).getString("feedback");
					if (feedback.equals("")) {
						throw new NoSuchElementException("Remark cannot be empty");
					}
					checkSpeciaChracters(feedback);
				}
				if (status.equals("APPROVED") && dsaOnboardDetails.getTypeOfRelationship().equalsIgnoreCase("DSA")) {
					JSONObject codeCreation = dsaCodeCreationService.codeCreation(dsaOnboardDetails);
					dsaOnboardDetails.setDsaCodeCreationResponse(codeCreation.toString());
					if (codeCreation != null) {
						String dsaCode = codeCreation.getString("dsaCode");
						dsaOnboardDetails.setDsaCode(dsaCode);
						JSONObject dmsUpload = dmsUploadService.dmsUpload(applicationNo, dsaCode);
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
					JSONObject addDsaId = dsaCodeCreationService.addDsaId(dsaOnboardDetails);
					dsaOnboardDetails.setFinacalDsaMasterResponse(addDsaId.toString());
				}
				String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
				dsaUserLogService.saveUserLog(Long.parseLong(applicationNo), status, X_User_ID, remarks);

				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("status", HttpStatus.OK.toString());
				response.put("message", saveData);
				logger.debug(response.toString());
				String data = response.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
			} else {
				throw new NoSuchElementException("Please Enter Proper Status");
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

	@RequestMapping(value = "/saveRegistrationDetailsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveRegistrationDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String key = regKey;
		String decryptContainerString = Crypt.decrypt(encryptString, key);
		JSONObject jsonObject = new JSONObject(decryptContainerString);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");

		DsaOnboardDetails dsaOnboardDetails = dsaOnBoardDetailsService.getByApplicationNo(applicationNo);

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
		if (entity.equalsIgnoreCase("Individual") && dsaOnboardDetails.getEntity() == null) {
			DsaOnboardMember dsaOnboardMember = new DsaOnboardMember();
			dsaOnboardMember.setMobile(mobileNo);
			dsaOnboardMember.setMobileNoVerify(true);
			dsaOnboardMember.setApplicationNo(Long.parseLong(applicationNo));
			dsaOnboardMember.setMember(RandomStringUtils.randomAlphanumeric(10));
			String saveData = dsaOnboardMemberService.saveData(dsaOnboardMember);
		}
		dsaOnboardDetails.setEntity(entity);
		dsaOnboardDetails.setTypeOfRelationship(typeOfRelationship);
		dsaOnboardDetails.setProductType(productType);
		dsaOnboardDetails.setNoOfPartner(noOfPartner);
		if (dsaOnboardDetails.getLeadId() == null) {
			dsaOnboardDetails.setLeadId(leadId);
			dsaOnboardDetails.setBranchId(branchId);
			dsaOnboardDetails.setLeadName(leadName);
		}
		dsaOnboardDetails.setConstitutionType(constitutionType);
		dsaOnboardDetails.setUpdatedDate(LocalDateTime.now());
		dsaOnboardDetails.setStatus("Progress");
		dsaOnboardDetails.setFlowStatus("DSAREG");

		String saveData = dsaOnBoardDetailsService.saveData(dsaOnboardDetails);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("message", saveData);
		String data = response.toString();
		String encryptString2 = Crypt.encrypt(data, key);
		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
		data2.put("value", encryptString2);
		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
		data3.put("Data", data2);
		return new ResponseEntity<Object>(data3, HttpStatus.OK);

	}

	@PostMapping("/getByAppEncy/{applicationNo}")
	public ResponseEntity<Object> getByApp(@PathVariable("applicationNo") String applicationNo) throws Exception {
		DsaOnboardDetails dsaOnboardDetails = dsaOnBoardDetailsService.getByregiCode(applicationNo);

		String key = regKey;
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("status", HttpStatus.OK.toString());
		JSONObject j = new JSONObject(dsaOnboardDetails);
		response.put("Data", j);
		logger.debug(response.toString());
		String data = response.toString();
		String encryptString2 = Crypt.encrypt(data, key);
		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
		data2.put("value", encryptString2);
		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
		data3.put("Data", data2);
		return new ResponseEntity<Object>(data3, HttpStatus.OK);

	}

	@RequestMapping(value = "/getDocumentTypesEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getDocumentTypes(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		logger.debug("updateStatus Start");
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");

			Set<String> list = imageService.getDocumentTypesWeb(applicationNo);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(list);
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

	@RequestMapping(value = "/fetchDocumentNameEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchDocumentName(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");

			DsaOnboardDetails dsaOnboardDetails = dsaOnBoardDetailsService.getByApplicationno(applicationNo);

			org.json.simple.JSONArray documentOptional = new org.json.simple.JSONArray();
			documentOptional.add("connectorAgreement");
			documentOptional.add("vendorEnrolmentSheet");
			documentOptional.add("salesManagerVisitReport");
			documentOptional.add("bankStatement");
			documentOptional.add("GstinLetter");
			documentOptional.add("Others");
			org.json.simple.JSONArray documentCompulsory = new org.json.simple.JSONArray();

			if (dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL")
					&& dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PROPRIETARY")) {
				documentCompulsory.add("addressProof(CopyOfElectricityBill)");
				documentCompulsory.add("addressProof(propertyTaxBill)");
				documentCompulsory.add("addressProof(rentalAgreement)");
				documentCompulsory.add("addressProof(udyogAadhar)");
				documentCompulsory.add("addressProof(shopAct)");
				documentCompulsory.add("addressProof(index)");
				documentCompulsory.add("proprietorshipDeclarationForm");
			} else if (dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL")
					&& dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PARTNERSHIP")) {
				documentOptional.add("partnershipPan");
				documentCompulsory.add("addressProof(CopyOfElectricityBill)");
				documentCompulsory.add("addressProof(propertyTaxBill)");
				documentCompulsory.add("addressProof(rentalAgreement)");
				documentCompulsory.add("addressProof(udyogAadhar)");
				documentCompulsory.add("partnershipDeed");
				documentCompulsory.add("partnersAuthorityLetterWithPan");
			} else if (dsaOnboardDetails.getEntity().equalsIgnoreCase("NON INDIVIDUAL")
					&& dsaOnboardDetails.getConstitutionType().equalsIgnoreCase("PRIVATE LTD")) {
				documentOptional.add("companyPan");
				documentOptional.add("boardResolution");
				documentCompulsory.add("COI");
				documentCompulsory.add("addressProof(CopyOfElectricityBill)");
				documentCompulsory.add("addressProof(propertyTaxBill)");
				documentCompulsory.add("addressProof(rentalAgreement)");
				documentCompulsory.add("addressProof(udyogAadhar)");
				documentCompulsory.add("MOA AOA");
				documentCompulsory.add("listOfDirectors");
			}

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("documentOptional", documentOptional);
			response.put("documentCompulsory", documentCompulsory);

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

	@RequestMapping(value = "/fetchDocumentEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchDocument(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String documentType = jsonObject.getJSONObject("Data").getString("documentType");

			List<Image> list = imageService.fetchByDocumentType(applicationNo, documentType);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(list);
			response.put("Data", j);
			logger.debug("final response" + list.size());
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

	@RequestMapping(value = "/getCPVListEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> getCPVList(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String city = jsonObject.getJSONObject("Data").getString("city");

			List<DsaOnboardCPV> list = dsaOnboardCPVService.getCPVList(city);

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();

			response.put("Data", list);
			JSONArray j = new JSONArray(list);
			response.put("Data", j);
			logger.debug("final response" + list.size());
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

	@RequestMapping(value = "/saveImageEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveImage(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String member = "SELF";
			if (jsonObject.getJSONObject("Data").has("member")) {
				member = jsonObject.getJSONObject("Data").getString("member");
			}
			String base64Image = jsonObject.getJSONObject("Data").getString("base64Image");
			byte[] image = Base64.getDecoder().decode(base64Image);

			if (!document.isEmpty()) {
				imageService.saveImageWeb(image, document, applicationNo, member);
			}
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();

			response.put("status", HttpStatus.OK.toString());
			response.put("Data", "Image Saved");
			logger.debug(response.toString());
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

	@RequestMapping(value = "/fetchStateEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchState(
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			Set<String> list = dsaOnboardCPVService.fetchState();

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(list);

			response.put("Data", j);
			logger.debug(response.toString());
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_Session_ID);
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

	@RequestMapping(value = "/fetchCityEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchCity(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_Session_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);

			String state = jsonObject.getJSONObject("Data").getString("state");
			Set<String> list = dsaOnboardCPVService.fetchCity(state);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(list);
			response.put("Data", j);
			logger.debug(response.toString());
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_Session_ID);
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

	@RequestMapping(value = "/sendToCPVEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> sendToCPV(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_Session_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);

			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String id = jsonObject.getJSONObject("Data").getString("id");
			String emailId = jsonObject.getJSONObject("Data").getString("emailId");

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();

			response.put("message", "send Successfully");

			logger.debug(response.toString());
			String data = response.toString();
			String encryptString2 = Crypt.encrypt(data, X_Session_ID);
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
