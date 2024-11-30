package com.suryoday.twowheeler.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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

import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.twowheeler.pojo.MandateDetails;
import com.suryoday.twowheeler.service.NachMandateService;

@RestController
@RequestMapping("/twowheeler")
public class TwowheelerMandateControllerEncy {

	@Autowired
	private NachMandateService nachMandateService;

	@Autowired
	UserService userService;

	private static Logger logger = LoggerFactory.getLogger(TwowheelerMandateControllerEncy.class);

	@RequestMapping(value = "/nachMandateCreationEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> mandateCreation(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "applicationNo", required = true) String applicationNo,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
//		 JSONObject nachMandate=nachMandateService.mandateCreation(jsonObject);
//		 String data = nachMandate.getString("data");
			String data = "{\r\n" + "    \"Data\": {\r\n"
					+ "        \"TimeStamp\": \"2023-01-19T06:52:30.357064Z\",\r\n" + "        \"Status\": \"200\",\r\n"
					+ "        \"Code\": \"201\",\r\n" + "        \"Message\": \"228a232c3da366444555599\",\r\n"
					+ "        \"MoreInfo\": \"mandate request created.\",\r\n"
					+ "        \"Reference\": \"321a669f-6749-46df-8cf3-cf16cdf26904\"\r\n" + "    }\r\n" + "}";
			JSONObject Data1 = new JSONObject(data);
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (Data1.has("Data")) {
				h = HttpStatus.OK;
				String reference = Data1.getJSONObject("Data").getString("Reference");
				nachMandateService.savemandate(jsonObject, applicationNo, reference);
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;
			}
			logger.debug(Data1.toString());
			String data1 = Data1.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data1, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchMandateEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchMandate(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "applicationNo", required = true) String applicationNo,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
//		 JSONObject nachMandate=nachMandateService.fetchMandate(jsonObject);		
//		 String data = nachMandate.getString("data");
			String referenceNo = jsonObject.getJSONObject("Data").getString("ReferenceNumber");
			String data = "{\r\n" + "    \"Data\": {\r\n"
					+ "        \"TimeStamp\": \"2023-04-25T08:10:09.336854Z\",\r\n" + "        \"Status\": \"200\",\r\n"
					+ "        \"Code\": \"200\",\r\n" + "        \"ReferenceNumber\": \"NACH0016673456734444343\",\r\n"
					+ "        \"UtilityCode\": " + referenceNo + ",\r\n" + "        \"CategoryCode\": \"L001\",\r\n"
					+ "        \"SchemeName\": \"TWO-WHEELER-LOAN\",\r\n"
					+ "        \"ConsumerReferenceNumber\": \"LN100033\",\r\n"
					+ "        \"MandateType\": \"RCUR\",\r\n" + "        \"Frequency\": \"MNTH\",\r\n"
					+ "        \"FirstCollectionDate\": \"2021-10-25\",\r\n"
					+ "        \"FinalCollectionDate\": \"2030-10-25\",\r\n" + "        \"AmountType\": \"MAXA\",\r\n"
					+ "        \"CollectionAmount\": 10,\r\n" + "        \"CustomerName\": \"Abhinav\",\r\n"
					+ "        \"Landline\": \"\",\r\n" + "        \"MobileNumber\": \"9723481316\",\r\n"
					+ "        \"EmailId\": \"yashpandit1599@gmail.com\",\r\n" + "        \"Pan\": \"ABCDE0011A\",\r\n"
					+ "        \"BankId\": \"KKBK\",\r\n" + "        \"AccountType\": \"SAVINGS\",\r\n"
					+ "        \"AccountNumber\": \"73136237432\",\r\n"
					+ "        \"RequestStatus\": \"Initiated\",\r\n" + "        \"MntdId\": null,\r\n"
					+ "        \"ReasonCode\": null,\r\n" + "        \"ReasonDescription\": null,\r\n"
					+ "        \"Surl\": \"https://sandbox.pcrp.in/tMIGCw\",\r\n"
					+ "        \"MoreInfo\": \"mandate request found.\",\r\n"
					+ "        \"Reference\": \"29023f22-088f-4c4d-9774-01b1201a3eda\"\r\n" + "    }\r\n" + "}";
			JSONObject Data1 = new JSONObject(data);
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (Data1.has("Data")) {
				h = HttpStatus.OK;
//			 String referenceNo = jsonObject.getJSONObject("Data").getString("ReferenceNumber");
				MandateDetails mandateDetails = nachMandateService.getByApplicationNoAndReference(applicationNo,
						referenceNo);
//				mandateDetails.setRequestStatus(Data1.getJSONObject("Data").getString("RequestStatus"));
				mandateDetails.setRequestStatus("APPROVED");
				nachMandateService.save(mandateDetails);
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;
			}
			logger.debug(Data1.toString());

			String data1 = Data1.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data1, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/fetchMandateDetailsEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> fetchMandateDetails(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-Correlation-ID", required = true) String headerPersist,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID, HttpServletRequest request,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID) {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationno = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			List<MandateDetails> mandateDtls = nachMandateService.fetchMandateDetails(applicationno);
			JSONArray array = new JSONArray(mandateDtls);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("Data", array);
			String data = response.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
			org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
			data2.put("value", encryptString2);
			org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
			data3.put("Data", data2);
			return new ResponseEntity<Object>(data3, HttpStatus.OK);
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
}
