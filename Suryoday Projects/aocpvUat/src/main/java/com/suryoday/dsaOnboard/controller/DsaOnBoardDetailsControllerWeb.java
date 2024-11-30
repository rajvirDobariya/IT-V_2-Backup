package com.suryoday.dsaOnboard.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.suryoday.dsaOnboard.pojo.DsaOnboardCPV;
import com.suryoday.dsaOnboard.pojo.DsaOnboardDetails;
import com.suryoday.dsaOnboard.pojo.DsaOnboardResponse;
import com.suryoday.dsaOnboard.service.DmsUploadService;
import com.suryoday.dsaOnboard.service.DsaCodeCreationService;
import com.suryoday.dsaOnboard.service.DsaImageService;
import com.suryoday.dsaOnboard.service.DsaOnBoardDetailsService;
import com.suryoday.dsaOnboard.service.DsaOnBoardPdfService;
import com.suryoday.dsaOnboard.service.DsaOnboardCPVService;
import com.suryoday.dsaOnboard.service.DsaOnboardMemberService;
import com.suryoday.dsaOnboard.service.DsaUserLogService;

@RestController
@RequestMapping("/dsaOnBoard/web")
public class DsaOnBoardDetailsControllerWeb {

	@Autowired
	DsaOnBoardDetailsService dsaOnBoardDetailsService;

	@Autowired
	DsaImageService imageService;

	@Autowired
	DsaOnboardMemberService dsaOnboardMemberService;

	@Autowired
	DsaUserLogService dsaUserLogService;

	@Autowired
	DsaOnboardCPVService dsaOnboardCPVService;

	@Autowired
	DsaCodeCreationService dsaCodeCreationService;

	@Autowired
	DmsUploadService dmsUploadService;

	@Autowired
	DsaOnBoardPdfService dsaOnBoardPdfService;

	Logger logger = LoggerFactory.getLogger(DsaOnBoardDetailsControllerWeb.class);

	@RequestMapping(value = "/fetchByDate", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByDate(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
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

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", fetchByApplicationNo);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/saveReferenceCheck", method = RequestMethod.POST)
	public ResponseEntity<Object> saveReferenceCheck(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
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
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/getByApplicationNo", method = RequestMethod.POST)
	public ResponseEntity<Object> getByApplicationNo(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
		String emailId = jsonObject.getJSONObject("Data").getString("emailId");
		List<DsaOnboardResponse> dsaOnboardDetails = dsaOnBoardDetailsService.getByApplication(applicationNo, mobileNo,
				emailId);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", dsaOnboardDetails);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/saveCreditFeedBack", method = RequestMethod.POST)
	public ResponseEntity<Object> saveCreditFeedBack(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String status = jsonObject.getJSONObject("Data").getString("status");
		JSONArray creditFeedback = jsonObject.getJSONObject("Data").getJSONArray("creditFeedback");
		String remarks = creditFeedback.getJSONObject(creditFeedback.length() - 1).getString("feedback");

		if (status.equals("DSANSM") || status.equals("REFERBACK") || status.equals("REJECTED")) {
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
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			throw new NoSuchElementException("Please Enter Proper Status");
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

	@RequestMapping(value = "/getReviewData", method = RequestMethod.POST)
	public ResponseEntity<Object> getReviewData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");

		org.json.simple.JSONObject fetchByApplicationNo = dsaOnBoardDetailsService.getReviewData(applicationNo, "web");

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", fetchByApplicationNo);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/saveCreditOpsFeedBack", method = RequestMethod.POST)
	public ResponseEntity<Object> saveCreditOpsFeedBack(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
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
					logger.debug("API Response" + dmsUpload.toString());
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
			return new ResponseEntity<Object>(response, HttpStatus.OK);

		} else {
			throw new NoSuchElementException("Please Enter Proper Status");
		}

	}

	@PostMapping("/getByApp/{applicationNo}")
	public ResponseEntity<Object> getByApp(@PathVariable("applicationNo") String applicationNo) {
		DsaOnboardDetails dsaOnboardDetails = dsaOnBoardDetailsService.getByregiCode(applicationNo);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("status", HttpStatus.OK.toString());
		response.put("Data", dsaOnboardDetails);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/getCPVList", method = RequestMethod.POST)
	public ResponseEntity<Object> getCPVList(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String city = jsonObject.getJSONObject("Data").getString("city");

		List<DsaOnboardCPV> list = dsaOnboardCPVService.getCPVList(city);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK);
		response.put("Data", list);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/sendToCPV", method = RequestMethod.POST)
	public ResponseEntity<Object> sendToCPV(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String id = jsonObject.getJSONObject("Data").getString("id");
		String emailId = jsonObject.getJSONObject("Data").getString("emailId");

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK);
		response.put("message", "send Successfully");
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/fetchState", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchState(
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		Set<String> list = dsaOnboardCPVService.fetchState();

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK);
		response.put("Data", list);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/fetchCity", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchCity(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String state = jsonObject.getJSONObject("Data").getString("state");
		Set<String> list = dsaOnboardCPVService.fetchCity(state);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		response.put("status", HttpStatus.OK);
		response.put("Data", list);
		logger.debug(response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/getDocumentTypes", method = RequestMethod.POST)
	public ResponseEntity<Object> getDocumentTypes(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		logger.debug("updateStatus Start");
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");

		Set<String> list = imageService.getDocumentTypesWeb(applicationNo);
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("Data", list);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/saveImage", method = RequestMethod.POST)
	public ResponseEntity<Object> saveImage(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
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
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@PostMapping("/dmsUpload")
	public ResponseEntity<?> dmsUpload(@RequestBody String jsonRequest) throws Exception {
		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String dsaCode = jsonObject.getJSONObject("Data").getString("dsaCode");
		JSONObject dmsUpload = dmsUploadService.dmsUpload(applicationNo, dsaCode);
		logger.debug("API Response" + dmsUpload.toString());
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		if (dmsUpload != null) {
			JSONArray jsonArray = dmsUpload.getJSONArray("Response");

			for (int n = 0; n < jsonArray.length(); n++) {
				JSONObject jsonResponse = jsonArray.getJSONObject(n);

				if (jsonResponse.has("Errors")) {
					response.put("dms upload", "NO");
					break;
				} else {
					response.put("dms upload", "YES");
				}
			}
		}

		response.put("status", HttpStatus.OK.toString());
		response.put("Data", "update done");
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}
