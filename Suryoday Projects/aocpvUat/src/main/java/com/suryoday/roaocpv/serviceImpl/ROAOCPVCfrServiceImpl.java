package com.suryoday.roaocpv.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.service.ROAOCPVCfrService;

@Service
public class ROAOCPVCfrServiceImpl implements ROAOCPVCfrService {
	private static Logger logger = LoggerFactory.getLogger(ROAOCPVCfrServiceImpl.class);

	@Override
	public JSONObject Cfrcase(String PanNumber, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		JSONObject parentRequest = parentRequest(PanNumber);
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
			obj = new URL(x.BASEURLV2 + "fetch/fraud/case?api_key=" + x.api_key);
			logger.debug(x.BASEURLV2 + "fetch/fraud/case?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			con.setRequestProperty("X-From-ID", header.getString("X-From-ID"));
			sendResponse = getResponseData(parentRequest, sendResponse, con, "POST");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendResponse;
	}

	private JSONObject parentRequest(String PanNumber) {
		JSONObject jsonResponse = new JSONObject();
		org.json.JSONObject data = new org.json.JSONObject();
		data.put("PanNumber", "");
		data.put("PartyName", "Amer");
		data.put("FraudCaseNumber", "");
		jsonResponse.put("Data", data);
		return jsonResponse;
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
}