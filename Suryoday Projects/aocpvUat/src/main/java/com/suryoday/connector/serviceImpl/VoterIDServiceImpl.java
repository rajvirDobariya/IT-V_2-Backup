package com.suryoday.connector.serviceImpl;

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

import com.suryoday.connector.service.VoterIDService;

@Service
public class VoterIDServiceImpl implements VoterIDService {

	private static Logger logger = LoggerFactory.getLogger(VoterIDServiceImpl.class);

	public JSONObject voterID(JSONObject jSONObject, JSONObject header) {

		JSONObject sendResponse = new JSONObject();

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

			logger.debug(x.BASEURLV2 + "karza/voterId?api_key=" + x.api_key);
			obj = new URL(x.BASEURLV2 + "karza/voterId?api_key=" + x.api_key);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-karza-key", header.getString("X-karza-key"));

			sendResponse = getResponseData(jSONObject, sendResponse, con, "POST");
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

	public JSONObject getHardCodeValue() {

		JSONObject jsonResponse = new JSONObject();
		org.json.JSONObject result = new org.json.JSONObject();
		result.put("ps_lat_long", "0.0,0.0");
		result.put("rln_name_v1", "विजयराज सामादारीया");
		result.put("rln_name_v2", "");
		result.put("rln_name_v3", "");
		result.put("part_no", "140");
		result.put("rln_type", "F");
		result.put("section_no", "5");
		result.put("id", "S131820140050273");
		result.put("epic_no", "SHA4722088");
		result.put("rln_name", "Vijairaj Samdariya");
		result.put("district", "MumbaiCity");
		result.put("last_update", "23-01-2021");
		result.put("state", "Maharashtra");
		result.put("ac_no", "182");
		result.put("slno_inpart", "273");
		result.put("ps_name",
				"BrihanMumbai Mahanagarpalika, Worli Naka School, Ground Floor, Room No. 1, Dr. Annie Besant Road, Mumbai 400 018");
		result.put("pc_name", "Mumbai South");
		result.put("house_no", "1302");
		result.put("name", "Gaurav Vijairaj Samdariya");
		result.put("part_name", "Dr. Annie Besant Road");
		result.put("st_code", "S13");
		result.put("gender", "M");
		result.put("age", "32");
		result.put("ac_name", "Worli");
		result.put("name_v1", "गौरव विजयराज सामादारीया");
		result.put("dob", "07-10-1987");
		result.put("name_v3", "");
		result.put("name_v2", "");
		jsonResponse.put("result", result);
		jsonResponse.put("request_id", "9a8dd3b0-0b7d-487b-bf8e-3fb2e491d581");
		jsonResponse.put("status-code", "101");

		return jsonResponse;
	}

}
