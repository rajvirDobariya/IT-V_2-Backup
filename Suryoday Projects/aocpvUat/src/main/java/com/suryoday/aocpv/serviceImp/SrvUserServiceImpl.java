package com.suryoday.aocpv.serviceImp;

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

import com.suryoday.aocpv.service.SrvUserService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;

@Service
public class SrvUserServiceImpl implements SrvUserService {

	private static Logger logger = LoggerFactory.getLogger(SrvUserServiceImpl.class);

	@Override
	public JSONObject login() {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = getRequest();
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
			logger.debug(x.ServoSysUAT + "DMSServices/resources/SRVUserService/Login");
			obj = new URL(x.ServoSysUAT + "DMSServices/resources/SRVUserService/Login");

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("dsName", "Common_DB");
			con.setRequestProperty("Cookie", "JSESSIONID=-Yv4hPn84FLDHGUqztMjuhng");
			sendResponse = getResponseData(request, sendResponse, con, "POST");

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private JSONObject getRequest() {
		org.json.JSONObject request = new org.json.JSONObject();
		request.put("username", "AOCPV");
		request.put("password", "AOCPV@123");
		request.put("encryption", false);
		request.put("isForceFull", "Y");

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
}
