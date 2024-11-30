package com.suryoday.twowheeler.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.twowheeler.service.TWBreService;

@Component
@RestController
@RequestMapping(value = "/twowheeler")
public class TWBreController {

	@Autowired
	TWBreService twbreservice;

	@RequestMapping(value = "/BreLoan", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> TWBreLoan(@RequestBody String bm,
			@RequestHeader(name = "Authorization", required = true) String Authorization, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("Authorization", Authorization);
		JSONObject jsonObject = new JSONObject(bm);

		JSONObject crmModification = twbreservice.TWbreLoan(jsonObject, Header);
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (crmModification != null) {
			String Data2 = crmModification.getString("data");
			System.out.println("data2");
			System.out.println(Data2);
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("Data")) {
				h = HttpStatus.OK;
				return new ResponseEntity<Object>(crmModification.toString(), h);
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;
			}
			return new ResponseEntity<Object>(Data1.toString(), h);
		} else {
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
