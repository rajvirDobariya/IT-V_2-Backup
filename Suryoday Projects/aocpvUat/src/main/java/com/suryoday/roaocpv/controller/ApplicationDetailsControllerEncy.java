package com.suryoday.roaocpv.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.pojo.ApplicationDetailList;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.pojo.ApplicationDetailsResponse;
import com.suryoday.roaocpv.service.ApplicationDetailsService;

@RestController
@RequestMapping("/roaocpv")
public class ApplicationDetailsControllerEncy {

	@Autowired
	ApplicationDetailsService applicationDetailsService;

	@Autowired
	AocpCustomerDataService aocpCustomerDataService;

	@Autowired
	UserService userService;

	@Autowired
	AocpvImageService aocpvImageService;

	private static Logger logger = LoggerFactory.getLogger(ApplicationDetailsControllerEncy.class);

	@RequestMapping(value = "/fetchByStatusAndBranchIdEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByStatusAndBranchId(@RequestBody String jsonRequest,
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
			System.out.println(encryptString);
			// logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("request" + decryptContainerString);
			if (jsonRequest.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String status = jsonObject.getJSONObject("Data").getString("status");
			String branchId = jsonObject.getJSONObject("Data").getString("branchId");

			List<ApplicationDetailList> list = applicationDetailsService.fetchByStatusAndBranchId(status, branchId);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(list);
			response.put("Data", j);
			logger.debug("final response" + response.size());
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

	@RequestMapping(value = "/passorfailEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> passorfail(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
			// logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("request" + decryptContainerString);
			if (decryptContainerString.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");
			String flowStatus = jsonObject.getJSONObject("Data").getString("flowStatus");
			String status = jsonObject.getJSONObject("Data").getString("status");
			String appNoWithProductCode = applicationDetailsService.passorfail(applicationId, flowStatus, status);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", "Done");
			response.put("appNoWithProductCode", appNoWithProductCode);
			logger.debug("final response" + response.toString());
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

	@RequestMapping(value = "/fetchByApplicationIdEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByApplicationId(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
			// logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("request" + decryptContainerString);
			if (decryptContainerString.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");

			ApplicationDetailsResponse applicationDetails = applicationDetailsService
					.fetchAllByApplicationId(applicationId);

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONObject j = new JSONObject(applicationDetails);
			response.put("Data", j);
			logger.debug("final response" + response);
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
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
			// logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("request" + decryptContainerString);
			if (jsonRequest.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String status = jsonObject.getJSONObject("Data").getString("status");
			String branchId = jsonObject.getJSONObject("Data").getString("branchId");
			String startDate = jsonObject.getJSONObject("Data").getString("startDate");
			String endDate = jsonObject.getJSONObject("Data").getString("endDate");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
			LocalDate startdate = LocalDate.parse(startDate, formatter);
			LocalDate enddate = LocalDate.parse(endDate, formatter);

			List<ApplicationDetails> list = applicationDetailsService.fetchByDate(status, branchId, startdate, enddate);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(list);
			response.put("Data", j);
			logger.debug("final response" + response.size());
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

	@RequestMapping(value = "/addpersonalDetailsEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> personalDetails(@RequestParam("file") MultipartFile[] files,
			@RequestParam("Data") String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("request" + decryptContainerString);
			if (jsonRequest.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");
			String personalDetails = jsonObject.getJSONObject("Data").getJSONObject("personalDetails").toString();
			long applicationNoInLong = Long.parseLong(applicationId);
			org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
			String flowStatus = "PD";
			String message = applicationDetailsService.addpersonalDetails(applicationId, personalDetails);
			aocpvImageService.saveCustomerPhoto(files, applicationNoInLong, document, flowStatus);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", message);
			logger.debug("final response" + response.toString());
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

	@RequestMapping(value = "/createLeadEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> savePersonalDetailsData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request) {

		logger.debug("save data start");
		logger.debug("request" + jsonRequest);
		// logger.debug("POST Request : " + bm);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			String data = "";

			if (decryptContainerString.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
			String applicationNo = applicationDetailsService.createLead(decryptContainerString, X_User_ID);

			if (applicationNo.isEmpty()) {
				return new ResponseEntity<>("Kindly Enter Valid Details", HttpStatus.BAD_REQUEST);
			}
			JSONObject reponse = new JSONObject();
			reponse.put("value", "Lead was created Sucessfully");
			reponse.put("applicationNo", applicationNo);
			org.json.JSONObject mainReposne = new org.json.JSONObject();
			mainReposne.put("Data", reponse);
			data = mainReposne.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", encryptString2);
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Data", data2);
			logger.debug("response : " + data3.toString());
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);

		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/saveMultipleImagesEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveMultipleImages(@RequestParam("file") MultipartFile files[],
			@RequestParam("Data") String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("request" + decryptContainerString);
			System.out.println("JSON STRING --->" + jsonObject);

			if (decryptContainerString.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
			org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String member = "SELF";
			if (jsonObject.getJSONObject("Data").has("member")) {
				member = jsonObject.getJSONObject("Data").getString("member");
			}
			long applicationno = Long.parseLong(applicationNo);
			aocpCustomerDataService.flowStatusChange(applicationno);
			logger.debug("image save to db start");
			String saveImage = aocpvImageService.saveMultipleImages(files, document, applicationno, member);

			logger.debug("image save to db sucessfully");
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", saveImage);
			response.put("status", HttpStatus.OK.toString());
			// logger.debug("final response"+response.toString());
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

	@RequestMapping(value = "/saveCIfEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveCIf(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("request" + decryptContainerString);
			if (decryptContainerString.isEmpty()) {
				logger.debug("request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");
			JSONObject jsonObject2 = jsonObject.getJSONObject("Data");
			String message = applicationDetailsService.saveCIf(applicationId, jsonObject2);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", message);
			logger.debug("final response" + response.toString());
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

	@RequestMapping(value = "/saveEmiResponseEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveEmiResponse(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("request" + jsonRequest);
			if (jsonRequest.isEmpty()) {
				logger.debug("request is empty" + jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}

			String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");
			String requestedLoanResponse = jsonObject.getJSONObject("Data").getJSONObject("requestedLoanResponse")
					.toString();

			ApplicationDetails applicationDetails = applicationDetailsService.fetchByApplicationId(applicationId);
			applicationDetails.setResponseEmi(requestedLoanResponse);
			applicationDetailsService.save(applicationDetails);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", "update successfully");
			logger.debug("final response" + response.toString());
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

	@RequestMapping(value = "/bestEmiEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> bestEmi(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("request" + jsonRequest);
			if (jsonRequest.isEmpty()) {
				logger.debug("request is empty" + jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}

			String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");

			ApplicationDetails applicationDetails = applicationDetailsService.fetchByApplicationId(applicationId);
			org.json.simple.JSONObject bestOffer = new org.json.simple.JSONObject();
			bestOffer.put("maxEmiEligibility", applicationDetails.getMaxEmiEligibility());
			bestOffer.put("bestEligibleEmi", applicationDetails.getBestEligibleEmi());
			bestOffer.put("requiredAmount", applicationDetails.getRequiredAmount());
			bestOffer.put("applicationId", applicationId);

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", bestOffer);
			logger.debug("final response" + response.toString());
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

	@RequestMapping(value = "/saveBestOfferEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveBestOffer(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		System.out.println(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("request" + jsonRequest);
			if (jsonRequest.isEmpty()) {
				logger.debug("request is empty" + jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}

			String applicationId = jsonObject.getJSONObject("Data").getString("applicationId");
			String loanAmount = jsonObject.getJSONObject("Data").getString("loanAmount");
			String tenure = jsonObject.getJSONObject("Data").getString("tenure");
			String emi = jsonObject.getJSONObject("Data").getString("emi");
			String rateOfInterest = jsonObject.getJSONObject("Data").getString("rateOfInterest");

			ApplicationDetails applicationDetails = applicationDetailsService.fetchByApplicationId(applicationId);
			applicationDetails.setEmi(emi);
			applicationDetails.setTenure(tenure);
			applicationDetails.setRateOfInterest(rateOfInterest);
			applicationDetails.setRequiredAmount(loanAmount);

			applicationDetailsService.save(applicationDetails);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("message", "update successfully");
			logger.debug("final response" + response.toString());
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
