package com.suryoday.goNogo.controller;

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
import com.suryoday.goNogo.service.GoNogoCustDetailsService;

@RestController
@RequestMapping("/goNogo")
public class GoNoGoSendOtpController {

	@Autowired
	SendOtpService otpService;

	@Autowired
	GoNogoCustDetailsService goNogoCustDetailsService;

	Logger logger = LoggerFactory.getLogger(GoNoGoSendOtpController.class);

	@RequestMapping(value = "/sendOtp/email", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> sendOtpAndEmail(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID, HttpServletRequest request,
			@RequestHeader(name = "X-CALL-ID", required = true) String X_CALL_ID,
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

		if (Integer.parseInt(X_CALL_ID) > 3) {
			logger.debug("Max Limit Exceeded");

			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "Max Limit Exceeded");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);

			return new ResponseEntity<Object>(data3.toString(), HttpStatus.BAD_REQUEST);

		} else {

			if (X_Request_ID.equals("NOVOPAY")) {
				JSONObject jsonObject = new JSONObject(bm);

				String mobile = jsonObject.getJSONObject("Data").getString("MobileNumber");

				JSONObject sendOtp = otpService.sendOtp(jsonObject, Header);

				HttpStatus h = HttpStatus.BAD_GATEWAY;
				if (sendOtp != null) {
					String Data2 = sendOtp.getString("data");
					JSONObject Data1 = new JSONObject(Data2);

					if (Data1.has("Data")) {
						h = HttpStatus.OK;
//							dsaOnBoardDetailsService.validateMobileNo(mobile);
					} else if (Data1.has("Error")) {
						h = HttpStatus.BAD_REQUEST;

					}

					return new ResponseEntity<Object>(Data1.toString(), h);

				} else {
					logger.debug("timeout");
					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			} else {
				logger.debug("Invalid Request");
				return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);

			}

		}

	}

	@RequestMapping(value = "/validateOTP", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> validateOTP(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
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

		if (X_Request_ID.equals("NOVOPAY")) {
			JSONObject jsonObject = new JSONObject(jsonRequest);

			String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
			String OTP = jsonObject.getJSONObject("Data").getString("OTP");

			JSONObject sendsms = otpService.validateOTP(OTP, Header);
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (sendsms != null) {
				String Data2 = sendsms.getString("data");

				JSONObject Data1 = new JSONObject(Data2);

				System.out.println(Data1);

				if (Data1.has("Data")) {
					String applicationNo = goNogoCustDetailsService.createLead(mobileNo);
					Data1.getJSONObject("Data").put("applicationNo", applicationNo);
					h = HttpStatus.OK;

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}
				return new ResponseEntity<Object>(Data1.toString(), h);
			} else {
				logger.debug("timeout");
				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		} else {
			logger.debug("Invalid Request");
			return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);

		}

	}
}
