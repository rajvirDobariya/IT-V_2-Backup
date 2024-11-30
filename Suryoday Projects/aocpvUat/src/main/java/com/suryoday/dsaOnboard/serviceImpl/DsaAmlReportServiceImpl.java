package com.suryoday.dsaOnboard.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.dsaOnboard.pojo.DsaOnboardMember;
import com.suryoday.dsaOnboard.service.DsaAmlReportService;
import com.suryoday.dsaOnboard.service.DsaOnboardMemberService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;

@Service
public class DsaAmlReportServiceImpl implements DsaAmlReportService {

	@Autowired
	DsaOnboardMemberService dsaOnboardMemberService;

	private static Logger logger = LoggerFactory.getLogger(DsaAmlReportServiceImpl.class);

	@Override
	public JSONObject getAmlReport(String applicationNo, String member, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = getRequest(applicationNo, member);
		URL obj = null;
		try {
			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.bypassssl();
			x.getappprop();
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			obj = new URL(x.BASEURL + "customer/aml/query/v2?api_key=" + x.api_key);
			logger.debug(x.BASEURL + "customer/aml/query/v2?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			con.setRequestProperty("X-From-ID", header.getString("X-From-ID"));
			sendResponse = getResponseData(request, sendResponse, con, "POST");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendResponse;
	}

	private JSONObject getResponseData(JSONObject parent, JSONObject sendAuthenticateResponse, HttpURLConnection con,
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
			JSONObject sendauthenticateResponse1 = new JSONObject(response.toString());
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

	private JSONObject getRequest(String applicationNo, String member) {

		DsaOnboardMember dsaOnboardMember = dsaOnboardMemberService.getByApplicationnoAndMember(applicationNo, member);

		JSONObject jsonRequest = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject customer = new JSONObject();
		JSONObject identification_Type1 = new JSONObject();
		JSONObject identification_Type2 = new JSONObject();
		JSONObject identification_Type3 = new JSONObject();
		JSONArray identification_Types = new JSONArray();

		data.put("EntityType", "");
		data.put("CreationDateTime", LocalDate.now());
		data.put("RequestorName", "Suryoday");
		data.put("BranchId", "");
		data.put("RequestType", "");
		customer.put("DateOfBirth", dsaOnboardMember.getDateOfBirth());
		customer.put("FirstName", dsaOnboardMember.getName());
		customer.put("LastName", dsaOnboardMember.getName());
		customer.put("MiddleName", "");
		customer.put("UCIC", applicationNo);
		if (dsaOnboardMember.getAadharNo() != null) {
			identification_Type1.put("IdentityNumber", dsaOnboardMember.getAadharNo());
			identification_Type1.put("IdentityType", "AADHAAR");
			identification_Types.put(identification_Type1);
		}
		if (dsaOnboardMember.getPanCardNo() != null) {
			identification_Type2.put("IdentityNumber", dsaOnboardMember.getPanCardNo());
			identification_Type2.put("IdentityType", "PAN");
			identification_Types.put(identification_Type2);
		}
		customer.put("Identification_Type", identification_Types);

		data.put("Customer", customer);
		jsonRequest.put("Data", data);
		return jsonRequest;
	}

}
