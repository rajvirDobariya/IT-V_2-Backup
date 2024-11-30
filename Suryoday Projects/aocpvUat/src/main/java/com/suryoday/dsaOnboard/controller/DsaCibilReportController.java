package com.suryoday.dsaOnboard.controller;

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

import com.suryoday.dsaOnboard.service.DsaCibilReportService;

@RestController
@RequestMapping("/dsaOnBoard")
public class DsaCibilReportController {

	@Autowired
	DsaCibilReportService dsaCibilReportService;

	private static Logger logger = LoggerFactory.getLogger(DsaCibilReportController.class);

	@RequestMapping(value = "/cibilReport", method = RequestMethod.POST)
	public ResponseEntity<Object> createAadharReference(@RequestBody String request,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("api_key", "kyqak5muymxcrjhc5q57vz9v");

		if (X_Request_ID.equals("DSA")) {
			JSONObject jsonObject = new JSONObject(request);
			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String member = jsonObject.getJSONObject("Data").getString("member");
			JSONObject cibilReport = dsaCibilReportService.getCibilReport(jsonObject, Header);

			logger.debug("response from cibilReport" + cibilReport);

			HttpStatus h = HttpStatus.OK;
			if (cibilReport != null) {
				String Data2 = cibilReport.getString("data");

				JSONObject Data1 = new JSONObject(Data2);
//				dsaOnboardMemberService.saveResponse("gst", jsonObjectData.getString("gstin"), applicationNo,
//						Data1.toString(), member);

				return new ResponseEntity<Object>(Data1.toString(), h);
			} else {
				logger.debug("GATEWAY_TIMEOUT");
				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		} else {
			logger.debug("INVALID REQUEST");
			return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);

		}

	}
}
