package com.suryoday.dsaOnboard.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.dsaOnboard.pojo.DsaOnboardMember;
import com.suryoday.dsaOnboard.service.DsaOnboardMemberService;
import com.suryoday.roaocpv.service.ROAOCPVCfrService;

@RestController
@RequestMapping("/dsaOnBoard")
public class DsaCfrReportControllerEncy {

	@Autowired
	ROAOCPVCfrService roaocpvCfrService;

	@Autowired
	DsaOnboardMemberService dsaOnboardMemberService;

	@Autowired
	UserService userService;

	@PostMapping(value = "/cfrreportEncy", produces = "application/json")
	public ResponseEntity<Object> cfrreport(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonrequest = new JSONObject(decryptContainerString);
			String applicationNo = jsonrequest.getJSONObject("Data").getString("applicationNo");
			String member = jsonrequest.getJSONObject("Data").getString("member");
			DsaOnboardMember dsaOnboardMember = dsaOnboardMemberService.getByApplicationnoAndMember(applicationNo,
					member);

			JSONObject Header = new JSONObject();
			Header.put("X-Correlation-ID", X_CORRELATION_ID);
			Header.put("X-From-ID", X_From_ID);
			Header.put("X-Request-ID", X_Request_ID);

			JSONObject jsonObject1 = roaocpvCfrService.Cfrcase(dsaOnboardMember.getPanCardNo(), Header);
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
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3.toString(), h);
				}
				String data = jsonObject2.toString();
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
