package com.suryoday.dsaOnboard.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.dsaOnboard.service.DsaAmlReportService;
import com.suryoday.dsaOnboard.service.DsaOnboardMemberService;

@RestController
@RequestMapping("/dsaOnBoard")
public class DsaAmlReportController {

	@Autowired
	DsaOnboardMemberService dsaOnboardMemberService;
	
	@Autowired
	DsaAmlReportService dsaAmlReportService;
	
	@PostMapping(value = "/amlReport", produces = "application/json")
	public ResponseEntity<Object> cfrreport(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {
		JSONObject jsonrequest = new JSONObject(bm);
		String applicationNo = jsonrequest.getJSONObject("Data").getString("applicationNo");
		String member = jsonrequest.getJSONObject("Data").getString("member");
		  
		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-Request-ID", X_Request_ID);
		
		JSONObject jsonObject1 = dsaAmlReportService.getAmlReport(applicationNo,member,Header);
		
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (jsonObject1 != null) {
			String Data2 = jsonObject1.getString("data");
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("Data")) {
				h = HttpStatus.OK;
			} else if (Data1.has("Errors")) {
				h = HttpStatus.BAD_REQUEST;
			}
			return new ResponseEntity<Object>(Data1.toString(), h);
		} else {
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
