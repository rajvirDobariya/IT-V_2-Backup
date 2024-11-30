package com.suryoday.roaocpv.controller;

import java.time.LocalDateTime;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.roaocpv.service.ROAOCPVAccountCreationService;

@RestController
@RequestMapping("/roaocpv")
public class ROAOCPVAccountCreationController {

	private static Logger logger = LoggerFactory.getLogger(ROAOCPVAccountCreationController.class);
	
	@Autowired
	ROAOCPVAccountCreationService roaocpvAccountCreationService;

	@Autowired
	CRMController CRMcontroller;
	
	@Autowired
	AocpvImageService aocpvImageService;
	
	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;
	
	@RequestMapping(value = "/newAccountCreation", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> accountCreation(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req,
			HttpServletResponse res)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject = new JSONObject(bm);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		Long applicationNoInLong = Long.parseLong(applicationNo);
		AocpvLoanCreation findByApplicationNo = aocpvLoanCreationService.findByApplicationNo(applicationNoInLong);
		//logger.debug("start account opneing"+findByApplicationNo);
		if(findByApplicationNo.getAccountNumber() != null) {
			org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
 	 		response.put("Message", "Account Number Already Exists!");
 	 		response.put("AccountNumber", findByApplicationNo.getAccountNumber());
 	 		return new ResponseEntity<Object>(response,HttpStatus.OK);	
		}
		
		//logger.debug("start account opneing request");
		JSONObject dedupeCall = roaocpvAccountCreationService.accountCreation(jsonObject, Header);
		
		//logger.debug("End account opneing request"+dedupeCall.toString());
		String Data2 = dedupeCall.getString("data");
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		JSONObject Data1 = new JSONObject(Data2);
		if (Data1.has("Data")) {
			h = HttpStatus.OK;

		} else if (Data1.has("Error")) {
			h = HttpStatus.BAD_REQUEST;
			return new ResponseEntity<Object>(Data1.toString(), h);
		}
		String account = Data1.getJSONObject("Data").getString("AccountID");
		//logger.debug("start account opneing pdf request");
		ResponseEntity<Object> fetchByApplicationPDF = CRMcontroller.fetchByApplicationPDF(bm,headerPersist,X_From_ID,X_To_ID,X_Transaction_ID,X_User_ID,X_Request_ID,req,res);
		//logger.debug("stop account opneing pdf request");
		JSONObject jsonObject1 = new JSONObject(fetchByApplicationPDF);
		String string = jsonObject1.getJSONObject("body").getString("Data1");

		//String string = jsonObject1.getString("result");
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
	//	String message =aocpvImageService.savePdf(image,document,applicationNoInLong);
	//	logger.debug("start account opneing Save request");
		String message =aocpvImageService.savePdf(image,jsonObject2,applicationNoInLong);
	//	logger.debug("End account opneing Save request");
		findByApplicationNo.setAccountNumber(account);
		LocalDateTime now = LocalDateTime.now();
		findByApplicationNo.setAccountOpeningTimeStamp(now);
		aocpvLoanCreationService.saveData(findByApplicationNo);
		if (dedupeCall != null) {
			org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
 	 		response.put("Message", "Account Number Generated SuccessFully");
 	 		response.put("AccountNumber", account);
 	 		return new ResponseEntity<Object>(response,HttpStatus.OK);	

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

}
