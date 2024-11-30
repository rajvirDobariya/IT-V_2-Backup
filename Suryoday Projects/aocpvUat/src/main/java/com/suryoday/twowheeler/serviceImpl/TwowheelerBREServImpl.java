package com.suryoday.twowheeler.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.service.TwowheelerBREService;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;

@Component
public class TwowheelerBREServImpl implements TwowheelerBREService {

	private static Logger logger = LoggerFactory.getLogger(TwowheelerBREServImpl.class);

	@Autowired
	TwowheelerFamilyMemberService familymemberservice;

	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;

	@Override
	public JSONObject fetchBre(JSONObject header, String leadId) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = new JSONObject();
		request.put("onboarding_completed", false);
		request.put("policy", "TW_POLICY_1");
		URL obj = null;
		try {

			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			// GenerateProperty x = GenerateProperty.getInstance();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//			logger.debug(x.onefin + "api/v1/client/loan/"+leadId+"/approve/");
//			obj = new URL(x.onefin + "api/v1/client/loan/"+leadId+"/approve/");
			logger.debug(x.BASEURL + "client/loan/" + leadId + "/approve?api_key=" + x.api_key);
			obj = new URL(x.BASEURL + "client/loan/" + leadId + "/approve?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			// con.setRequestProperty("X-Correlation-ID",
			// header.getString("X-Correlation-ID"));
			con.setRequestProperty("Authorization", header.getString("Authorization"));
			sendResponse = getResponseData(request, sendResponse, con, "POST");

		} catch (Exception e) {

			e.getMessage();
		}

		return sendResponse;
	}

	private static JSONObject getResponseData(JSONObject parent, JSONObject sendAuthenticateResponse,
			HttpURLConnection con, String MethodType) throws IOException {

		con.setDoOutput(true);
		OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
		os.write(parent.toString());
		os.flush();
		os.close();

		int responseCode = con.getResponseCode();
		logger.debug("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONObject sendauthenticateResponse1 = new JSONObject();
			sendauthenticateResponse1.put("data", response.toString());
			sendAuthenticateResponse = sendauthenticateResponse1;
			System.out.println(sendAuthenticateResponse);

		} else {
			logger.debug("POST request not worked");

			JSONObject sendauthenticateResponse1 = new JSONObject();

			JSONObject errr = new JSONObject();
			errr.put("Description", "Server Error " + responseCode);

			JSONObject j = new JSONObject();
			j.put("Error", errr);

			sendauthenticateResponse1.put("data", "" + j);
			sendAuthenticateResponse = sendauthenticateResponse1;
			System.out.println(sendAuthenticateResponse);
		}

		return sendAuthenticateResponse;

	}

	@Override
	public JSONObject createLead(String applicationNo, String member, JSONObject header) {

		JSONObject request = getRequest(applicationNo, member, "POST");

		JSONObject sendResponse = new JSONObject();
		URL obj = null;
		try {
			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			x.bypassssl();
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//			obj = new URL(x.onefin + "api/v1/client/loan/");
//			logger.debug(x.onefin + "api/v1/client/loan/");
			logger.debug(x.BASEURL + "lead/loan/creation?api_key=" + x.api_key);
			obj = new URL(x.BASEURL + "lead/loan/creation?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", header.getString("Authorization"));
			sendResponse = getResponse(request, sendResponse, con, "POST");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendResponse;
	}

	private static JSONObject getResponse(JSONObject parent, JSONObject sendAuthenticateResponse, HttpURLConnection con,
			String MethodType) throws IOException {
		con.setDoOutput(true);
		OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
		os.write(parent.toString());
		os.flush();
		os.close();
		int responseCode = con.getResponseCode();
		logger.debug("POST Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			JSONObject sendauthenticateResponse1 = new JSONObject();
			sendauthenticateResponse1.put("data", response.toString());
			sendAuthenticateResponse = sendauthenticateResponse1;
		} else {
			logger.debug("Getting 400 From Third Party");
			JSONObject sendauthenticateResponse1 = new JSONObject();
			JSONObject errr = new JSONObject();
			errr.put("Description", "Server Error " + responseCode);
			JSONObject j = new JSONObject();
			j.put("Error", errr);
			sendauthenticateResponse1.put("data", "" + j);
			sendAuthenticateResponse = sendauthenticateResponse1;
		}
		return sendAuthenticateResponse;
	}

	private JSONObject getRequest(String applicationNo, String member, String methodType) {

		org.json.JSONObject request = new org.json.JSONObject();
		org.json.simple.JSONArray kyc = new org.json.simple.JSONArray();
		org.json.simple.JSONObject kyc1 = new org.json.simple.JSONObject();
		org.json.simple.JSONObject kyc2 = new org.json.simple.JSONObject();
		org.json.simple.JSONArray co_applicant_details = new org.json.simple.JSONArray();
		org.json.simple.JSONObject co_applicant_detail = new org.json.simple.JSONObject();
		org.json.simple.JSONObject additional_variable = new org.json.simple.JSONObject();
		org.json.simple.JSONObject disbursal_detail = new org.json.simple.JSONObject();
		logger.debug("step 1 ");
		TwoWheelerFamilyMember familyMember = familymemberservice.getByApplicationNoAndMember(applicationNo,
				"APPLICANT");

		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		String genderCode = "";
		logger.debug("step 2 ");
		if (familyMember.getPanCard() != null) {
			logger.debug("step 3 " + familyMember.getPanCard());
			String panCardResponse = familyMember.getPanCardResponse();
			JSONObject panResponse = new JSONObject(panCardResponse);
			kyc1.put("kyc_type", 2);
//			kyc1.put("identifier", panResponse.getString("PanID"));
//			kyc1.put("identifier","CCUPU1429K");x
			kyc1.put("identifier", familyMember.getPanCard());
			kyc1.put("verified_using", 1);
			kyc1.put("kyc_name", panResponse.getString("NameOnCard"));
			if (familyMember.getPancardNoVerify().equals("YES")) {
				kyc1.put("is_verified", true);
			} else {
				kyc1.put("is_verified", true);
			}
			kyc.add(kyc1);
		} else if (familyMember.getVoterId() != null) {
			logger.debug("step 4 " + familyMember.getVoterId());
			String voterIdResponse = familyMember.getVoterIdResponse();
			JSONObject voterResponse = new JSONObject(voterIdResponse);
			kyc2.put("kyc_type", 5);
			kyc2.put("identifier", voterResponse.getJSONObject("result").getString("epic_no"));
			kyc2.put("verified_using", 1);
			kyc2.put("kyc_name", voterResponse.getJSONObject("result").getString("name"));
			kyc2.put("is_verified", true);
			kyc.add(kyc2);
			genderCode = voterResponse.getJSONObject("result").getString("gender");
		}
		request.put("kyc", kyc);
		request.put("sourcing_channel", "TW");
		request.put("product", "TW");

		if (familyMember.getEkycResponse() != null && familyMember.getGenderCode() == null) {
			org.json.JSONObject ekyc = new org.json.JSONObject(familyMember.getEkycResponse());
			JSONObject PoiResponse = ekyc.getJSONObject("Response").getJSONObject("KycRes").getJSONObject("UidData")
					.getJSONObject("Poi");
			genderCode = PoiResponse.getString("gender");
		} else if (familyMember.getGenderCode() != null) {
			genderCode = familyMember.getGenderCode();
		}
		if (genderCode.equals("F")) {
			request.put("gender", 2);
		} else if (genderCode.equals("M")) {
			request.put("gender", 1);
		}

		String address = twowheelerDetails.getAddress();
		if (address != null) {
			org.json.JSONArray addressArray = new org.json.JSONArray(address);
			for (int n = 0; n < addressArray.length(); n++) {
				JSONObject addressInJson = addressArray.getJSONObject(n);
				String addressType = addressInJson.getString("addressType");
				if (addressType.equalsIgnoreCase("CURRENT ADDRESS")) {
					request.put("address", addressInJson.getString("address_Line1"));
					request.put("pincode", addressInJson.getString("pincode"));
				}
			}
		}
		request.put("address_match", "Matched");
		request.put("address_type", "Current Residence");
		request.put("full_name", twowheelerDetails.getName());
		request.put("loan_type", 4);
		request.put("loan_amount", Double.parseDouble(twowheelerDetails.getRequiredAmount()));
		request.put("negative_locality", "");
		request.put("credit_score", 0);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate birthdate = LocalDate.parse(familyMember.getDob(), dateTimeFormatter);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		String dobString = birthdate.format(formatter);
		String today = LocalDate.now().format(formatter);
		request.put("date_of_birth", dobString);
		if (familyMember.getMobile() != null) {
			request.put("mobile_number", Long.parseLong(familyMember.getMobile()));
		}
		request.put("partner_loan_id", twowheelerDetails.getAppNoWithProductCode());
		request.put("payment_frequency", 3);
		request.put("loan_application_date", today);
		request.put("agreement_signature_type", 2);

		request.put("monthly_emi", 0);
		request.put("repayment_count", 0);// tenuor

//		additional_variable.put("face_match_score", 70);
		if (familyMember.getNameMatchScore() != null) {
			int parseDouble = (int) Double.parseDouble(familyMember.getNameMatchScore());
			additional_variable.put("name_match_score", parseDouble);
		}
		additional_variable.put("penny_drop_name_match_score", 0);
		additional_variable.put("residence_ownership_status", "");
		additional_variable.put("residential_stability", 0);
		additional_variable.put("geo_location", 40);

		if (methodType.equals("PUT")) {

			if (familyMember.getPenny_drop_name_match_score() != null) {
				additional_variable.put("penny_drop_name_match_score",
						Integer.parseInt(familyMember.getPenny_drop_name_match_score()));
			}
			int employmentType = 0;
			if (familyMember.getEmploymentType().equals("UNEMPLOYED")) {
				employmentType = 0;
			} else if (familyMember.getEmploymentType().equals("SALARIED")) {
				employmentType = 1;
			} else if (familyMember.getEmploymentType().equals("SELF_EMP_PROF")) {
				employmentType = 2;
			} else if (familyMember.getEmploymentType().equals("SELF_EMP_BUSINESS")) {
				employmentType = 3;
			} else if (familyMember.getEmploymentType().equals("AGRICULTURE")) {
				employmentType = 4;
			}

			additional_variable.put("customer_catagory", Integer.parseInt(familyMember.getCategoryCode()));
//			additional_variable.put("employment_stability", familyMember.getWorkStability());
			additional_variable.put("business_stability", familyMember.getWorkStability());
			request.put("employment_type", employmentType);

			request.put("email", familyMember.getEmailId());
			request.put("customer_category", familyMember.getCategory());

			logger.debug("step 5 ");
			if (familyMember.getAddressArray() != null) {
				org.json.JSONArray addressArray = new org.json.JSONArray(familyMember.getAddressArray());
				for (int n = 0; n < addressArray.length(); n++) {
					JSONObject addressInJson = addressArray.getJSONObject(n);
					String addressType = addressInJson.getString("addressType");
					if (addressType.equalsIgnoreCase("CURRENT ADDRESS")) {
						additional_variable.put("residence_ownership_status",
								addressInJson.getString("houseOwnership"));
						if (!addressInJson.getString("residenceStabilityYear").equals("")) {
							additional_variable.put("residential_stability",
									Integer.parseInt(addressInJson.getString("residenceStabilityYear")));
						}
					}
				}
			}
			double annualIncome = Double.parseDouble(familyMember.getAnnualIncome());
			double monthlyIncome = Math.round(annualIncome / 12);
			request.put("negative_locality", familyMember.getNegativeLocality());
			if (familyMember.getDistanceFromBranch() != null && !familyMember.getDistanceFromBranch().equals("")) {
				additional_variable.put("geo_location", Integer.parseInt(familyMember.getDistanceFromBranch()));
			}
			request.put("monthly_emi", 6000);
			request.put("monthly_income", monthlyIncome);
			if (twowheelerDetails.getAmount() != null && twowheelerDetails.getTenure() != null) {
				request.put("loan_amount", Double.parseDouble(twowheelerDetails.getAmount()));
				request.put("loan_tenure", Integer.parseInt(twowheelerDetails.getTenure()));
				request.put("repayment_count", Integer.parseInt(twowheelerDetails.getTenure()));
				String rateOfInterest = twowheelerDetails.getRateOfInterest();
				String rateOfInterestWithoutSign = rateOfInterest.split("%")[0];
				request.put("interest_rate", Double.parseDouble(rateOfInterestWithoutSign));
				if (twowheelerDetails.getLoanCharges() != null) {
					JSONArray loanCarges = new JSONArray(twowheelerDetails.getLoanCharges());
					for (int n = 0; n < loanCarges.length(); n++) {
						JSONObject jsonObject = loanCarges.getJSONObject(n);
						if (jsonObject.getString("chargeName").equalsIgnoreCase("Processing Fee")) {
							float processingFee = Float.parseFloat(jsonObject.getString("totalAmount"));
							request.put("processing_fee", processingFee);
						}
					}
				}

			}
//			request.put("applicant_profile", 3);
			request.put("fathers_name", familyMember.getFatherName());
			request.put("mothers_name", familyMember.getMotherName());
			request.put("agreement_date", today);
			request.put("loan_amount", Double.parseDouble(twowheelerDetails.getAmount()));
			request.put("occupation", familyMember.getOccupation());

			logger.debug("step 6 ");
			TwoWheelerFamilyMember familyMemberCoApp = familymemberservice.fetchByApplicationNoAndMember(applicationNo,
					"CO_APPLICANT");
			if (familyMemberCoApp.getMember() != null) {
				logger.debug("step 7 ");
				co_applicant_detail.put("co_dob", familyMemberCoApp.getDob());
				co_applicant_detail.put("co_email", familyMemberCoApp.getEmailId());
				co_applicant_detail.put("co_phone", familyMemberCoApp.getMobile());
				String genderCodeCoApplicant = familyMemberCoApp.getGenderCode();
				if (genderCode.equals("F")) {
					co_applicant_detail.put("co_gender", 2);
				} else if (genderCode.equals("M")) {
					co_applicant_detail.put("co_gender", 1);
				}
				co_applicant_detail.put("co_aadhaar", familyMemberCoApp.getAadharCard());
				co_applicant_detail.put("co_address", familyMemberCoApp.getAddress());
				co_applicant_detail.put("co_pancard", familyMemberCoApp.getPanCard());
				co_applicant_detail.put("co_relation", familyMemberCoApp.getNomineeRelationship());
				co_applicant_detail.put("co_full_name", familyMemberCoApp.getFirstName() + ""
						+ familyMemberCoApp.getMiddleName() + "" + familyMemberCoApp.getLastName());
				co_applicant_details.add(co_applicant_detail);

				request.put("co_applicant_details", co_applicant_details);
			}

		}

		request.put("additional_variables", additional_variable);
		request.put("disbursal_detail", disbursal_detail);
		System.out.println(request);
		return request;

	}

	@Override
	public JSONObject breRules(String applicationNo) {

		TwowheelerDetailesTable twowheelerDetails = twowheelerDetailsService.getByApplication(applicationNo);
		if (twowheelerDetails.getBreResponse() != null) {
			JSONObject jsonObject = new JSONObject(twowheelerDetails.getBreResponse());
			JSONObject Data = new JSONObject();
			JSONObject data = new JSONObject();
			JSONArray breData = new JSONArray();
			for (String keys : jsonObject.keySet()) {
				if (!jsonObject.getJSONObject(keys).get("deviation_group").equals(null)) {
					JSONObject jsonObject2 = jsonObject.getJSONObject(keys);
					JSONObject breData1 = new JSONObject();
					breData1.put("Parameter", keys);
					breData1.put("description", jsonObject2.get("description"));
					breData1.put("Deviation", jsonObject2.get("deviation"));
					breData1.put("result", jsonObject2.get("result"));
					breData1.put("deviation_group", jsonObject2.get("deviation_group"));
					breData.put(breData1);
				}
			}
			data.put("BREData", breData);
			Data.put("Data", data.toString());
			return Data;
		}
		throw new NoSuchElementException("No Record Found");
	}

	@Override
	public JSONObject updateBre(String applicationNo, String leadId, JSONObject header) {
		JSONObject request = getRequest(applicationNo, leadId, "PUT");

		JSONObject sendResponse = new JSONObject();
		URL obj = null;
		try {
			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			x.bypassssl();
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			logger.debug(x.BASEURL + "lead/loan/creation/" + leadId + "/?api_key=" + x.api_key);
			obj = new URL(x.BASEURL + "lead/loan/creation/" + leadId + "/?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", header.getString("Authorization"));
			sendResponse = getResponse(request, sendResponse, con, "PUT");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendResponse;

	}

	@Override
	public JSONObject CheckBreStatus(JSONObject header, String leadId) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = new JSONObject();
		request.put("onboarding_completed", false);
		request.put("policy", "TW_POLICY_2");
		URL obj = null;
		try {

			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			// GenerateProperty x = GenerateProperty.getInstance();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//			logger.debug(x.onefin + "api/v1/client/loan/"+leadId+"/approve/");
//			obj = new URL(x.onefin + "api/v1/client/loan/"+leadId+"/approve/");
			logger.debug(x.BASEURL + "client/loan/" + leadId + "/approve?api_key=" + x.api_key);
			obj = new URL(x.BASEURL + "client/loan/" + leadId + "/approve?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			// con.setRequestProperty("X-Correlation-ID",
			// header.getString("X-Correlation-ID"));
			con.setRequestProperty("Authorization", header.getString("Authorization"));
			sendResponse = getResponseData(request, sendResponse, con, "POST");

		} catch (Exception e) {

			e.getMessage();
		}

		return sendResponse;
	}

	@Override
	public JSONObject equifaxReport(String applicationNo, JSONObject header, String leadId) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = new JSONObject();
		URL obj = null;
		try {

			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			// GenerateProperty x = GenerateProperty.getInstance();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			logger.debug(x.BASEURL + "onefin/equifax/" + leadId + "?api_key=" + x.api_key);
			obj = new URL(x.BASEURL + "onefin/equifax/" + leadId + "?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			// con.setRequestProperty("X-Correlation-ID",
			// header.getString("X-Correlation-ID"));
			con.setRequestProperty("Authorization", header.getString("Authorization"));
			sendResponse = getResponseData(request, sendResponse, con, "POST");

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;

	}
}
