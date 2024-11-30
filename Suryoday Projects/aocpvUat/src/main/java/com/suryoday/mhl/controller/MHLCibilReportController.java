package com.suryoday.mhl.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.mhl.pojo.CibilEqifaxDetails;
import com.suryoday.mhl.pojo.MemberManagement;
import com.suryoday.mhl.service.MHLCibilReportService;
import com.suryoday.mhl.service.MemberManagementService;

@RestController
@RequestMapping("/mhl/uidauth")
public class MHLCibilReportController {

	@Autowired
	MHLCibilReportService mHLCibilReportService;

	@Autowired
	MemberManagementService memberManagementService;

	private static Logger logger = LoggerFactory.getLogger(MHLCibilReportController.class);

	@RequestMapping(value = "/cibilReport", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getCibilReport(@RequestBody String parent) {

		logger.debug("POST Request : " + parent);
		JSONObject jsonObject = new JSONObject(parent);
		String applicationNo = jsonObject.getString("applicationNo");
		JSONArray array = jsonObject.getJSONArray("memberId");
		MemberManagement member = null;
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		List<MemberManagement> listOfMemberManagements = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			Object object = array.get(i);
			String memberId = object.toString();
			if (memberId != null) {
				long MemberIdInLong = Long.parseLong(memberId);

				member = memberManagementService.fetchByApplicationAndMemId(applicationNo, MemberIdInLong);
				String personalDetails = mHLCibilReportService.getPersonalDetails(member);

				String mainRequest = mHLCibilReportService.getJsonRequest(personalDetails);

				logger.debug("MainRequest Sent To API" + mainRequest);

				String cibilReport = mHLCibilReportService.getCibilReport(mainRequest);
				logger.debug("Response From the API : " + cibilReport);

				org.json.JSONObject jsonConverted1 = XML.toJSONObject(cibilReport);
				String response = jsonConverted1.toString();
				logger.debug("XML Data Converted To JSON : " + jsonConverted1);
				logger.debug("JSONString : " + response);

				JSONObject jsonResponse = new JSONObject(response);
				System.out.println(jsonResponse);
				if (!jsonResponse.equals(null)) {

					if (jsonResponse.has("soapenv:Envelope")) {
						h = HttpStatus.OK;
						String cibilReportHtmlString = jsonResponse.getJSONObject("soapenv:Envelope")
								.getJSONObject("soapenv:Body").getJSONObject("sch:InquiryResponse")
								.getJSONObject("sch:HtmlReportResponse").getString("sch:Content");
						CibilEqifaxDetails cibilEqifaxDetails = new CibilEqifaxDetails();
						cibilEqifaxDetails.setBureauReportResponse(cibilReportHtmlString);
						cibilEqifaxDetails.setApplicationNo(applicationNo);
						cibilEqifaxDetails.setMemberId(memberId);
						LocalDateTime now = LocalDateTime.now();
						cibilEqifaxDetails.setUpdateDate(now);
						mHLCibilReportService.saveCibilReportData(cibilEqifaxDetails);

					} else if (jsonResponse.has("Description")) {
						h = HttpStatus.BAD_REQUEST;
					}

				}
			}
			listOfMemberManagements.add(member);

		}
		if (listOfMemberManagements.isEmpty()) {
			org.json.JSONObject response = new org.json.JSONObject();
			org.json.JSONObject success = new org.json.JSONObject();
			success.put("Value", "Error ");
			response.put("Data", success);
			logger.debug("Main Response : " + response.toString());
			return new ResponseEntity<Object>(response.toString(), h);
		}
		org.json.JSONObject response = new org.json.JSONObject();
		org.json.JSONObject success = new org.json.JSONObject();
		success.put("Value", "Successfully Stored Data");
		response.put("Data", success);
		logger.debug("Main Response : " + response.toString());
		return new ResponseEntity<>(response.toString(), h);

	}

}
