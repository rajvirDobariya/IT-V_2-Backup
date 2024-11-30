package com.suryoday.roaocpv.controller;

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
import com.suryoday.roaocpv.service.ApplicationDetailsService;
import com.suryoday.roaocpv.service.ROAOCPVVoterIDService;

@RestController
@RequestMapping("/roaocpv")
public class ROAOCPVVoterIDEncyController {

	@Autowired
	ROAOCPVVoterIDService voterIDService;

	@Autowired
	UserService userService;

	@Autowired
	ApplicationDetailsService applicationDetailsService;

	private static Logger logger = LoggerFactory.getLogger(ROAOCPVVoterIDEncyController.class);

	@RequestMapping(value = { "/voterIdency" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public ResponseEntity<Object> voterId(@RequestBody String bm,
			@RequestHeader(name = "X-karza-key", required = true) String X_KARZA_KEY,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "applicationNo", required = true) String applicationNo,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {
		boolean sessionId = this.userService.validateSessionId(X_Session_ID, request);
		if (sessionId) {
			JSONObject encryptJSONObject = new JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			logger.debug("start request" + bm.toString());
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			String data = "";
			JSONObject Header = new JSONObject();
			Header.put("X-karza-key", X_KARZA_KEY);
			logger.debug("POST Request : " + decryptContainerString);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			System.out.println(jsonObject.toString());
			jsonObject = jsonObject.getJSONObject("Data");
			String voterId = jsonObject.getString("epic_no");
			JSONObject voterID = this.voterIDService.voterID(jsonObject, Header);
			logger.debug("response from voterId API : " + voterID);
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
						JSONObject voterRes = Data1.getJSONObject("result");
						applicationDetailsService.saveData("voterId", voterId, applicationNo, voterRes.toString());
					}

				logger.debug("Main Response : " + Data1.toString());
				data = Data1.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				JSONObject jSONObject1 = new JSONObject();
				jSONObject1.put("value", encryptString2);
				JSONObject jSONObject2 = new JSONObject();
				jSONObject2.put("Data", jSONObject1);
				logger.debug("response : " + jSONObject2.toString());
				return new ResponseEntity(jSONObject2.toString(), h);
			}
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
		JSONObject data2 = new JSONObject();
		data2.put("value", "SessionId is expired or Invalid sessionId");
		JSONObject data3 = new JSONObject();
		data3.put("Error", data2);
		logger.debug("SessionId is expired or Invalid sessionId");
		return new ResponseEntity(data3.toString(), HttpStatus.UNAUTHORIZED);
	}

}
