package com.suryoday.connector.controller;


import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.connector.service.CityPincodeService;

@RestController
@RequestMapping("/connector/v1")
public class CityPincodeController {

	
	@Autowired
	private CityPincodeService cityPincodeService;
	
	private static Logger logger = LoggerFactory.getLogger(CityPincodeController.class);
	
	@RequestMapping(value="/cityPincode/{cityPincode}", method = RequestMethod.GET,produces = "application/json")
	public ResponseEntity<Object> cityPinCode(@PathVariable ("cityPincode") String cityPincode,
			 @RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,HttpServletRequest req)  throws Exception{
	
		JSONObject Header= new JSONObject();
		 Header.put("X-Correlation-ID",X_CORRELATION_ID );
		 Header.put("X-From-ID",X_From_ID );
		 Header.put("X-To-ID",X_To_ID );
		 Header.put("X-Transaction-ID",X_Transaction_ID );
		 Header.put("X-User-ID",X_User_ID );
		 Header.put("X-Request-ID",X_Request_ID );
		 logger.debug("GET Request",cityPincode);
		 
			if(X_Request_ID.equals("IEXCEED"))
			{
				
		        JSONObject cityPinCode = cityPincodeService.getCityPincode(cityPincode, Header);
		        logger.debug("Response from API",cityPinCode);
			
				HttpStatus  h=HttpStatus.BAD_GATEWAY;
				 if(cityPinCode!=null)
				 {
					 String Data2= cityPinCode.getString("data");
					 System.out.println("data2");
					 JSONObject Data1= new JSONObject(Data2);
					logger.debug("JSON Response From API",Data2);
					 
					 if(Data1.has("Data"))
					 {
						   h= HttpStatus.OK;
						   
					 }
					 else if(Data1.has("Error"))
					 {
						 h= HttpStatus.BAD_REQUEST;
						 
					 }
					 logger.debug("Main Response From API",Data1.toString());
					 return new ResponseEntity<Object>(Data1.toString(), h);
					 
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
		
	}

}
