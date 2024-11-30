package com.suryoday.aocpv.controller;

import java.util.List;

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

import com.suryoday.aocpv.pojo.AocpvPerposecategory;
import com.suryoday.aocpv.service.AocpvPerposecategoryService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;

@RestController
@RequestMapping("/aocpv")
public class AocpvPerposecategoryEncyController {

	@Autowired
	AocpvPerposecategoryService aocpvPerposecategoryService;

	@Autowired
	UserService userService;

	private static Logger logger = LoggerFactory.getLogger(AocpvPerposecategoryEncyController.class);

	@RequestMapping(value = "/fetchByCategoryIdEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fecthByCategoryId(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID, HttpServletRequest request) {

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			logger.debug("start request" + jsonRequest.toString());

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			String data = "";
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String categoryId = jsonObject.getString("categoryId");
			List<AocpvPerposecategory> listOfCategory = aocpvPerposecategoryService.fetchByCategoryId(categoryId);
			if (listOfCategory.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			org.json.JSONObject data1 = new org.json.JSONObject();
			data1.put("Data", listOfCategory);
			data = data1.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", encryptString2);
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Data", data2);
			logger.debug("response : " + data3.toString());

			return new ResponseEntity<>(data3.toString(), HttpStatus.OK);

		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

}
