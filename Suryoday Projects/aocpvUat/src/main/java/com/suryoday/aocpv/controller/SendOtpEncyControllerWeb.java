package com.suryoday.aocpv.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
import com.suryoday.connector.rd.util.Crypt;
import com.suryoday.connector.service.UserService;

@RestController
@RequestMapping("/aocpv/Web")
public class SendOtpEncyControllerWeb {

	Logger logger = LoggerFactory.getLogger(SendOtpEncyControllerWeb.class);
	@Autowired
	SendOtpService otpService;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/sendOtpEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> sendOtpAndEmail(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID, HttpServletRequest request,
			@RequestHeader(name = "X-CALL-ID", required = true) String X_CALL_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) throws Exception {

		logger.debug("sendOtp start");
		logger.debug("sendOtp request : " + bm);
		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		User fetchById = userService.fetchById(X_User_ID);

		if (fetchById.getLockingEndTime() != null && fetchById.getLockingEndTime().isBefore(LocalDateTime.now())) {
			fetchById.setSmscallId(0);
			fetchById.setSendSmscallId(0);
			fetchById.setLockingEndTime(null);
		}
		if (fetchById.getSendSmscallId() != null && fetchById.getSendSmscallId() >= 3
				|| fetchById.getSmscallId() != null && fetchById.getSmscallId() >= 3) {

			long seconds = LocalDateTime.now().until(fetchById.getLockingEndTime(), ChronoUnit.SECONDS);
			org.json.JSONObject errorResp = new org.json.JSONObject();
			errorResp.put("Description", "Max Limit Exceeded Please try after " + seconds + " seconds");
			errorResp.put("Code", "Error");
			errorResp.put("X-Correlation-ID", X_CORRELATION_ID);
			errorResp.put("timeLeft", seconds);
			org.json.JSONObject resp = new org.json.JSONObject();
			resp.put("Error", errorResp);
			resp.put("statusCode", "400");
			String data = resp.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", encryptString2);
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Data", data2);
			logger.debug("response : " + data3.toString());
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
		} else if (fetchById.getLockingEndTimeSendSms() != null
				&& fetchById.getLockingEndTimeSendSms().isAfter(LocalDateTime.now())) {
			long seconds = LocalDateTime.now().until(fetchById.getLockingEndTimeSendSms(), ChronoUnit.SECONDS);
			org.json.JSONObject errorResp = new org.json.JSONObject();
			Integer sendSmscallId = fetchById.getSendSmscallId();
			int a = 3 - sendSmscallId;
			errorResp.put("Description", a + " Attempt Left Please try after " + seconds + " seconds");
			errorResp.put("Code", "Error");
			errorResp.put("X-Correlation-ID", X_CORRELATION_ID);
			errorResp.put("timeLeft", seconds);
			org.json.JSONObject resp = new org.json.JSONObject();
			resp.put("Error", errorResp);
			resp.put("statusCode", "400");
			String data = resp.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", encryptString2);
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Data", data2);
			logger.debug("response : " + data3.toString());
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
		}

		else {

//			boolean sessionId = userService.validateSessionId(X_Session_ID, request);
//				String encodedId=userService.validateUser(X_User_ID);
			boolean sessionId = true;
			if (sessionId) {
				JSONObject encryptJSONObject = new JSONObject(bm);
				String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

				String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);

				JSONObject jsonObject = new JSONObject(decryptContainerString);
				logger.debug("request" + jsonObject);
				String mobileNoInRequestWithCountryCode = jsonObject.getJSONObject("Data").getString("MobileNumber");
				String mobileNoInRequest = mobileNoInRequestWithCountryCode.substring(2);

				if (!fetchById.getMobileNo().equals(mobileNoInRequest)) {
					org.json.JSONObject errorResp = new org.json.JSONObject();

					errorResp.put("Description", "Wrong Mobile Number");
					errorResp.put("Code", "Error");
					errorResp.put("X-Correlation-ID", X_CORRELATION_ID);
					org.json.JSONObject resp = new org.json.JSONObject();
					resp.put("Error", errorResp);
					resp.put("statusCode", "400");
					String data = resp.toString();
					String encryptString2 = Crypt.encrypt(data, X_encode_ID);
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
					logger.debug("response : " + data3.toString());
					return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);

				}

				JSONObject sendOtp = otpService.sendOtp(jsonObject, Header);
//					String sendotpString="{\r\n"
//							+ "    \"data\": \"{\\\"Data\\\":{\\\"Status\\\":\\\"APP-SURYOESBPRD-1705301393249-178-DC0101\\\",\\\"X-Correlation-ID\\\":\\\"DIG123456789056\\\",\\\"TransactionCode\\\":\\\"00\\\"}}\"\r\n"
//							+ "}";
//					JSONObject sendOtp =new JSONObject(sendotpString);
				HttpStatus h = HttpStatus.BAD_GATEWAY;
				if (sendOtp != null) {
					String Data2 = sendOtp.getString("data");
					JSONObject Data1 = new JSONObject(Data2);
					logger.debug("response" + Data1);
					if (Data1.has("Data")) {
						h = HttpStatus.OK;
						Data1.getJSONObject("Data").put("statusCode", h);
						Data1.getJSONObject("Data").put("mobile",
								mobileNoInRequestWithCountryCode.replaceAll("\\w(?=\\w{4})", "X"));
						if (fetchById.getSendSmscallId() == null) {
							fetchById.setSendSmscallId(0);
						}
						int smscallId = fetchById.getSendSmscallId();
						smscallId++;
						if (smscallId == 3) {
							fetchById.setLockingStartTime(LocalDateTime.now());
							fetchById.setLockingEndTime(LocalDateTime.now().plus(5, ChronoUnit.MINUTES));
						} else {
							fetchById.setLockingEndTimeSendSms(LocalDateTime.now().plusMinutes(1));
						}
						fetchById.setSendSmscallId(smscallId);

					} else if (Data1.has("Error")) {
						h = HttpStatus.BAD_REQUEST;
						Data1.getJSONObject("Error").put("statusCode", h);
						Data1.getJSONObject("Data").put("mobile",
								mobileNoInRequestWithCountryCode.replaceAll("\\w(?=\\w{4})", "X"));
					}
					userService.save(fetchById);
					String data = Data1.toString();
					String encryptString2 = Crypt.encrypt(data, X_encode_ID);
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
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", "SessionId is expired or Invalid sessionId");
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Error", data2);
				logger.debug("SessionId is expired or Invalid sessionId");
				return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
			}

		}

	}

	@RequestMapping(value = "/validateOTPEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> validateOTP(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-CALL-ID", required = true) String X_CALL_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Content-Type-Options", required = true) String X_Content_Type_Options,
			@RequestHeader(name = "X-Frame-Options", required = true) String X_Frame_Options,
			@RequestHeader(name = "Content-Security-Policy", required = true) String Content_Security_Policy,
			@RequestHeader(name = "X-XSS-Protection", required = true) String X_XSS_Protection,
			@RequestHeader(name = "Strict-Transport-Security", required = true) String Strict_Transport_Security,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		logger.debug("validateOTP start");
		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Frame-Options", X_Frame_Options);
		Header.put("Content-Security-Policy", Content_Security_Policy);
		Header.put("X-XSS-Protection", X_XSS_Protection);
		Header.put("Strict-Transport-Security", Strict_Transport_Security);
		Header.put("X-Content-Type-Options", X_Content_Type_Options);
		Header.put("X-Request-ID", X_Request_ID);

		User fetchById = userService.fetchById(X_User_ID);
		if (fetchById.getLockingEndTime() != null && fetchById.getLockingEndTime().isBefore(LocalDateTime.now())) {
			fetchById.setSmscallId(0);
			fetchById.setLockingEndTime(null);
		}
		if (fetchById.getSmscallId() != null && fetchById.getSmscallId() >= 3) {

			long seconds = LocalDateTime.now().until(fetchById.getLockingEndTime(), ChronoUnit.SECONDS);
			org.json.JSONObject errorResp = new org.json.JSONObject();
			errorResp.put("Description", "Max Limit Exceeded Please try after " + seconds + " seconds");
			errorResp.put("Code", "Error");
			org.json.JSONObject resp = new org.json.JSONObject();
			resp.put("Error", errorResp);
			resp.put("statusCode", "400");
			String data = resp.toString();
			String encryptString2 = Crypt.encrypt(data, X_encode_ID);
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", encryptString2);
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Data", data2);
			logger.debug("response : " + data3.toString());
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.OK);
		}
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			JSONObject encryptJSONObject = new JSONObject(jsonRequest);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);

			JSONObject jsonObject = new JSONObject(decryptContainerString);

			String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
			String OTP = jsonObject.getJSONObject("Data").getString("OTP");

			JSONObject sendsms = null;
//				if(X_User_ID.equals("92485") || X_User_ID.equals("92271") || X_User_ID.equals("92331") || X_User_ID.equals("38339") || X_User_ID.equals("32697") || X_User_ID.equals("90929") || X_User_ID.equals("92113")) {
//					String validate="{\r\n"
//							+ "    \"data\": \"{\\\"Data\\\":{\\\"Status\\\":\\\"OTP Validated Successfully\\\",\\\"X-Correlation-ID\\\":\\\"DIG123456789056\\\",\\\"TransactionCode\\\":\\\"00\\\"}}\"\r\n"
//							+ "}";
//							 sendsms = new JSONObject(validate);	
//				}
//				else {
			sendsms = otpService.validateOTP(OTP, Header);
//				}

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (sendsms != null) {
				String Data2 = sendsms.getString("data");

				JSONObject Data1 = new JSONObject(Data2);

				System.out.println(Data1);

				if (Data1.has("Data")) {
//						String applicationNo = dsaOnBoardDetailsService.createLead(mobileNo);
					h = HttpStatus.OK;
					Data1.put("statusCode", "200");
					fetchById.setSmscallId(0);
					fetchById.setSendSmscallId(0);
				} else if (Data1.has("Error")) {
					h = HttpStatus.OK;
					Data1.put("statusCode", "400");
					if (fetchById.getSmscallId() == null) {
						fetchById.setSmscallId(0);
					}
					int smscallId = fetchById.getSmscallId();
					smscallId++;
					if (smscallId == 3) {
						fetchById.setLockingStartTime(LocalDateTime.now());
						fetchById.setLockingEndTime(LocalDateTime.now().plusMinutes(5));
					}
					fetchById.setSmscallId(smscallId);
					int a = 3 - smscallId;
					Data1.getJSONObject("Error").put("Description", "OTP Validation Failed " + a + " Attempt Left");
				}
				userService.save(fetchById);
				String data = Data1.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
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
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}

	}

}
