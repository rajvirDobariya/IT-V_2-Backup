package com.suryoday.aocpv.controller;

import javax.servlet.http.HttpServletRequest;

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

import com.suryoday.aocpv.service.PushNotificationService;

@Component
@RestController
@RequestMapping(value = "aocpv")
public class PushNotificationController {
	Logger logger = LoggerFactory.getLogger(PushNotificationController.class);
	@Autowired
	PushNotificationService pushnotificationservice;

	@RequestMapping(value = "/sendNotification", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> sendNotification(@RequestBody String bm,
			@RequestHeader(name = "Authorization-key", required = true) String Authorization_key,
			@RequestHeader(name = "Content-Type", required = true) String Content_Type, HttpServletRequest req)
			throws Exception {
		logger.debug("sendNotification start");
		logger.debug("sendNotification request" + bm);
		JSONObject Header = new JSONObject();
		Header.put("Authorization-key", Authorization_key);
		Header.put("Content-Type", Content_Type);

		JSONObject jsonObject = new JSONObject(bm);
		System.out.println(jsonObject.toString());
		JSONObject sendOtp = pushnotificationservice.sendNotification(jsonObject, Header);

		HttpStatus h = HttpStatus.OK;
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
			logger.debug("response" + Data1);
			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {
			logger.debug("timeout");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
