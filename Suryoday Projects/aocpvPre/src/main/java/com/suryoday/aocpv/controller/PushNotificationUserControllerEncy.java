package com.suryoday.aocpv.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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
import com.suryoday.aocpv.pojo.NotificationUser;
import com.suryoday.aocpv.repository.PushNotificationUserRepo;
import com.suryoday.aocpv.service.PushNotificationUserService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
@Component
@RestController
@RequestMapping(value="aocpv")
public class PushNotificationUserControllerEncy {
	
	Logger logger = LoggerFactory.getLogger(PushNotificationUserControllerEncy.class);
	
	@Autowired
	PushNotificationUserService pushnotificationservice;
	
	@Autowired
	PushNotificationUserRepo pushnotificationrepo;
	
	@Autowired 
	UserService userService;
	
	@RequestMapping(value="/saveUserEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> saveUserEncy(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("saveUser start");
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
		long pid=1;
		String userId = jsonObject.getJSONObject("Data").getString("UserId");
		String tokenId = jsonObject.getJSONObject("Data").getString("TokenId");
		String deviceId = jsonObject.getJSONObject("Data").getString("DeviceId");
		String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
		String role = jsonObject.getJSONObject("Data").getString("Role");
		LocalDateTime createdAt=LocalDateTime.now();
		
		NotificationUser notificationUser=new NotificationUser();
		Optional<Long> optional1 = pushnotificationrepo.fetchLastId();
		if (optional1.isPresent()) {
			pid= optional1.get();
			pid++;

		}
		notificationUser.setPid(pid);
		notificationUser.setUserId(userId);
		notificationUser.setTokenId(tokenId);
		notificationUser.setDeviceId(deviceId);
		notificationUser.setBranchId(branchId);
		notificationUser.setRole(role);
		notificationUser.setCreatedAt(createdAt);
		
		String saveData=pushnotificationservice.save(notificationUser);
	
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		org.json.simple.JSONObject  success= new org.json.simple.JSONObject();
		success.put("Success","Token Generated Suceessfully");
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
	
	
	@RequestMapping(value="/fetchTokenEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> fetchTokenEncy(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("fetchTokenEncy start");
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

		String userId = jsonObject.getJSONObject("Data").getString("UserId");
		String tokenId = jsonObject.getJSONObject("Data").getString("TokenId");
		NotificationUser notificationuser=pushnotificationservice.fetchToken(tokenId);
		String newUserId = notificationuser.getUserId();
		System.out.println(newUserId);
		if(!newUserId.equals(userId))
		{
			org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
			response.put("TokenId","");
			response.put("Success","Token id already exists with another User Id");
			return new ResponseEntity<Object>(response,HttpStatus.OK);
		}
		else if(newUserId.equals(userId))
		{
			org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
			response.put("TokenId",tokenId);
			response.put("Success","Fetch Successfully");
			return new ResponseEntity<Object>(response,HttpStatus.OK);
		}
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("Error","Error Occured");
		return new ResponseEntity<Object>(response,HttpStatus.BAD_REQUEST);
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
	
	
	@RequestMapping(value="/updateTokenEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> updateTokenEncy(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("updateTokenEncy start");
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
		String userId = jsonObject.getJSONObject("Data").getString("UserId");
		String tokenId = jsonObject.getJSONObject("Data").getString("TokenId");
		
			LocalDateTime updatedAt=LocalDateTime.now();
			pushnotificationservice.update(tokenId,userId,updatedAt);
			org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
			org.json.simple.JSONObject  success= new org.json.simple.JSONObject();
			success.put("Success","Token Updated");
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
	
	@RequestMapping(value="/fetchTokenAllEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> fetchTokenAllEncy(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("fetchTokenAllEncy start");
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
		String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
		 if(branchId.isEmpty()&&!empId.isEmpty())
			{
				 List<NotificationUser> list=pushnotificationservice.fetchTokenByUserId(empId);
				 System.out.println(list);
				 NotificationUser notificationUser = list.get(0);
				 String tokenId = notificationUser.getTokenId();
				 JSONObject Data1=new JSONObject();
				 Data1.put("Token",tokenId);
				 return new ResponseEntity<Object>(Data1.toString(),HttpStatus.OK);
		        }
		 else if(!branchId.isEmpty()&&empId.isEmpty())
			{
				 List<NotificationUser> list=pushnotificationservice.fetchTokenByBranchId(branchId);
				 System.out.println(list);
				 JSONArray regIds=new JSONArray();
					int i=0;
					while(i<list.size())
					{
						NotificationUser notificationUser = list.get(i);
						String tokenId = notificationUser.getTokenId();
						regIds.put(tokenId);
						i++;
					}
	
				 JSONObject Data1=new JSONObject();
				 Data1.put("Tokens",regIds);
				 return new ResponseEntity<Object>(Data1.toString(),HttpStatus.OK);
		        }
		 List<NotificationUser> list=pushnotificationservice.fetchTokenByUserIdAndBranchId(empId,branchId);
		 System.out.println(list);
		 NotificationUser notificationUser = list.get(0);
		 String tokenId = notificationUser.getTokenId();
		 JSONObject Data1=new JSONObject();
		 Data1.put("Token",tokenId);
		 return new ResponseEntity<Object>(Data1.toString(),HttpStatus.OK);
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
