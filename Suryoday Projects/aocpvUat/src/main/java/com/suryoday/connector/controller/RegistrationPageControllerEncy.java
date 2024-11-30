package com.suryoday.connector.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.exceptionhandling.EmptyInputException;
import com.suryoday.connector.pojo.RegistrationPage;
import com.suryoday.connector.service.ConnectorImageService;
import com.suryoday.connector.service.RegistrationService;

@RestController
@RequestMapping(value = "/connector")
public class RegistrationPageControllerEncy {

	RegistrationService registrationService;

	ConnectorImageService connectorImageService;

	Logger logger = LoggerFactory.getLogger(RegistrationPageControllerEncy.class);

	@RequestMapping(value = "/registorPageEncy", method = RequestMethod.POST)
	public ResponseEntity<Object> savePersonalDetailsData(@RequestParam("file") MultipartFile files[],
			@RequestParam("Data") String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		JSONObject jsonObject = new JSONObject(jsonRequest);
		logger.debug("save data start");
		logger.debug("request" + jsonRequest);
		System.out.println("JSON STRING " + jsonObject);

		if (jsonRequest.isEmpty()) {
			logger.debug("request is empty" + jsonRequest);
			throw new EmptyInputException("Input cannot be empty");
		}

		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		Long appln = Long.parseLong(applicationNo);
		String status = jsonObject.getJSONObject("Data").getString("status");
		String flowStatus = jsonObject.getJSONObject("Data").getString("flowStatus");
		String leadNumber = jsonObject.getJSONObject("Data").getString("leadNumber");
		String mobile = jsonObject.getJSONObject("Data").getString("mobile");
		String mobileVerify = jsonObject.getJSONObject("Data").getString("mobileVerify");
		String email = jsonObject.getJSONObject("Data").getString("email");
		String emailVerify = jsonObject.getJSONObject("Data").getString("emailVerify");

		String pancard = jsonObject.getJSONObject("Data").getString("pancard");
		String pancardVerify = jsonObject.getJSONObject("Data").getString("pancardVerify");
		// String pancardResponse =
		// jsonObject.getJSONObject("Data").getString("pancardResponse");

		String gstno = jsonObject.getJSONObject("Data").getString("gstno");
		String gstVerify = jsonObject.getJSONObject("Data").getString("gstVerify");
		// String gstResponse =
		// jsonObject.getJSONObject("Data").getString("gstResponse");

		String aadhaar = jsonObject.getJSONObject("Data").getString("aadhaar");
		String aadhaarVerify = jsonObject.getJSONObject("Data").getString("aadhaarVerify");
		// String aadhaarResponse =
		// jsonObject.getJSONObject("Data").getString("aadhaarResponse");

		String companyName = jsonObject.getJSONObject("Data").getString("companyName");
		String entity = jsonObject.getJSONObject("Data").getString("entity");
		String custname = jsonObject.getJSONObject("Data").getString("custname");
		String dob = jsonObject.getJSONObject("Data").getString("dob");
		String mobileNo2 = jsonObject.getJSONObject("Data").getString("mobileNo2");
		String presentOccupation = jsonObject.getJSONObject("Data").getString("presentOccupation");
		String mobilelinkwithaadhar = jsonObject.getJSONObject("Data").getString("mobilelinkwithaadhar");

		JSONObject addressDetails = jsonObject.getJSONObject("Data").getJSONObject("addressDetails");
		String addressDetailsFlow = jsonObject.getJSONObject("Data").getJSONObject("addressDetails")
				.getString("flowStatus");

		JSONObject bankDetails = jsonObject.getJSONObject("Data").getJSONObject("bankDetails");
		String bankDetailsFlow = jsonObject.getJSONObject("Data").getJSONObject("bankDetails").getString("flowStatus");

		JSONObject othersDetails = jsonObject.getJSONObject("Data").getJSONObject("othersDetails");
		String othersDetailsFlow = jsonObject.getJSONObject("Data").getJSONObject("othersDetails")
				.getString("flowStatus");

		String imageFlowStatus = jsonObject.getJSONObject("Data").getJSONObject("images").getString("flowStatus");

		String imageFlow = null;

		RegistrationPage registrationPage = new RegistrationPage();
		registrationPage.setApplicationNo(appln);
		registrationPage.setStatus(status);

		if (entity.isEmpty() && mobile.isEmpty() && mobileVerify.isEmpty() && companyName.isEmpty()
				&& presentOccupation.isEmpty() && mobile.isEmpty() && mobileVerify.isEmpty() && mobileNo2.isEmpty()
				&& email.isEmpty() && emailVerify.isEmpty() && mobilelinkwithaadhar.isEmpty() && dob.isEmpty()
				&& custname.isEmpty()) {

			registrationPage.setFlowStatus("Progress");
		} else {
			registrationPage.setFlowStatus("BD");
		}

		if (addressDetailsFlow.equals("AD")) {
			registrationPage.setFlowStatus("AD");
		} else {
			registrationPage.setFlowStatus("BD");
		}

		if (bankDetailsFlow.equals("BKND")) {
			registrationPage.setFlowStatus("BKND");
		} else {
			registrationPage.setFlowStatus("AD");
		}

		if (othersDetailsFlow.equals("OD")) {
			registrationPage.setFlowStatus("OD");
		} else {
			registrationPage.setFlowStatus("BKND");
		}

		if (othersDetailsFlow.equals("OD")) {
			registrationPage.setFlowStatus("OD");
			imageFlow = "OD";
		} else {
			registrationPage.setFlowStatus("BKND");
		}

		if (imageFlowStatus.equals("UDD")) {
			registrationPage.setFlowStatus("UDD");
		} else {
			registrationPage.setFlowStatus("OD");
		}

		if ((status.equals("")) || (status.equals(null))) {
			registrationPage.setStatus("Initiated");
		} else {
			registrationPage.setStatus(status);
		}

		registrationPage.setMobile(mobile);
		registrationPage.setMobileVerify(mobileVerify);
		registrationPage.setEmail(email);
		registrationPage.setEmailVerify(emailVerify);
		registrationPage.setEntity(entity);
		if (entity.equals("Individual")) {
			registrationPage.setPancard(pancard);
			registrationPage.setPancardVerify(pancardVerify);

			registrationPage.setGstno("");
			registrationPage.setGstVerify("");
		} else {
			registrationPage.setPancard("");
			registrationPage.setPancardVerify("");

			registrationPage.setGstno(gstno);
			registrationPage.setGstVerify(gstVerify);
		}
		registrationPage.setAadhaar(aadhaar);
		registrationPage.setAadhaarVerify(aadhaarVerify);
		registrationPage.setCompanyName(companyName);
		registrationPage.setCustname(custname);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dateOfBirth = LocalDate.parse(dob, formatter);
		registrationPage.setDob(dateOfBirth);
		registrationPage.setMobileNo2(mobileNo2);
		registrationPage.setMobilelinkwithaadhar(mobilelinkwithaadhar);
		registrationPage.setPresentOccupation(presentOccupation);

		registrationPage.setAddressDetails(addressDetails.toString());
		registrationPage.setBankDetails(bankDetails.toString());
		registrationPage.setOthersDetails(othersDetails.toString());

		registrationPage.setLeadNumber(leadNumber);

		org.json.JSONArray document = jsonObject.getJSONObject("Data").getJSONArray("document");

		String msgImage = connectorImageService.saveAllImages(files, appln, document, imageFlow);

		List<RegistrationPage> listRegistration = registrationService.getReveiewDetails(appln, imageFlow);

		registrationService.saveAllDetails(registrationPage);

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("message", "Data Save Successfully  &  " + msgImage);
		response.put("status", HttpStatus.OK.toString());
		response.put("Data", listRegistration);
		logger.debug("final response" + response.toString());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

}
