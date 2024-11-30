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
import com.suryoday.connector.service.PanCardConsumingService;
import com.suryoday.connector.service.UserService;

@RestController
@RequestMapping(value = "/aocpv/web")
public class PanCardConsumingControllerWebEncy {

	@Autowired
	PanCardConsumingService panCardConsumingService;

	@Autowired
	UserService userService;
	
	private static Logger logger = LoggerFactory.getLogger(PanCardConsumingControllerWebEncy.class);

	@RequestMapping(value = "/panCardValidateEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> panCardValidation(@RequestBody String json,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);

			String key = X_Session_ID;

			String data = "";

			if (X_Request_ID.equals("IEXCEED")) {
				JSONObject jsonObject=new JSONObject(json);
				JSONObject jsonRequest = jsonObject.getJSONArray("Data").getJSONObject(0);
				String panCardNo = jsonRequest.getString("Pan");
				 JSONObject panCardValidate = panCardConsumingService.panCardValidation(jsonObject, Header);
			        logger.debug("Response from the API",panCardValidate);
					HttpStatus  h=HttpStatus.BAD_REQUEST;
				if (panCardValidate != null) {
					 String Data2= panCardValidate.getString("data");
					 JSONObject Data1= new JSONObject(Data2);
					 
					 logger.debug("JSON Object from Response",Data2);

					if (Data1.has("Data")) {
						JSONObject responseResponse = Data1.getJSONArray("Data").getJSONObject(0).getJSONArray("OutputData").getJSONObject(0);
						 if(responseResponse.getString("PanStatus").equals("E")) {
							 h=HttpStatus.OK;
						 }
					}

					logger.debug("Main Response from API : " + Data1.toString());
					data = Data1.toString();
					String encryptString2 = Crypt.encrypt(data, X_encode_ID);
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
			} else {
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
