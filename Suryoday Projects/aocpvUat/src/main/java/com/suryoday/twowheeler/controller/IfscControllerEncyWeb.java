package com.suryoday.twowheeler.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.connector.rd.util.Crypt;
import com.suryoday.connector.service.UserService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;

@Controller
@RestController
@RequestMapping("/twowheeler/web")
public class IfscControllerEncyWeb {
	private static Logger logger = LoggerFactory.getLogger(IfscController.class);

	@Autowired
	UserService userService;

	@Autowired
	TwowheelerFamilyMemberService familyMemberService;

	@RequestMapping(value = "/searchIfscEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> searchIfsc(@RequestBody String bm,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "ApplicationNo", required = true) String applicationNo,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Request-ID", X_Request_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("Content-Type", ContentType);

		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {

			JSONObject encryptJSONObject = new JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			String data = "";
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String ifscCode = jsonObject.getJSONObject("Data").getString("IfscCode");
//		JSONObject searchIfsc = ifscservice.searchIfsc(ifscCode ,Header);
			String a = "{\r\n" + "    \"Data\": [\r\n" + "        {\r\n" + "            \"Bank\": {\r\n"
					+ "                \"BranchDesc\": \"SBIN DHULE\",\r\n"
					+ "                \"Description\": \"STATE BANK OF INDIA\",\r\n"
					+ "                \"Address\": {\r\n" + "                    \"City\": \"\"\r\n"
					+ "                },\r\n" + "                \"Code\": \"002\"\r\n" + "            },\r\n"
					+ "            \"TransactionCode\": \"00\",\r\n" + "            \"IfscCode\": \"SBIN0000366\"\r\n"
					+ "        }\r\n" + "    ]\r\n" + "}";
			JSONObject searchIfsc = new JSONObject(a);
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (searchIfsc != null) {
//			String Data2 = searchIfsc.getString("data");
//			System.out.println("data2");
//			System.out.println(Data2);
				JSONObject Data1 = searchIfsc;
				if (Data1.has("Data")) {
					h = HttpStatus.OK;
					JSONObject bankRespJson = Data1.getJSONArray("Data").getJSONObject(0).getJSONObject("Bank");
					System.out.println(bankRespJson);
					String bankName = bankRespJson.getString("Description");
					String branchName = bankRespJson.getString("BranchDesc");
					System.out.println("Bank Name :" + bankName + "BranchName :" + branchName);

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}
				data = Data1.toString();
				String encryptString2 = Crypt.encrypt(data, X_encode_ID);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				logger.debug("response : " + data3.toString());
				return new ResponseEntity<Object>(data3.toString(), h);

			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/pennyDropEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> pennyDrop(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_encode_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {
		logger.debug("pennyDrop start");
		logger.debug("pennyDrop request");
		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {

			JSONObject encryptJSONObject = new JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = Crypt.decrypt(encryptString, X_encode_ID);
			String data = "";
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			if (X_Request_ID.equals("AVAIL")) {
				String accountNo = jsonObject.getJSONObject("Data").getString("AccountNo");
				String ifsc = jsonObject.getJSONObject("Data").getString("IfscCode");
				String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
//		 	String member = jsonObject.getJSONObject("Data").getString("member");
				String bankName = jsonObject.getJSONObject("Data").getString("bankName");

				HttpStatus h = HttpStatus.BAD_REQUEST;
//		 	 TwoWheelerFamilyMember twoWheelerFamilyMember = familyMemberService.getByApplicationNoAndMember(applicationNo, member);
//		 	if(twoWheelerFamilyMember.getAccountNo()!=null && twoWheelerFamilyMember.getAccountNo().equals(accountNo)){
//		 		String pennyDropResponse = twoWheelerFamilyMember.getPennyDropResponse();
//		 		org.json.JSONObject pennyDrop=new org.json.JSONObject(pennyDropResponse);
//		 		data = pennyDrop.toString();
//				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
//				org.json.JSONObject data2 = new org.json.JSONObject();
//				data2.put("value", encryptString2);
//				org.json.JSONObject data3 = new org.json.JSONObject();
//				data3.put("Data", data2);
//				logger.debug("response : " + data3.toString());
//				return new ResponseEntity<Object>(data3.toString(), h.);

//		 	}

//	        JSONObject pennyDrop = ifscservice.pennyDrop(accountNo,ifsc, Header);
				String a = "{\r\n" + "    \"Response\": {\r\n" + "        \"Data\": {\r\n"
						+ "            \"Status\": \"00\",\r\n"
						+ "            \"RequestId\": \"b408ec4f-12d5-4b1d-aad4-f23af2b75ecc\",\r\n"
						+ "            \"StatusCode\": \"101\",\r\n" + "            \"Result\": {\r\n"
						+ "                \"BankResponse\": \"Transaction Successful\",\r\n"
						+ "                \"BankTransactionStatus\": \"true\",\r\n"
						+ "                \"AccountNumber\": \"31326908944\",\r\n"
						+ "                \"IFSC\": \"SBIN0000366\",\r\n"
						+ "                \"AccountName\": \"Mr  AKASH BHAURAO  R\"\r\n" + "            }\r\n"
						+ "        }\r\n" + "    }\r\n" + "}";
				JSONObject pennyDrop = new JSONObject(a);
				if (pennyDrop != null) {
//				 String Data2= pennyDrop.getString("data");
//				 logger.debug("data2");
					JSONObject Data1 = pennyDrop;

					logger.debug(Data1.toString());
//				 JSONObject jsonObject = Data1.getJSONObject("Response").getJSONObject("Data").getJSONObject("Result");
//				 logger.debug("Resp"+jsonObject);
					if (Data1.getJSONObject("Response").has("Data")) {

						JSONObject result = Data1.getJSONObject("Response").getJSONObject("Data")
								.getJSONObject("Result");
//						 twoWheelerFamilyMember.setBankName(bankName);
//						 twoWheelerFamilyMember.setAccountNo(accountNo);
//						 twoWheelerFamilyMember.setIfscCode(result.getString("IFSC"));
//						 twoWheelerFamilyMember.setAccountholderName(result.getString("AccountName"));
//						 twoWheelerFamilyMember.setPennyDropResponse(Data1.toString());
//						 familyMemberService.save(twoWheelerFamilyMember);

						h = HttpStatus.OK;
					} else if (Data1.getJSONObject("Response").has("Error")) {
						h = HttpStatus.BAD_REQUEST;

					}
					logger.debug("response" + Data1);
					data = Data1.toString();
					String encryptString2 = Crypt.encrypt(data, X_encode_ID);
					org.json.JSONObject data2 = new org.json.JSONObject();
					data2.put("value", encryptString2);
					org.json.JSONObject data3 = new org.json.JSONObject();
					data3.put("Data", data2);
					logger.debug("response : " + data3.toString());
					return new ResponseEntity<Object>(data3.toString(), h);

				} else {
					logger.debug("timeout");
					return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
				}
			} else {
				logger.debug("Invalid Request");
				return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);

			}
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
