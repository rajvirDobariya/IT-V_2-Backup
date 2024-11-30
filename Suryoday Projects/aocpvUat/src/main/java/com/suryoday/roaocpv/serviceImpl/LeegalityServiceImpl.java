package com.suryoday.roaocpv.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.service.LeegalityService;

@Service
public class LeegalityServiceImpl implements LeegalityService {

	private static Logger logger = LoggerFactory.getLogger(LeegalityServiceImpl.class);

	@Autowired
	AocpvImageService aocpvImageService;

	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;

	AocpvLoanCreation loanCreation;

	@Override
	public JSONObject leegalityCheck(long applicationNoInLong, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = getRequest(applicationNoInLong);
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
			logger.debug(x.BASEURL + "leegality/create?api_key=" + x.api_key);
			obj = new URL(x.BASEURL + "leegality/create?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			sendResponse = getResponseData(request, sendResponse, con, "POST");
			String Data2 = sendResponse.getString("data");
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("Data")) {
				loanCreation.setDocumentId(Data1.getJSONObject("Data").getString("DocumentId"));
				loanCreation.setLeegalityResponse(Data1.toString());
				loanCreation.setSendAgreement("YES");
				loanCreation.setIsVerify("NO");
				aocpvLoanCreationService.saveData(loanCreation);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private JSONObject getRequest(long applicationNoInLong) {

		AocpvImages app = aocpvImageService.getByApplicationNo(111);
		byte[] images = app.getImages();
		String encoded = Base64.getEncoder().encodeToString(images);

		loanCreation = aocpvLoanCreationService.findByApplicationNo(applicationNoInLong);

		org.json.JSONObject request = new org.json.JSONObject();
		org.json.simple.JSONObject Data = new org.json.simple.JSONObject();
		org.json.simple.JSONArray Invitees = new org.json.simple.JSONArray();
		org.json.simple.JSONArray CC = new org.json.simple.JSONArray();
		org.json.simple.JSONObject cc = new org.json.simple.JSONObject();
		org.json.simple.JSONObject Invitee1 = new org.json.simple.JSONObject();
		org.json.simple.JSONObject Invitee2 = new org.json.simple.JSONObject();
		org.json.simple.JSONObject File = new org.json.simple.JSONObject();

		Invitee1.put("Name", loanCreation.getCustomerName());
		Invitee1.put("Email", "ratnesh.shinde@suryodaybank.com");
		Invitee1.put("Phone", Long.toString(loanCreation.getMobileNo()));

		Invitee2.put("Name", "");
		Invitee2.put("Email", "");
		Invitee2.put("Phone", "");

		Invitees.add(Invitee1);
		Invitees.add(Invitee2);

		cc.put("Name", "Dipak");
		cc.put("Email", "Dipak.vitmal@suryodaybank.com");

		File.put("Name", "Bc AG");
		File.put("File", encoded);

		CC.add(cc);
		Data.put("ProfileId", "VaGmc7p");
		Data.put("StampValue", "0");
		Data.put("Irn", "88989235647");
		Data.put("File", File);
		Data.put("Invitees", Invitees);
		Data.put("CC", CC);
		request.put("Data", Data);
		return request;
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

	@Override
	public JSONObject statusCheck(long applicationNoInLong, JSONObject header) {

		loanCreation = aocpvLoanCreationService.findByApplicationNo(applicationNoInLong);
		JSONObject sendResponse = new JSONObject();
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
			org.json.JSONObject request = new org.json.JSONObject();
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			logger.debug(
					x.BASEURL + "leegality/status/check/" + loanCreation.getDocumentId() + "?api_key=" + x.api_key);
			obj = new URL(
					x.BASEURL + "leegality/status/check/" + loanCreation.getDocumentId() + "?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			sendResponse = getResponseForGetMethod(request, sendResponse, con, "GET");
			String Data2 = sendResponse.getString("data");
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("Data")) {
				int count = 0;
				JSONArray jsonArray = Data1.getJSONObject("Data").getJSONArray("Requests");
				for (int n = 0; n < jsonArray.length(); n++) {
					JSONObject jsonObject = jsonArray.getJSONObject(n);
					String Signed = jsonObject.getString("Signed");
					if (Signed.equalsIgnoreCase("false")) {
						count++;
					}
				}
				if (count == 0) {
					loanCreation.setIsVerify("YES");
					aocpvLoanCreationService.saveData(loanCreation);
					sendResponse.put("is_verify", "YES");
				} else {
					sendResponse.put("is_verify", "NO");
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private static JSONObject getResponseForGetMethod(JSONObject parent, JSONObject sendAuthenticateResponse,
			HttpURLConnection con, String MethodType) throws IOException {

		con.setDoOutput(true);
		// OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
		// os.write(parent.toString());
		// os.flush();
		// os.close();

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
			logger.debug("GET request not worked");

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
}
