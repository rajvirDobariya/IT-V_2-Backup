package com.suryoday.hastakshar.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.connector.rd.util.Crypt;
import com.suryoday.connector.service.UserService;
import com.suryoday.hastakshar.service.ReqStatusService;

@RestController
@RequestMapping("/hastakshar/web")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SaveHastRequestDataWebEncy {

	@Autowired
	ReqStatusService reqstatusservice;
	@Autowired
	UserService userService;
	private static Logger logger = LoggerFactory.getLogger(SaveHastRequestDataWebEncy.class);

	@PostMapping(value = "/saveNewRequestEncy", produces = "application/json")
	public ResponseEntity<Object> saveNewRequestEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId =true;
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject SavedData = reqstatusservice.saveNewRequestData(jsonObject, X_User_ID);
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", SavedData);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/fetchToptenEncy", produces = "application/json")
	public ResponseEntity<Object> fetchToptenEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId=true;
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONArray fetchList = reqstatusservice.fetchList(jsonObject);
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", fetchList);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/fetchBySearchEncy", produces = "application/json")
	public ResponseEntity<Object> fetchBySearchEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId =true;
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONArray getResponse = reqstatusservice.fetchBySearch(jsonObject);
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", getResponse);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/fetchByDateEncy", produces = "application/json")
	public ResponseEntity<Object> fetchByDateEncy(@RequestBody String requestBody,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(requestBody);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId =true;
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);

			JSONArray getResponse = reqstatusservice.fetchByDate(jsonObject);
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", getResponse);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/fetchByStatusEncy", produces = "application/json")
	public ResponseEntity<Object> fetchByStatusEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId = true;
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONArray getResponse = reqstatusservice.fetchByStatus(jsonObject);
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", getResponse);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/updateStatusEncy", produces = "application/json")
	public ResponseEntity<Object> updateStatusEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId =true;
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject getResponse = reqstatusservice.updateStatus(jsonObject, X_User_ID);
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", getResponse);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/fetchUserLogEncy", produces = "application/json")
	public ResponseEntity<Object> fetchUserLogEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId = true;
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONArray getResponse = reqstatusservice.fetchUserLog(jsonObject);
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", getResponse);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/saveAttachmentEncy", produces = "application/json")
	public ResponseEntity<Object> saveAttachmentEncy(@RequestParam("Data") String jsonRequest,
			@RequestParam("file") List<MultipartFile> files,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId = true;
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject getResponse = reqstatusservice.saveAttachment(jsonObject, files);
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", getResponse);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/fetchAttachmentEncy", produces = "application/json")
	public ResponseEntity<Object> fetchAttachmentEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject getResponse = reqstatusservice.fetchAttachment(jsonObject);
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", getResponse);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/fetchStatusByEmpIDEncy", produces = "application/json")
	public ResponseEntity<Object> fetchStatusByEmpIDEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId = true;
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONArray getResponse = reqstatusservice.fetchStatusByEmpID(jsonObject);
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", getResponse);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/reAssignApproverEncy", produces = "application/json")
	public ResponseEntity<Object> reAssignApproverEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId = true;
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject getResponse = reqstatusservice.reAssignApprover(jsonObject);
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", getResponse);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/getApprovalOfProxyEncy", produces = "application/json")
	public ResponseEntity<Object> getApprovalOfProxyEncy(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			JSONObject getResponse = reqstatusservice.getApprovalOfProxy(jsonObject);
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", getResponse);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
}
