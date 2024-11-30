package com.suryoday.dsaOnboard.controller;

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
import com.suryoday.dsaOnboard.service.DsaCibilReportService;

@RestController
@RequestMapping("/dsaOnBoard")
public class DsaCibilReportControllerEncy {

	@Autowired
	DsaCibilReportService dsaCibilReportService;
	
	@Autowired
	UserService userService;
	
	private static Logger logger = LoggerFactory.getLogger(DsaCibilReportControllerEncy.class);
	
	
	@RequestMapping(value = "/cibilReportEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> createAadharReference(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, 
			HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("api_key", "kyqak5muymxcrjhc5q57vz9v");
	
		if (X_Request_ID.equals("DSA")) {
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			boolean sessionId = userService.validateSessionId(X_Session_ID, request);
			    if (sessionId == true ) {
				 String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString,X_encode_ID);
				JSONObject jsonObject = new JSONObject(decryptContainerString);
			 String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			 String member = jsonObject.getJSONObject("Data").getString("member");
			JSONObject cibilReport = dsaCibilReportService.getCibilReport(jsonObject, Header);

			logger.debug("response from cibilReport" + cibilReport);

			HttpStatus h = HttpStatus.OK;
			if (cibilReport != null) {
				String Data2 = cibilReport.getString("data");

				JSONObject Data1 = new JSONObject(Data2);
//				dsaOnboardMemberService.saveResponse("gst", jsonObjectData.getString("gstin"), applicationNo,
//						Data1.toString(), member);
				String data = Data1.toString();
				String encryptString2 = Crypt.encrypt(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				logger.debug("response : " + data3.toString());
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
		} else {
			logger.debug("INVALID REQUEST");
			return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);

		}

	}
}
