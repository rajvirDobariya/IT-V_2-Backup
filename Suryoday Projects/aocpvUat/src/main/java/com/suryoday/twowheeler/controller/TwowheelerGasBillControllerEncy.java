package com.suryoday.twowheeler.controller;

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
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerGasBillService;

@RestController
@RequestMapping("/twowheeler")
public class TwowheelerGasBillControllerEncy {

	@Autowired
	TwowheelerGasBillService twowheelerGasBillService;

	@Autowired
	UserService userService;

	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;

	private static Logger logger = LoggerFactory.getLogger(TwowheelerGasBillControllerEncy.class);

	@RequestMapping(value = "/gasBillVerifyEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> gasBillVerify(@RequestBody String request,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "ApplicationNo", required = true) String applicationNo,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(request);
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
			if (twowheelerDetails.getUtilityBillIsVerify() != null
					&& twowheelerDetails.getUtilityBillIsVerify().equalsIgnoreCase("YES")) {
				String utilityBillResponse = twowheelerDetails.getUtilityBillResponse();
				org.json.JSONObject utilityResponse = new org.json.JSONObject(utilityBillResponse);

				String data = utilityResponse.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				logger.debug(data3.toString());
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
			}

			JSONObject gasBillVerify = twowheelerGasBillService.gasBillVerify(jsonObject);
			logger.debug("response from gasBillVerify" + gasBillVerify);

			HttpStatus h = HttpStatus.BAD_REQUEST;
			if (gasBillVerify != null) {
				String Data2 = gasBillVerify.getString("data");

				JSONObject Data1 = new JSONObject(Data2);
				logger.debug("JSON Object " + Data2);
				String statusCode = Data1.getString("status-code");
				if (statusCode.equals("101")) {
					h = HttpStatus.OK;
				} else {
					org.json.simple.JSONObject errorResponse = new org.json.simple.JSONObject();
					org.json.simple.JSONObject error = new org.json.simple.JSONObject();

					String errorcoede = Data1.getString("status-code");

					if (errorcoede.equals("102")) {
						error.put("Description", "Invalid ID number or combination of inputs");
					} else if (errorcoede.equals("103")) {
						error.put("Description", "No records found for the given ID or combination of inputs");
					} else if (errorcoede.equals("104")) {
						error.put("Description", "Max retries exceeded");
					} else if (errorcoede.equals("105")) {
						error.put("Description", "Missing Consent");
					} else if (errorcoede.equals("106")) {
						error.put("Description", "Multiple Records Exist");
					} else if (errorcoede.equals("107")) {
						error.put("Description", "Not Supported");
					} else {
						error.put("Description", "");
					}

					error.put("Code", errorcoede);
					errorResponse.put("Error", error);

					twowheelerDetails.setUtilityBillIsVerify("NO");

					String saveSingleData = twowheelerDetailsService.saveData(twowheelerDetails);

					String data = errorResponse.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
					data2.put("value", encryptString2);
					org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
					data3.put("Data", data2);
					logger.debug(data3.toString());
					return new ResponseEntity<Object>(data3, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				logger.debug("Main Response" + Data1.toString());
				twowheelerDetails.setUtilityBillIsVerify("YES");
				twowheelerDetails.setUtilityBillResponse(Data1.toString());
				twowheelerDetails.setUtilityBillNo(jsonObject.getString("lpg_id"));
				twowheelerDetails.setServiceProvider("");
				twowheelerDetails.setUtilityBill("GAS BILL");
				twowheelerDetails.setServiceProviderCode("");
				twowheelerDetails.setFlowStatus("TWUBC");
				String saveSingleData = twowheelerDetailsService.saveData(twowheelerDetails);
				String data = Data1.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);
			} else {
				logger.debug("GATEWAY_TIMEOUT");
				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
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
