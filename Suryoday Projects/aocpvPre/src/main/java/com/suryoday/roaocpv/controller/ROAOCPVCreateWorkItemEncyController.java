package com.suryoday.roaocpv.controller;

import java.io.IOException;

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

import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.service.ROAOCPVCreateWorkItemService;

@RestController
@RequestMapping(value = "/roaocpv")
public class ROAOCPVCreateWorkItemEncyController {

    @Autowired
    ROAOCPVCreateWorkItemService roaocpvcreateworkitemservice;
    
    @Autowired
	UserService userService;
    
    private static Logger logger = LoggerFactory.getLogger(ROAOCPVCreateWorkItemEncyController.class);
    
//    @RequestMapping(value = "/workitemencyeg", method = RequestMethod.POST , produces = "application/json")
//    public ResponseEntity<Object> workitem() throws IOException {
//    	
//    	JSONObject sendAuthenticateResponse= new JSONObject();
//    	sendAuthenticateResponse=roaocpvcreateworkitemservice.workitem();
//    	logger.debug("Response in controller: "+sendAuthenticateResponse);
//		return new ResponseEntity<>(sendAuthenticateResponse.toString(), HttpStatus.OK);
//    }
    
    @RequestMapping(value = "/workitemency", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<Object> workitem(@RequestBody String bm,
    		@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
    		 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,HttpServletRequest request
    		) throws NullPointerException, IOException {
    	
    	String key = X_Session_ID;
    	boolean sessionId = userService.validateSessionId(X_Session_ID, request);
    	 if (sessionId == true ) {
    		 JSONObject sendAuthenticateResponse= new JSONObject();
    		 org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
    		 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
 			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

    			JSONObject jsonObject = new JSONObject(decryptContainerString);
    	    	sendAuthenticateResponse=roaocpvcreateworkitemservice.workitem(jsonObject);
    	    	  logger.debug("Main Response from API : " + sendAuthenticateResponse.toString());
                  String data = sendAuthenticateResponse.toString();
                  String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
                  org.json.JSONObject data2 = new org.json.JSONObject();
                  data2.put("value", encryptString2);
                  org.json.JSONObject data3 = new org.json.JSONObject();
                  data3.put("Data", data2);
                  logger.debug("response : " + data3.toString());
                  return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
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
