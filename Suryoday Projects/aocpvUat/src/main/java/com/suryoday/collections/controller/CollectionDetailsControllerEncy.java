package com.suryoday.collections.controller;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.pojo.GeoLcation;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.collections.others.PDFReceipt;
import com.suryoday.collections.pojo.CSVModel;
import com.suryoday.collections.pojo.CSVModelResponse;
import com.suryoday.collections.pojo.ReceiptImage;
import com.suryoday.collections.pojo.ReceiptImageResponse;
import com.suryoday.collections.pojo.ReceiptPojo;
import com.suryoday.collections.pojo.ReceiptResponse;
import com.suryoday.collections.pojo.WebReceiptResponse;
import com.suryoday.collections.service.CSVService;
import com.suryoday.collections.service.ReceiptImageService;
import com.suryoday.collections.service.ReceiptService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.mhl.controller.ExcelController;

@Controller
@RequestMapping("/collections")
public class CollectionDetailsControllerEncy {
	@Autowired
	CSVService csvService;
	@Autowired
	UserService userService;

	@Autowired
	AocpCustomerDataService aocpCustomerDataService;

	@Autowired
	AocpvImageService aocpvImageService;

	@Autowired
	ReceiptImageService imageService;

	@Autowired
	ReceiptService receiptService;

	Logger logger = LoggerFactory.getLogger(CollectionDetailsControllerEncy.class);

	@RequestMapping(value = "/fetchAllRecordsEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getCIFData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE, HttpServletRequest req)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		// System.out.println(jsonRequest);

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			System.out.println(encryptString);
			// logger.debug("start request" + jsonRequest.toString());
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject Header = new JSONObject();
			Header.put("X-Request-ID", X_REQUEST_ID);
			Header.put("X-Transaction-ID", X_TRANSACTION_ID);
			Header.put("X-To-ID", X_TO_ID);
			Header.put("X-From-ID", X_FORM_ID);
			Header.put("Content-TypeD", CONTEND_TYPE);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("Fetch All Details");
			logger.debug("Request" + decryptContainerString);

			if (decryptContainerString.isEmpty()) {
				logger.debug("Request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String status = jsonObject.getJSONObject("Data").getString("Status");
			String branchID = jsonObject.getJSONObject("Data").getString("BRANCH_CODE");
			logger.debug("db Call start");
			List<CSVModelResponse> csvModel = csvService.findByStatusAll(status, branchID);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONArray j = new JSONArray(csvModel);
			int count = csvModel.size();
			// org.json.simple.JSONObject response= new org.json.simple.JSONObject();
			// response.put("status", HttpStatus.OK);
			response.put("Data", j);
			logger.debug("Final Response" + count);
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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

	@RequestMapping(value = "/fetchAllLoanEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getLoanData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE, HttpServletRequest req)
			throws Exception {
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
			JSONObject Header = new JSONObject();
			Header.put("X-Request-ID", X_REQUEST_ID);
			Header.put("X-Transaction-ID", X_TRANSACTION_ID);
			Header.put("X-To-ID", X_TO_ID);
			Header.put("X-From-ID", X_FORM_ID);
			Header.put("Content-TypeD", CONTEND_TYPE);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			logger.debug("Fetch All Loan Details");
			logger.debug("Request" + decryptContainerString);

			if (decryptContainerString.isEmpty()) {
				logger.debug("Request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String customerNO = jsonObject.getJSONObject("Data").getString("CUSTOMERID");
			Long customerNOLong = Long.parseLong(customerNO);
			logger.debug("db Call start");
			List<CSVModel> csvModel = csvService.findByAllLoan(customerNO);
			Long application = aocpCustomerDataService.getByCustomerIDApplication(customerNOLong);
			// System.out.println(application);
			String address = aocpvImageService.getGeoLoactionByApplication(application);
			JSONObject jsonObjectImage = new JSONObject(address);

			String pimage = jsonObjectImage.getString("image");
			String pLat = jsonObjectImage.getString("Lat");
			String pLong = jsonObjectImage.getString("Long");
			String pAddress = jsonObjectImage.getString("Address");
			String ptimestamp = jsonObjectImage.getString("timestamp");
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			GeoLcation geolocation = new GeoLcation(pimage, pLat, pLong, pAddress, ptimestamp);

			// response.put("status", HttpStatus.OK);
			JSONArray j = new JSONArray(csvModel);
			JSONObject k = new JSONObject(geolocation);
			response.put("Data", j);
			// response.put("Customer Number", customerNO);
			response.put("GeoLocation", k);
			System.out.println("Final Response" + response.toString());
			// logger.debug("Final Response"+response.toString());
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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

	@RequestMapping(value = "/searchByNumberEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> fetchByAccount(@RequestBody String jsonRequest,
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

			String accountNo = jsonObject.getJSONObject("Data").getString("aggrementID");

			List<CSVModel> csvModel = csvService.findByAccountNo(accountNo);
			JSONArray j = new JSONArray(csvModel);
			int count = csvModel.size();
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", j);
			System.out.println("Final Response" + count);

			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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

	@RequestMapping(value = "/fetchAllReceiptTopEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getTopReceipt(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE, HttpServletRequest req)
			throws Exception {
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
			logger.debug("Fetch All Details");
			logger.debug("Request" + decryptContainerString);

			if (decryptContainerString.isEmpty()) {
				logger.debug("Request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String branchId = jsonObject.getJSONObject("Data").getString("branchId");

			logger.debug("db Call start");
			List<ReceiptResponse> receipts = receiptService.findTopReceiptList(branchId);
			JSONArray j = new JSONArray(receipts);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			// response.put("status", HttpStatus.OK);
			response.put("Data", j);
			logger.debug("Final Response" + response.toString());
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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

	@RequestMapping(value = "/getReceiptDetailsEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getReceiptDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE, HttpServletRequest req)
			throws Exception {

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
			logger.debug("Fetch All Details");
			logger.debug("Request" + decryptContainerString);

			if (decryptContainerString.isEmpty()) {
				logger.debug("Request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String branchId = jsonObject.getJSONObject("Data").getString("branchId");

			logger.debug("db Call start");

			List<ReceiptPojo> receipt = receiptService.findAllDetails(branchId);
			String receiptNo = receiptService.getReceiptNumber(branchId);
			List<ReceiptImage> imagereceipt = imageService.findAllDetails2(receiptNo);

			List<ReceiptImageResponse> list = new ArrayList<>();
			for (ReceiptImage image : imagereceipt) {
				ReceiptImageResponse imageResponse = new ReceiptImageResponse();
				imageResponse.setAddress(image.getAddress());
				imageResponse.setCustomerID(image.getCustomerID());
				imageResponse.setCustomerImage(image.getCustomerID());
				imageResponse.setLatitude(image.getLatitude());
				imageResponse.setLongitude(image.getLongitude());
				imageResponse.setReceiptNo(image.getReceiptNo());
				imageResponse.setTimestamp(image.getTimestamp());
				byte[] images2 = image.getImages();
				String encoded = Base64.getEncoder().encodeToString(images2);
				imageResponse.setImages(encoded);
				list.add(imageResponse);
			}
			WebReceiptResponse webReceipt = new WebReceiptResponse();
			webReceipt.setReceiptPojo(receipt);
			webReceipt.setReceiptImage(list);

			// JSONArray = new JSONArray(webReceipt);
			JSONObject p = new JSONObject(webReceipt);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			// response.put("status", HttpStatus.OK);

			response.put("Data", p);

			logger.debug("Final Response" + response.toString());
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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

	@RequestMapping(value = "/getReceiptByDateEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getReceiptByDate(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE, HttpServletRequest req)
			throws Exception {

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
			logger.debug("Fetch All Details");
			logger.debug("Request" + decryptContainerString);

			if (decryptContainerString.isEmpty()) {
				logger.debug("Request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
			String startDate = jsonObject.getJSONObject("Data").getString("StartDate");
			String endDate = jsonObject.getJSONObject("Data").getString("EndDate");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate startdate = LocalDate.parse(startDate, formatter);
			DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate enddate = LocalDate.parse(endDate, formatter2);

			List<ReceiptResponse> receipt = receiptService.findAllDetailsByDate(branchId, startdate, enddate);
			JSONArray p = new JSONArray(receipt);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", p);
			logger.debug("Final Response" + response.toString());
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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

	@RequestMapping(value = "/getCustomerByDateEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getCustomerByDate(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE, HttpServletRequest req)
			throws Exception {

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
			logger.debug("Fetch All Details");
			logger.debug("Request" + decryptContainerString);

			if (decryptContainerString.isEmpty()) {
				logger.debug("Request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}

			String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
			String startDate = jsonObject.getJSONObject("Data").getString("StartDate");
			String endDate = jsonObject.getJSONObject("Data").getString("EndDate");
			String status = jsonObject.getJSONObject("Data").getString("Status");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate startdate = LocalDate.parse(startDate, formatter);
			DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate enddate = LocalDate.parse(endDate, formatter2);

			List<CSVModelResponse> csv = csvService.findAllCustomerByDate(branchId, startdate, enddate, status);
			JSONArray s = new JSONArray(csv);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", s);
			// logger.debug("Final Response"+response.toString());
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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

	@RequestMapping(value = "/getReceiptPDFEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getReceiptPDF(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		res.setContentType("application/pdf");
		res.setHeader("Content-Disposition", "attachment; filename=Receipt.pdf");

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
			logger.debug("Fetch All Details");
			logger.debug("Request" + decryptContainerString);

			if (decryptContainerString.isEmpty()) {
				logger.debug("Request is empty" + decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
			String receiptNo = jsonObject.getJSONObject("Data").getString("ReceiptNO");
			List<ReceiptPojo> receiptPDF = receiptService.getReceiptPDFDetails(receiptNo);

			String sConfigFile = "props/Letters/receipt.properties";
			InputStream in = ExcelController.class.getClassLoader().getResourceAsStream(sConfigFile);
			if (in == null) {
				System.out.println("Blank File");
			}
			Properties prop = new Properties();
			prop.load(in);

			String title = prop.getProperty("header");
			String lefttable1 = prop.getProperty("left1");
			String lefttable2 = prop.getProperty("left2");
			String lefttable3 = prop.getProperty("left3");
			String lefttable4 = prop.getProperty("left4");
			String lefttable5 = prop.getProperty("left5");
			String lefttable6 = prop.getProperty("left6");
			String lefttable7 = prop.getProperty("left7");
			String lefttable8 = prop.getProperty("left8");
			String lefttable9 = prop.getProperty("left9");
			String lefttable10 = prop.getProperty("left10");

			List<String> list = new ArrayList<String>();
			list.add(title);
			list.add(lefttable1);
			list.add(lefttable2);
			list.add(lefttable3);
			list.add(lefttable4);
			list.add(lefttable5);
			list.add(lefttable6);
			list.add(lefttable7);
			list.add(lefttable8);
			list.add(lefttable9);
			list.add(lefttable10);

			PDFReceipt receipt = new PDFReceipt(list, receiptPDF);
			byte[] pdfr = receipt.exportReceipt(res);
			System.out.println(pdfr.length);
			String base64 = Base64.getEncoder().encodeToString(pdfr);
			String result = new String(base64);
			System.out.println("Conversion " + result);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", result);
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
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
