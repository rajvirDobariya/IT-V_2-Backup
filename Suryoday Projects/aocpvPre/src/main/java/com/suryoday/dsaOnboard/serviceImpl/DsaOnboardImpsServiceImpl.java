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

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.suryoday.dsaOnboard.service.DsaOnboardImpsService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;

@Service
public class DsaOnboardImpsServiceImpl implements DsaOnboardImpsService {

	private static Logger logger = LoggerFactory.getLogger(DsaOnboardImpsServiceImpl.class);

	@Override
	public JSONObject searchIfsc(String ifscCode, JSONObject header) {

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
			obj = new URL(x.BASEURL + "transaction/ifsc/code/?api_key=" + x.api_key+"&IFSCCode="+ifscCode);
			 logger.debug(x.BASEURL + "transaction/ifsc/code/?api_key=" + x.api_key+"&IFSCCode="+ifscCode);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", "COR");
			con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			con.setRequestProperty("UserID", header.getString("X-User-ID"));
			
			sendResponse = getResponse(ifscCode, sendResponse, con, "GET");

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private static JSONObject getResponse(String parent, JSONObject sendAuthenticateResponse, HttpURLConnection con,
			String MethodType) throws IOException {

		con.setDoOutput(true);

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
	public JSONObject pennyLess(String accountNo, String ifsc, JSONObject header,String accountHolderName) {
		JSONObject sendResponse = new JSONObject();
		JSONObject parentRequest = parentRequest(accountNo, ifsc,accountHolderName);
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
			obj = new URL(x.BASEURL + "payments/imps/suryoday/name?api_key=" + x.api_key);
			 logger.debug(x.BASEURL + "payments/imps/suryoday/name?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", "TAB");
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));
			con.setRequestProperty("X-To-ID", "CB");
			con.setRequestProperty("X-From-ID", "TAB");
			con.setRequestProperty("cache-control", "no-cache");
			sendResponse = getResponseData(parentRequest, sendResponse, con, "PUT");
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

	private JSONObject parentRequest(String accountNo, String ifsc,String accountHolderName) {
		JSONObject request=new JSONObject();
		JSONObject Data=new JSONObject();
		Data.put("SourceAccountNumber", "10000800070059");
		Data.put("TransferAmount", "1");
		Data.put("BeneficiaryAccountNumber", accountNo);
		Data.put("CustomerNumber", "200260309");
		Data.put("Remarks", "CONNECTOR");
		Data.put("BeneficiaryIfscCode", ifsc);
		Data.put("BeneficiaryName", accountHolderName);
		Data.put("FromMobileNumber", "8898923652");
		Data.put("ClientRefID", "987331641283");
		Data.put("BCUserID", "1");
		request.put("Data", Data);
		logger.debug("request"+request);
		return request;
		
	}

}
