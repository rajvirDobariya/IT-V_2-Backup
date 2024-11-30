package com.suryoday.aocpv.web;

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

import com.suryoday.connector.rd.util.Crypt;
import com.suryoday.connector.service.UserService;
import com.suryoday.connector.service.VoterIDService;

@RestController
@RequestMapping("/aocpv/web")
public class VoterIdControllerWebEncy {
	
	@Autowired
	VoterIDService voterIDService;
	
	@Autowired
	UserService userService;
	
	private static Logger logger = LoggerFactory.getLogger(VoterIdControllerWebEncy.class);

	@RequestMapping(value = { "/voterIdEncy" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public ResponseEntity<Object> voterId(@RequestBody String bm,
			@RequestHeader(name = "X-karza-key", required = true) String X_KARZA_KEY,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			HttpServletRequest request,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID) throws Exception {
		JSONObject Header = new JSONObject();
		Header.put("X-karza-key", X_KARZA_KEY);
		logger.debug("POST Request : " + bm);
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		    if (sessionId == true ) {
			 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		String key = X_Session_ID;
		String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
				 String voterId = jsonObject.getString("epic_no");
				
				JSONObject voterID = voterIDService.voterID(jsonObject, Header);
				logger.debug("response from voterId API : " + voterID);

		//System.out.println(jsonObject.toString());
		
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (voterID != null) {
			String Data2 = voterID.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);
			logger.debug("JSON Object : " + Data2);
			if (Data1.has("status-code"))
				if (Data1.getString("status-code").equals("103")) {
					h = HttpStatus.BAD_REQUEST;
				} else {
					h = HttpStatus.OK;
				}
			logger.debug("Main Response : " + Data1.toString());
			String	data = Data1.toString();
	 		String encryptString2 = Crypt.encrypt(data, X_encode_ID);
	 		org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
	 		data2.put("value", encryptString2);
	 		org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
	 		data3.put("Data", data2);
	 		return new ResponseEntity<Object>(data3, h);
		}
		logger.debug("GATEWAY_TIMEOUT");
		return new ResponseEntity("timeout", HttpStatus.GATEWAY_TIMEOUT);
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
