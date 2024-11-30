package com.suryoday.roaocpv.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
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

import com.suryoday.roaocpv.service.IndividualClientDetailsService;

@RestController
@RequestMapping(value = "roaocpv")
@Component
public class IndividualClientDetailsController {

	private static Logger logger = LoggerFactory.getLogger(IndividualClientDetailsController.class);
	@Autowired
	IndividualClientDetailsService indivialClientDetailsService;

	@RequestMapping(value = "/indivialClientDetails", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> indivialClientDetails(@RequestBody String bm,
			@RequestHeader(name = "Authorization", required = true) String Authorization,
			@RequestHeader(name = "Content-Type", required = true) String ContentType, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("Authorization", Authorization);
		Header.put("Content-Type", ContentType);

		JSONObject jsonObject = new JSONObject(bm);

		String branchId = jsonObject.getJSONObject("Data").getString("BranchID");
		String clientId = jsonObject.getJSONObject("Data").getString("ClientID");
		JSONObject clientDetails = indivialClientDetailsService.indivialClientDetails(branchId, clientId, Header);

		HttpStatus h = HttpStatus.BAD_GATEWAY;
		if (clientDetails != null) {
			String Data2 = clientDetails.getString("data");
			System.out.println("data2");
			System.out.println(Data2);
			JSONArray Resp = new JSONArray(Data2);
			JSONObject Data1 = Resp.getJSONObject(0);
			Object personalDetailsXml = Data1.get("PersonalDetails");
			JSONObject personalDetailsJson = XML.toJSONObject(personalDetailsXml.toString());
			Object addressDetailsXml = Data1.get("AddressDetails");
			JSONObject addressDetailsJson = XML.toJSONObject(addressDetailsXml.toString());
			Object relationalDetailsXml = Data1.get("RelationalDetails");
			JSONObject relationalDetailsJson = XML.toJSONObject(relationalDetailsXml.toString());
			Object bankDetailsXml = Data1.get("BankAccountDetails");
			JSONObject bankDetailsJson = XML.toJSONObject(bankDetailsXml.toString());
			Object employmentDetailsXml = Data1.get("EmploymentDetails");
			JSONObject employmentDetailsJson = XML.toJSONObject(employmentDetailsXml.toString());
			Object clientOtherDetailsXml = Data1.get("ClientOtherDetails");
			JSONObject clientOtherDetailsJson = XML.toJSONObject(clientOtherDetailsXml.toString());
			Object familyFinInfoDetailsXml = Data1.get("FamilyFinInfoDetails");
			JSONObject familyFinInfoDetailsJson = XML.toJSONObject(familyFinInfoDetailsXml.toString());
			Object familyRelationDetailsXml = Data1.get("FamilyRelationDetails");
			JSONObject familyRelationDetailsJson = XML.toJSONObject(familyRelationDetailsXml.toString());
			System.out.println(Data1);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			JSONObject resp = new JSONObject();
			resp.put("PersonalDetails", personalDetailsJson.getJSONObject("PersonalDetails"));
			resp.put("AddressDetails", addressDetailsJson.getJSONObject("AddressDetails"));
			resp.put("RelationalDetails", relationalDetailsJson.getJSONObject("RelationDetails"));
			resp.put("BankAccountDetails", bankDetailsJson.getJSONObject("BankAccountDetails"));
			resp.put("EmploymentDetails", employmentDetailsJson.getJSONObject("EmploymentDetails"));
			resp.put("ClientOtherDetails", clientOtherDetailsJson.getJSONObject("ClientOtherDetails"));
			resp.put("FamilyFinInfoDetails", familyFinInfoDetailsJson.getJSONObject("FamilyFinInfoDetails"));
			resp.put("FamilyRelationDetails", familyRelationDetailsJson.getJSONObject("FamilyRelationDetails"));
			resp.put("Status", "1");
			resp.put("ErrorCode", "");
			resp.put("Response", "true");
			resp.put("ResponseMsg", "");
			response.put("Data", resp);
//			response.put("Data",Data1);

			if (Data1.has("Data")) {
				h = HttpStatus.OK;

				return new ResponseEntity<Object>(Data1.toString(), h);
			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}

			return new ResponseEntity<Object>(response.toString(), h.OK);

		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}
}
