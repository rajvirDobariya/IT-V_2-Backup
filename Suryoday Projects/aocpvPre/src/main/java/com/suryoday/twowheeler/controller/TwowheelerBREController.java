package com.suryoday.twowheeler.controller;

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

import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.service.TwowheelerBREService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;

@Component
@RestController
@RequestMapping(value="/twowheeler")
public class TwowheelerBREController {
	
	@Autowired
	TwowheelerBREService bresService;
	
	@Autowired
	TwowheelerFamilyMemberService familymemberService;
	
	private static Logger logger = LoggerFactory.getLogger(TwowheelerBREController.class);
	
	@RequestMapping(value = "/checkstatus", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> checkstatus(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {
		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);
		JSONObject jsonObject = new JSONObject(bm);
		String applicationNo=jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String member = jsonObject.getJSONObject("Data").getString("Member");
		TwoWheelerFamilyMember familyMember= familymemberService.getByApplicationNoAndMember(applicationNo,member);
		String breStatus="Approved";//INITIATED , APPROVED, REJECT
		if(familyMember.getLeadId() == null) {
			 JSONObject createLead = bresService.createLead(applicationNo,member, Header);
			 HttpStatus h = HttpStatus.OK;
				if (createLead != null) {
					String Data2 = createLead.getString("data");
					logger.debug("data2"+Data2);
					JSONObject Data1 = new JSONObject(Data2);

					logger.debug(Data1.toString());

					if (Data1.has("data")) {
						h = HttpStatus.OK;
						String leadID = Data1.getJSONObject("data").getString("lead_id");
						if(familyMember.getLeadId() == null) {
							familyMember.setLeadId(leadID);
							familyMember.setIsLeadCreated("YES");
							familyMember.setIsBreRuring("NO");
							familyMember.setBreStatus("INITIATED");
							familymemberService.saveData(familyMember);
						}
					} 
					else if (Data1.has("error")) {
						String leadId = Data1.getJSONObject("error").getJSONObject("details").getString("lead_id");
						familyMember.setLeadId(leadId);
						familyMember.setIsLeadCreated("YES");
						familyMember.setIsBreRuring("NO");
						familyMember.setBreStatus("INITIATED");
						familymemberService.saveData(familyMember);
						

					}
				}
				else {

						return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
					}
		
		}
		else if(familyMember.getIsBreRuring().equalsIgnoreCase("YES") && familyMember.getBreStatus().equals("INITIATED"))
		{
			JSONObject fetchBre = bresService.fetchBre(Header,familyMember.getLeadId());
			HttpStatus h = HttpStatus.OK;
			if (fetchBre != null) {
				String Data2 = fetchBre.getString("data");
				logger.debug("data2");
				JSONObject Data1 = new JSONObject(Data2);
				
				if (Data1.has("data")) {
					h = HttpStatus.OK;
					String status = Data1.getJSONObject("data").getString("status");
					JSONObject breResp = Data1.getJSONObject("data").getJSONObject("bre_response");
					if(status.equalsIgnoreCase("Approved"))
					{
						familyMember.setBreResponse(breResp.toString());
						Object eligibilityAmount = breResp.getJSONObject("LOAN_ELIGIBILITY_AMOUNT").getJSONObject("derived_values").get("LOAN_ELIGIBILITY_AMOUNT_FROM_GRID");
						familyMember.setMaxEmiEligibility(eligibilityAmount.toString());
						familyMember.setBreStatus(status);
						familyMember.setBreResponse(breResp.toString());
						familymemberService.save(familyMember);
					}
					else if(status.equalsIgnoreCase("Rejected"))
					{
						familyMember.setBreStatus(status);
						familyMember.setBreResponse(breResp.toString());
						familymemberService.saveData(familyMember);
					}
				}else if (Data1.has("error")) {
					h = HttpStatus.BAD_REQUEST;
					return new ResponseEntity<Object>(Data1.toString(),HttpStatus.OK);
				}
				
			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
		org.json.simple.JSONObject  response= new org.json.simple.JSONObject();
		response.put("LeadCreated", familyMember.getIsLeadCreated());
		response.put("isBreRuring", familyMember.getIsBreRuring());
		response.put("BreStatus", familyMember.getBreStatus());
		
		logger.debug("final response"+response.size());
		return new ResponseEntity<Object>(response,HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/brerules", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> twbrerules(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {
		
			JSONObject jsonObject=new JSONObject(bm);
			String applicationNo=jsonObject.getJSONObject("Data").getString("ApplicationNo");
			JSONObject brerules=bresService.breRules(applicationNo);
			return new ResponseEntity<Object>(brerules.toString(),HttpStatus.OK);

	}
	
	@RequestMapping(value = "/equifaxReport", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> equifaxreport(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest req)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		JSONObject jsonObject1 = new JSONObject(bm);
		String applicationNo=jsonObject1.getJSONObject("Data").getString("ApplicationNo");	
//		TwoWheelerFamilyMember twfamilymember = familymemberService.fetchByApplicationNoAndMember(applicationNo,"APPLICANT");
//		JSONObject	fetchBre=bresService.equifaxReport(applicationNo, Header, twfamilymember.getLeadId());
		JSONObject fetchBre = bresService.equifaxReport(applicationNo);
		HttpStatus h = HttpStatus.OK;
		if (fetchBre != null) {
			String Data2 = fetchBre.getString("data");
			System.out.println("data2");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}
			System.out.println(Data1.toString());
			return new ResponseEntity<Object>(Data1.toString(), h);
		}
		else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
			}
}
