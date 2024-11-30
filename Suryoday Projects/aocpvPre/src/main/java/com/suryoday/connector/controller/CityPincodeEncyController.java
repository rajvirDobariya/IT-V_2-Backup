package com.suryoday.connector.controller;


import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.CityPincodeService;
import com.suryoday.connector.service.UserService;

@RestController
@RequestMapping("/connector/v1")
public class CityPincodeEncyController {

	
	@Autowired
	private CityPincodeService cityPincodeService;
	
	@Autowired
	UserService userService;
	
	private static Logger logger = LoggerFactory.getLogger(CityPincodeEncyController.class);
	
	@RequestMapping(value="/cityPincodeEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> cityPinCode(
			 @RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "cityPincode", required = true) String cityPincode,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,HttpServletRequest request)  throws Exception{
	
		JSONObject Header= new JSONObject();
		 Header.put("X-Correlation-ID",X_CORRELATION_ID );
		 Header.put("X-From-ID",X_From_ID );
		 Header.put("X-To-ID",X_To_ID );
		 Header.put("X-Transaction-ID",X_Transaction_ID );
		 Header.put("X-User-ID",X_User_ID );
		 Header.put("X-Request-ID",X_Request_ID );
		// logger.debug("POST Request : " + cityPincode);
		 
		 boolean sessionId = userService.validateSessionId(X_Session_ID, request);
			if (sessionId == true ) {
				userService.getSessionId(X_User_ID, request);

				String key = X_Session_ID;

				
				String data = "";
		 
			if(X_Request_ID.equals("IEXCEED"))
			{
				
		        JSONObject cityPinCode = cityPincodeService.getCityPincode(cityPincode, Header);
		      //  logger.debug("Response from API : " + cityPinCode);
			
				HttpStatus  h=HttpStatus.BAD_GATEWAY;
				 if(cityPinCode!=null)
				 {
					 String Data2= cityPinCode.getString("data");
					// System.out.println("data2");
					 JSONObject Data1= new JSONObject(Data2);
				//	logger.debug("JSON Response From API : " + Data2);
					 
					 if(Data1.has("Data"))
					 {
						   h= HttpStatus.OK;
						   
					 }
					 else if(Data1.has("Error"))
					 {
						 h= HttpStatus.BAD_REQUEST;
						 
					 }
				//	 logger.debug("Main Response From API : " + Data1.toString());
					 data = Data1.toString();
						String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
						org.json.JSONObject data2 = new org.json.JSONObject();
						data2.put("value", encryptString2);
						org.json.JSONObject data3 = new org.json.JSONObject();
						data3.put("Data", data2);
						logger.debug("response : " + data3.toString());
					 return new ResponseEntity<Object>(data3.toString(), h);
					 
				 }
				 else
				 {
					 logger.debug("GATEWAY_TIMEOUT");
					 return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				 }	
			}
			else
			{ 
				logger.debug("INVALID REQUEST");
				return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);
				
			}
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
