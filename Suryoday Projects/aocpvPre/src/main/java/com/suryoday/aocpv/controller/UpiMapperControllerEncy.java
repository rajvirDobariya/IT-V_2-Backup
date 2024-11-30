
package com.suryoday.aocpv.controller;

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

import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.UpiMapperService;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;

@Component
@RestController
@RequestMapping(value = "/aocpv")
public class UpiMapperControllerEncy {
	@Autowired
	UpiMapperService upimapperservice;
	@Autowired
	UserService userService;
	@Autowired
	AocpCustomerDataService aocpvCustomerService;
	private static Logger logger = LoggerFactory.getLogger(UpiMapperControllerEncy.class);

	@RequestMapping(value = "/upiMapperEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> upiMapper(@RequestBody String bm,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "ApplicationNo", required = true) String applicationNo,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Request-ID", X_Request_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
		boolean sessionId = userService.validateSessionId(X_Session_ID, req);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, req);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");

			String key = X_Session_ID;

			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String MobileNo = jsonObject.getJSONObject("Data").getString("MobileNo");
			JSONObject request=new JSONObject();
			JSONObject Data=new JSONObject();
			Data.put("ClientReferenceId","2023092109062712581821");
			Data.put("MerchantId","MER0000000000002");
			Data.put("MerchantVpa","bhavanimedicalstore@suryoday");
			Data.put("UPINumber",MobileNo);
			request.put("Data",Data);
			JSONObject upiMapper = upimapperservice.upiMapper(request, Header);

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (upiMapper != null) {
				String Data2 = upiMapper.getString("data");
				System.out.println("data2");
				System.out.println(Data2);
				JSONObject Data1 = new JSONObject(Data2);
				if (Data1.has("Data")) {
					h = HttpStatus.OK;
					String upaAddress = Data1.getJSONObject("Data").getString("VirtualAddress");
					String upaName = Data1.getJSONObject("Data").getString("VerifiedName");
					String clientRefId = Data1.getJSONObject("Data").getString("ClientReferenceId");
					String transactionId = Data1.getJSONObject("Data").getString("TransactionId");
//					AocpCustomer aocpvCustomer = aocpvCustomerService.getByApplicationNo(Long.parseLong(applicationNo));
//					aocpvCustomer.setUpaResponse(Data1.toString());
//					aocpvCustomer.setUpaAddress(upaAddress);
//					aocpvCustomerService.saveSingleData(aocpvCustomer);
					org.json.simple.JSONObject resp=new org.json.simple.JSONObject();
					org.json.simple.JSONObject respData=new org.json.simple.JSONObject();
					respData.put("virtualAddress",upaAddress);
					respData.put("verifiedName",upaName);
					respData.put("clientReferenceId",clientRefId);
					respData.put("transactionId",transactionId);
					if(upaName.equals("")) {
						respData.put("upiVerify","NO");
						}else {
							respData.put("upiVerify","YES");
						}
					resp.put("Data",respData);
					String data = resp.toString();
					String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
					org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
					data2.put("value", encryptString2);
					org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
					data3.put("Data", data2);
					return new ResponseEntity<Object>(data3, h);
				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}

				String data = Data1.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				
				return new ResponseEntity<Object>(data3, h);

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
}
