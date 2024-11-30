package com.suryoday.twowheeler.controller;

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

import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.service.SPDCApiService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;

@Component
@RestController
@RequestMapping(value = "/twowheeler")
public class SPDCApiController {

	private static Logger logger = LoggerFactory.getLogger(SPDCApiController.class);

	@Autowired
	SPDCApiService spdcservice;

	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;

	@RequestMapping(value = "/spdc", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> spdc(@RequestBody String bm,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "ApplicationNo", required = true) String applicationNo,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Request-ID", X_Request_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("Content-Type", ContentType);

		JSONObject jsonObject = new JSONObject(bm);
		JSONObject spdc = spdcservice.spdcManagement(jsonObject, Header);
		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		twowheelerDetails.setSecurityPdcDetails(jsonObject.toString());

		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (spdc != null) {
			String Data2 = spdc.getString("data");
			System.out.println("data2");
			System.out.println(Data2);
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("Data")) {
				h = HttpStatus.OK;
				String referenceNumber = Data1.getJSONObject("Data").getString("ReferenceNumber");
				twowheelerDetails.setSpdcReferenceNo(referenceNumber);
				twowheelerDetailsService.saveData(twowheelerDetails);
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
