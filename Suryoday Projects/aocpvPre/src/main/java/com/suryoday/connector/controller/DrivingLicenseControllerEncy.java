package com.suryoday.connector.controller;

import javax.servlet.http.HttpServletRequest;

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

import com.suryoday.aocpv.service.AocpvIncomeService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.service.ROAOCPVDrivingLicenseService;


@Component
@RestController
@RequestMapping(value="/connector/v1")
public class DrivingLicenseControllerEncy {
	
	@Autowired
	ROAOCPVDrivingLicenseService drivinglicenseservice;
	
	@Autowired
	AocpvIncomeService incomeDetailsService;
	
	@Autowired 
	UserService userService;

	private static Logger logger = LoggerFactory.getLogger(DrivingLicenseControllerEncy.class);
	
	@RequestMapping(value = "/authenticateDrivingLicenseEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> authenticateDrivingLicenseEncy(@RequestBody String bm,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type,
			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			 @RequestHeader(name = "applicationNo", required = true) String applicationNo,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			 @RequestHeader(name = "member", required = true) String member,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("Content-Type", Content_Type);
		Header.put("X-Request-ID", X_Request_ID);
		logger.debug("POST Request" + bm);
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		  if (sessionId == true ) {
			  userService.getSessionId(X_User_ID, req); 
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				//System.out.println(encryptString);
		//logger.debug("start request" + jsonRequest.toString());

		String key = X_Session_ID;

		String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
	//	logger.debug("PD request"+decryptContainerString);
			JSONObject jsonObject=new JSONObject(decryptContainerString);
			String drivingLicenseNo = jsonObject.getJSONObject("Data").getString("DrivingLicenseNumber");
			String dob=jsonObject.getJSONObject("Data").getString("DOB");
			System.out.println(jsonObject.toString());
//			JSONObject authenticateDrivingLicense = drivinglicenseservice.authenticateDrivingLicense(drivingLicenseNo,dob,Header);
			JSONObject authenticateDrivingLicense=drivinglicenseservice.getDrivingLicenseDetails(drivingLicenseNo,dob);
			logger.debug("response from verifyPassport" + authenticateDrivingLicense);

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (authenticateDrivingLicense != null) {
				String Data2 = authenticateDrivingLicense.getString("data");
				System.out.println("data2");
				JSONObject Data1 = new JSONObject(Data2);
				logger.debug("JSON Object " + Data2);
				org.json.simple.JSONObject response=new org.json.simple.JSONObject();
				response.put("Data",Data1);
				if (Data1.has("Data")) {
					h = HttpStatus.OK;
					incomeDetailsService.saveDrivingLicense(Data1.toString(),applicationNo,member);
//					applicationDetailsService.saveData("drivingLicense", drivingLicenseNo, applicationNo,Data1.toString());
					String	data = Data1.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3.toString(), h.OK);

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}
				String	data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3.toString(), h);
			} else {
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
}
