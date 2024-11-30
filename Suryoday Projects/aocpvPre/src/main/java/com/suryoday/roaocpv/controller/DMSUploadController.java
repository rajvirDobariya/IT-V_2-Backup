package com.suryoday.roaocpv.controller;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.roaocpv.service.DMSUploadService;


@RestController
@RequestMapping(value = "/roaocpv")
public class DMSUploadController {

    @Autowired
    DMSUploadService dmsuplaodservice;
    
    private static Logger logger = LoggerFactory.getLogger(DMSUploadController.class);
    
    @RequestMapping(value = "/dmsupload", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<Object> dmsuplaod(@RequestBody String jsonRequest) throws IOException {
    	JSONObject jsonObject=new JSONObject(jsonRequest);
    	JSONObject sendAuthenticateResponse= new JSONObject();
    	sendAuthenticateResponse=dmsuplaodservice.dmsupload(jsonObject);
    	JSONArray jsonArray = sendAuthenticateResponse.getJSONArray("Response");
    	HttpStatus h = HttpStatus.OK;
    	org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
    	response.put("Message", "DOCUMENT UPLOAD SUCCESSFULLY");
	 	
    	for(int n=0;n<jsonArray.length();n++) {
    		JSONObject jsonResponse = jsonArray.getJSONObject(n);
    		if(jsonResponse.has("Errors")) {
        		h = HttpStatus.BAD_REQUEST;
        		response.put("Message", "FAILED !! PLEASE TRY AGAIN");
        	}
    	}
 
    	logger.debug("Response in controller: "+response);
		return new ResponseEntity<>(response.toString(), h);
    }

}