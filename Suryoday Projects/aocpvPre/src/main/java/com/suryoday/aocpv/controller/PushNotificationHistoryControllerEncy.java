package com.suryoday.aocpv.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.suryoday.aocpv.pojo.NotificatonUserHistory;
import com.suryoday.aocpv.repository.PushNotificationHistoryRepo;
import com.suryoday.aocpv.service.PushNotificationHistoryService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;

@Component
@RestController
@RequestMapping(value="aocpv")
public class PushNotificationHistoryControllerEncy {
	Logger logger = LoggerFactory.getLogger(PushNotificationHistoryControllerEncy.class);
	@Autowired
	PushNotificationHistoryService pushnotificationhistoryservice;
	
	@Autowired
	PushNotificationHistoryRepo pushnotificationhistoryrepo;
	
	@Autowired 
	UserService userService;
	
	
	@RequestMapping(value="/fetchCountEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> fetchCountEncy(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("fetchCountEncy start");
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
		String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
		String role = jsonObject.getJSONObject("Data").getString("Role");
	List<NotificatonUserHistory> list=pushnotificationhistoryservice.fetchCount(branchId,role);
		
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		org.json.simple.JSONObject  count= new org.json.simple.JSONObject();
		count.put("Count",list.size());
		response.put("Data",count);
		System.out.println("Final Response"+response.toString());
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
	
	@RequestMapping(value="/saveNotificationHistoryEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> saveNotificationHistoryEncy(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("saveNotificationHistoryEncy start");
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
		long id=1;
		String empId = jsonObject.getJSONObject("Data").getString("EmpId");
		String tokenId = jsonObject.getJSONObject("Data").getString("TokenId");
		String senderId = jsonObject.getJSONObject("Data").getString("SenderId");
		String notificationTemplateId = jsonObject.getJSONObject("Data").getString("NotificationTemplateId");
		String content = jsonObject.getJSONObject("Data").getString("Content");
		String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
		String role = jsonObject.getJSONObject("Data").getString("Role");
		LocalDateTime createdAt=LocalDateTime.now();
		
		NotificatonUserHistory notificationUserHistory=new NotificatonUserHistory();
		Optional<Long> optional1 = pushnotificationhistoryrepo.fetchLastId();
		if (optional1.isPresent()) {
			id= optional1.get();
			id++;

		}
		notificationUserHistory.setId(id);
		notificationUserHistory.setEmpId(empId);
		notificationUserHistory.setTokenId(tokenId);
		notificationUserHistory.setSenderId(senderId);
		notificationUserHistory.setNotificationtemplateId(notificationTemplateId);
		notificationUserHistory.setContentTemplate(content);
		notificationUserHistory.setBranchId(branchId);
		notificationUserHistory.setRole(role);
		notificationUserHistory.setCreatedAt(createdAt);
		notificationUserHistory.setReadHistory(1);
		notificationUserHistory.setTrash(1);
		
		String saveData=pushnotificationhistoryservice.save(notificationUserHistory);
	
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		org.json.simple.JSONObject  success= new org.json.simple.JSONObject();
		success.put("Success","Data Saved Suceessfully");
		response.put("Data", success);
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
	
	@RequestMapping(value="/updateReadEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> updateReadEncy(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("fetchTop10CountEncy start");
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
		String empId = jsonObject.getJSONObject("Data").getString("EmpId");
		int read=0;
			LocalDateTime updatedAt=LocalDateTime.now();
			pushnotificationhistoryservice.update(read,empId,updatedAt);
			org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
			org.json.simple.JSONObject  success= new org.json.simple.JSONObject();
			success.put("Success","Data Updated");
			response.put("Data",success);
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
	
	@RequestMapping(value="/fetchTop10Ency", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> fetchTop10Ency(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("fetchTop10Ency start");
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
		int read=1;
		String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
		String role = jsonObject.getJSONObject("Data").getString("Role");
	List<NotificatonUserHistory> list=pushnotificationhistoryservice.fetchTop10(branchId,role,read);
		
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Data",list);
		System.out.println("Final Response"+response.toString());
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
