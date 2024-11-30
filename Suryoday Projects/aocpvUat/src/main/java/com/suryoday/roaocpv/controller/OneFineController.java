package com.suryoday.roaocpv.controller;

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

import com.suryoday.roaocpv.service.OneFineService;

@Component
@RestController
@RequestMapping(value = "roaocpv")
public class OneFineController {
	@Autowired
	OneFineService onefineservice;
	private static Logger logger = LoggerFactory.getLogger(OneFineController.class);

	@RequestMapping(value = "/checkOneFineStatus", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> checkOneFineStatus(@RequestBody String bm,
			@RequestHeader(name = "Authorization", required = true) String Authorization,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("Authorization", Authorization);
		Header.put("Content-Type", ContentType);

		JSONObject jsonObject = new JSONObject(bm);
		String leadId = jsonObject.getString("LeadId");
		System.out.println(leadId);
		JSONObject checkOneFineStatus = onefineservice.checkOneFineStatus(leadId, Header);

		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (checkOneFineStatus != null) {
			String Data2 = checkOneFineStatus.getString("data");
			System.out.println("data2");
			System.out.println(Data2);
			JSONObject Data1 = new JSONObject(Data2);
			System.out.println(Data1);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", Data1);

			if (Data1.has("Data")) {
				h = HttpStatus.OK;

				return new ResponseEntity<Object>(Data1.toString(), h);
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(response.toString(), h.OK);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
