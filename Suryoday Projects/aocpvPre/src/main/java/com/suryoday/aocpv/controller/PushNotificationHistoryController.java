package com.suryoday.aocpv.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

@Component
@RestController
@RequestMapping(value="aocpv")
public class PushNotificationHistoryController {

	Logger logger = LoggerFactory.getLogger(PushNotificationHistoryController.class);
	@Autowired
	PushNotificationHistoryService pushnotificationhistoryservice;
	
	@Autowired
	PushNotificationHistoryRepo pushnotificationhistoryrepo;
	
	@RequestMapping(value="/fetchCount", method = RequestMethod.POST ,produces = "application/json")
	public ResponseEntity<Object> fetchCount(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("fetchCount start");
		if(jsonRequest.isEmpty()) {
			System.out.println("request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
		JSONObject jsonObject=new JSONObject(jsonRequest);
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
	
	@RequestMapping(value="/saveNotificationHistory", method = RequestMethod.POST ,produces = "application/json")
	public ResponseEntity<Object> saveNotificationHistory(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("saveNotificationHistory start");
		System.out.println("request"+jsonRequest);
		JSONObject jsonObject=new JSONObject(jsonRequest);
	if(jsonRequest.isEmpty()) {
		System.out.println("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
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
	
	
	@RequestMapping(value="/updateRead", method = RequestMethod.POST ,produces = "application/json")
	public ResponseEntity<Object> updateRead(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("updateToken start");
		System.out.println("request"+jsonRequest);
		JSONObject jsonObject=new JSONObject(jsonRequest);
	if(jsonRequest.isEmpty()) {
		System.out.println("request is empty"+jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}
		
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
//		}
		  }
	
	@RequestMapping(value="/fetchTop10", method = RequestMethod.POST ,produces = "application/json")
	public ResponseEntity<Object> fetchTop10(@RequestBody String jsonRequest ,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {
		System.out.println("fetchTop10 start");
		if(jsonRequest.isEmpty()) {
			System.out.println("request is empty"+jsonRequest);
				throw new EmptyInputException("Input cannot be empty");
			}
		JSONObject jsonObject=new JSONObject(jsonRequest);
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
	
}
