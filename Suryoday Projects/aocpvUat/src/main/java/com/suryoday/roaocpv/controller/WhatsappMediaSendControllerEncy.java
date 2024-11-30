package com.suryoday.roaocpv.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
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
import com.suryoday.roaocpv.service.WhatsappMediaSendService;

@RestController
@RequestMapping("/roaocpv")
public class WhatsappMediaSendControllerEncy {

	@Autowired
	WhatsappMediaSendService mediaSendService;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/whatsappMediaSendEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> whatsappMediaSend(@RequestBody String requestBody,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject = new JSONObject(requestBody);

		JSONObject jsonObject1 = mediaSendService.sendMedia(jsonObject, Header);
		JSONObject jsonObject2 = null;
		JSONArray jsonObject3 = null;
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (jsonObject1 != null) {
			String Data2 = jsonObject1.getString("data");
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("Data")) {
				jsonObject2 = Data1.getJSONObject("Data");
				h = HttpStatus.OK;
			} else if (Data1.has("Errors")) {
				jsonObject3 = Data1.getJSONArray("Errors");
				h = HttpStatus.BAD_REQUEST;
				return new ResponseEntity<Object>(jsonObject3.toString(), h);
			}

			return new ResponseEntity<Object>(jsonObject2.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@RequestMapping(value = "/whatsappMessageSendEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> whatsappMessageSend(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject jsonObject1 = mediaSendService.sendMessage(jsonObject, Header);
			JSONObject jsonObject2 = null;
			JSONArray jsonObject3 = null;
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (jsonObject1 != null) {
				String Data2 = jsonObject1.getString("data");
				JSONObject Data1 = new JSONObject(Data2);
				if (Data1.has("Data")) {
					jsonObject2 = Data1.getJSONObject("Data");
					h = HttpStatus.OK;
				} else if (Data1.has("Errors")) {
					jsonObject3 = Data1.getJSONArray("Errors");
					h = HttpStatus.BAD_REQUEST;
					String data = jsonObject3.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
					data2.put("value", encryptString2);
					org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3, h);
				}
				String data = jsonObject2.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
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
