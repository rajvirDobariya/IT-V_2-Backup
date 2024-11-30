package com.suryoday.roaocpv.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.pojo.GeoLcation;
import com.suryoday.aocpv.pojo.Image;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.pojo.Address;
import com.suryoday.roaocpv.pojo.PersonalDetailResponse;
import com.suryoday.roaocpv.service.ApplicationDetailsService;
import com.suryoday.roaocpv.service.CRMService;


@Controller
@RequestMapping("/rocpv")
public class CRMControllerEncy {
	
	@Autowired
	CRMService cRMService;
	
	@Autowired
	UserService userService;
	
	 @Autowired
	 AocpvImageService aocpvImageService;
	 
	 @Autowired
	ApplicationDetailsService applicationDetailsService;
		
	 
	Logger logger = LoggerFactory.getLogger(CRMControllerEncy.class);

	@RequestMapping(value="/consumeCustomerDataEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getCustomerData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
				String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				System.out.println(encryptString);
			//	logger.debug("start request" + jsonRequest.toString());
				String key = X_Session_ID;
				String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
		 JSONObject Header= new JSONObject();
		 Header.put("X-Request-ID",X_REQUEST_ID );
		 Header.put("X-Transaction-ID",X_TRANSACTION_ID );
		 Header.put("X-To-ID",X_TO_ID );
		 Header.put("X-From-ID",X_FORM_ID );
		 Header.put("Content-TypeD",CONTEND_TYPE );
		 
		 JSONObject jsonObject=new JSONObject(decryptContainerString);
			logger.debug("Fetch All Details");
			logger.debug("Request"+decryptContainerString);
			
			if(decryptContainerString.isEmpty()) {
				logger.debug("Request is empty"+decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
		 
			String appln = jsonObject.getJSONObject("Data").getString("ApplicationNumber");
	
	 logger.debug("db Call start");
     System.out.println("List"+appln);  
     org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
     
     int max = 899999;
		int min = 800000;
		int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
		//String stan = Integer.toString(random_int);
		if (random_int % 2 == 0) {
			 Thread.sleep(5000);
     	 response.put("Success", "CRM Number Generated");    			
		} else {    			
			Thread.sleep(5000);
    	 	response.put("Error", "CRM Number Not Generated");        			
		}        
		String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
		data2.put("value", encryptString2);
		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
		data3.put("Data", data2);
		System.out.println(data3);
		return new ResponseEntity<Object>(data3, HttpStatus.OK);
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
	
	@RequestMapping(value = "/CRMEncy", method = RequestMethod.PUT , produces = "application/json")
    public ResponseEntity<Object> crmData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		//System.out.println(jsonRequest);
		
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
				String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				//System.out.println(encryptString);
			//	logger.debug("start request" + jsonRequest.toString());
				String key = X_Session_ID;
				String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
		
		 
		 JSONObject jsonObject=new JSONObject(decryptContainerString);
		 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
	    	JSONObject jsonObjectresponse= new JSONObject(); 
	    	jsonObjectresponse = cRMService.crmData(applicationNo,X_User_ID);
	    	HttpStatus  h=HttpStatus.BAD_REQUEST;
	    	if(jsonObjectresponse!=null) {
	    		 String Data2= jsonObjectresponse.getString("data");
				 JSONObject Data1= new JSONObject(Data2);
				 if(Data1.has("Data")) {
					 h= HttpStatus.OK;
				 }
				 String	data = Data1.toString();
	    			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
	    			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
	    			data2.put("value", encryptString2);
	    			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
	    			data3.put("Data", data2);
	    			return new ResponseEntity<Object>(data3, HttpStatus.OK);
	    	}else {
	    		 logger.debug("GATEWAY_TIMEOUT");
				 return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
    		
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
	@RequestMapping(value = "/getBranchNameCodeEncy", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<Object> getBranchNameCode(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req) throws IOException {
    	   	
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
	
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
				
			//	logger.debug("start request" + jsonRequest.toString());
				String key = X_Session_ID;
			
		 JSONObject Header= new JSONObject();
		 Header.put("X-Request-ID",X_REQUEST_ID );
		 Header.put("X-Transaction-ID",X_TRANSACTION_ID );
		 Header.put("X-To-ID",X_TO_ID );
		 Header.put("X-From-ID",X_FORM_ID );
		 Header.put("Content-TypeD",CONTEND_TYPE );
		 Header.put("X-Correlation-ID", X_Correlation_ID);
		 Header.put("X-User-ID", X_User_ID); 
		 
	  
		JSONObject	jsonObject = cRMService.getBranchNameCode(Header);
    	
    	HttpStatus h = HttpStatus.BAD_REQUEST;
		if (jsonObject != null) {
			String Data2 = jsonObject.getString("data");
			//System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			//System.out.println(Data1);

			if (Data1.has("Data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}
			  String data = Data1.toString();
		  		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		  		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
		  		data2.put("value", encryptString2);
		  		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
		  		data3.put("Data", data2);
		  		//System.out.println(data3);
		  		return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
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
	
	@RequestMapping(value = "/fetchAllBranchUATEncy", method = RequestMethod.POST , produces = "application/json")
    public ResponseEntity<Object> fetchAllBranchUAT(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req) throws IOException {
    	   	
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		//System.out.println(jsonRequest);
		
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
				String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				System.out.println(encryptString);
			//	logger.debug("start request" + jsonRequest.toString());
				String key = X_Session_ID;
				String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			 
		 JSONObject jsonObject=new JSONObject(decryptContainerString);
			logger.debug("Fetch All Details");
			logger.debug("Request"+decryptContainerString);
			
			if(decryptContainerString.isEmpty()) {
				logger.debug("Request is empty"+decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}	
		
		logger.debug("your rest api is working");
    	JSONObject jsonObject2= new JSONObject();   	
    	System.out.println("1");
    	jsonObject2 = cRMService.fetchAllBranch();
    	JSONObject jsonModified = new JSONObject(); 
    	jsonModified = jsonObject2;
    	 org.json.JSONArray jsonArray = jsonModified.getJSONObject("Data").getJSONArray("ReferenceData");
  	  org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
  	  response.put("Data",jsonArray);
  	  String data = response.toString();
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
		data2.put("value", encryptString2);
		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
		data3.put("Data", data2);
		System.out.println(data3);
		return new ResponseEntity<Object>(data3, HttpStatus.OK);
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
	
	@RequestMapping(value="/fetchAllImagesEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> fetchAllImages(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
		 
		 org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			//System.out.println(jsonRequest);
			
			boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		       if (sessionId == true ) {
		    	   userService.getSessionId(X_User_ID, request); 
					String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
					System.out.println(encryptString);
				//	logger.debug("start request" + jsonRequest.toString());
					String key = X_Session_ID;
					String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			 JSONObject Header= new JSONObject();
			 Header.put("X-Request-ID",X_REQUEST_ID );
			 Header.put("X-Transaction-ID",X_TRANSACTION_ID );
			 Header.put("X-To-ID",X_TO_ID );
			 Header.put("X-From-ID",X_FORM_ID );
			 Header.put("Content-TypeD",CONTEND_TYPE );
			 
			 JSONObject jsonObject=new JSONObject(decryptContainerString);
				logger.debug("Fetch All Details");
				logger.debug("Request"+decryptContainerString);
				
				if(decryptContainerString.isEmpty()) {
					logger.debug("Request is empty"+decryptContainerString);
					throw new EmptyInputException("Input cannot be empty");
				}	
		 
			String appln = jsonObject.getJSONObject("Data").getString("ApplicationNumber");
			Long applicationNo = Long.parseLong(appln);
			
			List<AocpvImages> listAocpvImage = aocpvImageService.getByApplicationNoAll(applicationNo);
			List<Image> listOfImages=new ArrayList<>();
			if(listAocpvImage.isEmpty()) {
				throw new EmptyInputException("Images Lists are Empty");
			}
			else {
				
				for(AocpvImages aocpvImages:listAocpvImage) {
					
						String geoLocation = aocpvImages.getGeoLocation();
						JSONObject jsonObjectImage=new JSONObject(geoLocation);
				 		String pimage = jsonObjectImage.getString("image");
				 		String pLat = jsonObjectImage.getString("Lat");
				 		String pLong = jsonObjectImage.getString("Long");
				 		String pAddress = jsonObjectImage.getString("Address");
				 		String ptimestamp = jsonObjectImage.getString("timestamp");
			
				 		GeoLcation geolocation =new GeoLcation(pimage,pLat,pLong,pAddress,ptimestamp);
 		
				 		String documenttype = aocpvImages.getDocumenttype();
				 		String imageName = aocpvImages.getImageName();
				 		String type = aocpvImages.getType();
				 		long size = aocpvImages.getSize();
				 		String member=aocpvImages.getMember();
				 		byte[] images2 = aocpvImages.getImages();
				 		String encoded = Base64.getEncoder().encodeToString(images2);
				 		 
				 		Image images = new Image(documenttype,imageName,type,size,encoded,member,geolocation);
				 			
					 		listOfImages.add(images);						
				}
				//System.out.println("List of Images "+listOfImages);
		}
			
			 logger.debug("db Call start");

	            JSONArray psr= new JSONArray(listOfImages);
	            org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
	            org.json.simple.JSONObject  data1= new org.json.simple.JSONObject();
	            data1.put("images", psr);
	            response.put("Data", data1);
	            //response.put("Data", psr);
	            String data = response.toString();
	    		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
	    		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
	    		data2.put("value", encryptString2);
	    		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
	    		data3.put("Data", data2);
	    		return new ResponseEntity<Object>(data3, HttpStatus.OK);
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
	
	@RequestMapping(value="/getPersonalDetailsByApplnEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getPersonalDetailsByAppln(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		//System.out.println(jsonRequest);
		
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
				String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				System.out.println(encryptString);
			//	logger.debug("start request" + jsonRequest.toString());
				String key = X_Session_ID;
				String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
		 JSONObject Header= new JSONObject();
		 Header.put("X-Request-ID",X_REQUEST_ID );
		 Header.put("X-Transaction-ID",X_TRANSACTION_ID );
		 Header.put("X-To-ID",X_TO_ID );
		 Header.put("X-From-ID",X_FORM_ID );
		 Header.put("Content-TypeD",CONTEND_TYPE );
		 
		 JSONObject jsonObject=new JSONObject(decryptContainerString);
			logger.debug("Fetch All Details");
			logger.debug("Request"+decryptContainerString);
			
			if(decryptContainerString.isEmpty()) {
				logger.debug("Request is empty"+decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}	
		 
			String appln = jsonObject.getJSONObject("Data").getString("ApplicationNumber");
			PersonalDetailResponse personalDetail=applicationDetailsService.getPersonalDetailsByAppln(appln);
			
            org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
            JSONObject j=new JSONObject(personalDetail);
                       response.put("Data", j);
                
            System.out.println(response.toJSONString());    
            String data = response.toString();
    		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
    		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
    		data2.put("value", encryptString2);
    		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
    		data3.put("Data", data2);
    		System.out.println(data3);
    		return new ResponseEntity<Object>(data3, HttpStatus.OK);
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
	
	@RequestMapping(value="/savePersonalDetailsApplicationEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> savePersonalDetailsData(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req) {
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		//System.out.println(jsonRequest);
		
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
				String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				System.out.println(encryptString);
			//	logger.debug("start request" + jsonRequest.toString());
				String key = X_Session_ID;
				String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);		 
				JSONObject jsonObject=new JSONObject(decryptContainerString);
			logger.debug("Fetch All Details");
			logger.debug("Request"+decryptContainerString);
			
			if(decryptContainerString.isEmpty()) {
				logger.debug("Request is empty"+decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
		
			String title = jsonObject.getJSONObject("Data").getString("Title");
			String fname = jsonObject.getJSONObject("Data").getString("FirstName");
			String mname = jsonObject.getJSONObject("Data").getString("MiddleName");
			String lname = jsonObject.getJSONObject("Data").getString("LastName");
			String mobile = jsonObject.getJSONObject("Data").getString("MobileNO");
			String dateOfBirth = jsonObject.getJSONObject("Data").getString("DateOfBirth");
			String age = jsonObject.getJSONObject("Data").getString("Age");
			String gender = jsonObject.getJSONObject("Data").getString("Gender");
			String fatherName = jsonObject.getJSONObject("Data").getString("FatherName");
			String motherName = jsonObject.getJSONObject("Data").getString("MotherName");
			String spouseName = jsonObject.getJSONObject("Data").getString("SpouseName");
			String numberOfDependant = jsonObject.getJSONObject("Data").getString("DependantMember");
			String educationQualification = jsonObject.getJSONObject("Data").getString("EducationQualification");
			String caste = jsonObject.getJSONObject("Data").getString("Caste");
			String religion = jsonObject.getJSONObject("Data").getString("Religion");
		    String appln = jsonObject.getJSONObject("Data").getString("AppplicationNumber");
		
		    String message=applicationDetailsService.addpersonalDetails(appln,jsonObject.getJSONObject("Data").toString());
		    
		org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
		response.put("Message", message);
		
		 System.out.println(response.toJSONString());    
         String data = response.toString();
 		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		System.out.println(data3);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
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
	
	@RequestMapping(value="/getPermanentAdddressByApplnEncy", method = RequestMethod.POST,produces = "application/json")
	public ResponseEntity<Object> getPermanentAdddressByAppln(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req)  throws Exception{
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		//System.out.println(jsonRequest);
		
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
				String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				System.out.println(encryptString);
			//	logger.debug("start request" + jsonRequest.toString());
				String key = X_Session_ID;
				String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
		
		 JSONObject jsonObject=new JSONObject(decryptContainerString);
			logger.debug("Fetch All Details");
			logger.debug("Request"+decryptContainerString);
			
			if(decryptContainerString.isEmpty()) {
				logger.debug("Request is empty"+decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
		 
			String appln = jsonObject.getJSONObject("Data").getString("ApplicationNumber");
			List<Address> list =applicationDetailsService.getAddress(appln);

            org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
            JSONArray sdk= new JSONArray(list);
            response.put("Data", sdk);
                
 
            String data = response.toString();
    		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
    		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
    		data2.put("value", encryptString2);
    		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
    		data3.put("Data", data2);
    		return new ResponseEntity<Object>(data3, HttpStatus.OK);
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
	
	@RequestMapping(value="/saveAddressDetailsApplicationEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> saveAddressDetailsApplication(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			 @RequestHeader(name = "X-Request-ID", required = true) String X_REQUEST_ID,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "X-From-ID", required = true) String X_FORM_ID,
			 @RequestHeader(name = "X-To-ID", required = true) String X_TO_ID,
			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_TRANSACTION_ID,
			 @RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
				HttpServletRequest request,
			 @RequestHeader(name = "Content-Type", required = true) String CONTEND_TYPE,HttpServletRequest req) {
		
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		//System.out.println(jsonRequest);
		
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
				String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				System.out.println(encryptString);
			//	logger.debug("start request" + jsonRequest.toString());
				String key = X_Session_ID;
				String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
		 
		 JSONObject jsonObject=new JSONObject(decryptContainerString);
			logger.debug("Fetch All Details");
			logger.debug("Request"+decryptContainerString);
			
			if(decryptContainerString.isEmpty()) {
				logger.debug("Request is empty"+decryptContainerString);
				throw new EmptyInputException("Input cannot be empty");
			}
			
			String appln = jsonObject.getJSONObject("Data").getString("AppplicationNumber");
			org.json.JSONArray jsonArray = jsonObject.getJSONObject("Data").getJSONArray("Address");
			String message =applicationDetailsService.saveAddress(appln,jsonArray.toString());
			org.json.simple.JSONObject   response= new org.json.simple.JSONObject();
			response.put("Message",message );  
         String data = response.toString();
 		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
 		data2.put("value", encryptString2);
 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
 		data3.put("Data", data2);
 		System.out.println(data3);
 		return new ResponseEntity<Object>(data3, HttpStatus.OK);
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
