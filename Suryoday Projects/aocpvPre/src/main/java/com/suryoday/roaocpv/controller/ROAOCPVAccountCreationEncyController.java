package com.suryoday.roaocpv.controller;

import java.time.LocalDateTime;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.service.ROAOCPVAccountCreationService;

@RestController
@RequestMapping("/roaocpv")
public class ROAOCPVAccountCreationEncyController {

	@Autowired
	ROAOCPVAccountCreationService roaocpvAccountCreationService;

	@Autowired
	UserService userService;
	
	@Autowired
	CRMController CRMcontroller;
	
	@Autowired
	AocpvImageService aocpvImageService;
	
	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;
	
	@RequestMapping(value = "/newAccountCreationEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> accountCreation(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request,
			HttpServletResponse res)
			throws Exception {

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			// logger.debug("start request" + bm.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			String data = "";

			JSONObject Header = new JSONObject();
			Header.put("X-From-ID", X_From_ID);
			Header.put("X-To-ID", X_To_ID);
			Header.put("X-Transaction-ID", X_Transaction_ID);
			Header.put("X-User-ID", X_User_ID);
			Header.put("X-Request-ID", X_Request_ID);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			Long applicationNoInLong = Long.parseLong(applicationNo);
			AocpvLoanCreation findByApplicationNo = aocpvLoanCreationService.findByApplicationNo(applicationNoInLong);
			if(findByApplicationNo.getAccountNumber() != null) {
				org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
	 	 		response.put("Message", "SuccessFully AccountNumber Generated");
	 	 		response.put("AccountNumber", findByApplicationNo.getAccountNumber());
	 	 		data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3.toString(),HttpStatus.OK );	
	 	 		
			}
			JSONObject dedupeCall = roaocpvAccountCreationService.accountCreation(jsonObject, Header);
			String Data2 = dedupeCall.getString("data");
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("Data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;
				data = Data1.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3.toString(), h);
			}
			String account = Data1.getJSONObject("Data").getString("AccountID");
			
			ResponseEntity<Object> fetchByApplicationPDF = CRMcontroller.fetchByApplicationPDF(decryptContainerString,headerPersist,X_From_ID,X_To_ID,X_Transaction_ID,X_User_ID,X_Request_ID,request,res);
			JSONObject jsonObject1 = new JSONObject(fetchByApplicationPDF);
			String string = jsonObject1.getJSONObject("body").getString("Data1");

		//	byte[] image = string.getBytes();
			byte[] image = Base64.getDecoder().decode(string);
			String lat = jsonObject.getJSONObject("Data").getString("lat");
			String geoLong = jsonObject.getJSONObject("Data").getString("geoLong");
			
			JSONObject jsonObject2 = new JSONObject();
			JSONObject accountOpenpdf = new JSONObject();
			accountOpenpdf.put("Lat", lat);
			accountOpenpdf.put("image", "account_"+account+".pdf");
			accountOpenpdf.put("Long", geoLong);
			accountOpenpdf.put("Address", "");
			accountOpenpdf.put("timestamp", "");
			jsonObject2.put("accountOpenpdf", accountOpenpdf);

			String message =aocpvImageService.savePdf(image,jsonObject2,applicationNoInLong);
			findByApplicationNo.setAccountNumber(account);
			LocalDateTime now = LocalDateTime.now();
			findByApplicationNo.setAccountOpeningTimeStamp(now);
			aocpvLoanCreationService.saveData(findByApplicationNo);
			if (dedupeCall != null) {
				org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
	 	 		response.put("Message", "SuccessFully AccountNumber Generated");
	 	 		response.put("AccountNumber", account);
				data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3.toString(), h);

			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

}
