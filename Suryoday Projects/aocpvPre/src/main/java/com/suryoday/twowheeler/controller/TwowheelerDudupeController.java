package com.suryoday.twowheeler.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.pojo.PreApprovedListTwoWheeler;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.service.PreApprovalListService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerDudupeService;

@RestController
@RequestMapping("/twowheeler")
public class TwowheelerDudupeController {
	@Autowired
	TwowheelerDudupeService twowheelerDudupeService;
	
	@Autowired
	PreApprovalListService loanInputService;
	
	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;
	
	@RequestMapping(value = "/dedupe", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> dedupeCall(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {
		
		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", X_CORRELATION_ID);
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject = new JSONObject(bm);
		System.out.println(jsonObject.toString());
		JSONObject dedupeCall = twowheelerDudupeService.dedupeCall(jsonObject, Header);
		
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");

		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (dedupeCall != null) {
			String Data2 = dedupeCall.getString("data");
			JSONObject Data1 = new JSONObject(Data2);
			JSONObject jsonObject2 = Data1.getJSONObject("Error");
			org.json.JSONObject Data4 = new org.json.JSONObject();
			JSONObject jsonObject3 = null;
			if (!jsonObject2.has("Description")) {
				jsonObject3 = Data1.getJSONObject("Data");
				Data4.put("Data", jsonObject3);
				Data4.put("Error", jsonObject2.toString());
			} else {
				Data4.put("Error", jsonObject2);
			}

			if (Data1.has("Data")) {
				String matchFlag = Data1.getJSONObject("Data").getString("MatchFlag");
				if(matchFlag.equalsIgnoreCase("100% Match")) {
					JSONObject jsonForCIF = new JSONObject();
					
					String cifNumber = Data1.getJSONObject("Data").getJSONObject("CustomerDetails").getString("CIFNumber");
					long customerNo = Long.parseLong(cifNumber);
					 PreApprovedListTwoWheeler loanDetails = loanInputService.fetchByCustomerID(customerNo);
					if(loanDetails != null) {
						double amount = loanDetails.getAmount();
						Data4.put("Eligible Amount", amount);
						Data4.put("status", loanDetails.getStatus());
						Data4.put("updatedDate", loanDetails.getUpdatedate());
					}
					
					jsonForCIF.put("customerId", cifNumber);
//					twowheelerDetailsService.updateCustomerId(applicationNo,cifNumber);
			
				}
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}
			TwowheelerDetailesTable twowheelerDetailesTable = twowheelerDetailsService.getByApplication(applicationNo);
			twowheelerDetailesTable.setDudupeResponse(Data4.toString());
			twowheelerDetailesTable.setFlowStatus("TWDDC");
			twowheelerDetailsService.saveData(twowheelerDetailesTable);
			return new ResponseEntity<Object>(Data4.toString(), h);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
		
	}
}
