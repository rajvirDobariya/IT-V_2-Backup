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

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.twowheeler.pojo.TwowheelerPaymentDetails;
import com.suryoday.twowheeler.repository.TwowheelerRazorPayRepo;
import com.suryoday.twowheeler.service.TwowheelerRazorPayService;

@Component
public class TwowheelerRazorPayServImpl implements TwowheelerRazorPayService {
	private static Logger logger = LoggerFactory.getLogger(TwowheelerRazorPayServImpl.class);
	@Autowired
	TwowheelerRazorPayRepo twowheelerRazorPayRepo;

	@Override
	public JSONObject sendPaymentLink(String applicationNo, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = getRequest();

		URL obj = null;
		try {

			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			obj = new URL(x.BASEURLV2 + "paymentLink?api_key=" + x.api_key);
			logger.debug(x.BASEURLV2 + "paymentLink?api_key=" + x.api_key);
			;
//			 obj = new URL("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
//			logger.debug("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			sendResponse = getResponse(request, sendResponse, con, "POST");
//				
//				getHeadersRequestInfo(con);

		} catch (Exception e) {

			e.getMessage();
		}

		return sendResponse;
	}

	private JSONObject getRequest() {
		JSONObject parent = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("Type", "link");
		data.put("Expire_Time", "04:54:42");
		data.put("Description",
				"Dear Customer, Suryoday Bank requests payment towards loan repayment. Call Smile Centre on 18002667711 for queries");
		data.put("Receipt", "T1910");
		JSONObject customer = new JSONObject();
		customer.put("Email", "");
		customer.put("Name", "Rishabh");
		customer.put("Contact", "7977842840");
		data.put("Customer", customer);
		data.put("Expire_Date", "09-11-2023");
		data.put("Amount", "10.20");
		data.put("Currency", "INR");
		data.put("SMS_Notify", true);
		data.put("View_Less", true);
		data.put("Partial_Payment", false);
		data.put("Email_Notify", true);
		parent.put("Data", data);
		return parent;
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

	@Override
	public JSONObject fetchPaymentLink(String orderId, JSONObject header) {
		JSONObject sendResponse = new JSONObject();

		URL obj = null;
		try {

			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			obj = new URL(x.BASEURLV2 + "fetch/paymentLink/" + orderId + "?api_key=" + x.api_key);
			logger.debug(x.BASEURLV2 + "fetch/paymentLink/" + orderId + "?api_key=" + x.api_key);
			;
//			 obj = new URL("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
//			logger.debug("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			sendResponse = getResponse2(orderId, sendResponse, con, "GET");
//				
//				getHeadersRequestInfo(con);

		} catch (Exception e) {

			e.getMessage();
		}

		return sendResponse;

	}

	private static JSONObject getResponse2(String parent, JSONObject sendAuthenticateResponse, HttpURLConnection con,
			String MethodType) throws IOException {

		con.setDoOutput(true);
//		 OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
//		 os.write(parent.toString());
//		 os.flush();
//		 os.close();

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

	@Override
	public void save(TwowheelerPaymentDetails paymentDetails) {
		twowheelerRazorPayRepo.save(paymentDetails);
	}

}
