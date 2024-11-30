package com.suryoday.roaocpv.controller;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.service.LivenessImageService;

@RestController
@RequestMapping(value="/roaocpv")
public class LivenessImageControllerEncy {

	@Autowired
	 LivenessImageService livenessImageService;
	
	@Autowired
	AocpvImageService aocpvImageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	AocpCustomerDataService aocpCustomerDataService;
	
	@RequestMapping(value = "/livenessOfImageEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> faceMatch(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-Correlation-ID", X_Correlation_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);
		
org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
	       if (sessionId == true ) {
	    	   userService.getSessionId(X_User_ID, request); 
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			
    	JSONObject jsonObject=new JSONObject(decryptContainerString);
		String applicationNo=jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String documentType=jsonObject.getJSONObject("Data").getString("documentType");
		long applicationNoInLong = Long.parseLong(applicationNo);
		AocpvImages aocpvImage =aocpvImageService.getByApplicationNoAndName(applicationNoInLong, documentType);

			byte[] images = aocpvImage.getImages();
	 		String encoded = Base64.getEncoder().encodeToString(images);
	 		
		//JSONObject livenessOfimage = livenessImageService.checkLivenessOfImage(encoded, Header);
	 		JSONObject livenessOfimage=new JSONObject();
	 		org.json.simple.JSONObject result = new org.json.simple.JSONObject();
	 		org.json.simple.JSONObject data = new org.json.simple.JSONObject();
	 		
	 		result.put("isLive", true);
	 		result.put("reviewNeeded", null);
	 		result.put("multipleFacesDetected", false);
	 		result.put("livenessScore", 0.7922145646052028);
	 		data.put("result", result);
	 		data.put("requestId", "a897492f-ad14-4cff-a6c4-018a20e4cfae");
	 		data.put("statusCode", 101);
	 		livenessOfimage.put("data", data.toString());
		HttpStatus h = HttpStatus.OK;
		if (livenessOfimage != null) {
			String Data2 = livenessOfimage.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("result")) {
				h = HttpStatus.OK;
				if(documentType.equalsIgnoreCase("AO_Selfie") || documentType.equalsIgnoreCase("AO_Selfie")) {
				if(Data1.getJSONObject("result").getBoolean("isLive")) {
					AocpCustomer aocpCustomer = aocpCustomerDataService.fetchByApp(applicationNoInLong);
					if(documentType.equalsIgnoreCase("AO_Selfie")) {
						aocpCustomer.setAoSelfieeIsLive("Yes");
					}
					else if(documentType.equalsIgnoreCase("RO_Selfie")){
						aocpCustomer.setRoSelfieeIsLive("Yes");
					}
					String saveSingleData = aocpCustomerDataService.saveSingleData(aocpCustomer);
					}
				}
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			String data4 = Data1.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data4, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			System.out.println(data3);
			return new ResponseEntity<Object>(data3, h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	       } else {
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", "SessionId is expired or Invalid sessionId");
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Error", data2);
				return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
			}
	}
	       
}
