package com.suryoday.familyMember.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.familyMember.service.SendOTPService;


@RestController
@RequestMapping("/familyMember")
public class SendOTPController {

	@Autowired
	SendOTPService otpService;
	
	Logger logger = LoggerFactory.getLogger(SendOTPController.class);
	
	@RequestMapping(value="/sendOtp/email", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> sendOtpAndEmail(@RequestBody String bm,
			 @RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-CALL-ID", required = true) String X_CALL_ID,
			 
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,HttpServletRequest req)  throws Exception{
		logger.debug("sendOtp start");
		logger.debug("sendOtp request"+bm);
		JSONObject Header= new JSONObject();
		 Header.put("X-Correlation-ID",X_CORRELATION_ID );
		 Header.put("X-From-ID",X_From_ID );
		 Header.put("X-To-ID",X_To_ID );
		 Header.put("X-Transaction-ID",X_Transaction_ID );
		 Header.put("X-User-ID",X_User_ID );
		 Header.put("X-Request-ID",X_Request_ID );
		 
		 if(Integer.parseInt(X_CALL_ID)>3)
		 {
			 logger.debug("Max Limit Exceeded");
			 
			   org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", "Max Limit Exceeded");
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Error", data2);
				
				return new ResponseEntity<Object>(data3.toString(), HttpStatus.BAD_REQUEST);
		 }
		 else
		 {
			 
			 if(X_Request_ID.equals("NOVOPAY"))
				{
					JSONObject jsonObject = new JSONObject(bm);
					System.out.println(jsonObject.toString());
			        JSONObject sendOtp = otpService.sendOtp(jsonObject, Header);
			        
			        HttpStatus  h=HttpStatus.BAD_GATEWAY;
					 if(sendOtp!=null) {
						 String Data2= sendOtp.getString("data");
						 System.out.println("data2");
						 JSONObject Data1= new JSONObject(Data2);
						
						 System.out.println(Data1);
						 
						 if(Data1.has("Data"))
						 {
							   h= HttpStatus.OK;
							   
						 }
						 else if(Data1.has("Error"))
						 {
							 h= HttpStatus.BAD_REQUEST;
							 
						 }
						 logger.debug("response"+Data1);
						 return new ResponseEntity<Object>(Data1.toString(), h);
						 
					 }
					 else
					 {
						 logger.debug("timeout");
						 return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
					 }
				}
			 else
				{ 
				 logger.debug("Invalid Request");
					return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);
					
				} 
		 }
		 
	}
	@RequestMapping(value="/validateOTP/{OTP}", method = RequestMethod.GET,produces = "application/json")
	public ResponseEntity<Object> validateOTP(@PathVariable ("OTP") String OTP,
			 @RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,HttpServletRequest req)  throws Exception{
		logger.debug("validateOTP start");
		logger.debug("validateOTP request"+OTP);
		JSONObject Header= new JSONObject();
		 Header.put("X-Correlation-ID",X_CORRELATION_ID );
		 Header.put("X-From-ID",X_From_ID );
		 Header.put("X-To-ID",X_To_ID );
		 Header.put("X-Transaction-ID",X_Transaction_ID );
		 Header.put("X-User-ID",X_User_ID );
		 Header.put("X-Request-ID",X_Request_ID );
		 
		 
			if(X_Request_ID.equals("NOVOPAY"))
			{
				//JSONObject jsonObject = new JSONObject(bm);
				//System.out.println(jsonObject.toString());
		        //JSONObject sendsms = otpService.validateOTP(jsonObject, Header);
		        JSONObject sendsms = otpService.validateOTP(OTP, Header);
				HttpStatus  h=HttpStatus.BAD_GATEWAY;
				 if(sendsms!=null)
				 {
					 String Data2= sendsms.getString("data");
					 System.out.println("data2");
					 JSONObject Data1= new JSONObject(Data2);
					
					 System.out.println(Data1);
					 
					 if(Data1.has("Data"))
					 {
						   h= HttpStatus.OK;
						   
					 }
					 else if(Data1.has("Error"))
					 {
						 h= HttpStatus.BAD_REQUEST;
						 
					 }
					 logger.debug("response"+Data1);
					 return new ResponseEntity<Object>(Data1.toString(), h);
					 
				 }
				 else
				 {
					 logger.debug("timeout");
					 return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				 }	
			}
			else
			{ 
				 logger.debug("Invalid Request");
				return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);
				
			}
		
	}
}
