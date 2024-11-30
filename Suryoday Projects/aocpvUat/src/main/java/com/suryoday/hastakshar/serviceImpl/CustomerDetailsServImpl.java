package com.suryoday.hastakshar.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.suryoday.connector.serviceImpl.GenerateProperty;
import com.suryoday.hastakshar.service.CustomerDetailsService;

@Service
public class CustomerDetailsServImpl implements CustomerDetailsService {

	private static Logger logger = LoggerFactory.getLogger(CustomerDetailsServImpl.class);

	@Override
	public JSONObject getCustomerDetails(JSONObject jsonObject, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		JSONObject req = getCustomerDetailsReq(jsonObject);
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
			logger.debug(x.BASEURL + "customers/details/v2?api_key=" + x.api_key);

			obj = new URL(x.BASEURL + "customers/details/v2?api_key=" + x.api_key);
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			String formattedDateTime = now.format(dateTimeFormatter);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			// con.setRequestProperty("X-Correlation-ID", formattedDateTime);
			con.setRequestProperty("X-Request-ID", "IBR");
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("X-From-ID", "TAB");

			sendResponse = getResponseData(req, sendResponse, con, "POST");

//			JSONObject sendRespons = parseCustDetailsResponse(sendResponse);

		} catch (Exception e) {

			e.getMessage();
		}

		return sendResponse;
	}

	private JSONObject getCustomerDetailsReq(JSONObject jsonObject) {
		String mobileNo = "";
		String customerId = "";
		if (jsonObject.getJSONObject("Data").has("mobileNo")) {
			mobileNo = jsonObject.getJSONObject("Data").getString("mobileNo");
		}
		if (jsonObject.getJSONObject("Data").has("CustomerNo")) {
			customerId = jsonObject.getJSONObject("Data").getString("CustomerNo");
		}
		JSONObject parent = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("MobileNo", mobileNo);
		data.put("AadhaarNo", "");
		data.put("AadhaarReferenceNo", "");
		data.put("PanNo", "");
//		180268772
		// 180268778
		// 240000379
		// data.put("CustomerNo", "180268778");
		data.put("CustomerNo", customerId);
		data.put("BranchCode", "");
		data.put("ProductGroup", "CASA");
		parent.put("Data", data);
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
