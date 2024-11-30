package com.suryoday.hastakshar.serviceImpl;

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

import com.suryoday.connector.serviceImpl.GenerateProperty;

@Service
@SuppressWarnings("static-access")
public class EmailNotifictionService {

	public static final Logger logger = LoggerFactory.getLogger(EmailNotifictionService.class);

	public JSONObject NewReqEmail(String email, String policyNo, String applictioNO, String nature,
			String transactionTypes, String transactionDescription, String department, String requestBy) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = getRequest(email, policyNo, applictioNO, nature, transactionTypes, transactionDescription,
				department, requestBy);
		URL obj = null;
		try {
			GenerateProperty x = GenerateProperty.getInstance();
			x.getappprop();
			x.bypassssl();
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			logger.debug(x.BASEURLV2 + "template/email?api_key=" + x.api_key);
			obj = new URL(x.BASEURLV2 + "template/email?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Postman-Token", "68ff8d21-e74d-4b39-bdbe-6ea636ab2715");
			con.setRequestProperty("X-From-ID", "SXT");
			con.setRequestProperty("X-Request-ID", "SXT");
			con.setRequestProperty("X-To-ID", "CB");
			con.setRequestProperty("X-Transaction-ID", "EabeDcEE-db3c-BddD-CbD7-4bAA992c75d4");
			con.setRequestProperty("X-User-ID", "S2080");
			con.setRequestProperty("cache-control", "no-cache");
			logger.debug(request.toString());
			sendResponse = getResponseData(request, sendResponse, con, "POST");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendResponse;
	}

	private JSONObject getRequest(String email, String policyNo, String applictioNO, String nature,
			String transactionTypes, String transactionDescription, String department, String requestBy) {
		JSONObject Data = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("ToMail", email);
		data.put("Subject", "Approval");
		data.put("TemplateID", "approvalsystem08112023");
		data.put("ApiKey", "16AD312BB51E89A570D9A8903436A16A");
		data.put("FromName", "Suryoday Small Finance Bank Limited");
		data.put("FromMail", "alerts@suryodaybank.com");
		data.put("Type", "T");
		data.put("Var1", applictioNO);
		data.put("Var2", nature);
		data.put("Var3", transactionTypes);
		data.put("Var4", policyNo);
		data.put("Var5", transactionDescription);
		data.put("Var6", department);
		data.put("Var7", requestBy);
		data.put("Var8", "https://sarathi.suryodaybank.com/suryoday-aocpv/login");
		data.put("Var9", "");
		Data.put("Data", data);
		return Data;
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
			sendAuthenticateResponse = sendauthenticateResponse1;
		} else {
			logger.debug("POST request not worked");
			JSONObject sendauthenticateResponse1 = new JSONObject();
			JSONObject errr = new JSONObject();
			errr.put("Description", "Server Error " + responseCode);
			JSONObject j = new JSONObject();
			j.put("error", errr);
			sendauthenticateResponse1.put("data", "" + j);
			sendAuthenticateResponse = sendauthenticateResponse1;
		}
		return sendAuthenticateResponse;
	}
}
