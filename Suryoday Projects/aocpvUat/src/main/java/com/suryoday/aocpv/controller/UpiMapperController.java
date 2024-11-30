package com.suryoday.aocpv.controller;

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

import com.suryoday.aocpv.service.AocpCustomerDataService;

@Component
@RestController
@RequestMapping(value = "/aocpv")
public class UpiMapperController {

	@Autowired
	AocpCustomerDataService aocpvCustomerService;

	@RequestMapping(value = "/upiMapper", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> upiMapper(@RequestBody String bm,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "ApplicationNo", required = true) String applicationNo,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-Request-ID", X_Request_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);

		JSONObject jsonObject = new JSONObject(bm);
		String MobileNo = jsonObject.getJSONObject("Data").getString("MobileNo");
		JSONObject request = new JSONObject();
		JSONObject Data = new JSONObject();
		Data.put("ClientReferenceId", "2023092109062712581821");
		Data.put("MerchantId", "MER0000000000002");
		Data.put("MerchantVpa", "bhavanimedicalstore@suryoday");
		Data.put("UPINumber", MobileNo);
		request.put("Data", Data);
//		JSONObject upiMapper = upimapperservice.upiMapper(request ,Header);
		JSONObject upiMapper = null;
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (upiMapper == null) {
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

				org.json.simple.JSONObject resp = new org.json.simple.JSONObject();
				org.json.simple.JSONObject respData = new org.json.simple.JSONObject();
				respData.put("virtualAddress", upaAddress);
				respData.put("verifiedName", upaName);
				respData.put("clientReferenceId", clientRefId);
				respData.put("transactionId", transactionId);
				if (upaName.equals("")) {
					respData.put("upiVerify", "NO");
				} else {
					respData.put("upiVerify", "YES");
				}
				resp.put("Data", respData);
				return new ResponseEntity<Object>(resp.toString(), h);
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(Data1.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
