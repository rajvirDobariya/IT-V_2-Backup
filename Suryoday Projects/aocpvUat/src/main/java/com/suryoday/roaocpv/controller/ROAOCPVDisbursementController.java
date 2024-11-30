package com.suryoday.roaocpv.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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

import com.suryoday.roaocpv.service.ROAOCPVDisbursementService;

@Component
@RestController
@RequestMapping(value = "roaocpv")
public class ROAOCPVDisbursementController {
	@Autowired
	ROAOCPVDisbursementService disbursementservice;
	private static Logger logger = LoggerFactory.getLogger(ROAOCPVDisbursementController.class);

	@RequestMapping(value = "/checkDisbursementStatus", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> checkDisbursementStatus(@RequestBody String bm,
			@RequestHeader(name = "Authorization", required = true) String Authorization,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("Authorization", Authorization);
		Header.put("Content-Type", ContentType);

		JSONObject jsonObject = new JSONObject(bm);
		String branchId = jsonObject.getJSONObject("Data").getString("BranchID");
		String applicationId = jsonObject.getJSONObject("Data").getString("ApplicationID");
		JSONObject checkStatus = disbursementservice.checkDisbursementStatus(branchId, applicationId, Header);

		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (checkStatus != null) {
			String Data2 = checkStatus.getString("data");
			System.out.println("data2");
			System.out.println(Data2);
			JSONArray Resp = new JSONArray(Data2);
			JSONObject Data1 = Resp.getJSONObject(0);
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
