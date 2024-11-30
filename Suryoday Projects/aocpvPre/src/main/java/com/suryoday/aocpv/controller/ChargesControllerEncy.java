package com.suryoday.aocpv.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.pojo.Charges;
import com.suryoday.aocpv.service.ChargesService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.service.ApplicationDetailsService;

@RestController
@RequestMapping(value="aocpv")
public class ChargesControllerEncy {
	Logger logger = LoggerFactory.getLogger(ChargesControllerEncy.class);
	
	@Autowired 
	UserService userService;
	
	@Autowired
	ChargesService chargesservice;
	
	@Autowired
	ApplicationDetailsService applicationDetailsService;
	
	@RequestMapping(value="/fetchByProductCodeEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> fetchByProductCodeEncy(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("fetchByProductCodeEncy start");
		System.out.println("request"+jsonRequest);
		
	if(jsonRequest.isEmpty()) {
		System.out.println("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		  if (sessionId == true ) {
			  userService.getSessionId(X_User_ID, request); 
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				//System.out.println(encryptString);
		//logger.debug("start request" + jsonRequest.toString());

		String key = X_Session_ID;

		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
	//	logger.debug("PD request"+decryptContainerString);
	 JSONObject jsonObject=new JSONObject(decryptContainerString);
		
		System.out.println("db Call start");
		String loanAmount = jsonObject.getJSONObject("Data").getString("LoanAmount");
		String tenure = jsonObject.getJSONObject("Data").getString("Tenure");
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String flowStatus = jsonObject.getJSONObject("Data").getString("flowStatus");
		System.out.println("db Call start");
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		fetchByApplicationId.setFlowStatus(flowStatus);
		fetchByApplicationId.setTenure(tenure);
		fetchByApplicationId.setAmount(loanAmount);
		applicationDetailsService.save(fetchByApplicationId);
		List<Charges> list=chargesservice.fetchByProductCode(loanAmount,tenure);
		Charges charges = list.get(0); 
		
		System.out.println("db Call end"+list);
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data", charges);
		System.out.println("final response"+response.toString());
		return new ResponseEntity<Object>(response,HttpStatus.OK);

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
	
	@RequestMapping(value="/saveProductCodeEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> saveProductCodeEncy(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("saveProductCodeEncy start");
		System.out.println("request"+jsonRequest);
		
	if(jsonRequest.isEmpty()) {
		System.out.println("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		  if (sessionId == true ) {
			  userService.getSessionId(X_User_ID, request); 
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				//System.out.println(encryptString);
		//logger.debug("start request" + jsonRequest.toString());

		String key = X_Session_ID;

		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
	//	logger.debug("PD request"+decryptContainerString);
	 JSONObject jsonObject=new JSONObject(decryptContainerString);
		
		System.out.println("db Call start");
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String productCode = jsonObject.getJSONObject("Data").getString("ProductCode");
		String netDisbustment = jsonObject.getJSONObject("Data").getString("netDisbustment");
		String fireInsurance = jsonObject.getJSONObject("Data").getString("fireInsurance");
		System.out.println("db Call start");
		ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(applicationNo);
		fetchByApplicationId.setProductCode(productCode);
		fetchByApplicationId.setFlowStatus("CHARGES");
		fetchByApplicationId.setNetDisbustment(netDisbustment);
		fetchByApplicationId.setFireInsurance(fireInsurance);
		fetchByApplicationId.setTermInsurance("YES");
		applicationDetailsService.save(fetchByApplicationId);
		System.out.println("db Call end");
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("message","save");
		System.out.println("final response"+response.toString());
		return new ResponseEntity<Object>(response,HttpStatus.OK);

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
