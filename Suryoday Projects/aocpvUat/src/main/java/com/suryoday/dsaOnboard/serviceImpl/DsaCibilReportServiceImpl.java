package com.suryoday.dsaOnboard.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.connector.serviceImpl.GenerateProperty;
import com.suryoday.dsaOnboard.pojo.DsaOnboardDetails;
import com.suryoday.dsaOnboard.pojo.DsaOnboardMember;
import com.suryoday.dsaOnboard.service.DsaCibilReportService;
import com.suryoday.dsaOnboard.service.DsaOnBoardDetailsService;
import com.suryoday.dsaOnboard.service.DsaOnboardMemberService;

@Service
public class DsaCibilReportServiceImpl implements DsaCibilReportService {

	@Autowired
	DsaOnBoardDetailsService dsaOnBoardDetailsService;

	@Autowired
	DsaOnboardMemberService dsaOnboardMemberService;

	private static Logger logger = LoggerFactory.getLogger(DsaCibilReportServiceImpl.class);

	@Override
	public JSONObject getCibilReport(JSONObject jsonObject, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = getRequest(jsonObject);
		URL obj = null;
		try {

			GenerateProperty x = GenerateProperty.getInstance();
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
			logger.debug(x.BASEURL + "cir360Report");
			obj = new URL(x.BASEURL + "cir360Report");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("api_key", x.api_key);

			sendResponse = getResponseData(request, sendResponse, con, "POST");

		} catch (Exception e) {

			e.printStackTrace();
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
		} else {
			logger.debug("POST request not worked");

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

	private JSONObject getRequest(JSONObject jsonObject) {
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String member = jsonObject.getJSONObject("Data").getString("member");
		DsaOnboardDetails dsaOnboardDetails = dsaOnBoardDetailsService.getByApplicationno(applicationNo);
		DsaOnboardMember dsaOnboardMember = dsaOnboardMemberService.getByApplicationnoAndMember(applicationNo, member);

		JSONObject data = new JSONObject();
		JSONObject requestHeader = new JSONObject();
		requestHeader.put("CustomerId", "21");
		requestHeader.put("UserId", "UAT_ASA");
		requestHeader.put("Password", "V2*Pdhbr");
		requestHeader.put("MemberNumber", "028FZ00016");
		requestHeader.put("SecurityCode", "FR7");
		requestHeader.put("CustRefField", "123456");
		JSONArray productCode = new JSONArray();
		productCode.put("CCR");
		requestHeader.put("ProductCode", productCode);
		data.put("RequestHeader", requestHeader);
		JSONObject requestBody = new JSONObject();
		requestBody.put("InquiryPurpose", "00");
//			requestBody.put("FirstName", dsaOnboardMember.getName());
		requestBody.put("FirstName", "Rahul Tripathy");
		requestBody.put("MiddleName", "");
		requestBody.put("LastName", "");
//			requestBody.put("DOB", dsaOnboardMember.getDateOfBirth());
		requestBody.put("DOB", "1979-02-26");
		JSONArray inquiryAddresses = new JSONArray();
		JSONObject inquiryAddress = new JSONObject();
		inquiryAddress.put("seq", "1");
		JSONArray addressType = new JSONArray();
		addressType.put("H");
		inquiryAddress.put("AddressType", addressType);
		inquiryAddress.put("AddressLine1",
				"B-25 Jalada RBI Officers Quarters P Balu Marg Prabhadevi Mumbai Maharashtra 400025");
		inquiryAddress.put("State", "MH");
		inquiryAddress.put("Postal", "400025");
		inquiryAddresses.put(inquiryAddress);
		requestBody.put("InquiryAddresses", inquiryAddresses);
		JSONArray inquiryPhones = new JSONArray();
		JSONObject inquiryPhone = new JSONObject();
		inquiryPhone.put("seq", "1");
//			inquiryPhone.put("Number", dsaOnboardDetails.getMobileNo());
		inquiryPhone.put("Number", "7021938214");
		inquiryPhone.put("seq", "1");
		JSONArray phoneType = new JSONArray();
		phoneType.put("M");
		inquiryPhone.put("PhoneType", phoneType);
		inquiryPhones.put(inquiryPhone);
		requestBody.put("InquiryPhones", inquiryPhones);
		JSONArray iDDetails = new JSONArray();
		JSONObject iDDetail1 = new JSONObject();
		iDDetail1.put("seq", "1");
		iDDetail1.put("IDType", "T");
		iDDetail1.put("IDValue", "ACUPT4571D");
		iDDetail1.put("Source", "Inquiry");
		iDDetails.put(iDDetail1);
		JSONObject iDDetail2 = new JSONObject();
		iDDetail2.put("seq", "2");
		iDDetail2.put("IDType", "P");
		iDDetail2.put("IDValue", "");
		iDDetail2.put("Source", "Inquiry");
		iDDetails.put(iDDetail2);
		JSONObject iDDetail3 = new JSONObject();
		iDDetail3.put("seq", "3");
		iDDetail3.put("IDType", "V");
		iDDetail3.put("IDValue", "");
		iDDetail3.put("Source", "Inquiry");
		iDDetails.put(iDDetail3);
		JSONObject iDDetail4 = new JSONObject();
		iDDetail4.put("seq", "4");
		iDDetail4.put("IDType", "D");
		iDDetail4.put("IDValue", "");
		iDDetail4.put("Source", "Inquiry");
		iDDetails.put(iDDetail4);
		JSONObject iDDetail5 = new JSONObject();
		iDDetail5.put("seq", "5");
		iDDetail5.put("IDType", "M");
		iDDetail5.put("IDValue", "");
		iDDetail5.put("Source", "Inquiry");
		iDDetails.put(iDDetail5);
		requestBody.put("IDDetails", iDDetails);
		JSONObject mfiDetails = new JSONObject();
		JSONArray familyDetails = new JSONArray();
		JSONObject familyDetail1 = new JSONObject();
		familyDetail1.put("seq", "1");
		familyDetail1.put("AdditionalNameType", "K02");
		familyDetail1.put("AdditionalName", "Radha");
		familyDetails.put(familyDetail1);
		JSONObject familyDetail2 = new JSONObject();
		familyDetail2.put("seq", "2");
		familyDetail2.put("AdditionalNameType", "K01");
		familyDetail2.put("AdditionalName", "");
		familyDetails.put(familyDetail2);
		mfiDetails.put("FamilyDetails", familyDetails);
		requestBody.put("MFIDetails", mfiDetails);
		JSONArray score = new JSONArray();
		JSONObject score1 = new JSONObject();
		score1.put("Type", "ERS");
		score1.put("Version", "3.1");
		score.put(score1);
		data.put("RequestBody", requestBody);
		data.put("Score", score);

		logger.debug("request" + data.toString());
		System.out.println(data);
		return data;

	}
}