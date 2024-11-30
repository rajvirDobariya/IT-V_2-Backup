package com.suryoday.roaocpv.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.connector.service.AadharReferenceService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.repository.ApplicationDetailsRepository;
import com.suryoday.roaocpv.service.ROAOCPVDedupeService;

@Service
public class ROAOCPVDedupeServiceImpl implements ROAOCPVDedupeService {

	@Autowired
	ApplicationDetailsRepository applicationDetailsRepository;

	@Autowired
	AadharReferenceService aadharReferenceService;

	private static Logger logger = LoggerFactory.getLogger(ROAOCPVDedupeServiceImpl.class);

	@Override
	public JSONObject dedupeCall(JSONObject jsonObject, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
		String emailId = jsonObject.getJSONObject("Data").getString("emailId");
		String mobileNoVerify = jsonObject.getJSONObject("Data").getString("mobileNoVerify");

		Optional<ApplicationDetails> findByApplicationId = applicationDetailsRepository
				.findByApplicationId(applicationNo);
		if (!findByApplicationId.isPresent()) {
			throw new NoSuchElementException("list is empty");
		}
		org.json.JSONObject data = new org.json.JSONObject();
		ApplicationDetails applicationDetails = findByApplicationId.get();
		String referenceNo = applicationDetails.getAadharNumber();
		long aadharNumber = 0;
		JSONObject Header = new JSONObject();
		Header.put("X-Correlation-ID", "DIG123456789056");
		Header.put("tenant", "140");
		Header.put("X-Request-ID", "IEXCEED");
		JSONObject fetchAadharByReferenceNo = aadharReferenceService.fetchAadharByReferenceNo(referenceNo, Header);
		if (fetchAadharByReferenceNo != null) {
			String Data2 = fetchAadharByReferenceNo.getString("data");
			JSONObject Data1 = new JSONObject(Data2);
			if (Data1.has("Data")) {
				aadharNumber = Data1.getJSONObject("Data").getLong("AadharNumber");
			}
		}

		String panCard = applicationDetails.getPanCard();
		String voterId = applicationDetails.getVoterId();
		applicationDetails.setFlowStatus("DD");
		applicationDetailsRepository.save(applicationDetails);
		if (panCard != null) {
			data.put("PAN", panCard);
		} else if (aadharNumber != 0) {
			data.put("AadharNumber", aadharNumber);
		} else if (voterId != null) {
			data.put("VoterID", voterId);
		} else if (mobileNo != null) {
			data.put("MobileNo", mobileNo);
		}

		org.json.JSONObject request = new org.json.JSONObject();
		request.put("Data", data);

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
			logger.debug(x.BASEURL + "customer/retail/dedupe/v2?api_key=" + x.api_key);

			obj = new URL(x.BASEURL + "customer/retail/dedupe/v2?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("X-From-ID", header.getString("X-From-ID"));
			con.setRequestProperty("X-To-ID", header.getString("X-To-ID"));
			con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));

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

}
