package com.suryoday.twowheeler.controller;

import javax.servlet.http.HttpServletRequest;

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

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.exceptionhandling.ResourceNotFoundException;
import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.service.TwowheelerBREService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;

@RestController
@RequestMapping(value = "/twowheeler")
public class TwowheelerBREControllerEncy {
	@Autowired
	TwowheelerBREService bresService;

	@Autowired
	UserService userService;

	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;

	@Autowired
	TwowheelerFamilyMemberService familyMemberService;

	private static Logger logger = LoggerFactory.getLogger(TwowheelerBREControllerEncy.class);

	@RequestMapping(value = "/checkstatusEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> checkstatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			String member = jsonObject.getJSONObject("Data").getString("Member");

			TwowheelerDetailesTable familyMember = twowheelerDetailsService.getByApplication(applicationNo);

			if (familyMember.getLeadId() == null) {
				try {
					JSONObject createLead = bresService.createLead(applicationNo, member, Header);
					HttpStatus h = HttpStatus.OK;
					if (createLead != null) {
						String Data2 = createLead.getString("data");
						JSONObject Data1 = new JSONObject(Data2);
						System.out.println(Data1.toString());
						if (Data1.has("data")) {
							h = HttpStatus.OK;
							String leadID = Data1.getJSONObject("data").getString("lead_id");
							if (familyMember.getLeadId() == null) {
								familyMember.setLeadId(leadID);
								familyMember.setIsLeadCreated("YES");
								familyMember.setIsBreRuning("YES");
								familyMember.setBreStatus("INITIATED");
								twowheelerDetailsService.saveData(familyMember);
							}
						} else if (Data1.has("error")) {
							String leadId = Data1.getJSONObject("error").getJSONObject("details").getString("lead_id");
							familyMember.setLeadId(leadId);
							familyMember.setIsLeadCreated("YES");
							familyMember.setIsBreRuning("YES");
							familyMember.setBreStatus("INITIATED");
							twowheelerDetailsService.saveData(familyMember);

						}

					} else {

						return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
					}
				} catch (Exception e) {
					throw new ResourceNotFoundException("BRE FAILDED");
				}
			} else if (familyMember.getIsBreRuning().equalsIgnoreCase("YES")
					&& familyMember.getBreStatus().equals("INITIATED")) {
				try {
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
							familyMemberService.updateMember("APPLICANT", applicationNo, bureauaScore);
							twowheelerDetailsService.saveData(familyMember);

						} else if (Data1.has("error")) {
							h = HttpStatus.BAD_REQUEST;
							return new ResponseEntity<Object>(Data1.toString(), HttpStatus.OK);
						}

					} else {

						return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
					}
				} catch (Exception e) {
					throw new ResourceNotFoundException("BRE FAILDED");
				}
			}
			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("LeadCreated", familyMember.getIsLeadCreated());
			response.put("isBreRuring", familyMember.getIsBreRuning());
			response.put("BreStatus", familyMember.getBreStatus());

			String data1 = response.toString();
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/updateBreEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> updateBre(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			String member = jsonObject.getJSONObject("Data").getString("Member");

			TwowheelerDetailesTable familyMember = twowheelerDetailsService.getByApplication(applicationNo);

			if (familyMember.getLeadId() != null) {
				try {
					JSONObject updateBre = bresService.updateBre(applicationNo, familyMember.getLeadId(), Header);
					HttpStatus h = HttpStatus.OK;
					if (updateBre != null) {
						String Data2 = updateBre.getString("data");
						JSONObject Data1 = new JSONObject(Data2);

						if (Data1.has("data")) {
							h = HttpStatus.OK;

							familyMember.setIsLeadCreated("YES");
							familyMember.setIsBreRuning("YES");
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
				} catch (Exception e) {
					throw new ResourceNotFoundException("BRE FAILDED");
				}
			}

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("LeadCreated", familyMember.getIsLeadCreated());
			response.put("isBreRuring", familyMember.getIsBreRuning());
			response.put("BreStatus", familyMember.getBreStatus());

			String data1 = response.toString();
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/CheckBreStatusEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> CheckBreStatus(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			String member = jsonObject.getJSONObject("Data").getString("Member");

			TwowheelerDetailesTable familyMember = twowheelerDetailsService.getByApplication(applicationNo);

			if (familyMember.getIsBreRuning().equalsIgnoreCase("YES")
					&& familyMember.getBreStatus().equals("UPDATED")) {
				try {
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
//							familyMember.setMaxEmiEligibility(eligibilityAmount.toString());
								familyMember.setBreStatus(status.toUpperCase());
								familyMember.setBreResponse(breResp.toString());
								familyMember.setIsBreRuning("YES");
								twowheelerDetailsService.saveData(familyMember);
							} else if (status.equalsIgnoreCase("Rejected")) {
								familyMember.setBreStatus(status.toUpperCase());
								familyMember.setBreResponse(breResp.toString());
								familyMember.setIsBreRuning("YES");
								twowheelerDetailsService.saveData(familyMember);
							}
						} else if (Data1.has("error")) {
							h = HttpStatus.BAD_REQUEST;
							return new ResponseEntity<Object>(Data1.toString(), HttpStatus.OK);
						}

					} else {

						return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
					}
				} catch (Exception e) {
					throw new ResourceNotFoundException("BRE FAILDED");
				}
			}

			org.json.simple.JSONObject response = new org.json.simple.JSONObject();
			response.put("LeadCreated", familyMember.getIsLeadCreated());
			response.put("isBreRuring", familyMember.getIsBreRuning());
			response.put("BreStatus", familyMember.getBreStatus());

			String data1 = response.toString();
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/brerulesEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> twbrerules(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
			JSONObject brerules = bresService.breRules(applicationNo);
			String Data = brerules.getString("Data");
			JSONObject Data1 = new JSONObject(Data);
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
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/equifaxReportEncy", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> equifaxreport(@RequestBody String jsonRequest,
			@RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
			@RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
			@RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
			@RequestHeader(name = "Authorization", required = true) String authorization,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "channel", required = true) String channel,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request)
			throws Exception {

		JSONObject Header = new JSONObject();
		Header.put("X-From-ID", X_From_ID);
		Header.put("X-To-ID", X_To_ID);
		Header.put("X-Transaction-ID", X_Transaction_ID);
		Header.put("Authorization", authorization);
		Header.put("X-User-ID", X_User_ID);
		Header.put("X-Request-ID", X_Request_ID);

		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String key = X_Session_ID;
			String decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, key);

			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String applicationNo = jsonObject.getJSONObject("Data").getString("ApplicationNo");
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
				String data1 = Data1.toString();
				String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data1, key);
				org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
				data2.put("value", encryptString2);
				org.json.simple.JSONObject data3 = new org.json.simple.JSONObject();
				data3.put("Data", data2);
				return new ResponseEntity<Object>(data3, HttpStatus.OK);

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
