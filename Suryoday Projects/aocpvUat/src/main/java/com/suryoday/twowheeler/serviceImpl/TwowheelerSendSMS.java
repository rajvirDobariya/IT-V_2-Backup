package com.suryoday.twowheeler.serviceImpl;

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
import org.springframework.stereotype.Service;

import com.suryoday.connector.serviceImpl.GenerateProperty;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;

@Service
public class TwowheelerSendSMS implements com.suryoday.twowheeler.service.TwowheelerSendSMS {

	private static Logger logger = LoggerFactory.getLogger(TwowheelerSendSMS.class);

	@Override
	public JSONObject sendSms(TwowheelerDetailesTable twowheelerDetails, String stage) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = getRequest(twowheelerDetails, stage);
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
			logger.debug(x.BASEURL1 + "notification/sms?api_key=" + x.api_key);

			obj = new URL(x.BASEURL1 + "notification/sms?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", "SXT");

			sendResponse = getResponseData(request, sendResponse, con, "POST");

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private JSONObject getRequest(TwowheelerDetailesTable twowheelerDetails, String stage) {
		JSONObject parent = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject dynamicParam1 = new JSONObject();
		JSONObject dynamicParam2 = new JSONObject();
		JSONObject dynamicParam3 = new JSONObject();
		JSONObject dynamicParam4 = new JSONObject();
		JSONArray dynamicParams = new JSONArray();

		data.put("OTP", "false");
		data.put("PhoneNumber", "91" + twowheelerDetails.getMobileNo());
		data.put("IntFlag", "0");
		if (stage.equals("SALES")) {
			dynamicParam1.put("Name", "Name");
			dynamicParam1.put("Value", "TW" + twowheelerDetails.getApplicationNo());
			data.put("TemplateId", "SMS1018");
			dynamicParams.put(dynamicParam1);
		} else if (stage.equals("APPROVED")) {
			dynamicParam1.put("Name", "Name");
			dynamicParam1.put("Value", twowheelerDetails.getName());
			dynamicParam2.put("Name", "Name");
			dynamicParam2.put("Value", twowheelerDetails.getName());
			dynamicParams.put(dynamicParam1);
			dynamicParams.put(dynamicParam2);
			data.put("TemplateId", "SMS1022");
		} else if (stage.equals("CREDITREJECT")) {
			dynamicParam1.put("Name", "Name");
			dynamicParam1.put("Value", twowheelerDetails.getName());
			dynamicParam2.put("Name", "Name");
			dynamicParam2.put("Value", "TW" + twowheelerDetails.getApplicationNo());
			data.put("TemplateId", "SMS1021");
			dynamicParams.put(dynamicParam1);
			dynamicParams.put(dynamicParam2);
		} else if (stage.equals("RMREJECT")) {
			dynamicParam1.put("Name", "Name");
			dynamicParam1.put("Value", twowheelerDetails.getName());
			dynamicParam2.put("Name", "Name");
			dynamicParam2.put("Value", twowheelerDetails.getName());
			dynamicParam3.put("Name", "Name");
			dynamicParam3.put("Value", "TW" + twowheelerDetails.getApplicationNo());
			dynamicParam4.put("Name", "Name");
			dynamicParam4.put("Value", twowheelerDetails.getAmount());
			data.put("TemplateId", "SMS1020");
			dynamicParams.put(dynamicParam1);
			dynamicParams.put(dynamicParam2);
			dynamicParams.put(dynamicParam3);
			dynamicParams.put(dynamicParam4);
		} else if (stage.equals("CREDITAPPROVAL")) {
			dynamicParam1.put("Name", "Name");
			dynamicParam1.put("Value", twowheelerDetails.getName());
			dynamicParam2.put("Name", "Name");
			dynamicParam2.put("Value", twowheelerDetails.getName());
			dynamicParam3.put("Name", "Name");
			dynamicParam3.put("Value", "TW" + twowheelerDetails.getApplicationNo());
			dynamicParam4.put("Name", "Name");
			dynamicParam4.put("Value", twowheelerDetails.getAmount());
			data.put("TemplateId", "SMS1019");
			dynamicParams.put(dynamicParam1);
			dynamicParams.put(dynamicParam2);
			dynamicParams.put(dynamicParam3);
			dynamicParams.put(dynamicParam4);
		} else {
			dynamicParam1.put("Name", "Name");
			dynamicParam1.put("Value", twowheelerDetails.getName());
			dynamicParam2.put("Name", "Name");
			dynamicParam2.put("Value", "TW" + twowheelerDetails.getApplicationNo());
			data.put("TemplateId", "SMS1021");
			dynamicParams.put(dynamicParam1);
			dynamicParams.put(dynamicParam2);
			data.put("PhoneNumber", "91" + stage);
		}
		data.put("DynamicParam", dynamicParams);
		parent.put("Data", data);
		System.out.println("sms Request " + parent);
		return parent;
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
