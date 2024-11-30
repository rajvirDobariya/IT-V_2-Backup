package com.suryoday.twowheeler.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.service.TwowheelerBREService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;

@RestController
@RequestMapping(value = "/twowheeler")
public class TwowheelerBREController {

	@Autowired
	TwowheelerBREService bresService;

	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;

	@Autowired
	TwowheelerFamilyMemberService familyMemberService;

	private static Logger logger = LoggerFactory.getLogger(TwowheelerBREController.class);

	@RequestMapping(value = "/checkstatus", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> checkstatus(@RequestBody String jsonRequest,
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

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String member = jsonObject.getJSONObject("Data").getString("Member");

		TwowheelerDetailesTable familyMember = twowheelerDetailsService.getByApplication(applicationNo);

		if (familyMember.getLeadId() == null) {
			JSONObject createLead = bresService.createLead(applicationNo, member, Header);
			HttpStatus h = HttpStatus.OK;
			if (createLead != null) {
				String Data2 = createLead.getString("data");
				JSONObject Data1 = new JSONObject(Data2);

				if (Data1.has("data")) {
					h = HttpStatus.OK;
					String leadID = Data1.getJSONObject("data").getString("lead_id");
					if (familyMember.getLeadId() == null) {
						familyMember.setLeadId(leadID);
						familyMember.setIsLeadCreated("YES");
						familyMember.setIsBreRuning("NO");
						familyMember.setBreStatus("INITIATED");
						twowheelerDetailsService.saveData(familyMember);
					}
				} else if (Data1.has("error")) {
					String leadId = Data1.getJSONObject("error").getJSONObject("details").getString("lead_id");
					familyMember.setLeadId(leadId);
					familyMember.setIsLeadCreated("YES");
					familyMember.setIsBreRuning("NO");
					familyMember.setBreStatus("INITIATED");
					twowheelerDetailsService.saveData(familyMember);

				}
			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}

		} else if (familyMember.getIsBreRuning().equalsIgnoreCase("YES")
				&& familyMember.getBreStatus().equals("INITIATED")) {
			JSONObject fetchBre = bresService.fetchBre(Header, familyMember.getLeadId());
			HttpStatus h = HttpStatus.OK;
			if (fetchBre != null) {
				String Data2 = fetchBre.getString("data");
				logger.debug(Data2);

				JSONObject Data1 = new JSONObject(Data2);

				if (Data1.has("data")) {
					h = HttpStatus.OK;
					String status = Data1.getJSONObject("data").getString("status");
					JSONObject breResp = Data1.getJSONObject("data").getJSONObject("bre_response");
//
					familyMember.setBreResponse(breResp.toString());
					familyMember.setBreStatus(status.toUpperCase());
					String bureauaScore = breResp.getJSONObject("BUREAU_POLICY_CREDIT_ENQUIRY_TW")
							.getJSONObject("derived_values").getJSONObject("CR_OBJ").getJSONObject("SCORE")
							.getString("BureauScore");
					familyMember.setBureauaScore(bureauaScore);
					if (Long.parseLong(bureauaScore) > 200) {
						familyMember.setBreCustomerCategory("ETC");
					} else {
						familyMember.setBreCustomerCategory("NTC");
					}
					twowheelerDetailsService.saveData(familyMember);

				} else if (Data1.has("error")) {
					h = HttpStatus.BAD_REQUEST;
					return new ResponseEntity<Object>(Data1.toString(), HttpStatus.OK);
				}

			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("LeadCreated", familyMember.getIsLeadCreated());
		response.put("isBreRuring", familyMember.getIsBreRuning());
		response.put("BreStatus", familyMember.getBreStatus());

		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/updateBre", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> updateBre(@RequestBody String jsonRequest,
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

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String member = jsonObject.getJSONObject("Data").getString("Member");

		TwowheelerDetailesTable familyMember = twowheelerDetailsService.getByApplication(applicationNo);

		if (familyMember.getLeadId() != null) {
			JSONObject updateBre = bresService.updateBre(applicationNo, familyMember.getLeadId(), Header);
			HttpStatus h = HttpStatus.OK;
			if (updateBre != null) {
				String Data2 = updateBre.getString("data");
				JSONObject Data1 = new JSONObject(Data2);

				if (Data1.has("data")) {
					h = HttpStatus.OK;

					familyMember.setIsLeadCreated("YES");
					familyMember.setIsBreRuning("NO");
					familyMember.setBreStatus("UPDATED");
					twowheelerDetailsService.saveData(familyMember);

				}
//					else if (Data1.has("error")) {
//						String leadId = Data1.getJSONObject("error").getJSONObject("details").getString("lead_id");
//						familyMember.setLeadId(leadId);
//						familyMember.setIsLeadCreated("YES");
//						familyMember.setIsBreRuning("YES");
//						familyMember.setBreStatus("INITIATED");
//						familymemberService.saveData(familyMember);
//					}
			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}

		}

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("LeadCreated", familyMember.getIsLeadCreated());
		response.put("isBreRuring", familyMember.getIsBreRuning());
		response.put("BreStatus", familyMember.getBreStatus());

		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/CheckBreStatus", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> CheckBreStatus(@RequestBody String jsonRequest,
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

		JSONObject jsonObject = new JSONObject(jsonRequest);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		String member = jsonObject.getJSONObject("Data").getString("Member");

		TwowheelerDetailesTable familyMember = twowheelerDetailsService.getByApplication(applicationNo);

		if (familyMember.getIsBreRuning().equalsIgnoreCase("YES") && familyMember.getBreStatus().equals("UPDATED")) {
			JSONObject fetchBre = bresService.CheckBreStatus(Header, familyMember.getLeadId());
			HttpStatus h = HttpStatus.OK;
			if (fetchBre != null) {
				String Data2 = fetchBre.getString("data");
				logger.debug(Data2);

				JSONObject Data1 = new JSONObject(Data2);

				if (Data1.has("data")) {
					h = HttpStatus.OK;
					String status = Data1.getJSONObject("data").getString("status");
					JSONObject breResp = Data1.getJSONObject("data").getJSONObject("bre_response");
					if (status.equalsIgnoreCase("Approved")) {
						familyMember.setBreResponse(breResp.toString());
						Object eligibilityAmount = breResp.getJSONObject("LOAN_ELIGIBILITY_AMOUNT")
								.getJSONObject("derived_values").get("LOAN_ELIGIBILITY_AMOUNT_FROM_GRID");
//						familyMember.setMaxEmiEligibility(eligibilityAmount.toString());
						familyMember.setBreStatus(status.toUpperCase());
						familyMember.setBreResponse(breResp.toString());
						twowheelerDetailsService.saveData(familyMember);
					} else if (status.equalsIgnoreCase("Rejected")) {
						familyMember.setBreStatus(status.toUpperCase());
						familyMember.setBreResponse(breResp.toString());
						twowheelerDetailsService.saveData(familyMember);
					}
				} else if (Data1.has("error")) {
					h = HttpStatus.BAD_REQUEST;
					return new ResponseEntity<Object>(Data1.toString(), HttpStatus.OK);
				}

			} else {

				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		}

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		response.put("LeadCreated", familyMember.getIsLeadCreated());
		response.put("isBreRuring", familyMember.getIsBreRuning());
		response.put("BreStatus", familyMember.getBreStatus());

		logger.debug("final response" + response.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);

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

		JSONObject jsonObject = new JSONObject(bm);
		String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
		JSONObject brerules = bresService.breRules(applicationNo);
		String Data = brerules.getString("Data");
		JSONObject Data1 = new JSONObject(Data);
		return new ResponseEntity<Object>(Data1.toString(), HttpStatus.OK);

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
		String applicationNo = jsonObject1.getJSONObject("Data").getString("ApplicationNo");
		TwowheelerDetailesTable familyMember = twowheelerDetailsService.getByApplication(applicationNo);
		if (familyMember.getLeadId() == null) {
			throw new NoSuchElementException("Technical Error please try again");
		}
		JSONObject fetchBre = bresService.equifaxReport(applicationNo, Header, familyMember.getLeadId());

		HttpStatus h = HttpStatus.OK;
		if (fetchBre != null) {
			String Data2 = fetchBre.getString("data");
			twowheelerDetailsService.updateFlowStatus(applicationNo, "TWBR");
			JSONObject Data1 = new JSONObject(Data2);

			System.out.println(Data1);

			if (Data1.has("data")) {
				h = HttpStatus.OK;

			} else if (Data1.has("Error")) {
				h = HttpStatus.BAD_REQUEST;

			}
			System.out.println(Data1.toString());
			return new ResponseEntity<Object>(Data1.toString(), h);
		} else {

			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@RequestMapping(value = "/webhookcallback", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> webhookcallback(@RequestBody String bm,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {
		logger.debug("webhookcallback start");
		logger.debug("request" + bm);
		String method = request.getMethod();
		System.out.println("method" + method);
		if (request.getMethod().equals("OPTIONS")) {
			return new ResponseEntity<Object>("Invalid Request ", HttpStatus.FORBIDDEN);
		}
		if (X_From_ID.equals("CB") && X_Request_ID.equals("SAR") && X_To_ID.equals("SAR")
				&& authorization.equals("Basic d2ViaG9va2NhbGxiYWNrOm9uZWZpbg=")) {

			JSONObject jsonObject = new JSONObject(bm);
			String Lead_ID = jsonObject.getString("lead_id");
			if (Lead_ID.equals("")) {
				throw new NoSuchElementException("Lead_Id cannot be Empty");
			}

			int respons = twowheelerDetailsService.fetchByLeadId(Lead_ID);
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			if (respons == 1) {
				logger.debug("message : Success");
				response.put("message", "Success");
			} else {
				response.put("message", "failed");
				logger.debug("message : failed");
			}
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Strict-Transport-Security", "false");
			responseHeaders.set("X-Frame-Options", "");
			responseHeaders.set("X-Content-Type-Options", "nosniff");
			responseHeaders.set("Content-Security-Policy", "src");
			responseHeaders.set("X-XSS-Protection", "0");

			return ResponseEntity.ok().headers(responseHeaders).body(response);
		} else {
			logger.debug("Invalid Request");
			return new ResponseEntity<Object>("Invalid Request ", HttpStatus.BAD_REQUEST);

		}

	}
}
