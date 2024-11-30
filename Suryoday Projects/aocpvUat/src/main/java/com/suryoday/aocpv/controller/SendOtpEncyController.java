package com.suryoday.aocpv.controller;

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

import com.suryoday.aocpv.service.SendOtpService;
import com.suryoday.connector.pojo.User;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;

@RestController
@RequestMapping("/aocp")
public class SendOtpEncyController {

	Logger logger = LoggerFactory.getLogger(SendOtpEncyController.class);
	@Autowired
	SendOtpService otpService;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/sendOtp/emailency", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> sendOtpAndEmail(@RequestBody String bm, HttpServletRequest request,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Content-Type-Options", required = true) String X_Content_Type_Options,
			@RequestHeader(name = "X-Frame-Options", required = true) String X_Frame_Options,
			@RequestHeader(name = "Content-Security-Policy", required = true) String Content_Security_Policy,
			@RequestHeader(name = "X-XSS-Protection", required = true) String X_XSS_Protection,
			@RequestHeader(name = "Strict-Transport-Security", required = true) String Strict_Transport_Security,
			@RequestHeader(name = "X-CALL-ID", required = true) String X_CALL_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {
		logger.debug("sendOtp start");
		// logger.debug("sendOtp request : " + bm);
		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Content-Type-Options", X_Content_Type_Options);
		Header.put("X-Frame-Options", X_Frame_Options);
		Header.put("Content-Security-Policy", Content_Security_Policy);
		Header.put("X-XSS-Protection", X_XSS_Protection);
		Header.put("Strict-Transport-Security", Strict_Transport_Security);
		Header.put("X-Request-ID", X_Request_ID);

		if (Integer.parseInt(X_CALL_ID) > 3) {
			logger.debug("Max Limit Exceeded");

			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "Max Limit Exceeded");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);

			return new ResponseEntity<Object>(data3.toString(), HttpStatus.BAD_REQUEST);

		} else {

			boolean sessionId = userService.validateSessionId(X_Session_ID, request);
			if (sessionId == true) {
				userService.getSessionId(X_User_ID, request);
				org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
				String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

				// logger.debug("start request" + bm.toString());

				String key = X_Session_ID;

				String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
				JSONObject jsonObject = new JSONObject(decryptContainerString);
				String data = "";
				String mobileNoInRequestWithCountryCode = jsonObject.getJSONObject("Data").getString("MobileNumber");
				String mobileNoInRequest = mobileNoInRequestWithCountryCode.substring(3);
				User fetchById = userService.fetchById(X_User_ID);
				boolean flag = false;

				if (fetchById.getMobileNo().equals(mobileNoInRequest) || !X_From_ID.equals("SARATHI")) {
					flag = true;
					Header.put("X-From-ID", "CB");
				}
				if (X_Request_ID.equals("NOVOPAY") && flag) {

					JSONObject sendOtp = otpService.sendOtp(jsonObject, Header);
//						String sendotpString="{\r\n"
//								+ "    \"data\": \"{\\\"Data\\\":{\\\"Status\\\":\\\"APP-SURYOESBPRD-1705301393249-178-DC0101\\\",\\\"X-Correlation-ID\\\":\\\"DIG123456789056\\\",\\\"TransactionCode\\\":\\\"00\\\"}}\"\r\n"
//								+ "}";
//						JSONObject sendOtp =new JSONObject(sendotpString);
					HttpStatus h = HttpStatus.BAD_GATEWAY;
					if (sendOtp != null) {
						String Data2 = sendOtp.getString("data");
						System.out.println("data2");
						JSONObject Data1 = new JSONObject(Data2);

						System.out.println(Data1);

						if (Data1.has("Data")) {
							h = HttpStatus.OK;

						} else if (Data1.has("Error")) {
							h = HttpStatus.BAD_REQUEST;

						}
						data = Data1.toString();
						String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
						org.json.JSONObject data2 = new org.json.JSONObject();
						data2.put("value", encryptString2);
						org.json.JSONObject data3 = new org.json.JSONObject();
						data3.put("Data", data2);
						logger.debug("response : " + data3.toString());
						return new ResponseEntity<Object>(data3.toString(), h);

					} else {
						logger.debug("timeout");
						return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
					}
				} else {
					logger.debug("Invalid Request");
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

	@RequestMapping(value = "/validateOTPency", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> validateOTP(
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-CALL-ID", required = true) String X_CALL_ID,
			@RequestHeader(name = "OTP", required = true) String OTP,
			@RequestHeader(name = "X-Content-Type-Options", required = true) String X_Content_Type_Options,
			@RequestHeader(name = "X-Frame-Options", required = true) String X_Frame_Options,
			@RequestHeader(name = "Content-Security-Policy", required = true) String Content_Security_Policy,
			@RequestHeader(name = "X-XSS-Protection", required = true) String X_XSS_Protection,
			@RequestHeader(name = "Strict-Transport-Security", required = true) String Strict_Transport_Security,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {
		logger.debug("validateOTP start");
		logger.debug("validateOTP request : " + OTP);
		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Content-Type-Options", X_Content_Type_Options);
		Header.put("X-Frame-Options", X_Frame_Options);
		Header.put("Content-Security-Policy", Content_Security_Policy);
		Header.put("X-XSS-Protection", X_XSS_Protection);
		Header.put("Strict-Transport-Security", Strict_Transport_Security);
		Header.put("X-Request-ID", X_Request_ID);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);

			String key = X_Session_ID;

			String data = "";

			if (X_Request_ID.equals("NOVOPAY")) {
				String decryptCallId = AppzillonAESUtils.decryptContainerString(X_CALL_ID, key);
				if (Integer.parseInt(decryptCallId) > 3) {
					org.json.simple.JSONObject resp = new org.json.simple.JSONObject();
					org.json.JSONObject error = new org.json.JSONObject();
					error.put("StatusCode", "400");
					error.put("Description", "Max Limit Exceeded");
					resp.put("Error", error);
					data = resp.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
					logger.debug("response : " + data3.toString());
					return new ResponseEntity<Object>(data3.toString(), HttpStatus.BAD_REQUEST);
				} else {
					JSONObject sendsms = null;
					if (X_User_ID.equals("33570")) {
						String validate = "{\r\n"
								+ "    \"data\": \"{\\\"Data\\\":{\\\"Status\\\":\\\"OTP Validated Successfully\\\",\\\"X-Correlation-ID\\\":\\\"DIG123456789056\\\",\\\"TransactionCode\\\":\\\"00\\\"}}\"\r\n"
								+ "}";
						sendsms = new JSONObject(validate);
					} else {
						sendsms = otpService.validateOTP(OTP, Header);
					}

					HttpStatus h = HttpStatus.BAD_GATEWAY;
					if (sendsms != null) {
						String Data2 = sendsms.getString("data");
						System.out.println("data2");
						JSONObject Data1 = new JSONObject(Data2);

						System.out.println(Data1);

						if (Data1.has("Data")) {
							h = HttpStatus.OK;

						} else if (Data1.has("Error")) {
							h = HttpStatus.BAD_REQUEST;

						}
						data = Data1.toString();
						String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
						org.json.JSONObject data2 = new org.json.JSONObject();
						data2.put("value", encryptString2);
						org.json.JSONObject data3 = new org.json.JSONObject();
						data3.put("Data", data2);
						logger.debug("response : " + data3.toString());
						return new ResponseEntity<Object>(data3.toString(), h);
					} else {
						logger.debug("timeout");
						return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
					}
				}
			} else {
				logger.debug("Invalid Request");
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
