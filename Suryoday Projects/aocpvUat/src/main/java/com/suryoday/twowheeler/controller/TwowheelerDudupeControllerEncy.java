package com.suryoday.twowheeler.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
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
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.pojo.TwowheelerResponse;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerDudupeService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;

@RestController
@RequestMapping("/twowheeler")
public class TwowheelerDudupeControllerEncy {
	@Autowired
	TwowheelerDudupeService twowheelerDudupeService;

	@Autowired
	TwowheelerFamilyMemberService familyMemberService;

	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/dedupeEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> dedupeCall(@RequestBody String bm,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(bm);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject Header = new JSONObject();
			Header.put("X-Correlation-ID", X_CORRELATION_ID);
			Header.put("X-From-ID", X_From_ID);
			Header.put("X-To-ID", X_To_ID);
			Header.put("X-Transaction-ID", X_Transaction_ID);
			Header.put("X-User-ID", X_User_ID);
			Header.put("X-Request-ID", X_Request_ID);

			JSONObject jsonObject = new JSONObject(decryptContainerString);

			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
			String member = jsonObject.getJSONObject("Data").getString("member");

			TwoWheelerFamilyMember familyMember = familyMemberService.getByApplicationNoAndMember(applicationNo,
					member);

			JSONObject dedupeCall = twowheelerDudupeService.dedupeCall(familyMember, Header);

			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (dedupeCall != null) {
				String Data2 = dedupeCall.getString("data");
				JSONObject Data1 = new JSONObject(Data2);

				if (Data1.has("Data")) {
					TwowheelerDetailesTable twowheelerDetailesTable = twowheelerDetailsService
							.getByApplication(applicationNo);
					String matchFlag = Data1.getJSONObject("Data").getString("MatchFlag");
					if (matchFlag.equalsIgnoreCase("100% Match")) {

						String cifNumber = Data1.getJSONObject("Data").getJSONObject("CustomerDetails")
								.getString("CIFNumber");
						String mobNo = Data1.getJSONObject("Data").getJSONObject("CustomerDetails")
								.getString("MobileNo");
						List<TwowheelerResponse> list = twowheelerDetailsService.getbyCustomerId(cifNumber);
						JSONArray externalDedupes = new JSONArray();
						for (TwowheelerResponse twowheelerDetailes : list) {
							JSONObject externalDedupe = new JSONObject();
							if (twowheelerDetailes != null
									&& !twowheelerDetailes.getApplicationNo().equals(applicationNo)) {
								externalDedupe.put("EligibleAmount", twowheelerDetailes.getPreApprovalAmount());
								externalDedupe.put("status", twowheelerDetailes.getStatus());
								externalDedupe.put("name", twowheelerDetailes.getName());
								externalDedupe.put("updatedDate", twowheelerDetailes.getUpdatedTimestamp());
								externalDedupe.put("applicationNo", twowheelerDetailes.getApplicationNo());
								externalDedupes.put(externalDedupe);
							}
						}
						Data1.put("InternalDedupe", externalDedupes);
						twowheelerDetailesTable.setCustomerId(cifNumber);
						familyMember.setCustomerId(cifNumber);
					}

					twowheelerDetailesTable.setFlowStatus("TWDDC");
					twowheelerDetailesTable.setDedupeCheckVerify("YES");
					familyMember.setDedupeResponse(Data1.toString());
					familyMemberService.save(familyMember);
					twowheelerDetailsService.saveData(twowheelerDetailesTable);
					h = HttpStatus.OK;

				} else if (Data1.has("Error")) {
					h = HttpStatus.BAD_REQUEST;

				}

				String data = Data1.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", encryptString2);
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3.toString(), h);

			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		} else {
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Error", data2);
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}
}
