package com.suryoday.roaocpv.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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

import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.roaocpv.service.ROAOCPVLoanCreationService;

@Component
@RestController
@RequestMapping(value = "roaocpv")
public class ROAOCPVLoanCreationControllerEncy {

	@Autowired
	ROAOCPVLoanCreationService loancreationservice;

	@Autowired
	UserService userService;

	private static Logger logger = LoggerFactory.getLogger(ROAOCPVLoanCreationControllerEncy.class);

	@RequestMapping(value = "/loanCreationEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> loanCreation(@RequestBody String bm,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "Authorization", required = true) String Authorization,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject Header = new JSONObject();
			Header.put("Authorization", Authorization);
			Header.put("Content-Type", ContentType);

			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			JSONObject clientCreation = loancreationservice.loanCreation(applicationNo, Header);

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (clientCreation != null) {
				String Data2 = clientCreation.getString("data");
				System.out.println("data2");
				System.out.println(Data2);
				JSONArray Resp = new JSONArray(Data2);
				JSONObject Data1 = Resp.getJSONObject(0);
				System.out.println(Data1);
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("Data", Data1);

				if (Data1.has("Data")) {
					h = HttpStatus.OK;
					String data = Data1.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3.toString(), h);

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}
				String data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3.toString(), h);

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

	@RequestMapping(value = "/statuscheckEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> statuscheck(@RequestBody String bm,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "Authorization", required = true) String Authorization,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject Header = new JSONObject();
			Header.put("Authorization", Authorization);
			Header.put("Content-Type", ContentType);

			String branchId = jsonObject.getJSONObject("Data").getString("BranchId");
			String applicationId = jsonObject.getJSONObject("Data").getString("ApplicationId");
			JSONObject statuscheck = loancreationservice.statuscheck(branchId, applicationId, Header);

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (statuscheck != null) {
				String Data2 = statuscheck.getString("data");
				System.out.println("data2");
				System.out.println(Data2);
				JSONArray Resp = new JSONArray(Data2);
				JSONObject Data1 = Resp.getJSONObject(0);
				System.out.println(Data1);
				org.json.simple.JSONObject response = new org.json.simple.JSONObject();
				response.put("Data", Data1);

				if (Data1.has("Data")) {
					h = HttpStatus.OK;
					String data = Data1.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3.toString(), h);
				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}
				String data = response.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3.toString(), h);

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
