package com.suryoday.aocpv.controller;

import java.util.List;

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
import com.suryoday.aocpv.service.PushNotificationService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;

@Component
@RestController
@RequestMapping(value="aocpv")
public class PushNotificationControllerEncy {
	Logger logger = LoggerFactory.getLogger(PushNotificationControllerEncy.class);
	@Autowired
	PushNotificationService pushnotificationservice;
	@Autowired 
	UserService userService;
	
	@RequestMapping(value="/sendNotificationEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> sendNotificationEncy(@RequestBody String jsonRequest ,
			 @RequestHeader(name = "Authorization-key", required = true) String Authorization_key,
			 @RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("sendNotificationEncy start");
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
		JSONObject Header= new JSONObject();
		 Header.put("Authorization-key",Authorization_key );
		 Header.put("Content-Type",Content_Type );

		 String empId = jsonObject.getJSONObject("Data").getString("EmpId");
			String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
			System.out.println(jsonObject.toString());
			 if(branchId.isEmpty()&&!empId.isEmpty())
			{
				 List<NotificationUser> list=pushnotificationservice.fetchByUserId(empId);
				 System.out.println(list);
				 NotificationUser notificationUser = list.get(0);
				 String tokenId = notificationUser.getTokenId();
				JSONObject sendNotification = pushnotificationservice.sendNotification(tokenId, Header);
		        if(sendNotification!=null) {
		        HttpStatus  h=HttpStatus.OK;
					 JSONObject Data1= new JSONObject();
					 JSONObject response= new JSONObject();
					 response.put("EmpId",empId);
					 response.put("DeviceId",notificationUser.getDeviceId());
					 response.put("TokenId",tokenId);
					 Data1.put("Data",response);
					 System.out.println(Data1);
	
					 return new ResponseEntity<Object>(Data1.toString(), h);
		        }
		        else
				 {
					 logger.debug("timeout");
					 return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				 }

			 }
			else if(empId.isEmpty()&&!branchId.isEmpty())
			{
				List<NotificationUser> list=pushnotificationservice.fetchByBranchId(branchId);
				JSONArray regIds=new JSONArray();
				int i=0;
				while(i<list.size())
				{
					NotificationUser notificationUser = list.get(i);
					String tokenId = notificationUser.getTokenId();
					regIds.put(tokenId);
					i++;
				}
				System.out.println(regIds);
				JSONObject sendNotification = pushnotificationservice.sendNotificationAll(regIds, Header);
				if(sendNotification!=null) {
		        HttpStatus  h=HttpStatus.OK;
		        JSONArray response=new JSONArray();
		        int j=0;
		        while(j<list.size()) {
		        	NotificationUser notificationUser = list.get(j);
					String tokenId = notificationUser.getTokenId();
					String deviceId = notificationUser.getDeviceId();
					String userId = notificationUser.getUserId();
					JSONObject resp=new JSONObject();
					resp.put("BranchId", branchId);
					resp.put("UserId",userId);
					resp.put("DeviceId",deviceId);
					resp.put("TokenId",tokenId);
					response.put(resp);
					j++;
		        	
			}
		        JSONObject Data1= new JSONObject();				 
				Data1.put("Data",response);
				 return new ResponseEntity<Object>(Data1.toString(), h);
				 
			 }
				else
				 {
					 logger.debug("timeout");
					 return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				 }

			
		}
			 	JSONObject response=new JSONObject();
				JSONObject error=new JSONObject();
				error.put("Error","Bad Request");
				response.put("Data",error);
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
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
